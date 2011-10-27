package umich.pitchcoach;

import org.apache.commons.lang3.StringEscapeUtils;

import umich.pitchcoach.data.access.PitchCoachDbHelper;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class NathanPartActivity extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {    	
    	setContentView(R.layout.main);
    	TextView t = (TextView) findViewById(R.id.outputter);
    	PitchCoachDbHelper dbHelper = new PitchCoachDbHelper(this);
    	try {
    		new LessonKeeper(this).getLessonById(4);
    	} catch(Exception e){
    		t.setText("Something went wrong: "+e.getMessage());
    	}    	
    	super.onCreate(savedInstanceState);    	
   }
}
