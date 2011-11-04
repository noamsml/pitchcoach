package umich.pitchcoach.demo;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class AutoScrollingLinearLayout extends LinearLayout {
	
	private HorizontalScrollView scrollview;
	
	public AutoScrollingLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setScrollView(HorizontalScrollView scrollview) {
		this.scrollview = scrollview;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		
		if (this.scrollview != null) this.scrollview.scrollTo(w, 0);
	}
	
	
	
}
