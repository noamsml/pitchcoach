package umich.pitchcoach.listeners;

import android.content.Context;
import android.widget.LinearLayout;


public abstract class SizableElement extends LinearLayout {

	public SizableElement(Context context) {
		super(context);
	}
	
	public abstract void sizeSet(int w, int h);

}
