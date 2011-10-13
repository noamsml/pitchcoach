package umich.pitchcoach;


public class SineWave implements ISampleSource {
	int wavelen;
	float[] data;
	public SineWave(float amp, int wavelen)
	{
		data = new float[wavelen];
		this.wavelen = wavelen;
		for (int i = 0; i < wavelen; i++)
		{
			data[i] = (float)(amp * Math.sin(i * 2 * Math.PI / wavelen));
		}
	}
	@Override
	public int readData(float[] data) {
		for (int i = 0; i < data.length; i++)
		{
			data[i] = this.data[i % wavelen];
		}
		
		return data.length;
	}
	
}
