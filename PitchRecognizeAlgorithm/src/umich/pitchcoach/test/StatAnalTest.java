package umich.pitchcoach.test;

import umich.pitchcoach.StatisticalAnalyzer;
import umich.pitchcoach.StatisticalData;
public class StatAnalTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		float[] data = new float[]{2,5,7,12,8,1,4,2,5,7,12,8,1,4,2,5,7,12,8,1,4,2,5,7,12,8,1,4};
		StatisticalData d = StatisticalAnalyzer.statAnal(data, 0, 7);
		System.out.println(d.firstQuartile);
		System.out.println(d.thirdQuartile);
		
	}

}
