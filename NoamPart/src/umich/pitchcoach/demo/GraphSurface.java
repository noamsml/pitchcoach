package umich.pitchcoach.demo;

import java.util.List;
import java.util.ArrayList;

import umich.pitchcoach.shared.IPitchReciever;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;

public class GraphSurface extends SurfaceView {
	protected ImageSource imagesource;
	protected static final double SECONDS_ONSCREEN = 10;
	protected IGraphNotifyReceiver notifyReceiver; //TODO: Make this hack unnecessary
	protected boolean isLive;
	
	public void makeLive()
	{
		isLive = true;
	}
	
	public void makeUnLive()
	{
		isLive = false;
	}
	
	public GraphSurface(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize();
	}
	
	public GraphSurface(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	public GraphSurface(Context context) {
		super(context);
		initialize();
	}

		
	protected void initialize()
	{
		imagesource = null;
		setWillNotDraw(false);
		setKeepScreenOn(true);
	}
	
	public void setImageSource(ImageSource data)
	{
		this.imagesource = data;
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		if (w == 0 || h == 0) return;
		if (oldw != w || oldh != h || imagesource == null)
		{
			setImageSource(new ImageSource(w,h));
		}
		notifyReceiver.imageSourceChanged();
	}
	
	
	public void setNotifyReceiver(IGraphNotifyReceiver notifyreceiver)
	{
		this.notifyReceiver = notifyreceiver;
		
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
