package umich.pitchcoach;

import umich.pitchcoach.data.entity.Lesson;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NathanPartActivity extends Activity {
	private LessonKeeper lk;
	private TextView t;
	private Button button;
	
	@Override
    public void onCreate(Bundle savedInstanceState){    	
    	super.onCreate(savedInstanceState);    	
		setContentView(R.layout.main);
		try {
			lk = new LessonKeeper(this);
		}
		catch (Exception e){
			// do nothing.
		}
		t = (TextView) findViewById(R.id.outputter);
    	button = (Button) findViewById(R.id.button);
    	button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				testGetLesson();
			}
		});		
   }
	
	private void testGetLesson(){
    	try {
    		Lesson l = lk.getLesson();
        	t.setText("Lesson Name: " + l.getLessonName());
    	} catch(Exception e){
    		t.setText(e.getMessage());
    	}
	}
	
}
