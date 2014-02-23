package umich.pitchcoach;

import umich.pitchcoach.shared.IPitchReciever;
import umich.pitchcoach.threadman.PitchThreadSpawn;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PitchRecogActivity extends Activity implements IPitchReciever {
	/** Called when the activity is first created. */
	TextView pitchDisplay;
	Button snapshotBtn;
	PitchThreadSpawn pitchService;
	Handler handler;

	private double roundToNearest(double val, double to) {
		return Math.round(val / to) * to;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		pitchDisplay = (TextView) findViewById(R.id.pitchText);
		snapshotBtn = (Button) findViewById(R.id.snapshotBtn);
		pitchService = new PitchThreadSpawn();

		snapshotBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				pitchService.diagnosticDumpSamples("/sdcard/snap.txt");

			}

		});
		// spitchService = new MockPitchThreadSpawn(R.xml.replay_values,
		// getResources()); //This is how you use the mock pitch thread spawner
		handler = new Handler();
	}

	@Override
	public void receivePitch(double pitch, double time) {
		pitchDisplay.setText(Double.toString(roundToNearest(pitch, 6)
		// pitch
				));
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// pitchService.stopPitchService();
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