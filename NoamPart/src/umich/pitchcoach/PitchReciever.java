package umich.pitchcoach;

public interface PitchReciever {
	public void receivePitch(double pitch);
	public void runOnUiThread(Runnable r);
}
