package umich.pitchcoach.flow;

import android.content.Context;
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
					String nextPitch = pitchkeeper.getNextPitch();	
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
	public GraphContainer currentGraph() {
		return currentGraph;
	}
	
	
}
