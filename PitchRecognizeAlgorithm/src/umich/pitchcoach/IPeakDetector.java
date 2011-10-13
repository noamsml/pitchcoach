package umich.pitchcoach;


public interface IPeakDetector {
	//A quasi-constructor. Prepare the detector to detect on this range. May be expensive as
	//it is allowed to copy data.
	public void prepareData(float[] data, int offset, int length); 
	//Returns the index of the next peak or -1 if done.
	public int findNextPeak();
}
