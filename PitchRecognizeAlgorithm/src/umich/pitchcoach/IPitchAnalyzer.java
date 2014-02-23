package umich.pitchcoach;

public interface IPitchAnalyzer {

	// Returns -1 if it determines silence
	public double getPitch(ISampleSource buf, int sampleRate)
			throws NoMoreDataException;

	public double bucketSize(int sampleRate);
}
