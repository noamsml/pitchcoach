package umich.pitchcoach.test;

import umich.pitchcoach.IPitchAnalyzer;
import umich.pitchcoach.NoMoreDataException;
import umich.pitchcoach.PitchAnalyzerDCT;

public class PitchAnalyzerCorrectnessTest {
	public static void main(String[] args)
	{
		IPitchAnalyzer pitchanal = new PitchAnalyzerDCT(8192);
		float[] freqs = new float[]{120, 220, 320, 420, 520};
		float[][] opatterns = new float[][]{
				new float[]{5000},
				new float[]{5000, 10000},
				new float[]{5000, 50000, 50000, 5000}
		};
		
		for (int freqidx = 0; freqidx < freqs.length;  freqidx++)
		{
			float freq = freqs[freqidx];
			for (int opatternidx = 0; opatternidx < opatterns.length; opatternidx++)
			{
				float[] opattern = opatterns[opatternidx];
				for (int noise = 0; noise < 400; noise += 100)
				{
					NoisyOvertonePattern pat = new NoisyOvertonePattern(freq, 44100, opattern, noise);
					double res=0;
					try {
						res = pitchanal.getPitch(pat, 44100);
					} catch (NoMoreDataException e) {
						//Will never occur
					}
					double diff = res - freq;
					if (Math.abs(diff) < 6)
					{
						System.out.format("PASSED: freq %f Hz, opattern #%d, noise at %d\n", freq, opatternidx, noise);
					}
					else
					{
						System.out.format("FAILED %.2f: freq %f Hz, opattern #%d, noise at %d\n", diff, freq, opatternidx, noise);
					}
					
				}
				
			}
		}
	}
}
