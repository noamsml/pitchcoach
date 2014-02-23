package umich.pitchcoach.flow;

import android.content.Context;
import android.os.Handler;
import umich.pitchcoach.data.Event;
import umich.pitchcoach.data.EventStream;
import umich.pitchcoach.dataAdapt.IPitchSource;
import umich.pitchcoach.demo.GraphContainer;
import umich.pitchcoach.demo.PitchKeeper;
import umich.pitchcoach.listeners.IRenderNotify;
import umich.pitchcoach.shared.IPitchReciever;
import umich.pitchcoach.threadman.RenderThreadManager;

abstract public class PlayManager implements IRenderNotify {
	protected IPitchSource eventstream;
	private IPitchReciever callback;
	private RenderThreadManager renderThreadManager;
	protected Context context;
	Promise currentPlay;

	public PlayManager(Context context, IPitchSource eventstream) {
		this.eventstream = eventstream;
		this.context = context;
		renderThreadManager = new RenderThreadManager(new Handler(), this);
	}

	public abstract Promise begin();

	public abstract Promise addGraph(boolean silent);

	public abstract Promise addEventPart(String nextPitch, boolean silenced,
			Event currentEvent);

	public abstract GraphContainer currentGraph();

	public abstract GraphContainer nextGraph();

	public Promise play() {
		return new Promise() {
			public void go() {
				currentGraph().setActive();
				renderThreadManager.startRenderThread(currentGraph());
				currentPlay = this;
			}
		};
	}

	public void setCallback(IPitchReciever callback) {
		this.callback = callback;
	}

	@Override
	public void renderIsDone(double pitch, double time) {
		currentGraph().onPitch(pitch, time);
		if (currentGraph().isDone()) {
			renderThreadManager.stopRenderThread();
			currentGraph().makeDone();
			if (currentPlay != null)
				currentPlay.done();
			currentPlay = null;
		}
		this.callback.receivePitch(pitch, time);
	}

	public void pause() {
		renderThreadManager.stopRenderThread();
	}

	public void unpause() {
		if (currentPlay != null)
			renderThreadManager.startRenderThread(currentGraph());
	}

}
