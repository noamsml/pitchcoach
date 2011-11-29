package umich.pitchcoach.demo;

import umich.pitchcoach.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartScreen extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.startscreen);
		final Button pitchMode = (Button) findViewById(R.id.PitchMButton);
		linkToActivity(pitchMode, PitchGraphActivity.class);
		
		final Button tutorial = (Button) findViewById(R.id.tutMButton);    
		linkToActivity(tutorial, TutorialActivity.class);
		
		
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