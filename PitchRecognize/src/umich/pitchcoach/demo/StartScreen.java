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
	AnimationDrawable animatedStart;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startscreen);
				
		final Button sing = (Button) findViewById(R.id.singBtn);    
		sing.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent myIntent = new Intent(view.getContext(), PitchGraphActivity.class);
				startActivityForResult(myIntent, 0);
			}
		});		

		final Button learn = (Button) findViewById(R.id.learnBtn);    
		learn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
			}
		});

		final Button tune = (Button) findViewById(R.id.tuneBtn);    
		tune.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
			}
		});		


	}
}