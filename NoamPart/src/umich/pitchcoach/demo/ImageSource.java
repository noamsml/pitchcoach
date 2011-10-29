package umich.pitchcoach.demo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Paint;
import android.graphics.Color;

public class ImageSource {
	private Bitmap bitmap;
	private Canvas canvas;
	private int width;
	private int height;
	
	private boolean onceAround;
	
	private Paint bgPaint;
	private Paint fgPaint;
	
	private final static double SECONDS_TO_SHOW = 10;
	private final static double MIN_HZ = 70;
	private final static double LOG_MINHZ = Math.log(MIN_HZ);
	private final static double MAX_HZ = 700;
	private final static double LOG_MAXHZ = Math.log(MAX_HZ);
	
	private double pixel_per_sec;
	
	private double current_x;
	private double current_y;
	
	
	public ImageSource(int width, int height)
	{
		this.bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		this.canvas = new Canvas(this.bitmap);
		this.width = width;
		this.height = height;
		this.pixel_per_sec = width / SECONDS_TO_SHOW;
		this.current_x = 0;
		
		this.bgPaint = new Paint();
		this.bgPaint.setColor(Color.BLACK);
		this.fgPaint = new Paint();
		this.fgPaint.setColor(Color.GREEN);
		this.onceAround = false;
	}
	
	private Rect makeRect(double left, double top, double right, double bottom)
	{
		return new Rect((int)left, (int)top, (int) right, (int) bottom);
	}
	
	
	private RectF makeRectF(double left, double top, double right, double bottom)
	{
		return new RectF((float)left, (float)top, (float) right, (float) bottom);
	}
	
	public synchronized void addDatapoint(double pitch, double time) //Assume time is never greater than width //MAKE SYNC
	{
		double pixelwidth = getPixelWidth(time);
		double pixely = getPixelYPos(pitch);
		if (this.current_x + pixelwidth > this.width)
		{
			drawBrokenDatapoint(this.current_x, this.current_x + pixelwidth, this.current_y, pixely);
			this.current_x = this.current_x + pixelwidth - this.width;
		}
		else
		{
			drawDatapoint(this.current_x, this.current_x + pixelwidth, this.current_y, pixely);
			this.current_x = this.current_x + pixelwidth;
		}
		this.current_y = pixely;
	}
	
	
	private void drawDatapoint(double curx, double nextx, double cury, double nexty)
	{
		canvas.drawRect(this.makeRectF(curx, 0, nextx, height), this.bgPaint);
		canvas.drawLine((float)curx, (float)(height-cury), (float)nextx, (float)(height - nexty), this.fgPaint);
	}
	
	private void drawBrokenDatapoint(double curx, double nextx, double cury, double nexty)
	{
		double inty = cury  + (width - nextx) * (nexty-cury)/(nextx-curx);
		drawDatapoint(curx, width, cury, inty);
		drawDatapoint(0, nextx-width, cury, inty);
		onceAround = true;
	}
	
	
	private double getPixelWidth(double time)
	{
		return time * this.pixel_per_sec;
	}
	
	private double getPixelYPos(double time)
	{
		double logtime = Math.log(time) - LOG_MINHZ;
		
		return logtime * (height)/(LOG_MAXHZ - LOG_MINHZ);
	}
	
	public synchronized void straightWrite(Canvas c, int otherwidth, int otherheight)
	{
		//TODO: Implement for realz
		if (!onceAround) 
			c.drawBitmap(bitmap, null, makeRect(0,0,width,height), null);
		else
		{
			c.drawBitmap(bitmap,makeRect(current_x, 0, width, height),makeRectF(0,0,width-current_x,height), null);
			c.drawBitmap(bitmap, makeRect(0, 0, current_x, height), makeRectF(width-current_x,0,width,height), null);
		}
	}

}