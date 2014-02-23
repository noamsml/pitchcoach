package umich.pitchcoach;

public class SpikeEliminator {
	double lastSampleVerified;
	double stashedSample;
	boolean hasStashedSample;
	double threshold;

	public SpikeEliminator(double threshold) {
		this.threshold = threshold;
		this.hasStashedSample = false;
		this.lastSampleVerified = -1;
	}

	public double getSmoothedSample(double d) {
		if (lastSampleVerified == -1) {
			lastSampleVerified = d;
			return d;
		}

		if (verifyDiff(lastSampleVerified, d)) {
			lastSampleVerified = d;
			hasStashedSample = false;
			return d;
		} else {
			if (hasStashedSample && verifyDiff(stashedSample, d)) {
				lastSampleVerified = d;
				hasStashedSample = false;
				return d;
			} else {
				hasStashedSample = true;
				stashedSample = d;
				return -1;
			}
		}
	}

	private boolean verifyDiff(double sample1, double sample2) {
		double diff = Math.max(sample1, sample2) / Math.min(sample1, sample2);
		return diff < threshold;
	}
}
