package umich.pitchcoach.threadman;

import android.content.Context;
import android.os.Handler;
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
	}
	
	public void setRenderElement(IImageSourceSource sourceSource) {
		this.sourceSource = sourceSource;
		if (renderThread != null) renderThread.setRenderElement(sourceSource);
	}

	public void diagnostics() {
		// TODO Auto-generated method stub
		
	}

	public void startRenderThread(IImageSourceSource sourceSource) {
		Log.v("RenderThread", "started");
		if (renderThread != null) stopRenderThread();
		renderThread = new OffscreenRenderThread(this.appHandler, this.renderNotify, sourceSource);
		renderThread.start();
		
		//ANDROID IS FUCKING RETARDED
		renderThread.handlerLock.lock();
		while (renderThread.handler == null) {
			try {
				renderThread.handlerCond.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		pitchservice.startPitchService(renderThread, renderThread.handler);
		renderThread.handlerLock.unlock();
	}
	
	public void stopRenderThread() {
		pitchservice.stopPitchService();
		if (renderThread != null) renderThread.interrupt();
		renderThread = null;
	}
}
