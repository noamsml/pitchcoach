package umich.pitchcoach.test;

import umich.pitchcoach.IPitchAnalyzer;
import umich.pitchcoach.NoMoreDataException;
import umich.pitchcoach.PitchAnalyzerDCT;
import umich.pitchcoach.SineWave;
//import android.os.Debug;
import android.os.Handler;
import android.os.SystemClock;
public class AnalysisTestingThread extends Thread {
	ILogReceiver logger;
	Handler handler;
	SineWave wave;
	int RATE = 44100;
	
	public AnalysisTestingThread(ILogReceiver logger, Handler handler)
	{
		this.logger = logger;
		this.handler = handler;
		wave = new SineWave(1000f, RATE/4);
	}
	
	@Override
	public void run()
	{
		
		logString(String.format("%s\t%s\t%s\t%s", "SIZE", "STIME", "ATIME", "BSIZE"));
		int size = 256;
		while (size <= RATE)
		{
			runTest(size);
			size *= 2;
		}
		
	}
	
	private void logString(final String s)
	{
		handler.post(new Runnable () {

			@Override
			public void run() {
				logger.getLogString(s);
			}
			
		});
	}
	
	@SuppressWarnings("unused")
	private void runTest(int sampleSize)
	{
		//if (sampleSize == 8192) Debug.startMethodTracing("pitchcoach.trace");
		IPitchAnalyzer analyzer = new PitchAnalyzerDCT(sampleSize);
		double time  = SystemClock.currentThreadTimeMillis();
		try {
			double pitch = analyzer.getPitch(wave, RATE);
		} catch (NoMoreDataException e) {
			//This will never ever occur
		}
		time = SystemClock.currentThreadTimeMillis() - time;
		
		logString(String.format("%s\t%.2f\t%.2f\t%.2f", sampleSize, 1000 * (float)sampleSize/RATE, time, analyzer.bucketSize(RATE)));
		//Debug.stopMethodTracing();
	}
}
