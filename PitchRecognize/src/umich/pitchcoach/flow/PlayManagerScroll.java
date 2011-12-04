package umich.pitchcoach.flow;

import java.util.Iterator;

import android.content.Context;
import umich.pitchcoach.data.Event;
import umich.pitchcoach.dataAdapt.IPitchSource;
import umich.pitchcoach.demo.GraphContainer;
import umich.pitchcoach.demo.PitchKeeper;
import umich.pitchcoach.demo.ScrollContainer;
import umich.pitchcoach.shared.IPitchReciever;

public class PlayManagerScroll extends PlayManager {
	private ScrollContainer scroller;
	private GraphContainer currentGraph;
	private GraphContainer nextGraph;
		
	public PlayManagerScroll(Context ctx, IPitchSource keeper, ScrollContainer scroller)
	{
		super(ctx, keeper);
		this.scroller = scroller; 
		currentGraph = nextGraph = null;
	}

	@Override
	public Promise begin() {
		return addGraph().then(addGraph());
	}

	@Override
	public Promise addGraph() {
		//Hack
		return new PromiseFactoryPromise(new IPromiseFactory() {
			public Promise getPromise() {
					String nextPitch = eventstream.getNextPitch();	
					if (nextPitch == null) return new Promise() {
						public void go() {
							currentGraph = nextGraph;
							nextGraph = null;
							done();
						}
					};
					final GraphContainer next = new GraphContainer(context, nextPitch);
					return scroller.addElement(next).then(new Runnable() {
						public void run()
						{
							currentGraph = nextGraph;
							nextGraph = next;
						}
					});
				}
		});
	}
	
	@Override
	public Promise addEventPart(final String nextPitch, final boolean silenced, final Event currentEvent) {
		return new PromiseFactoryPromise(new IPromiseFactory() {
			public Promise getPromise() {	
					if (nextPitch == null) return new Promise() {
						public void go() {
							currentGraph = nextGraph;
							nextGraph = null;
							done();
						}
					};
					final GraphContainer next = new GraphContainer(context, nextPitch);
					next.setSilence(silenced);
					next.setEvent(currentEvent);
					return scroller.addElement(next).then(new Runnable() {
						public void run()
						{
							currentGraph = nextGraph;
							nextGraph = next;
						}
					});
				}
		});
	}

	
	@Override
	public GraphContainer currentGraph() {
		return currentGraph;
	}
	
	@Override
	public GraphContainer nextGraph() {
		return nextGraph;
	}
	
}
