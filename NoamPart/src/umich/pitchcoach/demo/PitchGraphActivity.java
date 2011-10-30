package umich.pitchcoach.demo;

import umich.pitchcoach.LetterNotes;
import umich.pitchcoach.R;
import android.app.Activity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class PitchGraphActivity extends Activity implements IGraphNotifyReceiver {
	GraphContainer currentGraph;
	OffscreenRenderThread renderThread;
	Button diagBtn;
	TextView feedbackTxt;
	LinearLayout graphLayout;

	private void startRenderThread() {
		stopRenderThread();
		renderThread = new OffscreenRenderThread(this, getApplicationContext());
		imageSourceChanged();
		renderThread.start();
	}
	
	private void setCurrentGraph(GraphContainer container)
	{
		this.currentGraph = container;
		container.makeLive();
		imageSourceChanged();
	}
	
	public void imageSourceChanged() {
		if (currentGraph != null)
		{
			if (this.renderThread != null) renderThread.setImage(currentGraph.getImageSource());	
		}
	}

	private void stopRenderThread() {
		if (renderThread != null) renderThread.interrupt();
		renderThread = null;
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mockui);
		View v = findViewById(R.id.graphLinearLayout);
		graphLayout = (LinearLayout)v;
		GraphContainer theGraph = new GraphContainer(getApplicationContext(), this, "E4");
		addGraph(theGraph);
		setCurrentGraph(theGraph);
		
		diagBtn = (Button)findViewById(R.id.diagBtn);
		diagBtn.setOnClickListener(new View.OnClickListener () {
			@Override
			public void onClick(View v) {
				renderThread.diagnostics();
			}
			
		});

		feedbackTxt = (TextView)findViewById(R.id.feedbackTxt);
	
	}

	private void addGraph(GraphContainer theGraph) {
		graphLayout.addView(theGraph, 500, 300);
	}

	@Override
	protected void onPause() {
		super.onPause();
		stopRenderThread();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		startRenderThread();
		
	}

	public void updateIncidentalUI(double pitch, double timeInSeconds) {
		// TODO Auto-generated method stub
	  int intPitch = (int)pitch;
	  feedbackTxt.setText(LetterNotes.freqToNoteSpec(intPitch));
	  this.currentGraph.updateGraph();
	}


}
