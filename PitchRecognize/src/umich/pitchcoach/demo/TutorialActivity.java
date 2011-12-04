package umich.pitchcoach.demo;

import java.util.ArrayList;
import java.util.Arrays;

import umich.pitchcoach.LetterNotes;
import umich.pitchcoach.NotePlayer.Note;
import umich.pitchcoach.dataAdapt.PitchSequence;
import umich.pitchcoach.flow.DialogPromise;
import umich.pitchcoach.flow.IPromiseFactory;
import umich.pitchcoach.flow.Promise;
import umich.pitchcoach.flow.PromiseFactoryPromise;

import android.app.Activity;
import android.os.Bundle;

public class TutorialActivity extends PitchActivityFramework{
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		myPitchKeeper = new PitchSequence(new String[]{"C#3", "E3", "C4", "C3"});

		super.onCreate(savedInstanceState);
		lifebar.setVisibility(lifebar.INVISIBLE);
		tutorial().go();
	}
	
	public Promise playScale()
	{
		return new PromiseFactoryPromise(new IPromiseFactory() {
			public Promise getPromise() {
				int[] steps = new int[]{0,2,4,5,7,9,11,12};
				Note[] notes = new Note[steps.length+1]; 
				
				for (int i = 0; i < steps.length; i++ )
				{
					notes[i] = new Note(playmanager.currentGraph().getFrequency() * Math.pow(LetterNotes.stepVal, steps[i]), 0.4);
				}
				notes[notes.length - 1] = new Note(playmanager.currentGraph().getFrequency(), 0.8);
				return noteplayer.playNote(notes);
			}
		});
	}
	
	//Note: this might be over-abstracting it :P
	public Promise verifyLoop(final IPromiseFactory fact) {
		final Activity that = this;
		return new PromiseFactoryPromise(new IPromiseFactory() {
			public Promise getPromise() {
				if (playmanager.currentGraph().isDone()) {
					if (playmanager.currentGraph().getFinalEvaluation() > 0) 
						return new Promise();
					else 
						return new DialogPromise(that, "Not quite. Let's try again").then(new Runnable() {
							public void run() 
							{
								playmanager.currentGraph().reset();
							}
						}).then(fact.getPromise()).then(this);
				}	
				else 
					return fact.getPromise().then(this);

			}
		});
	}
	
	public Promise playVerifyLoop()
	{
		return verifyLoop(new IPromiseFactory() {
			public Promise getPromise() {
				return play();
			}
		});
	};
	
	public Promise tutorial() {
		final Activity that = this;
		return playmanager.begin().then(
			new DialogPromise(this, "Welcome to pitchcoach!")	
		).then(playVerifyLoop()).then(Promise.nTimes(3, new IPromiseFactory() {
			public Promise getPromise() {
				return playmanager.addGraph().then(playVerifyLoop()).then(new DialogPromise(that, "Good job!"));
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
