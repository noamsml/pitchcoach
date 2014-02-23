package umich.pitchcoach.demo;

/*
 * http://en.wikipedia.org/wiki/Scientific_pitch_notation for pitch values
 * 
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import umich.pitchcoach.R;
import umich.pitchcoach.RangePitchDetect;
import umich.pitchcoach.NotePlayer.NotePlayer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class RangeSelect extends Activity {
	/** Called when the activity is first created. */
	public static FileOutputStream fos;
	public static String FILENAME = "singing_range.txt";
	public static String DIFFICULTY_FILENAME = "difficulty.txt";
	private int minFreq, maxFreq;
	private NotePlayer noteplayer;
	private Button bassSelect, tenorSelect, altoSelect, sopranoSelect,
			autoDetect, easySelect, mediumSelect, hardSelect, done;

	private static String BASS_RANGE = "82 330";
	private static String TENOR_RANGE = "130 523";
	private static String ALTO_RANGE = "196 660";
	private static String SOPRANO_RANGE = "262 1047";

	TextView thisTxt;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		noteplayer = new NotePlayer();

		setContentView(R.layout.rangeselect);

		thisTxt = (TextView) findViewById(R.id.changeTxt);

		bassSelect = (Button) findViewById(R.id.bassBtn);
		bassSelect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				minFreq = 82; // E2
				maxFreq = 330; // E4
				noteplayer.playNote(minFreq, 1)
						.then(noteplayer.playNote(maxFreq, 1)).go();
				writeContent(BASS_RANGE, FILENAME);
				setVocalRange(bassSelect);
			}
		});

		tenorSelect = (Button) findViewById(R.id.tenorBtn);
		tenorSelect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				minFreq = 130; // C3
				maxFreq = 523; // C5
				noteplayer.playNote(minFreq, 1)
						.then(noteplayer.playNote(maxFreq, 1)).go();
				writeContent(TENOR_RANGE, FILENAME);
				setVocalRange(tenorSelect);
			}
		});

		altoSelect = (Button) findViewById(R.id.altoBtn);
		altoSelect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				minFreq = 196; // G3
				maxFreq = 660; // E5
				noteplayer.playNote(minFreq, 1)
						.then(noteplayer.playNote(maxFreq, 1)).go();
				writeContent(ALTO_RANGE, FILENAME);
				setVocalRange(altoSelect);
			}
		});

		sopranoSelect = (Button) findViewById(R.id.sopranoBtn);
		sopranoSelect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				minFreq = 262; // C4
				maxFreq = 1047; // C6
				noteplayer.playNote(minFreq, 1)
						.then(noteplayer.playNote(maxFreq, 1)).go();
				writeContent(SOPRANO_RANGE, FILENAME);
				setVocalRange(sopranoSelect);
			}
		});

		autoDetect = (Button) findViewById(R.id.autoRange);
		autoDetect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent myIntent = new Intent(view.getContext(),
						RangePitchDetect.class);
				startActivityForResult(myIntent, 0);
			}
		});

		done = (Button) findViewById(R.id.doneBtn);
		done.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});

		easySelect = (Button) findViewById(R.id.easyBtn);
		easySelect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				writeContent("e", DIFFICULTY_FILENAME);
				setDifficulty(easySelect);
			}
		});

		mediumSelect = (Button) findViewById(R.id.mediumBtn);
		mediumSelect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				writeContent("m", DIFFICULTY_FILENAME);
				setDifficulty(mediumSelect);
			}
		});

		hardSelect = (Button) findViewById(R.id.hardBtn);
		hardSelect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				writeContent("h", DIFFICULTY_FILENAME);
				setDifficulty(hardSelect);
			}
		});

		loadSelected();
	}

	private void loadSelected() {
		StringBuilder inb = new StringBuilder();
		try {
			FileInputStream fis = openFileInput(DIFFICULTY_FILENAME);
			int ch;
			while ((ch = fis.read()) != -1)
				inb.append((char) ch);
			if (inb.toString().length() == 0
					|| inb.toString().equalsIgnoreCase("e")) {
				setDifficulty(easySelect);
			} else if (inb.toString().equalsIgnoreCase("m")) {
				setDifficulty(mediumSelect);
			} else if (inb.toString().equalsIgnoreCase("h")) {
				setDifficulty(hardSelect);
			} else {
				setDifficulty(easySelect);
			}
			fis.close();
			fis = openFileInput(FILENAME);

			inb = new StringBuilder();
			while ((ch = fis.read()) != -1)
				inb.append((char) ch);

			if (inb.toString().length() == 0
					|| inb.toString().equalsIgnoreCase(BASS_RANGE)) {
				setVocalRange(bassSelect);
			} else if (inb.toString().equalsIgnoreCase(TENOR_RANGE)) {
				setVocalRange(tenorSelect);
			} else if (inb.toString().equalsIgnoreCase(ALTO_RANGE)) {
				setVocalRange(altoSelect);
			} else if (inb.toString().equalsIgnoreCase(SOPRANO_RANGE)) {
				setVocalRange(sopranoSelect);
			} else {
			}
			fis.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * Button loadMe = (Button) findViewById(R.id.loadMe);
	 * loadMe.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View view) { StringBuilder inb = new
	 * StringBuilder(); try { FileInputStream fis = openFileInput(FILENAME); int
	 * ch; while((ch = fis.read()) != -1) inb.append((char)ch);
	 * thisTxt.setText("'"+inb+"'"+" was read from file"); fis.close(); } catch
	 * (FileNotFoundException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } catch (IOException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); } } });
	 */

	private void setVocalRange(Button desiredRange) {
		bassSelect.setSelected(false);
		sopranoSelect.setSelected(false);
		tenorSelect.setSelected(false);
		altoSelect.setSelected(false);
		desiredRange.setSelected(true);
	}

	private void setDifficulty(Button desiredDifficulty) {
		easySelect.setSelected(false);
		mediumSelect.setSelected(false);
		hardSelect.setSelected(false);
		desiredDifficulty.setSelected(true);
	}

	private void writeContent(String toWrite, String filename) {
		try {
			fos = openFileOutput(filename, Context.MODE_PRIVATE);
			fos.write(toWrite.getBytes());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}