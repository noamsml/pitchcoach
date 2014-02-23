package umich.pitchcoach.demo;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import java.util.LinkedList;

import umich.pitchcoach.flow.Promise;
import umich.pitchcoach.listeners.OnScrollListener;
import umich.pitchcoach.listeners.SizableElement;

//BUG BUG BUG ALERT: ON TASK CHANGE, RENDER THREAD SOMETIME BREAKS
//TOFIX: All this

public class ScrollContainer extends HorizontalScrollView {

	private static int MAX_CHILDREN = 5;
	private OnScrollListener onscroll;
	private LinearLayout layout;
	private LinkedList<ElemAddPromise> toAdd;
	private boolean autoScrolling;

	class ElemAddPromise extends Promise {
		private SizableElement elem;

		public ElemAddPromise(SizableElement elem) {
			super();
			this.elem = elem;
		}

		public SizableElement getElem() {
			return elem;
		}

		public void go() {
			if (getWidth() == 0 || getHeight() == 0) {
				toAdd.add(this);
			} else {
				addElementInternal(this);
			}
		}
	};

	class ScrollABit implements Runnable {
		Promise p;

		public ScrollABit(Promise p) {
			this.p = p;
		}

		@Override
		public void run() {
			if (!scrollAtEnd()) {
				autoScrolling = true;
				scrollBy(getWidth() / (3 * 5), 0);
				uiThreadHandler.postDelayed(this, 30);
			} else {
				if (autoScrolling) {
					autoScrolling = false;
					p.done();
				}
			}
		}
	}

	Handler uiThreadHandler;
	boolean firstTime = true; // HACK

	public ScrollContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		final ScrollContainer that = this;
		uiThreadHandler = new Handler();
		this.layout = new LinearLayout(context);

		this.addView(this.layout);
		toAdd = new LinkedList<ElemAddPromise>();
		autoScrolling = false;

	}

	public Promise addElement(final SizableElement elem) {
		return new ElemAddPromise(elem);
	}

	private void addElementInternal(ElemAddPromise p) {
		p.getElem().sizeSet(this.getWidth() / 3, this.getHeight());

		if (this.layout.getChildCount() >= MAX_CHILDREN) {
			this.autoScrolling = true;
			this.layout.removeViewAt(0);
			this.scrollBy(-this.getWidth() / 3, 0);
			this.autoScrolling = false;
		}
		this.layout.addView(p.getElem(), this.getWidth() / 3, this.getHeight());
		if (this.layout.getChildCount() > 2) {
			uiThreadHandler.postDelayed(new ScrollABit(p), 200);
		} else
			p.done();
	}

	public void setOnScrollListener(OnScrollListener l) {
		this.onscroll = l;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		this.layout.setPadding(this.getWidth() / 3, 0, 0, 0);
		super.onSizeChanged(w, h, oldw, oldh);
		if (w != 0 && h != 0) {
			while (toAdd.size() != 0)
				addElementInternal(toAdd.removeFirst());
		}
	}

	/*
	 * @Override protected void onScrollChanged(int l, int t, int oldl, int
	 * oldt) { // TODO Auto-generated method stub super.onScrollChanged(l, t,
	 * oldl, oldt);
	 * 
	 * int maxScroll = Math.max(this.getChildAt(0).getWidth() -
	 * this.getWidth(),0); if (l < maxScroll) { if (onscroll != null &&
	 * !autoScrolling) onscroll.scrollBack(); scrollAtEnd = false; } } else { if
	 * (!scrollAtEnd) { if (onscroll != null && !autoScrolling)
	 * onscroll.scrollEnd(); scrollAtEnd = true; } } }
	 */

	public boolean scrollAtEnd() {
		int maxScroll = Math.max(
				this.getChildAt(0).getWidth() - this.getWidth(), 0);
		return (this.getScrollX() >= maxScroll);
	}

}
