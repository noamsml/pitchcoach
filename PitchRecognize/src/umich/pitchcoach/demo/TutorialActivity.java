package umich.pitchcoach.demo;

import java.util.ArrayList;
import java.util.Arrays;

import umich.pitchcoach.LetterNotes;
import umich.pitchcoach.dataAdapt.PitchSequence;
import umich.pitchcoach.flow.DialogPromise;
import umich.pitchcoach.flow.IPromiseFactory;
import umich.pitchcoach.flow.Promise;

import android.os.Bundle;

public class TutorialActivity extends PitchActivityFramework{
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		myPitchKeeper = new PitchSequence(new String[]{"C#3", "E3", "C4", "C3"});

		super.onCreate(savedInstanceState);
		tutorial().go();
	}
	
	public Promise playScale()
	{
		class playNoteRelative implements IPromiseFactory {
			int steps;
			private double duration;
			public playNoteRelative(int steps, double duration)
			{
				this.steps = steps;
				this.duration = duration;
			}
			
			@Override
			public Promise getPromise() {
				return noteplayer.playNote(playmanager.currentGraph().getFrequency() * Math.pow(LetterNotes.stepVal, steps), duration);
			}
			
		}
		
		Promise first = new Promise(); //easiest to do
		
		for (int i : new int[]{0,2,4,5,7,9,11,12})
		{
			first = first.then(new playNoteRelative(i, 0.4));
		}
		
		return first.then(new playNoteRelative(0,0.8));
		
	}
	
	public Promise tutorial() {
		return playmanager.begin().then(
			new DialogPromise(this, "Welcome to pitchcoach!")	
		).then(setListening()).then(this.playScale()).then(playmanager.play()).then(Promise.nTimes(3, new IPromiseFactory() {
			public Promise getPromise() {
				return playmanager.addGraph().then(play());
			}
		})).then(new DialogPromise(this, "Cool! You've just finished the entire tutorial. Congrats! Click 'OK' to go back to the home screen")).then(
					new Runnable() {
						public void run() {
							finish();
						}
					}
				);
	}
	
}
