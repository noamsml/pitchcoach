package umich.pitchcoach.demo;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceView;

public class GraphSurface extends SurfaceView {
	protected ImageSource imagesource;
	protected static final double SECONDS_ONSCREEN = 10;
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
	
	public GraphSurface(Context context, double target) {
		super(context);
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
	
	protected void sizeSet(int w, int h) {
			this.imagesource = new ImageSource(w,h, this.target);
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
