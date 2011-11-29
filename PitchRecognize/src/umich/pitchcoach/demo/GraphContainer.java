package umich.pitchcoach.demo;

import umich.pitchcoach.LetterNotes;
import umich.pitchcoach.R;
import umich.pitchcoach.listeners.IImageSourceSource;
import umich.pitchcoach.listeners.SizableElement;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GraphContainer extends SizableElement implements IImageSourceSource {
	public GraphSurface graph;
	private String targetPitch;
	private GraphEvaluator eval;
	private double targetFreq;
	
	public GraphContainer(Context context, String targetPitch) {
		super(context);
		targetFreq = LetterNotes.noteSpecToFreq(targetPitch);
		graph = new GraphSurface(context, targetFreq, targetPitch);
		graph.setTint(0x88000000);
		this.setOrientation(LinearLayout.VERTICAL);
		this.addView(graph);
		this.targetPitch = targetPitch;
		
		eval = new GraphEvaluator(this.targetPitch);
	}
	/*
	public GraphContainer(Context context, Bundle restoreState)
	{
		super(context);
	}
	
	
	public Bundle saveState() {
		Bundle b = new Bundle();
		b.putDouble("target", targetFreq);
		return b;
	}
	*/
	public void updateImage() {
		this.graph.invalidate();
	}

	public synchronized ImageSource getImageSource() {
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
	
	public boolean isDone() {
		return eval.isDone();
	}
	
	
	public void makeDone()
	{
		int color;
		int evalVal;
		evalVal = eval.getFinalEvaluation();
		graph.setTint(0x88ffffff);
		
		if (evalVal == 0) {
			graph.setPatch(R.drawable.ex);
		}
		else if (evalVal == 1) {
			graph.setPatch(R.drawable.vee);
		}
		else {
			graph.setPatch(R.drawable.vee);
		}
	}
	
	public int getFinalEvaluation() {
		return eval.getFinalEvaluation();
	}

	@Override
	public synchronized void sizeSet(int w, int h) {
		graph.sizeSet(w,h);
	}

	public void setActive() {
		graph.unsetTint();
		graph.unsetPatch();
	}
	
	public void setListening() {
		graph.setTint(0x88bbbbff);
		graph.setPatch(R.drawable.speaker);
	}

	public double getFrequency() {
		return targetFreq;
	}
	
	public boolean isCurrentlyCorrect() {
		return eval.isCurrentlyCorrect();
	}
	
}
