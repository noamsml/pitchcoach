package umich.pitchcoach.demo;

import java.util.ArrayList;
import java.util.Arrays;

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
	
	public Promise tutorial() {
		return playmanager.begin().then(
			new DialogPromise(this, "Welcome to pitchcoach!")	
		).then(this.play()).then(Promise.nTimes(3, new IPromiseFactory() {
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
