package umich.pitchcoach.demo;

import umich.pitchcoach.LetterNotes;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GraphContainer extends LinearLayout {
	public GraphSurface graph;
	public TextView text;
	private String targetPitch;
	
	public GraphContainer(Context context, IGraphNotifyReceiver notifyrecv, String targetPitch) {
		super(context);
		graph = new GraphSurface(context, LetterNotes.noteSpecToFreq(targetPitch), notifyrecv);
		this.setOrientation(LinearLayout.VERTICAL);
		
		text = new TextView(context);
		text.setText(targetPitch);
		text.setTextColor(Color.BLACK);
		text.setGravity(Gravity.CENTER_HORIZONTAL);
		this.addView(text);
		this.addView(graph);
		
		graph.setNotifyReceiver(notifyrecv);
		this.setBackgroundColor(Color.GRAY);
		this.setPadding(10, 10, 10, 10);
		this.targetPitch = targetPitch;
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
		//nada
	}
	
	public void setTargetPitch(String pitch){
		this.targetPitch = pitch;
		text.setText(targetPitch + " " + LetterNotes.noteSpecToFreq(pitch));
	}
	
}
