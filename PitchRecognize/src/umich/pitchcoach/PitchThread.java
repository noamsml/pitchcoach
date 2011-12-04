package umich.pitchcoach;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import umich.pitchcoach.shared.IPitchReciever;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;


public class PitchThread extends Thread {
	IPitchReciever notifyRecv;
	AudioRecord audioRec;
	Handler receivingHandler;
	circularShortBuffer persistSampleBuffer;
	IPitchAnalyzer analyzer;
	

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
		persistSampleBuffer  = new circularShortBuffer(Invariants.BUFSIZE);
		sampleBuffer = new short[Invariants.NUMSAMPLES];
		analyzer = new PitchAnalyzerDCT_HPS(Invariants.BUFSIZE);
		
		buflock = new ReentrantLock();
		dumpBuffer = new circularShortBuffer(Invariants.RATE * 2); //DELME
		this.setName("Pitch Thread " + this.getId());
		
	}
	
	public void run() {
		audioRec = new AudioRecord(MediaRecorder.AudioSource.VOICE_RECOGNITION, Invariants.RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT,
							4*AudioRecord.getMinBufferSize(Invariants.RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT));
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
		
		int size = audioRec.read(sampleBuffer, 0, Invariants.NUMSAMPLES);
		
		persistSampleBuffer.dumpData(sampleBuffer, size);
		
		//DELME
		buflock.lock();
		dumpBuffer.dumpData(sampleBuffer, size);
		buflock.unlock();
		//DELME
		
		double pitch;
		try {
			pitch = analyzer.getPitch(persistSampleBuffer, Invariants.RATE);
		} catch (NoMoreDataException e) {
			return;
		}
		
		if (pitch != -1) onPitch(pitch, getTimeDuration(size));
		
	}

	private double getTimeDuration(int size)
	{
		return size/((double)Invariants.RATE);
	}
	
}