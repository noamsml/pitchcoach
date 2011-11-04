package umich.pitchcoach.demo;

import java.util.List;
import java.util.ArrayList;

import umich.pitchcoach.LetterNotes;
import umich.pitchcoach.shared.IPitchReciever;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;

public class GraphSurface extends SurfaceView {
	protected ImageSource imagesource;
	protected static final double SECONDS_ONSCREEN = 10;
	protected GraphGlue uiGlue; //TODO: Make this hack unnecessary
	protected boolean isLive;
	protected double target;
	
	public void makeLive()
	{
		isLive = true;
	}
	
	public void makeUnLive()
	{
		isLive = false;
	}
	
	public GraphSurface(Context context, double target, GraphGlue uiGlue) {
		super(context);
		this.uiGlue = uiGlue;
		this.target = target;
		initialize();
	}

		
	protected void initialize()
	{
		imagesource = null;
		isLive = false;
		setWillNotDraw(false);
		setKeepScreenOn(true);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		if (w == 0 || h == 0) return;
		if (oldw != w || oldh != h || imagesource == null)
		{
			this.imagesource = new ImageSource(w,h, this.target);
		}
		uiGlue.imageChanged();
	}
	

	@Override
	protected void onDraw(Canvas c)
	{
		super.onDraw(c);
		if (this.imagesource != null)
		{
			this.imagesource.straightWrite(c, this.getWidth(), this.getHeight()); //method must be synchronized
		}
	}
	
}
