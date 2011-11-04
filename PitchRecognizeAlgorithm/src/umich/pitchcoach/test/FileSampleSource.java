package umich.pitchcoach.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import umich.pitchcoach.ISampleSource;
import umich.pitchcoach.NoMoreDataException;
import umich.pitchcoach.circularShortBuffer;

public class FileSampleSource implements ISampleSource{
	

	
	circularShortBuffer dataStore;
	int curline;
	Scanner inTokenizer;
	boolean done;
	private int numSamples;
	private int bufSize;
	
	private int curdatapoint;
	
	
	public int curline()
	{
		return Math.max(0, curdatapoint-bufSize);
	}
	
	public int curlineEnd()
	{
		return curline() + dataStore.getFill();
	}
	
	public FileSampleSource(String fname, int numSamples, int bufSize)
	{
		try {
			inTokenizer = new Scanner(new FileInputStream(fname));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		done = false;
		this.numSamples = numSamples;
		this.bufSize = bufSize;
		this.dataStore = new circularShortBuffer(bufSize);
	}
	
	@Override
	public int readData(float[] data) throws NoMoreDataException {
		int i=0;
		short[] inData = new short[numSamples];
		if (done) throw new NoMoreDataException();
		try {
			for (i=0; i < numSamples; i++)
			{
					inData[i] = (short)Float.parseFloat(inTokenizer.nextLine());
					curdatapoint++;
			}
		}
		catch (NoSuchElementException e)
		{
			done = true;
		}
		dataStore.dumpData(inData, i);
		return dataStore.readData(data);
	}

}
