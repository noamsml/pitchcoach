package umich.pitchcoach;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import edu.emory.mathcs.jtransforms.dct.FloatDCT_1D;


public class PitchAnalyzerDCT_HPS implements IPitchAnalyzer {
	private FloatDCT_1D dct;
	private float MINENERGY = (float)Math.pow(10, 15);
	
	int size;
	float[] floatbuf;
	float[] floatbuf_copy; //again, minimize allocations
	//private static final double SEARCH_LOW = 10;
	//private static final double SEARCH_HIGH = 5000;
	IPeakDetector peakdetect;
	HammingWindow hamming; 
	
	public PitchAnalyzerDCT_HPS(int size)
	{
		dct = new FloatDCT_1D(size);
		this.size = size;
		floatbuf = new float[size];
		floatbuf_copy = new float[size];
		peakdetect = new PeakDetectorRunningMaxRMS();
		hamming = new HammingWindow(size);
	}

	
	@SuppressWarnings("unused")
	@Override
	public double getPitch(ISampleSource buf, int sampleRate) throws NoMoreDataException {
		for (int i = 0; i < size; i++)
		{
			floatbuf[i] = 0;
		}
		
		buf.readData(floatbuf);
		
		
		hamming.applyHammingWindow(floatbuf);
		dct.forward(floatbuf, false);
		
		for (int i = 0; i < size; i++)
		{
			floatbuf[i] = Math.abs(floatbuf[i]);
		}
		
		for (int i = 0; i < floatbuf.length; i++)
		{
			floatbuf_copy[i] = floatbuf[i];
		}
		
		for (int compression = 2; compression < 4; compression++) 
		{
			for (int i = 1; i < floatbuf.length; i++)
			{
				floatbuf[i] = floatbuf[i] * sample_compressed(floatbuf_copy, 1, compression, i);
			}
		}
		
		
		String diagWriteToDisk = null;
		peakdetect.prepareData(floatbuf, 1, floatbuf.length / 3);
		/*
		if (diagWriteToDisk != null)
		{
			try {
				PrintWriter write = new PrintWriter(new FileOutputStream(diagWriteToDisk));
				for (int i = 1; i < floatbuf.length; i++) write.println(floatbuf[i]);
				write.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		int peak;
		do {
			peak = peakdetect.findNextPeak();
			//if (peak != -1) System.out.format("PEAK: %f\n", floatbuf[peak]);
		} while (peak != -1 && floatbuf[peak] < MINENERGY);
		if (peak == -1) return -1;
		return DCTIndexToFreq(peak, size, sampleRate);
	}
	
	
	private int getMax(float[] buffer, int begin, int end) {
		int peak = begin;
		for (int i = begin; i < end; i++) {
			if (buffer[i] > buffer[peak]) {
				peak = i;
			}
		}
		return peak;
	}


	private float sample_compressed(float[] buffer, int offset, int compression, int loc) {
		/*float result = 0;
		for (int i =0; i < compression; i++)
		{
			if (loc * compression + i < buffer.length)
				result += buffer[loc * compression + i];
		}
		result /= compression;*/
		
		if (offset + loc * compression < buffer.length) return buffer[offset + loc*compression];
		return 0;
		//return result;
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
