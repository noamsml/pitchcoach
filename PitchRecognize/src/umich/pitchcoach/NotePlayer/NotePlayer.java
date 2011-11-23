package umich.pitchcoach.NotePlayer;

import umich.pitchcoach.HammingWindow;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.os.Handler;

public class NotePlayer extends MediaPlayer {
	
	// originally from
	// http://marblemice.blogspot.com/2010/04/generate-and-play-tone-in-android.html
	// and modified by Steve Pomeroy <steve@staticfree.info>
	
	private int duration = 3; // seconds
	private final int sampleRate = 44100;
	private final int numSamples = duration * sampleRate;
	private final float[] sample = new float[numSamples];
	private double freqOfTone = 440; // hz

	private final byte generatedSnd[] = new byte[2 * numSamples];

	Handler handler = new Handler();
	Handler uiHandler;
	Runnable callback;
	
	public NotePlayer(Runnable callback){
		super();
		this.callback = callback;
	}
	
	
	public void setFrequency(double frequency){
		this.freqOfTone = frequency;
	}
	public void setDuration(int duration){
		this.duration = duration;
	}

	public void playNote(){
		// Use a new tread as this can take a while
		final Thread thread = new Thread(new Runnable() {
			public void run() {
				genTone();
				handler.post(new Runnable() {

					public void run() {
						playSound();
						handler.postDelayed(callback, duration*1000+1000);
					}
				});
			}
		});
		thread.setName("Play Thread");
		thread.start();
	}
	
	
	public void playNote(double frequency, Integer duration){
		this.setFrequency(frequency);
		this.setDuration(duration);
		this.playNote();
	}
	
	//##################
	
	public void playNote(Note note){	

	}
	
	public void playNote(String note){	

	}
	
	
	//##################
	
	void genTone() {
		int ATTACK_SIZE = 400;
		// fill out the array
		for (int i = 0; i < numSamples; ++i) {
			sample[i] = (float)(Math.sin(2 * Math.PI * i / (sampleRate / freqOfTone)));
			
			if (i < ATTACK_SIZE) sample[i] *= ((float)i)/ATTACK_SIZE;
			if (i > numSamples - ATTACK_SIZE) sample[i] *= ((float)numSamples-i)/ATTACK_SIZE; 
		}
		
		

		// convert to 16 bit pcm sound array
		// assumes the sample buffer is normalised.
		int idx = 0;
		for (final double dVal : sample) {
			// scale to maximum amplitude
			final short val = (short) ((dVal * 32767));
			// in 16 bit wav PCM, first byte is the low order byte
			generatedSnd[idx++] = (byte) (val & 0x00ff);
			generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);

		}
	}

	void playSound() {
		final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
				sampleRate, AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT, numSamples,
				AudioTrack.MODE_STATIC);
		audioTrack.write(generatedSnd, 0, generatedSnd.length);
		audioTrack.play();
	}

	
}

	
	
	
	
	
	