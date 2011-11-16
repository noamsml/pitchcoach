package umich.pitchcoach.demo;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import java.util.LinkedList;

import umich.pitchcoach.listeners.OnScrollListener;
import umich.pitchcoach.listeners.SizableElement;

//BUG BUG BUG ALERT: ON TASK CHANGE, RENDER THREAD SOMETIME BREAKS

public class ScrollContainer extends HorizontalScrollView {

	private static int MAX_CHILDREN = 5;
	private OnScrollListener onscroll;
	private LinearLayout layout;
	private LinkedList<SizableElement> toAdd;
	private boolean scrollAtEnd;
	private boolean autoScrolling;
	
	private Runnable scrollABit; //TODO: Figure out more elegan way to do this
	Handler uiThreadHandler;
	
	public ScrollContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		final ScrollContainer that = this;
		uiThreadHandler  = new Handler();
		scrollABit = new Runnable() {

			@Override
			public void run() {
				if (!scrollAtEnd) {
					autoScrolling = true;
					that.scrollBy(that.getWidth()/(3 * 5), 0);
					uiThreadHandler.postDelayed(this, 30);
				}
				else {
					autoScrolling = false;
					that.handleDoneScrolling();
				}
			}
			
		};
		this.layout = new LinearLayout(context) {
			@Override
			protected void onSizeChanged(int w, int h, int oldw, int oldh) {
				// TODO Auto-generated method stub
				super.onSizeChanged(w, h, oldw, oldh);
				if (w > oldw)
				{
					that.scrollAtEnd = false;
					uiThreadHandler.postDelayed(scrollABit, 200);
				}
			}
		};
		
		
		this.addView(this.layout);
		toAdd = new LinkedList<SizableElement>();
		scrollAtEnd = true;
		autoScrolling = false;
	
	}
	
	protected void handleDoneScrolling() {
		if (onscroll != null) onscroll.doneAutoScrolling();
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
		if (this.layout.getChildCount() > MAX_CHILDREN)
		{
			this.autoScrolling = true;
			this.layout.removeViewAt(0);
			this.scrollBy(- this.getWidth()/3, 0);
			this.autoScrolling = false;
			uiThreadHandler.postDelayed(scrollABit, 200);
			
		}
		this.layout.addView(elem, this.getWidth()/3, this.getHeight());
	}
	
	public void setOnScrollListener(OnScrollListener l) {
		this.onscroll = l;
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		this.layout.setPadding(this.getWidth()/3, 0,0,0);
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
				if (onscroll != null && !autoScrolling) onscroll.scrollBack();
				scrollAtEnd = false;
			}
		}
		else
		{
			if (!scrollAtEnd) {
				if (onscroll != null && !autoScrolling) onscroll.scrollEnd();
				scrollAtEnd = true;
			}
		}
	}
}
