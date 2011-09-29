package umich.pitchcoach;

import umich.pitchcoach.shared.IPitchReciever;
import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Looper;

import edu.emory.mathcs.jtransforms.dct.FloatDCT_1D;

public class PitchThread extends Thread {
	IPitchReciever notifyRecv;
	AudioRecord audioRec;
	Handler receivingHandler;
	
	//float[] dataBuffer;
	public final int RATE = 44100;
	public final int NUMSAMPLES = RATE;
	public boolean done;
	
	public PitchThread(IPitchReciever notifyRecv, Handler receivingHandler)	
	{
		this.notifyRecv = notifyRecv;
		//this.dataBuffer = new float[NUMSAMPLES];
		done = false;
		this.receivingHandler = receivingHandler; 
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
	
	public void onPitch(final double pitch, final double duration)
	{
		receivingHandler.post(new Runnable() {

			@Override
			public void run() {
				notifyRecv.receivePitch(pitch, duration);
			}
			
		});
	}
	
	public void handleAudioData()
	{
		short[] sampleBuffer = new short[NUMSAMPLES];
		float[] floatBuffer = new float[NUMSAMPLES];
		int size = audioRec.read(sampleBuffer, 0, NUMSAMPLES);
		float sum = 0;
		for (int i = 0; i < size; i++)
		{
			sum += Math.abs((float)sampleBuffer[i]);
		}
		if (sum/size > 1000)
		{
			for (int i = 0; i < size; i++)
			{
				floatBuffer[i] = (float)sampleBuffer[i];
			}
			
			doDCTanalysis(floatBuffer, size);
		}
	}

	private void doDCTanalysis(float[] floatBuffer, int size) {
		FloatDCT_1D dct = new FloatDCT_1D(size);
		dct.forward(floatBuffer, false);
		int topFreq = 1;
		for (int i = 1; i < size; i++)
		{
			if (floatBuffer[i] > floatBuffer[topFreq]) {
				topFreq = i;
			}
		}
		
		onPitch(DCTIndexToFreq(topFreq, size), getTimeDuration(size));
	}
	
	private double DCTIndexToFreq(int index, int size)
	{
		return ((double)(index * RATE)) / ((double)2 * size);
	}
	
	private double getTimeDuration(int size)
	{
		return size/((double)RATE);
	}
	
}