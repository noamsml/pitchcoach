package umich.pitchcoach.threadman;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import android.os.Handler;
import umich.pitchcoach.Invariants;
import umich.pitchcoach.PitchThread;
import umich.pitchcoach.shared.IPitchReciever;
import umich.pitchcoach.shared.IPitchServiceController;
import android.util.Log;

public class PitchThreadSpawn implements IPitchServiceController {

	private PitchThread thread;
	
	public PitchThreadSpawn() {
		thread = null;
	}
	
	@Override
	public void startPitchService(IPitchReciever callback, Handler handler) {
		if (thread != null) {
			stopPitchService();
			Log.w("Pitch Service Controller", "Pitch service started while another pitch service is running. This is bad practice.");
		}
		
		thread = new PitchThread(callback, handler);
		thread.start();

	}

	@Override
	public void stopPitchService() {
		// TODO Auto-generated method stub
		if (thread != null)
		{
			thread.done = true;
			thread = null;
		}
		else
		{
			Log.w("Pitch Service Controller", "No pitch service running while stopPitchService called");
		}

	}
	
	
	//Note: This is not to be used in production code
	public void diagnosticDumpSamples(final String fname) {
		thread.buflock.lock();
			final float[] data = new float[2 * Invariants.RATE];
			thread.dumpBuffer.readData(data);
		thread.buflock.unlock();
		
		//Another job for MR THREAD MAN
		
		Thread t = new Thread(new Runnable()
		{

			@Override
			public void run() {
				try {
					PrintWriter outfile = new PrintWriter(new FileOutputStream(fname));
					for (int i = 0; i < data.length; i ++ )
					{
						outfile.format("%s\n", data[i]);
					}
					outfile.close();
				}
				catch (FileNotFoundException e)
				{
					Log.e("WriteDataToFile", "Failed");
				}

				
			}
		});
		
		t.start();
	}

}
