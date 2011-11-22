package umich.pitchcoach.demo;

import java.util.ArrayList;

import android.content.DialogInterface;
import java.util.Arrays;

import umich.pitchcoach.LetterNotes;
import umich.pitchcoach.R;
import umich.pitchcoach.threadman.RenderThreadManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;

import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import umich.pitchcoach.listeners.IRenderNotify;
import umich.pitchcoach.listeners.OnScrollListener;

public class PitchGraphActivity extends Activity {

	PitchKeeper myPitchKeeper;
	GraphContainer currentGraph;
	GraphContainer nextGraph;
	
	ScrollContainer graphcont;
	RenderThreadManager renderThreadManager;
	
	LifeBar lifebar;
	
	public static String[] singTheseNotes = new String[]{"C3", "C#3", "D3", "D#3", "E3", "F3", "F#3", "G3", "G#3", "A3", "A#3", "B3"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mockui);
		lifebar = (LifeBar)findViewById(R.id.lifebar);
		graphcont = (ScrollContainer)findViewById(R.id.scroller);
		this.graphcont.setOnScrollListener(new OnScrollListener() {

			@Override
			public void scrollEnd() {
				renderThreadManager.startRenderThread(currentGraph);
				renderUnPause();
			}

			@Override
			public void scrollBack() {
				renderThreadManager.stopRenderThread();
				renderPause();
			}

			@Override
			public void doneAutoScrolling() {
				renderThreadManager.startRenderThread(currentGraph); //??
			}
			
		});
		myPitchKeeper = new PitchKeeper(new ArrayList<String>(Arrays.asList(singTheseNotes)));
		renderThreadManager = new RenderThreadManager(new Handler(), new IRenderNotify() {
			@Override
			public void renderIsDone(double pitch, double time) {
				updateIncidentalUI(pitch, time);
			}		//renderThread.setRenderElement(sourceSource);

			
		});
		
		addGraph();
		addGraph(); //current and next
		
	}

	private void addGraph() {
		GraphContainer theGraph = new GraphContainer(getApplicationContext(), myPitchKeeper.getRandomPitch());
		graphcont.addElement(theGraph);
		this.currentGraph = this.nextGraph;
		this.nextGraph = theGraph;
		if (this.currentGraph != null) {
			this.currentGraph.setActive();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		renderThreadManager.stopRenderThread();
	}

	@Override
	protected void onResume() {
		super.onResume();
		renderThreadManager.startRenderThread(currentGraph);
	}

	public void updateIncidentalUI(double pitch, double timeInSeconds) {
		
		currentGraph.onPitch(pitch, timeInSeconds);
		if (currentGraph.isDone()) {
			renderThreadManager.stopRenderThread();
			currentGraph.finalize();
			if (currentGraph.getFinalEvaluation() > 0) this.lifebar.addLives(25);
			else this.lifebar.addLives(-5);
			
			if (this.lifebar.isWin()) this.onWin();
			else if (this.lifebar.isDeath()) this.onDeath();
			else addGraph();
		}
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
