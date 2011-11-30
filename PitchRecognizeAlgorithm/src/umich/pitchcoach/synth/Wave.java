package umich.pitchcoach.synth;

public abstract class Wave {
	protected int rate;
	
	public Wave(int rate)
	{
		this.rate = rate;
	}
	
	public int getRate() {
		return rate;
	}
	
	public void synthWave(byte[] dataField, double lenInSeconds) {
		int numSamples = getNumSamples(lenInSeconds);
		for (int i = 0; i < numSamples; i++)
		{
			short value = (short) ((getSample(i) * 32767));
			// in 16 bit wav PCM, first byte is the low order byte
			dataField[i*2] = (byte) (value & 0x00ff);
			dataField[i*2+1] = (byte) ((value & 0xff00) >>> 8);
		}
	}
	
	public int getNumSamples(double lenInSeconds) {
		return (int)(lenInSeconds*rate); 
	}
	
	public int getByteLenarray(double lenInSeconds) {
		return getNumSamples(lenInSeconds)*2; 
	}
	
	public Wave add(Wave wave2)
	{
		return new SumWave(this, wave2);
	}
	
	public abstract double getSample(int sampleNum);
	
	public Wave envelope(double len, double lenEnv)
	{
		return new Envelope(this, len, lenEnv);
	}
}
