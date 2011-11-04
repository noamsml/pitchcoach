package umich.pitchcoach.demo;

import java.util.Date;

import umich.pitchcoach.PitchThreadSpawn;
import android.util.Log;

public class GraphGlue {
	protected GraphContainer activeGraphContainer;
	protected OffscreenRenderThread renderThread;
	protected PitchGraphActivity activity;
	PitchThreadSpawn pitchservice;

	
	
	public GraphGlue(PitchGraphActivity activity)
	{
		this.activity = activity;
		this.pitchservice = new PitchThreadSpawn();
	}
	
	public void diagnostics()
	{
		Date d = new Date();
		pitchservice.diagnosticDumpSamples("/sdcard/diagnostics" + d.getTime());
	}
	
	public void setActiveGraphCont(GraphContainer cont)
	{
		activeGraphContainer = cont;
		if (renderThread != null) renderThread.setImage(cont.getImageSource());
	}
	
	public void imageChanged()
	{
		if (renderThread != null) renderThread.setImage(activeGraphContainer.getImageSource());
	}
	
	public void startRenderThread() {
		Log.v("RenderThread", "started");
		if (renderThread != null) stopRenderThread();
		renderThread = new OffscreenRenderThread(this, activity.getApplicationContext());
		imageChanged();
		renderThread.start();
		
		//ANDROID IS FUCKING RETARDED
		renderThread.handlerLock.lock();
		while (renderThread.handler == null) {
			try {
				renderThread.handlerCond.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		pitchservice.startPitchService(renderThread, renderThread.handler);
		renderThread.handlerLock.unlock();
	}
	
	public void stopRenderThread() {
		pitchservice.stopPitchService();
		if (renderThread != null) renderThread.interrupt();
		renderThread = null;
		Log.v("RenderThread", "stopped");

	}
	
	public void setCurrentGraph(GraphContainer container)
	{
		if (activeGraphContainer != null) activeGraphContainer.makeDead();
		activeGraphContainer = container;
		container.makeLive();
		imageChanged();
	}
	
	
	public void onPitch(final double pitch, final double time)
	{
		activity.runOnUiThread(new Runnable () {
			@Override
			public void run() {
				activeGraphContainer.updateGraph();
				activeGraphContainer.onPitch(pitch, time);
				activity.updateIncidentalUI(pitch, time);
			}
			
		});
	}
	
	public GraphContainer getCurrentContainer()
	{
		return activeGraphContainer;
	}
}
