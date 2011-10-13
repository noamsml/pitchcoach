package umich.pitchcoach;


public interface IPitchAnalyzer {
	
	//Returns -1 if it determines silence
	public double getPitch(ISampleSource buf, int sampleRate);
	public double bucketSize(int sampleRate);
}
