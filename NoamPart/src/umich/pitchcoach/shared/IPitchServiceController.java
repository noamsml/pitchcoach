package umich.pitchcoach.shared;

import android.os.Handler;

public interface IPitchServiceController {
	public void startPitchService(IPitchReciever callback, Handler handler);
	public void stopPitchService();
}
