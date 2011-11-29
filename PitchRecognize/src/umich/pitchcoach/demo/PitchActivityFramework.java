package umich.pitchcoach.demo;

import java.util.ArrayList;
import java.util.Arrays;

import umich.pitchcoach.R;
import umich.pitchcoach.NotePlayer.NotePlayer;
import umich.pitchcoach.dataAdapt.IPitchSource;
import umich.pitchcoach.flow.PlayManagerScroll;
import umich.pitchcoach.flow.Promise;
import umich.pitchcoach.shared.IPitchReciever;
import android.app.Activity;
import android.os.Bundle;

public class PitchActivityFramework extends Activity {
	protected ScrollContainer graphcont;
	protected PlayManagerScroll playmanager;
	protected LifeBar lifebar;
	protected NotePlayer noteplayer;
	protected IPitchSource myPitchKeeper; //for now

	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mockui);
		lifebar = (LifeBar)findViewById(R.id.lifebar);
		graphcont = (ScrollContainer)findViewById(R.id.scroller);
	
		noteplayer = new NotePlayer();
		playmanager = new PlayManagerScroll(getApplicationContext(), myPitchKeeper, graphcont);	
		playmanager.setCallback(new IPitchReciever() {
			
			@Override
			public void receivePitch(double pitch, double timeInSeconds) {
				updateIncidentalUI(pitch, timeInSeconds);
			}		
			
		});

	}
	
	protected void updateIncidentalUI(double pitch, double time)
	{
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		noteplayer.die();
		playmanager.pause();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		playmanager.pause();
	}

	
	@Override
	protected void onResume() {
		super.onResume();
		
		
		noteplayer.riseFromDead();
		playmanager.unpause();
		//else playCurrentGraph();
	}
	
	protected Promise play() {
		return new Promise() {
			public void go() {
				playmanager.currentGraph().setListening();
				noteplayer.setFrequency(playmanager.currentGraph().getFrequency());
				noteplayer.setDuration(1);
				done();
			}
		}.then(noteplayer.playNote()).then(playmanager.play());
	}
}
