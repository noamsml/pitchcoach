package umich.pitchcoach.data;

import java.io.IOException;

import umich.pitchcoach.R;
import umich.pitchcoach.demo.PitchGraphActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TestEventStream extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.testevent);
		final TextView pitchText = (TextView) findViewById(R.id.eventText);
		final Button nextEvent = (Button) findViewById(R.id.nextBtn);
		final EventStream myStream = new EventStream(this);
		
		nextEvent.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Event myEvent = myStream.nextEvent();
				pitchText.setText("Name: "+myEvent.name);
			}
		});
		
	}
}