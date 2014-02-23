package umich.pitchcoach;

import java.io.IOException;

import umich.pitchcoach.shared.IPitchReciever;
import umich.pitchcoach.threadman.PitchThreadSpawn;
import umich.pitchcoach.demo.RangeSelect;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

public class RangePitchDetect extends Activity implements IPitchReciever {
	/** Called when the activity is first created. */
	TextView pitchDisplay;
	TextView timeLeft;
	PitchThreadSpawn pitchService;
	Handler handler;
	private double lowestPitch = 20000;
	private double secLowestPitch = 20000;
	private double highestPitch = 20;
	private double secHighestPitch = 20;
	private double timer = 0.0;
	private char lowHigh = 'l';
	private ProgressBar progress;

	private double roundToNearest(double val, double to) {
		return Math.round(val / to) * to;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.autorangeselect);
		pitchDisplay = (TextView) findViewById(R.id.autopitchamt);
		timeLeft = (TextView) findViewById(R.id.autoPitchText);
		pitchService = new PitchThreadSpawn();
		progress = (ProgressBar) findViewById(R.id.singBar);
		progress.setMax(100);
		progress.setProgress(0);

		handler = new Handler();
	}

	@Override
	public void receivePitch(double pitch, double time) {
		pitchDisplay.setText(LetterNotes.freqToNoteSpec(pitch)
		// pitch
				);

		timer = timer + time;

		progress.setProgress((int) (timer / 3.0 * 100));

		if (timer > 3.0) {
			if (lowHigh == 'l')
				dialogAndReset(secLowestPitch);
			else
				dialogAndFinish(secHighestPitch);
		}

		if (lowHigh == 'l') {
			if (pitch < lowestPitch)
				lowestPitch = pitch;
			else if (pitch < secLowestPitch)
				secLowestPitch = pitch;
		} else {
			if (pitch > highestPitch)
				highestPitch = pitch;
			else if (pitch > secHighestPitch)
				secHighestPitch = pitch;

		}
	}

	private void dialogAndReset(double pitch) {
		timer = 0;
		progress.setProgress(0);
		pitchService.stopPitchService();
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				"The lowest pitch you sang is "
						+ LetterNotes.freqToNoteSpec(pitch)
						+ ". Continue to your high range?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								lowHigh = 'h';
								dialog.cancel();
								startPService();
							}
						})
				.setNegativeButton("Try again",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								lowestPitch = 20000;
								secLowestPitch = 20000;
								dialog.cancel();
								startPService();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void dialogAndFinish(double pitch) {
		pitchService.stopPitchService();
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final int intLow = (int) roundToNearest(secLowestPitch, 6);
		final int intHigh = (int) roundToNearest(secHighestPitch, 6);
		if (intLow >= intHigh) {
			builder.setMessage(
					"The highest pitch you sang was lower than the lowest! Please try again!")
					.setCancelable(false)
					.setNegativeButton("Try Again",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									finish();
									Intent myIntent = new Intent(
											getApplicationContext(),
											RangePitchDetect.class);
									startActivity(myIntent);
								}
							});
		} else {
			builder.setMessage(
					"The highest pitch you sang is "
							+ LetterNotes.freqToNoteSpec(pitch)
							+ ". Are you finished?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									writeContent(intLow, intHigh);
									finish();
								}
							})
					.setNegativeButton("Try again",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									timer = 0;
									timeLeft.setText("3 more seconds!");
									highestPitch = 20;
									secHighestPitch = 20;
									dialog.cancel();
									startPService();
								}
							});
		}
		AlertDialog alert = builder.create();
		alert.show();

	}

	private void writeContent(int minFreq, int maxFreq) {
		try {
			String toWrite = Integer.toString(minFreq) + " "
					+ Integer.toString(maxFreq);
			RangeSelect.fos = openFileOutput(RangeSelect.FILENAME,
					Context.MODE_PRIVATE);
			RangeSelect.fos.write(toWrite.getBytes());
			RangeSelect.fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void startPService() {
		pitchService.startPitchService(this, handler);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		pitchService.startPitchService(this, handler);
	}

	@Override
	protected void onPause() {
		super.onPause();
		pitchService.stopPitchService();
	}
}