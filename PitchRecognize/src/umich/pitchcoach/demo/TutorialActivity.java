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
	boolean lifeBarEnabled = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		myEventStream = new PitchSequence(new String[]{"C#3", "E3", "C4", "D3", "C3", "E3", "E#3", "D4", "C#3"});

		super.onCreate(savedInstanceState);
		lifebar.setVisibility(lifebar.INVISIBLE);
		tutorial().go();
	}
	
	public void updateIncidentalUI(double pitch, double timeInSeconds) {
		//Yes, copy paste. I'm sorry
		if (lifeBarEnabled) {
			if (playmanager.currentGraph().isCurrentlyCorrect()) this.lifebar.addLives(timeInSeconds * 3);
			else this.lifebar.addLives(timeInSeconds * -1);
		}
	}
	
	public Promise playInterval()
	{
		return new PromiseFactoryPromise(new IPromiseFactory() {
			public Promise getPromise() {
				int[] steps = new int[]{0,4};
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
	
	public void enableLifeBar() {
		lifeBarEnabled = true;
		lifebar.setVisibility(lifebar.VISIBLE);
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
			new DialogPromise(this, "Welcome to pitchcoach! This program should teach you how to sing.")	
		).then(
				new DialogPromise(this, "Brace yourself! I'm about to play a note. When I'm done, I want you to try and sing it for me.")	
		).then(playVerifyLoop()).then(
			new DialogPromise(this, "Good job! Notice how the line onscreen matches the note you need to sing. Let's try it a few more times.")	
		).then(Promise.nTimes(3, new IPromiseFactory() {
			public Promise getPromise() {
				return playmanager.addGraph().then(playVerifyLoop());
			}
		})).then(new DialogPromise(this, "OK! Let's try an interval!")).then(
				new DialogPromise(this, "For intervals or scales, I'll play all the notes you need to sing, and then play the first note you need to sing again afterwards, to start you off.")).then(
						new DialogPromise(this, "This interval is a C3 to E3 interval. Give it a try.")).then(playmanager.addGraph()).then(new Runnable() {
			public void run() {
				playmanager.currentGraph().setListening();
			}
		}).then(playInterval()).then(playmanager.play()).then(Promise.nTimes(1, new IPromiseFactory() {

			@Override
			public Promise getPromise() {
				return playmanager.addGraph().then(playmanager.play());
			}
		})).then(new DialogPromise(this, "Great job!")).then(new DialogPromise(this, "The Life Bar [tm] (Awesome name, isn't it? We thought of it all by ourselves) shows you how much life you have left. If it fills up, you win! If it empties out, you lose! Let's try playing with the life bar on.")).then(
				new Runnable() {
					public void run() { enableLifeBar(); }
				}
		).then(Promise.nTimes(3, new IPromiseFactory() {

			@Override
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
