package umich.pitchcoach.demo;

import umich.pitchcoach.R;
import umich.pitchcoach.shared.IPitchReciever;
import umich.pitchcoach.threadman.PitchThreadSpawn;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class PitchTunerActivity extends Activity implements IPitchReciever{
	
	PitchTuner tuner;
	PitchThreadSpawn pitchService;
	Handler handler;
	TextView pitchDisplay;


	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tunerscreen);

        handler = new Handler();
        pitchService = new PitchThreadSpawn();

		tuner = new PitchTuner(getApplicationContext());
        pitchDisplay = (TextView)findViewById(R.id.Tuner_Pitch_Text);


	}

	@Override
	public void receivePitch(double pitch, double time) {
		pitchDisplay.setText(Double.toString(
				roundToNearest(pitch,6)
				//pitch
		));
	}
	
	
	@Override 
	protected void onDestroy()
	{
		super.onDestroy();
		//pitchService.stopPitchService();
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
	
	
	
	private double roundToNearest(double val, double to)
	{
		return Math.round(val/to) * to;
	}
	
	
}
