package umich.pitchcoach.demo;

import umich.pitchcoach.R;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class LifeBar extends ProgressBar {

	
	public LifeBar(Context context, AttributeSet attributes) {
		super(context, attributes);
		this.setMax(100);
		this.setIndeterminate(false);
		this.setProgress(50);
	}

	@Override
	public void setProgressDrawable(Drawable d) {
		//Thanks to the magic of StackOverflow for this one
		if (this.getProgressDrawable() != null) {
			Rect bounds = this.getProgressDrawable().getBounds();
			super.setProgressDrawable(d);
			this.getProgressDrawable().setBounds(bounds);
		}
		else super.setProgressDrawable(d);
	}

	@Override
	public synchronized void setProgress(int progress) {
		if (progress < 20) this.setProgressDrawable(getContext().getResources().getDrawable(R.drawable.red_progress));
		else if (progress < 50) this.setProgressDrawable(getContext().getResources().getDrawable(R.drawable.orange_progress));
		else if (progress < 70) this.setProgressDrawable(getContext().getResources().getDrawable(R.drawable.yellow_progress));
		else this.setProgressDrawable(getContext().getResources().getDrawable(R.drawable.green_progress));
		super.setProgress(progress);
		
	}
	
	public boolean isWin()
	{
		return this.getProgress() >= 100;
	}
	
	public boolean isDeath() 
	{
		return this.getProgress() <= 0;
	}
	
	public void addLives(int lives)
	{
		this.setProgress(this.getProgress() + lives);
	}
}
