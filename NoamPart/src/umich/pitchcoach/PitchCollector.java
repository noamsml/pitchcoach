package umich.pitchcoach;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Looper;

public class PitchCollector extends Thread {
	PitchReciever notifyRecv;
	AudioRecord audioRec;
	//float[] dataBuffer;
	public final int RATE = 44100;
	//public final int SAMPLERATE = 4; //4 times / sec
	public final int NUMSAMPLES = 256;
	public boolean done;
	
	public PitchCollector(PitchReciever notifyRecv)	
	{
		this.notifyRecv = notifyRecv;
		//this.dataBuffer = new float[NUMSAMPLES];
		done = false;
	}
	
	public void run() {
		audioRec = new AudioRecord(MediaRecorder.AudioSource.DEFAULT, RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT,
							4*AudioRecord.getMinBufferSize(RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT));
		audioRec.startRecording();
		
		while(!done)
		{
			handleAudioData();
		}
		
		audioRec.stop();
	}
	
	public void onPitch(final double pitch)
	{
		notifyRecv.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				notifyRecv.receivePitch(pitch);
			}
			
		});
	}
	
	public void handleAudioData()
	{
		short[] sampleBuffer = new short[NUMSAMPLES];
		int size = audioRec.read(sampleBuffer, 0, NUMSAMPLES);
		float sum = 0;
		for (int i = 0; i < size; i++)
		{
			sum += Math.abs((float)sampleBuffer[i]);
		}
		if (sum/size > 1000)
			onPitch(1.0);
		else 
			onPitch(0.0);
	}
	
	
}