package umich.pitchcoach.dataAdapt;

import umich.pitchcoach.data.Event;

public interface IPitchSource {
	public String getNextPitch();

	public Event getNextEvent();
}
