package umich.pitchcoach.test;

import umich.pitchcoach.Invariants;
import umich.pitchcoach.NoMoreDataException;
import umich.pitchcoach.PitchAnalyzerDCT;

public class FileDataTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FileSampleSource dataSource = new FileSampleSource("/home/noam/Schoolwork/MDE/Samples/diag2.txt", Invariants.NUMSAMPLES, Invariants.BUFSIZE);
		float data[] = new float[Invariants.BUFSIZE];
		PitchAnalyzerDCT anal = new PitchAnalyzerDCT(Invariants.BUFSIZE);
		int size = 0;
		boolean done = false;
		while(!done)
		{
			try {
				double pitch = anal.getPitch(dataSource, Invariants.RATE);
				System.out.format("Lines %d -- %d: %.2f\n", dataSource.curline(), dataSource.curlineEnd(), pitch);
			} catch (NoMoreDataException e) {
				done = true;
			}
		}
		
		
		
		
	}

}
