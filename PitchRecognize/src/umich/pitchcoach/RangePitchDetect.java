package umich.pitchcoach;

import java.io.IOException;

import umich.pitchcoach.shared.IPitchReciever;
import umich.pitchcoach.threadman.PitchThreadSpawn;
import umich.pitchcoach.demo.RangeSelect;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class RangePitchDetect extends Activity implements IPitchReciever {
    /** Called when the activity is first created. */
	TextView pitchDisplay;
	TextView timeLeft;
	PitchThreadSpawn pitchService;
	Handler handler;
	private double lowestPitch = 20000;
	private double secLowestPitch = 20000;
	private double highestPitch = 20;
	private double secHighestPitch = 20;
	private double timer = 0.0;
	private char lowHigh = 'l';
	
	
	private double roundToNearest(double val, double to)
	{
		return Math.round(val/to) * to;
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.autorangeselect);
        pitchDisplay = (TextView)findViewById(R.id.autopitchamt);
        timeLeft = (TextView)findViewById(R.id.autoPitchText);
        pitchService = new PitchThreadSpawn();
       

        handler = new Handler();
    }
	
	@Override
	public void receivePitch(double pitch, double time) {
		pitchDisplay.setText(Double.toString(
				roundToNearest(pitch,6)
				//pitch
		));

    timer = timer + time;
    if(timer < 2.0 && timer >= 1.0)
		  timeLeft.setText("2 more seconds!");
    else if(timer <= 3.0 && timer >= 2.0)
      timeLeft.setText("1 more second!");
    else if (timer > 3.0) {
      timeLeft.setText("0 more seconds!");
      if(lowHigh == 'l')
        dialogAndReset(secLowestPitch);
      else
        dialogAndFinish(secHighestPitch);
    }
    
    if(lowHigh == 'l') {
      if(pitch < lowestPitch)
        lowestPitch = pitch;
      else if(pitch < secLowestPitch)
        secLowestPitch = pitch;
    }
    else {
      if(pitch > highestPitch)
        highestPitch = pitch;
      else if(pitch > secHighestPitch)
        secHighestPitch = pitch;
      
    }
	}
	
	private void dialogAndReset(double pitch) {
	  timer = 0;
	  timeLeft.setText("3 more seconds!");
	  pitchService.stopPitchService();
	  AlertDialog.Builder builder = new AlertDialog.Builder(this);
	  builder.setMessage("The lowest pitch you sang is "+roundToNearest(pitch,6)+ " Hz. Continue to your high range?")
	         .setCancelable(false)
	         .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	             public void onClick(DialogInterface dialog, int id) {
	                  lowHigh = 'h';
	                  dialog.cancel();
	                  startPService();
	             }
	         })
	         .setNegativeButton("Try again", new DialogInterface.OnClickListener() {
	             public void onClick(DialogInterface dialog, int id) {
	                 lowestPitch = 20000;
	                 secLowestPitch = 20000;
	                  dialog.cancel();
	                  startPService();
	             }
	         });
	  AlertDialog alert = builder.create();
	  alert.show();
	}
	
	private void dialogAndFinish(double pitch) {
	  pitchService.stopPitchService();
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage("The highest pitch you sang is "+roundToNearest(pitch,6)+ " Hz. Are you finished?")
           .setCancelable(false)
           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
                    int intLow = (int)roundToNearest(secLowestPitch,6);
                    int intHigh = (int)roundToNearest(secHighestPitch,6);
                    writeContent(intLow,intHigh);
                    
                    finish();
               }
           })
           .setNegativeButton("Try again", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
                   timer = 0;
                   timeLeft.setText("3 more seconds!");
                   highestPitch = 20;
                   secHighestPitch = 20;
                    dialog.cancel();
                    startPService();
               }
           });
    AlertDialog alert = builder.create();
    alert.show();
	  
	}
	
  private void writeContent(int minFreq, int maxFreq) {
    try {
      String toWrite = Integer.toString(minFreq)+" "+Integer.toString(maxFreq);
      RangeSelect.fos = openFileOutput(RangeSelect.FILENAME, Context.MODE_PRIVATE);
      RangeSelect.fos.write(toWrite.getBytes());
      RangeSelect.fos.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
	
	private void startPService() {
	  pitchService.startPitchService(this, handler);
	}
	
	@Override 
	protected void onDestroy()
	{
		super.onDestroy();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		pitchService.startPitchService(this, handler);
	}
	

	@Override 
	protected void onPause()
	{
		super.onPause();
		pitchService.stopPitchService();
	}
}