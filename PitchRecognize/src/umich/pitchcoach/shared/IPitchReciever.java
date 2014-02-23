package umich.pitchcoach.shared;

public interface IPitchReciever {

	// CALLBACK: Called whenever a certain amount of pitch data has been
	// gathered by the pitch service
	public void receivePitch(double pitch, double timeInSeconds);
}
