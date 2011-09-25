package umich.pitchcoach;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class PitchRecogActivity extends Activity implements PitchReciever {
    /** Called when the activity is first created. */
	TextView pitchDisplay;
	PitchCollector pitchColl;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        pitchDisplay = (TextView)findViewById(R.id.pitchText);
        pitchColl = null;
       // startPitchColl();
        
    }
	

	private void startPitchColl() {
		pitchColl = new PitchCollector(this);
		pitchColl.start();
		
	}


	@Override
	public void receivePitch(double pitch) {
		pitchDisplay.setText(Double.toString(pitch));
	}
	
	
	@Override 
	protected void onDestroy()
	{
		super.onDestroy();
		if (pitchColl != null)
		{
			pitchColl.done = true;
		}
	}
	
	@Override 
	protected void onResume()
	{
		super.onResume();
		startPitchColl();
		
	}
	

	@Override 
	protected void onPause()
	{
		super.onPause();
		if (pitchColl != null)
		{
			pitchColl.done = true;
		}
	}
}