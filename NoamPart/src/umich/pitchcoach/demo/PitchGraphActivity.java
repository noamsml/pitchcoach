package umich.pitchcoach.demo;

import umich.pitchcoach.R;
import android.app.Activity;
import android.os.Bundle;

public class PitchGraphActivity extends Activity {
	GraphSurface surface;
	ImageSource image;
	OffscreenRenderThread renderThread;

	private void startRenderThread() {
		stopRenderThread();
		renderThread = new OffscreenRenderThread(surface.getContext());
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

}
