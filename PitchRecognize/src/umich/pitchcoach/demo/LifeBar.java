package umich.pitchcoach.demo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
	public synchronized void setProgress(int progress) {
		/*
		if (progress < 20) this.setProgressDrawable(new ColorDrawable(Color.RED));
		else if (progress < 50) this.setProgressDrawable(new ColorDrawable(0xffff0088));
		else if (progress < 70) this.setProgressDrawable(new ColorDrawable(Color.YELLOW));
		else this.setProgressDrawable(new ColorDrawable(Color.GREEN));*/
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
