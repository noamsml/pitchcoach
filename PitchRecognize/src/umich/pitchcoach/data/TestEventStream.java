package umich.pitchcoach.data;

import java.io.IOException;
import java.util.Iterator;

import umich.pitchcoach.LetterNotes;
import umich.pitchcoach.R;
import umich.pitchcoach.demo.PitchGraphActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TestEventStream extends Activity {
	
	private TextView pitchText;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.testevent);
		pitchText = (TextView) findViewById(R.id.eventText);
		final Button nextEvent = (Button) findViewById(R.id.nextBtn);
		final EventStream myStream = new EventStream(this);

		nextEvent.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Event myEvent = myStream.getNextEvent();
				String results = "";
				if (myStream.easyTest()!=null) results+="easy PASS ";
				if (myStream.medTest()!=null) results+="med PASS ";
				if (myStream.hardTest()!=null) results+="hard PASS";
				pitchText.setText(results);
			}
		});

	}

	public void readEvent(Event myEvent){				
		String pitchList = "";
		Iterator<String> pitchIterator = myEvent.pitchesToSing.iterator();
		while (pitchIterator.hasNext()){
			pitchList+=(" "+pitchIterator.next());
		}
		pitchText.setText("Name: "+ myEvent.name+"\nPitch(es):"+ pitchList);
	}
}