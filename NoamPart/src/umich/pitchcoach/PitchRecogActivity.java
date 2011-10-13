package umich.pitchcoach;

import umich.pitchcoach.shared.*;
import umich.pitchcoach.test.MockPitchThreadSpawn;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class PitchRecogActivity extends Activity implements IPitchReciever {
    /** Called when the activity is first created. */
	TextView pitchDisplay;
	IPitchServiceController pitchService;
	Handler handler;
	
	
	private double roundToNearest(double val, double to)
	{
		return Math.round(val/to) * to;
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        pitchDisplay = (TextView)findViewById(R.id.pitchText);
        pitchService = new PitchThreadSpawn();
        //pitchService = new MockPitchThreadSpawn(R.xml.replay_values, getResources()); //This is how you use the mock pitch thread spawner
        handler = new Handler();
    }
	
	@Override
	public void receivePitch(double pitch, double time) {
		pitchDisplay.setText(Double.toString(roundToNearest(pitch,6)));
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
}