package umich.pitchcoach;

public class HammingWindow {
	float[] window;
	public HammingWindow(int size)
	{
		window = new float[size];
		for (int i = 0; i < size; i++) window[i] = computeWindowFunc(i, size);
	}
	
	private float computeWindowFunc(int i, int size) {
		return (float)(0.54 - 0.46 * Math.cos((2 * Math.PI * i)/(size-1))); 
	}

	public void applyHammingWindow(float[] data)
	{
		if (window.length != data.length) throw new RuntimeException("Misuse of Hamming Window");
		for (int i = 0; i < window.length; i++) data[i] *= window[i];
	}
	
	
}
