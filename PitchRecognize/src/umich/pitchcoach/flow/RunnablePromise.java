package umich.pitchcoach.flow;

public class RunnablePromise extends Promise {
	private Runnable action;

	public RunnablePromise(Runnable r) {
		super();
		action = r;
	}

	@Override
	public void go() {
		this.action.run();
		this.done();
	}
}
