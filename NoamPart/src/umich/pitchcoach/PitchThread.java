package umich.pitchcoach;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import umich.pitchcoach.shared.IPitchReciever;
import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Looper;


public class PitchThread extends Thread {
	IPitchReciever notifyRecv;
	AudioRecord audioRec;
	Handler receivingHandler;
	circularShortBuffer persistSampleBuffer;
	IPitchAnalyzer analyzer;
	
	public final int RATE = 44100;
	public final int NUMSAMPLES = 1337;
	public final int BUFSIZE = 8192;
	public boolean done;
	
	//TODO remove these
	public Lock buflock;
	public circularShortBuffer dumpBuffer;
	
	short[] sampleBuffer;
	
	public PitchThread(IPitchReciever notifyRecv, Handler receivingHandler)	
	{
		this.notifyRecv = notifyRecv;
		//this.dataBuffer = new float[NUMSAMPLES];
		done = false;
		this.receivingHandler = receivingHandler; 
		persistSampleBuffer  = new circularShortBuffer(BUFSIZE);
		sampleBuffer = new short[NUMSAMPLES];
		analyzer = new PitchAnalyzerDCT(BUFSIZE);
		
		buflock = new ReentrantLock();
		dumpBuffer = new circularShortBuffer(RATE * 2); //DELME
		
	}
	
	public void run() {
		audioRec = new AudioRecord(MediaRecorder.AudioSource.VOICE_RECOGNITION, RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT,
							4*AudioRecord.getMinBufferSize(RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT));
		audioRec.startRecording();
		
		while(!done)
		{
			handleAudioData();
		}
		
		audioRec.stop();
	}
	
	public void onPitch(final double pitch, final double duration)
	{
		receivingHandler.post(new Runnable() {

			@Override
			public void run() {
				notifyRecv.receivePitch(pitch, duration);
			}
			
		});
	}
	
	//Code, You are UNCLEAN!!!
	//TODO Refactor
	public void handleAudioData()
	{
		
		int size = audioRec.read(sampleBuffer, 0, NUMSAMPLES);
		
		persistSampleBuffer.dumpData(sampleBuffer, size);
		
		//DELME
		buflock.lock();
		dumpBuffer.dumpData(sampleBuffer, size);
		buflock.unlock();
		//DELME
		
		double pitch = analyzer.getPitch(persistSampleBuffer, RATE);
		
		if (pitch != -1) onPitch(pitch, getTimeDuration(size));
		
	}

	private double getTimeDuration(int size)
	{
		return size/((double)RATE);
	}
	
}