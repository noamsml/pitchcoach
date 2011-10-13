package umich.pitchcoach;


public class PeakDetectorNaive implements IPeakDetector {
	float[] data;
	int offset;
	int endoffset;
	int currentLoc;
	int currentSign;
	int top;
	private static final double MIN_RATIO = 0.2;
	
	private int sign(float i)
	{
		if (i < 0) return -1;
		else if (i > 0) return 1;
		return 0;
	}
	
	@Override
	public void prepareData(float[] data, int offset, int endoffset) {
		this.data = data;
		this.offset = offset;
		this.endoffset = endoffset;
		this.currentLoc = offset+1;
		this.currentSign = 0;
		
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
		int tempSign;
		while (currentLoc < endoffset)
		{
			tempSign = sign(data[currentLoc] - data[currentLoc-1]);
			//subtle difference
			if (currentLoc == top)
			{
				currentLoc++;
				currentSign = tempSign;
				return currentLoc-1;
			}
			
			if (tempSign == 0 || tempSign == currentSign * -1 )
			{	
					if (data[currentLoc] / data[top] >= MIN_RATIO)
					{
						currentLoc++;
						currentSign = tempSign;
						return currentLoc-2;
					}
			}
			currentLoc++;
			currentSign = tempSign;
		}
		return -1;
	}

}
