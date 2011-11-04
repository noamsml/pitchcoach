package umich.pitchcoach;


public class PeakDetectorRunningMaxOutlier implements IPeakDetector {
	float[] data;
	int offset;
	int endoffset;
	int currentLoc;
	StatisticalData stats;
	private static final float OUTLIER_RATIO = 7.5f;
	private static final int SAMPLES_THRES=40;
	
	/* An unused method.
	private int sign(float i)
	{
		if (i < 0) return -1;
		else if (i > 0) return 1;
		return 0;
	}
	*/
	
	private int sampthres(int loc)
	{
		//return Math.max((int)(loc/1.5), SAMPLES_THRES);
		return SAMPLES_THRES;
	}
	
	@Override
	public void prepareData(float[] data, int offset, int endoffset) {
		this.data = data;
		this.offset = offset;
		this.endoffset = endoffset;
		this.currentLoc = offset;
		stats = StatisticalAnalyzer.statAnal(data, offset, endoffset);
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
			
			if (currentLoc - currentMax > sampthres(currentMax) && isOutlier(data[currentMax]))
			{
				return currentMax;
				
			}
			currentLoc++;
		}
		return -1;
	}

	
	private boolean isOutlier(float data)
	{
		return (data - stats.thirdQuartile > stats.quartileDiff * OUTLIER_RATIO);
	}
}
