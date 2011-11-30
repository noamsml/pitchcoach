package umich.pitchcoach.synth;

public class Envelope extends Wave{
	private int len;
	private int lenEnvelope;
	private Wave orig;
	
	public Envelope(Wave orig, double lenSeconds, double lenEnvelopeSeconds) {
		super(orig.getRate());
		this.orig = orig;
		len = this.getNumSamples(lenSeconds);
		lenEnvelope = this.getNumSamples(lenEnvelopeSeconds);
	}
	@Override
	public double getSample(int sampleNum) {
		if (sampleNum < lenEnvelope) return orig.getSample(sampleNum) * ((double)sampleNum)/lenEnvelope;
		if (sampleNum > len - lenEnvelope) return orig.getSample(sampleNum) * ((double)(len - sampleNum))/((double)lenEnvelope);
		return orig.getSample(sampleNum);
	}
}
