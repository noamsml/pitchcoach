package umich.pitchcoach.demo;

import java.util.Date;

import umich.pitchcoach.PitchThreadSpawn;
import umich.pitchcoach.R;
import umich.pitchcoach.shared.IPitchReciever;
import umich.pitchcoach.shared.IPitchServiceController;
import umich.pitchcoach.test.MockPitchThreadSpawn;
import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.os.Handler;
import android.view.SurfaceView;
import android.widget.Button;

public class OffscreenRenderThread extends Thread implements IPitchReciever {
	PitchThreadSpawn pitchservice;
	GraphSurface surface;
	ImageSource image;
	PitchGraphActivity pitchGraphActivity;
	
	
	public OffscreenRenderThread(PitchGraphActivity a, Context ctx)
	{
		this.image = image;
		this.surface = surface;
		pitchservice = new PitchThreadSpawn();
		//pitchservice = new MockPitchThreadSpawn(R.xml.replay_values, ctx.getResources());
		pitchGraphActivity = a;
	}
	
	@Override
	public void interrupt() {
		super.interrupt();
		pitchservice.stopPitchService();
	}

	@Override
	public void run() {
		Looper.prepare();
			pitchservice.startPitchService(this, new Handler());
		Looper.loop();
	}

	@Override
	public synchronized void receivePitch(final double pitch, final double timeInSeconds) {
		if (image != null) image.addDatapoint(pitch, timeInSeconds);
		if (surface != null) surface.postInvalidate();
		pitchGraphActivity.runOnUiThread(new Runnable () {

			@Override
			public void run() {
				pitchGraphActivity.updateIncidentalUI(pitch, timeInSeconds);
			}
			
		});
	}
	
	public void diagnostics()
	{
		Date d = new Date();
		pitchservice.diagnosticDumpSamples("/sdcard/diagnostics" + d.getTime());
	}
	
	public synchronized void setImage(ImageSource image)
	{
		this.image = image;
	}
	
	public synchronized void setSurface(GraphSurface surface)
	{
		this.surface = surface;
	}

}