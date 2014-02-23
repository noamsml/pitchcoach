package umich.pitchcoach.test;

import android.content.res.Resources;
import android.os.Handler;
import android.util.Log;
//import umich.pitchcoach.PitchThread;
import umich.pitchcoach.shared.IPitchReciever;
import umich.pitchcoach.shared.IPitchServiceController;

public class MockPitchThreadSpawn implements IPitchServiceController {

	private MockPitchThread thread;
	private int resid;
	private Resources resources;

	public MockPitchThreadSpawn(int resid, Resources resources) {
		thread = null;
		this.resid = resid;
		this.resources = resources;
	}

	@Override
	public void startPitchService(IPitchReciever callback, Handler handler) {
		if (thread != null) {
			stopPitchService();
			Log.w("Pitch Service Controller",
					"Pitch service started while another pitch service is running. This is bad practice.");
		}

		thread = new MockPitchThread(resources.getXml(resid), callback, handler);
		thread.start();

	}

	@Override
	public void stopPitchService() {
		// TODO Auto-generated method stub
		thread.interrupt();
		thread = null;

	}

}
