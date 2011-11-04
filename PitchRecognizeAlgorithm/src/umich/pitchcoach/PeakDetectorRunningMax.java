package umich.pitchcoach;


public class PeakDetectorRunningMax implements IPeakDetector {
	float[] data;
	int offset;
	int endoffset;
	int currentLoc;
	int top;


	private static final float MIN_RATIO = 0.01f;
	private static final int SAMPLES_THRES=20;

	/* An unused method.
	private int sign(float i)
	{
		if (i < 0) return -1;
		else if (i > 0) return 1;
		return 0;
	}
	*/

	@Override
	public void prepareData(float[] data, int offset, int endoffset) {
		this.data = data;
		this.offset = offset;
		this.endoffset = endoffset;
		this.currentLoc = offset;
		top = offset;
		for (int i = offset; i < endoffset; i++)
		{
			if (data[i] > data[top]) {
				top = i;
			}
		}
	}

	@Override
	public int findNextPeak() {
		int currentMax = this.currentLoc;
		while (currentLoc < endoffset)
		{
			if (data[currentLoc] >= data[currentMax])
			{
				currentMax = currentLoc;
			}

			if (currentLoc - currentMax > SAMPLES_THRES && data[currentMax]/data[top] >= MIN_RATIO)
			{
				return currentMax;

			}
			currentLoc++;
		}
		return -1;
	}

}
