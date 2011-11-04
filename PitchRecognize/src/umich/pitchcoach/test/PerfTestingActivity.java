package umich.pitchcoach.test;

import umich.pitchcoach.R;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class PerfTestingActivity extends Activity implements ILogReceiver {
	EditText textview;
	Button startBtn;
	Button saveBtn;
	AnalysisTestingThread testThread;
	Handler handler;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.debug_util);
        textview = (EditText)findViewById(R.id.debugText);
        startBtn = (Button)findViewById(R.id.testbutton_start);
        saveBtn = (Button)findViewById(R.id.testbutton_save);
        testThread = null;
        final ILogReceiver logreceive = this;
        handler = new Handler();
        
        startBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (testThread == null)
				{
					testThread = new AnalysisTestingThread(logreceive, handler);
					testThread.start();
				}
			}
        	
        });
	}

	@Override
	public void getLogString(String data) {
		textview.append(data + "\n");
	}
}
