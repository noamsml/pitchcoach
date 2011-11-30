package umich.pitchcoach.demo;

import java.util.ArrayList;
import java.util.Random;

import umich.pitchcoach.dataAdapt.IPitchSource;

/*
 * A class to keep pitches.
 */
public class PitchKeeper implements IPitchSource {
	
	private ArrayList<String> storedPitches;
	
	// Store a List of Strings
	public PitchKeeper(ArrayList<String> pitches){
		storedPitches =  new ArrayList<String>(pitches.size());
		for (String pitch : pitches) {
			storedPitches.add(new String(pitch));
		}
	}
	
	// Return a random String from the storedPitches
	public String getNextPitch() {
		Random randomNumGen = new Random();
		int numPitches = storedPitches.size();
		int randInt = randomNumGen.nextInt(numPitches);
		return storedPitches.get(randInt);
	}
	
}