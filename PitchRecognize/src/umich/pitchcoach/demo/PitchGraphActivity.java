package umich.pitchcoach.demo;

import java.util.ArrayList;
import java.util.Arrays;

import umich.pitchcoach.LetterNotes;
import umich.pitchcoach.R;
import umich.pitchcoach.threadman.RenderThreadManager;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import umich.pitchcoach.listeners.IRenderNotify;

public class PitchGraphActivity extends Activity {

	Button diagBtn, nextBtn; //DIAG
	TextView feedbackTxt; //DEP
	PitchKeeper myPitchKeeper;
	CharSequence lastPitchSung; //CRUFT
	GraphContainer currentGraph;
	
	ScrollContainer graphcont;
	RenderThreadManager renderThreadManager;
	
	public static String[] singTheseNotes = new String[]{"C3", "C#3", "D3", "D#3", "E3", "F3", "F#3", "G3", "G#3", "A3", "A#3", "B3"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mockui);
		graphcont = (ScrollContainer)findViewById(R.id.scroller);
		this.graphcont.setOnScrollListener(new OnScrollListener() {

			@Override
			public void scrollEnd() {
				renderThreadManager.startRenderThread();
				renderUnPause();
			}

			@Override
			public void scrollBack() {
				renderThreadManager.stopRenderThread();
				renderPause();
			}
			
		});
		myPitchKeeper = new PitchKeeper(new ArrayList<String>(Arrays.asList(singTheseNotes)));
		renderThreadManager = new RenderThreadManager(new Handler(), new IRenderNotify() {
			@Override
			public void renderIsDone(double pitch, double time) {
				updateIncidentalUI(pitch, time);
			}
			
		});
		
		addGraph();

		diagBtn = (Button)findViewById(R.id.diagBtn);
		diagBtn.setOnClickListener(new View.OnClickListener () {
			@Override
			public void onClick(View v) {
				renderThreadManager.diagnostics();
			}
		});
		nextBtn = (Button)findViewById(R.id.nextBtn);

		nextBtn.setOnClickListener(new View.OnClickListener () {
			@Override
			public void onClick(View v) {
				addGraph();
			}
		});
		feedbackTxt = (TextView)findViewById(R.id.feedbackTxt);

	}

	private void addGraph() {
		GraphContainer theGraph = new GraphContainer(getApplicationContext(), myPitchKeeper.getRandomPitch());
		graphcont.addElement(theGraph);
		this.currentGraph = theGraph;
		renderThreadManager.setRenderElement(theGraph);
	}

	@Override
	protected void onPause() {
		super.onPause();
		renderThreadManager.stopRenderThread();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		renderThreadManager.startRenderThread();
	}

	public void updateIncidentalUI(double pitch, double timeInSeconds) {
		// TODO Auto-generated method stub
		feedbackTxt.setText(LetterNotes.freqToNoteSpec(pitch));
		currentGraph.onPitch(pitch, timeInSeconds);
		if (currentGraph.isDone()) {
			currentGraph.finalize();
			addGraph();
		}
	}

	public void renderPause() {
		lastPitchSung = this.feedbackTxt.getText();
		this.feedbackTxt.setText("Pasued");
		
	}
	
	public void renderUnPause() {
		this.feedbackTxt.setText(lastPitchSung);
	}
}
