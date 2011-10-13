package umich.pitchcoach.test;


import umich.pitchcoach.*;

public class PeakDetectorTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SineWave sampleSource = new SineWave(300, 100);
		IPeakDetector detector = new PeakDetectorRunningMax();
		float[] data = new float[1002];
		sampleSource.readData(data);
		int peak;
		
		detector.prepareData(data, 0, data.length);
		while ((peak = detector.findNextPeak()) != -1)
		{
			System.out.println(peak);
		}
		

	}

}
