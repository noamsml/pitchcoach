package umich.pitchcoach.demo;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import java.util.LinkedList;

import umich.pitchcoach.listeners.SizableElement;

public class ScrollContainer extends HorizontalScrollView {


	private OnScrollListener onscroll;
	private LinearLayout layout;
	private LinkedList<SizableElement> toAdd;
	private boolean scrollAtEnd;
	
	public ScrollContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		final ScrollContainer that = this;
		this.layout = new LinearLayout(context) {
			@Override
			protected void onSizeChanged(int w, int h, int oldw, int oldh) {
				// TODO Auto-generated method stub
				super.onSizeChanged(w, h, oldw, oldh);
				if (that.scrollAtEnd) {
					that.scrollTo(this.getWidth(), 0);
				}
			}
		};
		
		
		this.addView(this.layout);
		toAdd = new LinkedList<SizableElement>();
		scrollAtEnd = true;
	
	}
	
	public void addElement(SizableElement elem) {
		if (this.getWidth() == 0 || this.getHeight() == 0)
		{
			toAdd.add(elem);
		}
		else {
			addElementInternal(elem);
		}
	}

	private void addElementInternal(SizableElement elem) {
		elem.sizeSet(this.getWidth()/3, this.getHeight());
		this.layout.addView(elem, this.getWidth()/3, this.getHeight());
	}
	
	public void setOnScrollListener(OnScrollListener l) {
		this.onscroll = l;
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		if (w != 0 && h != 0)
		{
			while (toAdd.size() != 0) addElementInternal(toAdd.removeFirst());
		}
	}
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		super.onScrollChanged(l, t, oldl, oldt);
	
		int maxScroll = Math.max(this.getChildAt(0).getWidth() - this.getWidth(),0);
		if (l < maxScroll)
		{
			if (scrollAtEnd) {
				if (onscroll != null) onscroll.scrollBack();
				scrollAtEnd = false;
			}
		}
		else
		{
			if (!scrollAtEnd) {
				if (onscroll != null) onscroll.scrollEnd();
				scrollAtEnd = true;
			}
		}
	}
}
