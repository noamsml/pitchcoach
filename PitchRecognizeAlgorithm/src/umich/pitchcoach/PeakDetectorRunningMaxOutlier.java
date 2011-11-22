package umich.pitchcoach;


abstract public class PeakDetectorRunningMaxOutlier implements IPeakDetector {
	float[] data;
	int offset;
	int endoffset;
	int currentLoc;
	StatisticalData stats;
	private static final int SAMPLES_THRES=20;
	
	private int sampthres(int loc)
	{
		return SAMPLES_THRES;
	}
	
	@Override
	public void prepareData(float[] data, int offset, int endoffset) {
		this.data = data;
		this.offset = offset;
		this.endoffset = endoffset;
		this.currentLoc = offset;
		stats = StatisticalAnalyzer.statAnal(data, offset, endoffset);
		//threshold = StatisticalAnalyzer.getTopNthSafe(data, offset, endoffset, numPeakElems);
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

	
	abstract protected boolean isOutlier(float data);
}
