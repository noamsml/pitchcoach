package umich.pitchcoach.demo;

import java.util.ArrayList;
import java.util.Arrays;

import umich.pitchcoach.R;
import umich.pitchcoach.NotePlayer.NotePlayer;
import umich.pitchcoach.data.EventStream;
import umich.pitchcoach.dataAdapt.IPitchSource;
import umich.pitchcoach.flow.IPromiseFactory;
import umich.pitchcoach.flow.PlayManagerScroll;
import umich.pitchcoach.flow.Promise;
import umich.pitchcoach.shared.IPitchReciever;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PitchActivityFramework extends Activity {
	protected ScrollContainer graphcont;
	protected PlayManagerScroll playmanager;
	protected LifeBar lifebar;
	protected NotePlayer noteplayer;
	protected IPitchSource myEventStream; // for now
	protected Button pauseButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mockui);
		lifebar = (LifeBar) findViewById(R.id.lifebar);
		graphcont = (ScrollContainer) findViewById(R.id.scroller);
		pauseButton = (Button) findViewById(R.id.pauseBtn);

		noteplayer = new NotePlayer();
		playmanager = new PlayManagerScroll(getApplicationContext(),
				myEventStream, graphcont);
		playmanager.setCallback(new IPitchReciever() {

			@Override
			public void receivePitch(double pitch, double timeInSeconds) {
				updateIncidentalUI(pitch, timeInSeconds);
			}

		});

		pauseButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				renderPause();
			}
		});

	}

	protected void updateIncidentalUI(double pitch, double time) {
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
		// else playCurrentGraph();
	}

	protected Promise setListening() {
		return new Promise() {
			public void go() {
				playmanager.currentGraph().setListening();
				done();
			}
		};
	}

	protected Promise play() {
		return setListening().then(new IPromiseFactory() {
			public Promise getPromise() {
				if (playmanager.currentGraph().isSilenced()) {
					return playmanager.play();
				} else {
					if (playmanager.currentGraph().getEvent() == null) {
						return noteplayer.playNote(
								playmanager.currentGraph().getFrequency(), 1)
								.then(playmanager.play());
					} else {
						return noteplayer
								.playNote(
										playmanager.currentGraph().getEvent().notesToPlay)
								.then(playmanager.play());
					}
				}
			}
		});
	}

	public void renderPause() { // Use DialogFragments, perhaps?

		noteplayer.die();
		playmanager.pause();

		final AlertDialog pauseAlert;
		pauseAlert = new AlertDialog.Builder(this).create();
		pauseAlert.setTitle("Paused");
		final Activity that = this;
		pauseAlert.setButton(AlertDialog.BUTTON_POSITIVE, "Restart",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						finish();
						Intent myIntent = new Intent(getApplicationContext(),
								that.getClass());
						startActivity(myIntent);
					}
				});
		pauseAlert.setButton(AlertDialog.BUTTON_NEUTRAL, "Resume",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						pauseAlert.cancel();
						renderUnPause();
					}
				});
		pauseAlert
				.setOnDismissListener(new DialogInterface.OnDismissListener() {

					@Override
					public void onDismiss(DialogInterface dialog) {
						renderUnPause();
					}
				});

		pauseAlert.setButton(AlertDialog.BUTTON_NEGATIVE, "Back to Menu",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						finish();
					}
				});
		pauseAlert.show();
	}

	public void renderUnPause() {
		noteplayer.riseFromDead();
		playmanager.unpause();
	}
}
