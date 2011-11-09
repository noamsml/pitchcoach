package umich.pitchcoach.demo;

import umich.pitchcoach.LetterNotes;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GraphContainer extends LinearLayout {
	public GraphSurface graph;
	public TextView text;
	private String targetPitch;
	private NotePlayer noteplayer;
	private GraphEvaluator eval;
	
	public GraphContainer(Context context, GraphGlue uiGlue, String targetPitch) {
		super(context);
		graph = new GraphSurface(context, LetterNotes.noteSpecToFreq(targetPitch), uiGlue);
		this.setOrientation(LinearLayout.VERTICAL);
		
		text = new TextView(context);
		text.setText(targetPitch);
		text.setTextColor(Color.BLACK);
		text.setGravity(Gravity.CENTER_HORIZONTAL);
		this.addView(text);
		this.addView(graph);
		
		this.setBackgroundColor(Color.WHITE);
		this.setPadding(10, 10, 10, 10);
		this.targetPitch = targetPitch;
		
		eval = new GraphEvaluator(this.targetPitch);
	}

	public void updateGraph() {
		this.graph.invalidate();
	}

	public ImageSource getImageSource() {
		// TODO Auto-generated method stub
		return graph.imagesource;
	}
	
	public void makeLive() {
		this.graph.makeLive();
	}
	
	public void makeDead() {
		this.graph.makeUnLive();
	}
	
	public void onPitch(double pitch, double time)
	{
		eval.onPitch(pitch, time);
	}
	
	public void setTargetPitch(String pitch){
		this.targetPitch = pitch;
		text.setText(targetPitch + " " + LetterNotes.noteSpecToFreq(pitch));
	}
	
	public boolean isDone() {
		return eval.isDone();
	}
	
	public void finalize()
	{
		int color;
		int evalVal;
		evalVal = eval.getFinalEvaluation();
		if (evalVal == 0) {
			color = 0xffffdddd;
		}
		else if (evalVal == 1) {
			color = 0xffffffdd;
		}
		else {
			color = 0xffddffdd;
		}
		
			
		this.setBackgroundColor(color);
	}
	
	public int getFinalEvaluation() {
		return eval.getFinalEvaluation();
	}
	
}
