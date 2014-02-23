package umich.pitchcoach.synth;

public abstract class Wave {
	protected int rate;

	public Wave(int rate) {
		this.rate = rate;
	}

	public int getRate() {
		return rate;
	}

	public void synthWave(short[] dataField, double offsetInSeconds,
			double lenInSeconds) {
		int numSamples = getNumSamples(lenInSeconds);
		int beginOffset = getNumSamples(offsetInSeconds);

		for (int i = 0; i < numSamples; i++) {
			short value = (short) ((getSample(i) * 32767));
			// in 16 bit wav PCM, first byte is the low order byte
			dataField[beginOffset + i] = (short) value;
		}
	}

	public int getNumSamples(double lenInSeconds) {
		return numSamples(lenInSeconds, rate);
	}

	public static int numSamples(double lenInSeconds, int rate) {
		return (int) (lenInSeconds * rate);
	}

	public Wave add(Wave wave2) {
		return new SumWave(this, wave2);
	}

	public abstract double getSample(int sampleNum);

	public Wave envelope(double len, double lenEnv) {
		return new Envelope(this, len, lenEnv);
	}
}
