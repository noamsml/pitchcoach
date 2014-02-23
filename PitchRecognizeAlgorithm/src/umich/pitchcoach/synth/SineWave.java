package umich.pitchcoach.synth;

public class SineWave extends Wave {

	private double amplitude;
	private double freq;

	public SineWave(int rate, double amplitude, double freq) {
		super(rate);
		this.amplitude = amplitude;
		this.freq = freq;
	}

	@Override
	public double getSample(int sampleNum) {
		return amplitude * Math.sin(2 * Math.PI / rate * freq * sampleNum);
	}
}
