package umich.pitchcoach.demo;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

public class LocationAwareScrollView extends HorizontalScrollView {
	
	OnScrollListener scroll;
	
	public LocationAwareScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setOnScrollListener(OnScrollListener l) {
		this.scroll = l;
	}
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		super.onScrollChanged(l, t, oldl, oldt);
		
		if (scroll != null)
		{
			int maxScroll = Math.max(this.getChildAt(0).getWidth() - this.getWidth(),0);
			if (l < maxScroll)
			{
				scroll.scrollBack();
			}
			else
			{
				scroll.scrollEnd();
			}
		}
	}
	
}
