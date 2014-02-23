package umich.pitchcoach;

public class PeakDetectorRunningMaxTop extends PeakDetectorRunningMaxOutlier {

	double top;
	private static double MIN_RATIO = 0.2;

	@Override
	public void prepareData(float[] data, int offset, int endoffset) {
		super.prepareData(data, offset, endoffset);
		top = getTop(data, offset, endoffset);
	}

	private double getTop(float[] data, int offset, int endoffset) {
		double top = data[offset];
		for (int i = offset; i < endoffset; i++) {
			top = Math.max(top, data[i]);
		}
		return top;
	}

	@Override
	protected boolean isOutlier(float data) {
		return data > MIN_RATIO * top;
	}

}
