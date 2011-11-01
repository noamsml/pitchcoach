package umich.pitchcoach.demo;

public class GraphGlue {
	protected GraphContainer activeGraphContainer;
	protected OffscreenRenderThread renderThread;
	protected PitchGraphActivity activity;
	
	
	public GraphGlue(PitchGraphActivity activity)
	{
		this.activity = activity;
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
		stopRenderThread();
		renderThread = new OffscreenRenderThread(this, activity.getApplicationContext());
		imageChanged();
		renderThread.start();
	}
	
	public void stopRenderThread() {
		if (renderThread != null) renderThread.interrupt();
		renderThread = null;

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
				activity.updateIncidentalUI(pitch, time);
				activeGraphContainer.updateGraph();
			}
			
		});
	}
}
