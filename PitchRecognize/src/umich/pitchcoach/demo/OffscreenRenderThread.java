package umich.pitchcoach.demo;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

import umich.pitchcoach.shared.IPitchReciever;
import android.content.Context;
import android.os.Looper;
import android.os.Handler;

public class OffscreenRenderThread extends Thread implements IPitchReciever {
	ImageSource image;
	GraphGlue uiGlue;
	Handler handler;
	Lock handlerLock;
	Condition handlerCond;
	
	public OffscreenRenderThread(GraphGlue uiGlue, Context ctx)
	{
		//pitchservice = new MockPitchThreadSpawn(R.xml.replay_values, ctx.getResources());
		this.uiGlue = uiGlue;
		handlerLock = new ReentrantLock();
		handlerCond = handlerLock.newCondition();
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
		if (image != null) image.addDatapoint(pitch, timeInSeconds);
		uiGlue.onPitch(pitch, timeInSeconds);
		
	}
	
	public synchronized void setImage(ImageSource image)
	{
		this.image = image;
	}

}
