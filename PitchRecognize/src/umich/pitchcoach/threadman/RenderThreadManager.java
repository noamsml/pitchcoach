package umich.pitchcoach.threadman;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import umich.pitchcoach.demo.GraphContainer;
import umich.pitchcoach.demo.OffscreenRenderThread;
import umich.pitchcoach.listeners.IImageSourceSource;
import umich.pitchcoach.listeners.IRenderNotify;

public class RenderThreadManager {
	private PitchThreadSpawn pitchservice;
	private OffscreenRenderThread renderThread;
	private Handler appHandler;
	private IRenderNotify renderNotify;
	private IImageSourceSource sourceSource;
	
	public RenderThreadManager(Handler h, IRenderNotify r)
	{
		this.appHandler = h;
		this.renderNotify = r;
		this.pitchservice = new PitchThreadSpawn();
		renderThread = null;
	}
	
	public void setRenderElement(IImageSourceSource sourceSource) {
		this.sourceSource = sourceSource;
		if (renderThread != null) renderThread.setRenderElement(sourceSource);
	}

	public void diagnostics() {
		// TODO Auto-generated method stub
		
	}

	public void startRenderThread(IImageSourceSource sourceSource) {
		Log.v("RenderThread", "started " + sourceSource.toString());
		//Figure out this clusterfuck sometime
		if (renderThread == null)  { //LAMEFIX: Starting render thread multiple times doesn't work
			Log.v("RenderThread", "start allowed");
			renderThread = new OffscreenRenderThread(this.appHandler, this.renderNotify, sourceSource);
			renderThread.start();
			
			//ANDROID IS FUCKING RETARDED
			renderThread.handlerLock.lock();
			while (renderThread.handler == null) {
				try {
					renderThread.handlerCond.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			pitchservice.startPitchService(renderThread, renderThread.handler);
			renderThread.handlerLock.unlock();
		}
	}
	
	public void stopRenderThread() {
		Log.v("RenderThread", "stopped");
		pitchservice.stopPitchService();
		if (renderThread != null) renderThread.handler.post(new Runnable () {

			@Override
			public void run() {
				Looper.myLooper().quit();
			}
			
		});
		renderThread = null;
	}
}
