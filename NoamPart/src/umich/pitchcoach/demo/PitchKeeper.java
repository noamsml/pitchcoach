package umich.pitchcoach.demo;

import java.util.ArrayList;
import java.util.Random;

/*
 * A class to keep pitches.
 */
public class PitchKeeper {
	
	private ArrayList<Double> storedPitches;
	
	// Store a List of Doubles
	public PitchKeeper(ArrayList<Double> pitches){
		storedPitches =  new ArrayList<Double>(pitches.size());
		for (Double pitch : pitches) {
			storedPitches.add(new Double(pitch));
		}
	}
	
	// Return a random Double from the storedPitches
	public Double getRandomPitch() {
		Random randomNumGen = new Random();
		int numPitches = storedPitches.size();
		int randInt = randomNumGen.nextInt(numPitches);
		return storedPitches.get(randInt);
	}
	
}