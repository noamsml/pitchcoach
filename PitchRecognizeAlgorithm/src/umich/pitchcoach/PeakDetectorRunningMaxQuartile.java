package umich.pitchcoach;

public class PeakDetectorRunningMaxQuartile extends
		PeakDetectorRunningMaxOutlier {

	StatisticalData stats;
	private static final float OUTLIER_RATIO = 7.5f;

	@Override
	public void prepareData(float[] data, int offset, int endoffset) {
		super.prepareData(data, offset, endoffset);
		stats = StatisticalAnalyzer.statAnal(data, offset, endoffset);
	}

	@Override
	protected boolean isOutlier(float data) {
		return (data - stats.thirdQuartile) > (OUTLIER_RATIO * stats.quartileDiff);
	}

}
