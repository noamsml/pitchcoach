package umich.pitchcoach.demo;

import umich.pitchcoach.LetterNotes;

public class GraphEvaluator {
	double timeSpent;
	double timeRight;
	double timeSinceRight;
	double freqCorrect;

	private static double timeRightNeeded = 1.0;
	private static double timeSpentNeeded = 3.0;
	private static double timeSpentOK = 1.0;

	public GraphEvaluator(String pitch) {
		timeSpent = 0;
		timeRight = 0;
		timeSinceRight = 0;
		freqCorrect = LetterNotes.noteSpecToFreq(pitch);
	}

	public boolean isDone() {
		return (timeRight > timeRightNeeded)
				|| (timeSpent > timeSpentNeeded && timeRight == 0);
	}

	// Bigger is better
	public int getFinalEvaluation() {
		if (timeRight < timeRightNeeded)
			return 0;
		else if (timeSpent > timeSpentOK)
			return 1;
		return 2;
	}

	public void onPitch(double pitch, double time) {
		timeSpent += time;
		if (LetterNotes.evalFreq(pitch, freqCorrect) == 0) {
			timeRight += time;
			timeSinceRight = 0;
		} else {
			if (timeRight != 0 || timeSinceRight != 0)
				timeSinceRight += time;
			timeRight = 0;
		}
	}

	public boolean isCurrentlyCorrect() {
		return timeRight != 0;
	}
}
