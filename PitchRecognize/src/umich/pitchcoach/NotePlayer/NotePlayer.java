package umich.pitchcoach.NotePlayer;

import java.util.ArrayList;
import java.util.LinkedList;

import umich.pitchcoach.HammingWindow;
import umich.pitchcoach.flow.Promise;
import umich.pitchcoach.synth.SineWave;
import umich.pitchcoach.synth.SquareWave;
import umich.pitchcoach.synth.Wave;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.os.Handler;

public class NotePlayer extends MediaPlayer {
	
	// originally from
	// http://marblemice.blogspot.com/2010/04/generate-and-play-tone-in-android.html
	// and modified by Steve Pomeroy <steve@staticfree.info>
	
	private double duration; // seconds
	private final int sampleRate = 44100;
	private int numSamples;
	private double freqOfTone = 440; // hz
	private boolean paused = false;

	private byte[] generatedSnd;

	Handler handler = new Handler();
	Handler uiHandler;
	
	LinkedList<Promise> thingsToDo = new LinkedList<Promise>(); //HACK
	
	public NotePlayer(){
		super();
	}
	
	
	public void setFrequency(double frequency){
		this.freqOfTone = frequency;
	}
	public void setDuration(double duration){
		this.duration = duration;
	}

	public Promise playNote(){
		Promise p = new Promise() {
			public void go() {
				final Promise that = this; //Don't ask why this is necessary, it's a braindead reason
				final Thread thread = new Thread(new Runnable() {
					public void run() {
						genTone();
						handler.post(new Runnable() {

							public void run() {
								playSound();
								//Someone kill me now
								handler.postDelayed(new Runnable() {

									@Override
									public void run() {
										if (!paused) 
											done();
										else thingsToDo.add(that);
									}
									
								}, (int)(duration*1000));
							}
						});
					}
				});
				thread.setName("Play Thread");
				thread.start();
			}
		};
		
		return p;
	}
	
	
	public Promise playNote(double frequency, double duration){
		this.setFrequency(frequency);
		this.setDuration(duration);
		return this.playNote();
	}
	
	//##################
	
	public void playNote(Note note){	

	}
	
	public void playNote(String note){	

	}
	
	
	//##################
	
	void genTone() {
		// fill out the array
		
		Wave w;
		//w = new SquareWave(sampleRate, 0.4, freqOfTone).add(new SineWave(sampleRate, 0.6, freqOfTone));
		w = new SineWave(sampleRate, 0.5, freqOfTone).add(new SineWave(sampleRate, 0.25, freqOfTone * 2)).add(
				new SineWave(sampleRate, 0.2, freqOfTone * 3)).add(new SineWave(sampleRate, 0.05, freqOfTone * 4));
		this.numSamples = w.getByteLenarray(duration);
		this.generatedSnd = new byte[numSamples];
		w.synthWave(generatedSnd, duration);
	}

	void playSound() {
		final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
				sampleRate, AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT, numSamples,
				AudioTrack.MODE_STATIC);
		audioTrack.write(generatedSnd, 0, generatedSnd.length);
		audioTrack.play();
	}
	
	public void die() {
		paused = true;
	}
	
	public void riseFromDead() {
		paused = false;
		while (thingsToDo.size() > 0)
		{
			thingsToDo.removeFirst().done();
		}
	}
	
}

	
	
	
	
	
	