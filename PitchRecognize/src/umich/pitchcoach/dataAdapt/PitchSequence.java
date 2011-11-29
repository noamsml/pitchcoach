package umich.pitchcoach.dataAdapt;

import java.util.ArrayList;

public class PitchSequence implements IPitchSource {
	private String[] pitches;
	int loc;
	
	public PitchSequence(String[] pitches)
	{
		this.pitches = pitches;
		loc = 0;
	}
	
	@Override
	public String getNextPitch() {
		if (loc == pitches.length) return null;
		return pitches[loc++];
	}

}
