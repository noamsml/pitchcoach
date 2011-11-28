package umich.pitchcoach.flow;

public class Promise {
	private Promise next;
	private Promise last;
	private boolean isDone;
	
	public Promise() {
		next = null;
		last = this;
		isDone = false;
	}
	
	
	private Promise setNext(Promise p)
	{
		if (getDone()) {
			p.go();
		}
		else {
			this.next = p;
		}
		
		return p;
	}
	
	public Promise then(Runnable r) {
		return then(new RunnablePromise(r));
	}
	
	public Promise then(Promise p)
	{
		if (this.last == this) this.setNext(p);
		else this.last.then(p);
		this.last = p;
		return this;
	}
	
	//Remember to call super
	public void done() {
		this.setDone();
		if (next != null) next.go();
	}
	
	public void go() {
		
	}
	
	synchronized private void setDone() {
		isDone = true;
	}
	
	synchronized public boolean getDone() {
		return isDone;
	}
}
