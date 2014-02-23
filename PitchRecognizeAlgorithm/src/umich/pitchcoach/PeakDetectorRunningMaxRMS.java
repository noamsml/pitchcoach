package umich.pitchcoach;

public class PeakDetectorRunningMaxRMS extends PeakDetectorRunningMaxOutlier {

	double rootMeanSquare;

	@Override
	public void prepareData(float[] data, int offset, int endoffset) {
		super.prepareData(data, offset, endoffset);
		rootMeanSquare = getRMS(data, offset, endoffset);
	}

	private double getRMS(float[] data, int offset, int endoffset) {
		double sumSq = 0;
		for (int i = offset; i < endoffset; i++) {
			sumSq += ((double) data[i] * (double) data[i])
					/ (endoffset - offset);
		}
		return Math.sqrt(sumSq);
	}

	@Override
	protected boolean isOutlier(float data) {
		return data > rootMeanSquare;
	}

}
