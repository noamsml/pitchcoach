package umich.pitchcoach.test;

import java.io.*;

public class OvertonePatternTest {

	/**
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException {
		NoisyOvertonePattern p = new NoisyOvertonePattern(500, 44100,
				new float[] { 1000, 1000, 500, 500 }, 100);

		float[] dat = new float[44100];
		p.readData(dat);
		PrintWriter out = new PrintWriter(new FileOutputStream(
				"/home/noam/Workshop/sine/dat1"));
		for (int i = 0; i < dat.length; i++) {
			out.println(dat[i]);
		}
		out.close();
	}

}
