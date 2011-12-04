package umich.pitchcoach.demo;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.content.DialogInterface;
import java.util.Arrays;

import umich.pitchcoach.LetterNotes;
import umich.pitchcoach.R;
import umich.pitchcoach.shared.IPitchReciever;
import umich.pitchcoach.threadman.RenderThreadManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import umich.pitchcoach.NotePlayer.Note;
import umich.pitchcoach.NotePlayer.NotePlayer;
import umich.pitchcoach.data.Event;
import umich.pitchcoach.data.EventStream;
import umich.pitchcoach.flow.DialogPromise;
import umich.pitchcoach.flow.PlayManagerScroll;
import umich.pitchcoach.flow.Promise;
import umich.pitchcoach.listeners.IRenderNotify;
import umich.pitchcoach.listeners.OnScrollListener;

public class PitchGraphActivity extends PitchActivityFramework {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		myEventStream = new EventStream(this);

		super.onCreate(savedInstanceState);

		playmanager.begin().then(
				new DialogPromise(this, "Get ready to Sing!")	
				).then(this.play()).then(new Runnable() {
			public void run() {
				playLoop();
			}
		}).go();
	}


	private void playLoop() {
		if (lifebar.isWin()) onWin();
		else if (lifebar.isDeath()) onDeath();
		else {
			addEvent();
		}
	}

	private Event curEvent = null;
	Iterator<String> pitches;
	private void addEvent() {
		if (curEvent == null) {
			curEvent = new Event(myEventStream.getNextEvent());
			pitches = curEvent.pitchesToSing.iterator();
			playmanager.addEventPart(pitches.next(), false, curEvent).then(this.play()).then(new Runnable() {
				public void run() {
					addEvent();
				}
			}).go();
		} else {
			if (pitches.hasNext()) {
				playmanager.addEventPart(pitches.next(), true, curEvent).then(this.play()).then(new Runnable() {
					public void run() {
						addEvent();
					}
				}).go();
			} else {
				curEvent = null;
				playLoop();
			}

		}
	}

	public void updateIncidentalUI(double pitch, double timeInSeconds) {
		if (playmanager.currentGraph().isCurrentlyCorrect()) this.lifebar.addLives(timeInSeconds * 3);
		else this.lifebar.addLives(timeInSeconds * -1);

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
		dialog.setButton(AlertDialog.BUTTON_POSITIVE, againMessage, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				Intent myIntent = new Intent(getApplicationContext(), PitchGraphActivity.class);
				startActivity(myIntent);
			}
		}
				);


		dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Back to Menu", new DialogInterface.OnClickListener() {

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
}
