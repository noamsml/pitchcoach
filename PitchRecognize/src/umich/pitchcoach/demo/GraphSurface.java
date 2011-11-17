package umich.pitchcoach.demo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.SurfaceView;

public class GraphSurface extends SurfaceView {
	protected ImageSource imagesource;
	protected static final double SECONDS_ONSCREEN = 5;
	protected boolean isLive;
	protected double target;
	protected Paint tint;
	protected Drawable patch;
	
	
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

	public void setTint(int color)
	{
		tint = new Paint();
		tint.setColor(color);
		invalidate();
	}
	
	public void unsetTint()
	{
		tint = null;
		invalidate();
	}
	
	public void setPatch(int id)
	{
		patch = this.getContext().getResources().getDrawable(id);
		invalidate();
	}
	
	public void unsetPatch()
	{
		patch = null;
		invalidate();
	}
	
	protected void initialize()
	{
		imagesource = null;
		isLive = false;
		setWillNotDraw(false);
		setKeepScreenOn(true);
	}
	
	public void sizeSet(int w, int h) {
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
		if (tint != null)
		{
			c.drawRect(new Rect(0,0,c.getWidth(),c.getHeight()), tint);
		}
		if (patch != null)
		{
			
			patch.setBounds(new Rect(this.getWidth()/2 - 50, this.getHeight()/2 - 50,
					this.getWidth()/2 + 50, this.getHeight()/2 + 50));
			patch.draw(c);
		}
	}
	
}
