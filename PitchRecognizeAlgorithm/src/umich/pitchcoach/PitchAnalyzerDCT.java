package umich.pitchcoach;


import edu.emory.mathcs.jtransforms.dct.FloatDCT_1D;


public class PitchAnalyzerDCT implements IPitchAnalyzer {
	private FloatDCT_1D dct;
	private IPeakDetector peakdetect;
	private static final double MINENERGY = 700;
	
	
	int size;
	float[] floatbuf;
	private static final double SEARCH_LOW = 10;
	private static final double SEARCH_HIGH = 5000;
	
	public PitchAnalyzerDCT(int size)
	{
		dct = new FloatDCT_1D(size);
		this.size = size;
		floatbuf = new float[size];
		peakdetect = new PeakDetectorRunningMaxOutlier();
	}

	
	@Override
	public double getPitch(ISampleSource buf, int sampleRate) {
		//?
		for (int i = 0; i < size; i++)
		{
			floatbuf[i] = 0;
		}
		
		buf.readData(floatbuf);
		
		//Optimize this?
		double sumEnergy = 0;
		for (int i = 0; i < floatbuf.length; i++)
		{
			sumEnergy += Math.abs(floatbuf[i]);
		}
		if (sumEnergy / floatbuf.length < MINENERGY) return -1;
		
		dct.forward(floatbuf, false);
		
		for (int i = 0; i < size; i++)
		{
			floatbuf[i] = Math.abs(floatbuf[i]);
		}
		
		int searchBegin = freqToDCTIndex(SEARCH_LOW, size, sampleRate);
		int searchEnd = freqToDCTIndex(SEARCH_HIGH, size, sampleRate)+1;
		
		
		
		
		peakdetect.prepareData(floatbuf, searchBegin, searchEnd);
		int peak = peakdetect.findNextPeak();
		return DCTIndexToFreq(peak, size, sampleRate);


	}
	
	
	private double DCTIndexToFreq(int index, int size, int rate)
	{
		return ((double)(index * rate)) / ((double)2 * size);
	}

	private int freqToDCTIndex(double freq, int size, int rate)
	{
		return (int)((freq*2*size)/(rate));
	}
	
	@Override
	public double bucketSize(int rate) {
		return ((double)rate) / ((double)2 * size);
	}
	
	


}
