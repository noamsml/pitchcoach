package umich.pitchcoach.flow;

public class PromiseFactoryPromise extends Promise {
	private IPromiseFactory factory;

	public PromiseFactoryPromise(IPromiseFactory pf)
	{
		this.factory = pf;
	}
	
	public void go() {
		this.factory.getPromise().then(new Runnable() {

			@Override
			public void run() {
				done();
			}
		}).go();
	}
}
