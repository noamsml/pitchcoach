package umich.pitchcoach.test;

import java.lang.reflect.Array;

import umich.pitchcoach.ISampleSource;


public class NoisyOvertonePattern implements ISampleSource {
	int samplenum;
	
	float freq;
	float rate;
	float[] opattern;
	float noiseamp;
	
	public NoisyOvertonePattern(float freq, float rate, float[] opattern, float noiseamp)
	{
		this.freq = freq;
		this.rate = rate;
		this.opattern = new float[opattern.length];
		for (int i = 0; i < opattern.length; i++)
		{
			this.opattern[i] = opattern[i];
		}
		this.noiseamp = noiseamp;
	}
	@Override
	public int readData(float[] data) {
		for (int i = 0; i < data.length; i++, samplenum++)
		{
			data[i] = 0;
			for (int onum = 0; onum < opattern.length; onum++)
			{
				data[i] += opattern[onum] * (float)Math.sin(samplenum * 2*Math.PI/(rate / (freq*(onum+1))));
			}
			data[i] += noiseamp * randomNegToPos();
		}
		return data.length;
	}
	
	private float randomNegToPos()
	{
		return (float)(2*(0.5 - Math.random()));
	}
	
	
}
