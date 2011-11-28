package umich.pitchcoach.demo;

import java.util.ArrayList;

import android.content.DialogInterface;
import java.util.Arrays;

import umich.pitchcoach.LetterNotes;
import umich.pitchcoach.R;
import umich.pitchcoach.shared.IPitchReciever;
import umich.pitchcoach.threadman.RenderThreadManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import umich.pitchcoach.NotePlayer.NotePlayer;
import umich.pitchcoach.flow.PlayManagerScroll;
import umich.pitchcoach.flow.Promise;
import umich.pitchcoach.listeners.IRenderNotify;
import umich.pitchcoach.listeners.OnScrollListener;

public class PitchGraphActivity extends Activity {

	PitchKeeper myPitchKeeper;
	ScrollContainer graphcont;
	PlayManagerScroll playmanager;
	LifeBar lifebar;
	NotePlayer noteplayer;
	
	boolean returning = false;
	
	public static String[] singTheseNotes = new String[]{"C3", "C#3", "D3", "D#3", "E3", "F3", "F#3", "G3", "G#3", "A3", "A#3", "B3"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mockui);
		lifebar = (LifeBar)findViewById(R.id.lifebar);
		graphcont = (ScrollContainer)findViewById(R.id.scroller);
	
		
		noteplayer = new NotePlayer();
		
		myPitchKeeper = new PitchKeeper(new ArrayList<String>(Arrays.asList(singTheseNotes)));
		playmanager = new PlayManagerScroll(getApplicationContext(), myPitchKeeper, graphcont);
		
		playmanager.setCallback(new IPitchReciever() {
			
			@Override
			public void receivePitch(double pitch, double timeInSeconds) {
				updateIncidentalUI(pitch, timeInSeconds);
			}		
			
		});
		
		
		playmanager.begin().then(this.play()).then(new Runnable() {
			public void run() {
				playLoop();
			}
		}).go();
		
	}

	
	private Promise play() {
		return new Promise() {
			public void go() {
				playmanager.currentGraph().setListening();
				noteplayer.setFrequency(playmanager.currentGraph().getFrequency());
				noteplayer.setDuration(1);
				done();
			}
		}.then(noteplayer.playNote()).then(playmanager.play());
	}


	private void playLoop() {
		if (lifebar.isWin()) onWin();
		else if (lifebar.isDeath()) onDeath();
		else {
			playmanager.addGraph().then(this.play()).then(new Runnable() {
				public void run() {
					playLoop();
				}
			}).go();
		}
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
		returning = true;
	}

	public void updateIncidentalUI(double pitch, double timeInSeconds) {
		if (playmanager.currentGraph().isCurrentlyCorrect()) this.lifebar.addLives(timeInSeconds * 15);
		else this.lifebar.addLives(timeInSeconds * -5);
		
	}
		
	private void onDeath() {
		AlertDialog deathAlert;
		deathAlert = new AlertDialog.Builder(this).create();
		deathAlert.setMessage("You ran out of life. Might wanna keep that day job.");
		deathAlert.setTitle("You died");

		buttonify(deathAlert, "Try Again");
		deathAlert.show();
		
	}
	
	private void buttonify(AlertDialog dialog, String againMessage) {
		dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface arg0) {
				finish();
				
			}
		});
		dialog.setButton(dialog.BUTTON_POSITIVE, againMessage, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				 Intent myIntent = new Intent(getApplicationContext(), PitchGraphActivity.class);
			     startActivity(myIntent);
				}
			}
		);
		
	
		dialog.setButton(dialog.BUTTON_NEGATIVE, "Back to Menu", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				 finish();
			}
		});
	}

	private void onWin() {
		AlertDialog winAlert;
		winAlert = new AlertDialog.Builder(this).create();
		winAlert.setMessage("Yay! You won!");
		winAlert.setTitle("You won");

		buttonify(winAlert, "Do it again!");
		winAlert.show();
		
	}

	public void renderPause() {
		//TODO: Manually generated method stub
	}
	
	public void renderUnPause() {
		//TODO: Manually generated method stub
	}
}
