package umich.pitchcoach;


public class circularShortBuffer implements ISampleSource{
	private short[] bufferData; //you better keep your shorts private :P
	private int fill;
	private int currentIndex;
	private int bufferSize;
	
	public circularShortBuffer(int size)
	{
		bufferSize = size;
		fill = 0;
		currentIndex = 0;
		bufferData = new short[size];
	}
	
	public void dumpData(short[] data, int dataSize)
	{
		fill = Math.min(bufferSize, fill + dataSize);
		
		//inefficient for large dataSize, but that's now what this is here for
		for (int i = 0; i < dataSize; i++)
		{
			bufferData[currentIndex] = data[i];
			currentIndex = (currentIndex+1)%bufferSize;
		}
	}
	
	public int readData(float[] data)
	{
		
		int startingIndex = (fill < bufferSize) ? 0 : currentIndex;
		for (int i = 0; i < fill; i++)
		{
			data[i] = bufferData[(startingIndex + i) % bufferSize];
		}
		return fill;
	}
	
	
	public int getSize()
	{
		return bufferSize;
	}
	
	public int getFill()
	{
		return fill;
	}
}
