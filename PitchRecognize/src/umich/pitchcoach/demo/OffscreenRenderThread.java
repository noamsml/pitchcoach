package umich.pitchcoach.demo;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

import umich.pitchcoach.listeners.IImageSourceSource;
import umich.pitchcoach.listeners.IRenderNotify;
import umich.pitchcoach.shared.IPitchReciever;
import android.content.Context;
import android.os.Looper;
import android.os.Handler;

public class OffscreenRenderThread extends Thread implements IPitchReciever {
	IImageSourceSource sourceSource;
	public Handler handler;
	public Lock handlerLock;
	public Condition handlerCond;
	public Handler receivingHandler;
	public IRenderNotify renderNotify; 
	
	public OffscreenRenderThread(Handler receivingHandler, IRenderNotify renderNotify, IImageSourceSource sourceSource)
	{
		handlerLock = new ReentrantLock();
		handlerCond = handlerLock.newCondition();
		this.receivingHandler = receivingHandler;
		this.renderNotify = renderNotify;
		this.sourceSource = sourceSource;
		this.setName("Render Thread " + this.getId());
	}
		@Override
	public void run() {
		Looper.prepare();
		handlerLock.lock();
		handler = new Handler();
		handlerCond.signal();
		handlerLock.unlock();
		Looper.loop();
	}

	@Override
	public synchronized void receivePitch(final double pitch, final double timeInSeconds) {
		final ImageSource image = getImage();
		if (image != null) 
		{
			image.addDatapoint(pitch, timeInSeconds);
		}
		
		receivingHandler.post(new Runnable() {

			@Override
			public void run() {
				sourceSource.updateImage();
				renderNotify.renderIsDone(pitch, timeInSeconds);
			}
			
		});
		
	}
	
	private ImageSource getImage() {
		if (this.sourceSource == null) return null;
		return this.sourceSource.getImageSource();
	}
	
	public synchronized void setRenderElement(IImageSourceSource imageSourceSource) {
		this.sourceSource =  imageSourceSource;
		
	}

}
