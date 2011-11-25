package umich.pitchcoach.synth;

public class SquareWave extends Wave {

	private double amplitude;
	private double freq;

	public SquareWave(int rate, double amplitude, double freq) {
		super(rate);
		this.amplitude = amplitude; 
		this.freq = freq;
	}

	@Override
	public double getSample(int sampleNum) {
		int halfWLNum = (int)(sampleNum/(rate/(freq*2)));
		if (halfWLNum % 2 == 0) return amplitude;
		else return -1 * amplitude;
	}
}
