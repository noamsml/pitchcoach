package umich.pitchcoach.demo;

import java.util.ArrayList;
import java.util.Arrays;

import umich.pitchcoach.LetterNotes;
import umich.pitchcoach.R;
import umich.pitchcoach.NotePlayer.Note;
import android.app.Activity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import umich.pitchcoach.NotePlayer.*;

public class PitchGraphActivity extends Activity {

	OffscreenRenderThread renderThread;
	Button diagBtn, nextBtn;
	TextView feedbackTxt;
	AutoScrollingLinearLayout graphLayout;
	PitchKeeper myPitchKeeper;
<<<<<<< HEAD
	LocationAwareScrollView scrollview;
	GraphGlue uiGlue;
	CharSequence lastPitchSung;
=======
	GraphContainer currentGraph;
	GraphContainer nextGraph;
	
	ScrollContainer graphcont;
	RenderThreadManager renderThreadManager;
	
	LifeBar lifebar;

>>>>>>> cb02f382e01bfcf91a2049910a3cea1d1907852f
	
	public static String[] singTheseNotes = new String[]{"C3", "C#3", "D3", "D#3", "E3", "F3", "F#3", "G3", "G#3", "A3", "A#3", "B3"};
	NotePlayer noteplayer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mockui);
		View v = findViewById(R.id.graphLinearLayout);
		graphLayout = (AutoScrollingLinearLayout)v;
		uiGlue = new GraphGlue(this);
		scrollview = (LocationAwareScrollView)findViewById(R.id.scroller); 
		scrollview.setOnScrollListener(uiGlue);
		graphLayout.setScrollView(scrollview);
		myPitchKeeper = new PitchKeeper(new ArrayList<String>(Arrays.asList(singTheseNotes)));
		noteplayer = new NotePlayer();
		

		GraphContainer theGraph = new GraphContainer(getApplicationContext(), uiGlue, myPitchKeeper.getRandomPitch());
		addGraph(theGraph);

		diagBtn = (Button)findViewById(R.id.diagBtn);
		diagBtn.setOnClickListener(new View.OnClickListener () {
			@Override
			public void onClick(View v) {
				uiGlue.diagnostics();
			}
		});
		nextBtn = (Button)findViewById(R.id.nextBtn);

		@SuppressWarnings("unused") // HACK
		final PitchGraphActivity that = this; //HACK 

		nextBtn.setOnClickListener(new View.OnClickListener () {
			@Override
			public void onClick(View v) {
				GraphContainer theGraph = new GraphContainer(getApplicationContext(), uiGlue, myPitchKeeper.getRandomPitch());
				addGraph(theGraph);
			
				// Play frequency of target
				// sample code usage
				noteplayer.setFrequency( 880 );
				noteplayer.setDuration( 1 );
				noteplayer.playNote();
	
				
			}
		});
<<<<<<< HEAD
		feedbackTxt = (TextView)findViewById(R.id.feedbackTxt);
=======
		myPitchKeeper = new PitchKeeper(new ArrayList<String>(Arrays.asList(singTheseNotes)));
		renderThreadManager = new RenderThreadManager(new Handler(), new IRenderNotify() {
			@Override
			public void renderIsDone(double pitch, double time) {
				updateIncidentalUI(pitch, time);
			}		
>>>>>>> cb02f382e01bfcf91a2049910a3cea1d1907852f

	}

	private void addGraph(GraphContainer theGraph) {
		graphLayout.addView(theGraph, 500, 300);
		uiGlue.setCurrentGraph(theGraph);
	}

	@Override
	protected void onPause() {
		super.onPause();
		uiGlue.stopRenderThread();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		uiGlue.startRenderThread();

	}

	public void updateIncidentalUI(double pitch, double timeInSeconds) {
		// TODO Auto-generated method stub
		feedbackTxt.setText(LetterNotes.freqToNoteSpec(pitch));
		if (uiGlue.getCurrentContainer().isDone()) {
			uiGlue.getCurrentContainer().finalize();
			GraphContainer theGraph = new GraphContainer(getApplicationContext(), uiGlue, myPitchKeeper.getRandomPitch());
			addGraph(theGraph);
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
