package umich.pitchcoach.flow;


import android.content.Context;
import android.os.Handler;
import umich.pitchcoach.demo.GraphContainer;
import umich.pitchcoach.demo.PitchKeeper;
import umich.pitchcoach.listeners.IRenderNotify;
import umich.pitchcoach.shared.IPitchReciever;
import umich.pitchcoach.threadman.RenderThreadManager;

abstract public class PlayManager implements IRenderNotify {
	protected PitchKeeper pitchkeeper;
	private IPitchReciever callback;
	private RenderThreadManager renderThreadManager;
	protected Context context; 
	Promise currentPlay;
	
	public PlayManager(Context context, PitchKeeper pitchkeeper) { //TODO: Change to eventstream or something
		this.pitchkeeper = pitchkeeper;
		this.context = context;
		renderThreadManager = new RenderThreadManager(new Handler(), this);
	}
	
	public abstract Promise begin();
	public abstract Promise addGraph(); 
	public abstract GraphContainer currentGraph();
	
	public Promise play() {
		return new Promise() {
			public void go() {
				currentGraph().setActive();
				renderThreadManager.startRenderThread(currentGraph());
				currentPlay = this;
			}
		};
	} 
	
	public void setCallback(IPitchReciever callback)
	{
		this.callback = callback;
	}

	@Override
	public void renderIsDone(double pitch, double time) {
		currentGraph().onPitch(pitch, time);
		if (currentGraph().isDone()) {
			renderThreadManager.stopRenderThread();
			currentGraph().finalize();
			if (currentPlay != null) currentPlay.done();
			currentPlay = null;
		}
		this.callback.receivePitch(pitch, time);
	}
	
	public void pause() {
		renderThreadManager.stopRenderThread();
	}
	
	public void unpause() {
		if (currentPlay != null) renderThreadManager.startRenderThread(currentGraph());
	}
	
	
}
