package umich.pitchcoach.synth;

public class SumWave extends Wave {

	Wave wave1;
	Wave wave2;
	
	public SumWave(Wave wave1, Wave wave2) {
		super(wave1.getRate());
		this.wave1 = wave1;
		this.wave2 = wave2;
	}

	@Override
	public double getSample(int sampleNum) {
		return wave1.getSample(sampleNum) + wave2.getSample(sampleNum);
	}
}
