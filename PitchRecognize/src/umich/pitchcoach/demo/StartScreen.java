package umich.pitchcoach.demo;

import umich.pitchcoach.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartScreen extends Activity {
	/** Called when the activity is first created. */	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startscreen);
				
		final Button sing = (Button) findViewById(R.id.singBtn);    
		linkToActivity(sing, PitchGraphActivity.class);
				
		final Button learn = (Button) findViewById(R.id.learnBtn);    
		
		linkToActivity(learn, TutorialActivity.class);
		
		//final Button tune = (Button) findViewById(R.id.tuneBtn);
		
		//linkToActivity(tune, PitchTunerActivity.class);
		
		final Button settings = (Button) findViewById(R.id.settingsBtn);
		linkToActivity(settings, RangeSelect.class);
	}
	
	public void linkToActivity(Button b, final Class c)
	{
		b.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent myIntent = new Intent(view.getContext(), c);
				startActivityForResult(myIntent, 0);
			}
		});		
	}
}