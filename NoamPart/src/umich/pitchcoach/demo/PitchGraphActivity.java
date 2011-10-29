package umich.pitchcoach.demo;

import umich.pitchcoach.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PitchGraphActivity extends Activity {
	GraphSurface surface;
	ImageSource image;
	OffscreenRenderThread renderThread;
	Button diagBtn;

	private void startRenderThread() {
		stopRenderThread();
		renderThread = new OffscreenRenderThread(this, getApplicationContext());
		renderThread.setImage(image);
		renderThread.setSurface(surface);
		renderThread.start();
	}
	
	public void setImage(ImageSource image)
	{
		this.image = image;
		renderThread.setImage(image);
	}


	private void stopRenderThread() {
		if (renderThread != null) renderThread.interrupt();
		renderThread = null;
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mockui);
		surface = (GraphSurface) findViewById(R.id.graphview);
		surface.setActivity(this);
		diagBtn = (Button)findViewById(R.id.diagBtn);
		diagBtn.setOnClickListener(new View.OnClickListener () {
			@Override
			public void onClick(View v) {
				renderThread.diagnostics();
			}
			
		});
	
	}

	@Override
	protected void onPause() {
		super.onPause();
		stopRenderThread();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		startRenderThread();
		
	}

	public void updateIncidentalUI(double pitch, double timeInSeconds) {
		// TODO Auto-generated method stub
		
	}

}
