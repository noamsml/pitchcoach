package umich.pitchcoach;

import umich.pitchcoach.shared.*;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class PitchRecogActivity extends Activity implements IPitchReciever {
    /** Called when the activity is first created. */
	TextView pitchDisplay;
	IPitchServiceController pitchService;
	Handler handler;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        pitchDisplay = (TextView)findViewById(R.id.pitchText);
        pitchService = new PitchThreadSpawn();
        handler = new Handler();
    }
	
	@Override
	public void receivePitch(double pitch, double time) {
		pitchDisplay.setText(Double.toString(pitch));
	}
	
	
	@Override 
	protected void onDestroy()
	{
		super.onDestroy();
		pitchService.stopPitchService();
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