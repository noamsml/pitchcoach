package umich.pitchcoach;

import android.os.Handler;
import umich.pitchcoach.shared.IPitchReciever;
import umich.pitchcoach.shared.IPitchServiceController;
import android.util.Log;

public class PitchThreadSpawn implements IPitchServiceController {

	private PitchThread thread;
	
	public PitchThreadSpawn() {
		thread = null;
	}
	
	@Override
	public void startPitchService(IPitchReciever callback, Handler handler) {
		if (thread != null) {
			stopPitchService();
			Log.w("Pitch Service Controller", "Pitch service started while another pitch service is running. This is bad practice.");
		}
		
		thread = new PitchThread(callback, handler);
		thread.start();

	}

	@Override
	public void stopPitchService() {
		// TODO Auto-generated method stub
		thread.done = true;
		thread = null;

	}

}
