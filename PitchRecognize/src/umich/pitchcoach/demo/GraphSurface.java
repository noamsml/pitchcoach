package umich.pitchcoach.demo;

import umich.pitchcoach.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
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
	protected Drawable silenceDrawable;
	protected String targetNoteName;
	protected Paint whitePaint;
	protected boolean silent;

	public void makeLive() {
		isLive = true;
	}

	public void makeUnLive() {
		isLive = false;
	}

	public GraphSurface(Context context, double target, String targetNoteName) {
		super(context);
		this.target = target;
		silent = false;
		this.targetNoteName = targetNoteName;
		this.silenceDrawable = context.getResources().getDrawable(
				R.drawable.smallerspeaker50); // FIXME
		initialize();
	}

	public void setTint(int color) {
		tint = new Paint();
		tint.setColor(color);
		invalidate();
	}

	public void unsetTint() {
		tint = null;
		invalidate();
	}

	public void setPatch(int id) {
		patch = this.getContext().getResources().getDrawable(id);
		invalidate();
	}

	public void unsetPatch() {
		patch = null;
		invalidate();
	}

	protected void initialize() {
		imagesource = null;
		isLive = false;
		whitePaint = new Paint();
		whitePaint.setColor(Color.WHITE);
		whitePaint.setTextSize(24);

		setWillNotDraw(false);
		setKeepScreenOn(true);
	}

	public void sizeSet(int w, int h) {
		this.imagesource = new ImageSource(w, h, this.target);
	}

	@Override
	protected void onDraw(Canvas c) {
		super.onDraw(c);
		if (this.imagesource != null) {
			this.imagesource
					.straightWrite(c, this.getWidth(), this.getHeight()); // method
																			// must
																			// be
																			// synchronized
		}
		if (tint != null) {
			c.drawRect(new Rect(0, 0, c.getWidth(), c.getHeight()), tint);
		}
		if (patch != null) {

			patch.setBounds(new Rect(this.getWidth() / 2 - 50,
					this.getHeight() / 2 - 50, this.getWidth() / 2 + 50, this
							.getHeight() / 2 + 50));
			patch.draw(c);
		}
		if (!this.silent) {
			silenceDrawable.setBounds(new Rect(this.getWidth() - 60, this
					.getHeight() - 60, this.getWidth() - 10,
					this.getHeight() - 10));
			silenceDrawable.draw(c);
		}

		c.drawText(this.targetNoteName, 30, 30, whitePaint);

	}

	public void setSilent(boolean silence) {
		this.silent = silence;
	}

	public void reset() {
		this.imagesource = new ImageSource(this.getWidth(), this.getHeight(),
				this.target);
	}

}
