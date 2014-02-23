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

public class NotePlayer {

	// originally from
	// http://marblemice.blogspot.com/2010/04/generate-and-play-tone-in-android.html
	// and modified by Steve Pomeroy <steve@staticfree.info>
	// adapted to PitchCoach by Noam Samuel and Lu Huang

	private final int sampleRate = 44100;
	private boolean paused = false;

	private byte[] generatedSnd;

	Handler handler = new Handler();
	Handler uiHandler;

	LinkedList<Promise> thingsToDo = new LinkedList<Promise>(); // HACK

	public NotePlayer() {
		super();
	}

	public double getDuration(Note[] notes) {
		double duration = 0;
		for (Note n : notes) {
			duration += n.time;
		}
		return duration;
	}

	public void genTone(Note[] notes, short[] toneBuf) {
		double runningDuration = 0;
		for (Note n : notes) {
			genTone(toneBuf, n.freq, runningDuration, n.time);
			runningDuration += n.time;
		}
	}

	public Promise playNote(final Note[] notes) {
		Promise p = new Promise() {
			public void go() {
				final Promise that = this; // Don't ask why this is necessary,
											// it's a braindead reason
				final Thread thread = new Thread(new Runnable() {
					public void run() {
						final double duration = getDuration(notes);
						final int numSamples = Wave.numSamples(duration,
								sampleRate);
						final short[] toneBuf = new short[numSamples];

						genTone(notes, toneBuf);
						handler.post(new Runnable() {

							public void run() {
								playSound(toneBuf, numSamples);
								// Someone kill me now
								handler.postDelayed(new Runnable() {

									@Override
									public void run() {
										if (!paused)
											done();
										else
											thingsToDo.add(that);
									}

								}, (int) (duration * 1000 + 100));
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

	public Promise playNote(double frequency, double duration) {

		return this.playNote(new Note[] { new Note(frequency, duration) });
	}

	// ##################

	void genTone(short[] generatedSnd, double freqOfTone, double startTime,
			double lenTime) {
		// fill out the array

		Wave w;

		w = new SineWave(sampleRate, 0.5, freqOfTone)
				.add(new SineWave(sampleRate, 0.25, freqOfTone * 2))
				.add(new SineWave(sampleRate, 0.2, freqOfTone * 3))
				.add(new SineWave(sampleRate, 0.05, freqOfTone * 4))
				.envelope(lenTime, 0.1);

		// w = new SquareWave(sampleRate, 0.5, freqOfTone).envelope(lenTime,
		// 0.1);
		w.synthWave(generatedSnd, startTime, lenTime);
	}

	void playSound(short[] generatedSnd, int numSamples) {
		final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
				sampleRate, AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT, numSamples * 4, // HACK:
																// NumSamples
																// seems to be
																// too small. No
																// clue why.
				AudioTrack.MODE_STATIC);
		audioTrack.write(generatedSnd, 0, generatedSnd.length);
		audioTrack.play();
	}

	public void die() {
		paused = true;
	}

	public void riseFromDead() {
		paused = false;
		while (thingsToDo.size() > 0) {
			thingsToDo.removeFirst().done();
		}
	}

}
