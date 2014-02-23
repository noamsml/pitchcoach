package umich.pitchcoach.test;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import umich.pitchcoach.shared.IPitchReciever;
import android.content.res.XmlResourceParser;
import android.os.Handler;
import android.util.Log;

public class MockPitchThread extends Thread {
	IPitchReciever notifyRecv;
	Handler receivingHandler;
	XmlResourceParser xmlFile;

	// float[] dataBuffer;
	public final int RATE = 44100;
	public final int NUMSAMPLES = 256;
	public boolean done;

	public MockPitchThread(XmlResourceParser xmlFile,
			IPitchReciever notifyRecv, Handler receivingHandler) {
		this.notifyRecv = notifyRecv;
		done = false;
		this.receivingHandler = receivingHandler;
		this.xmlFile = xmlFile;

	}

	public void run() {
		try {
			while (xmlFile.getEventType() != XmlPullParser.END_DOCUMENT) {
				if (xmlFile.getEventType() == XmlPullParser.START_TAG) {
					if (xmlFile.getName().equals("event")) {
						handleEvent(xmlFile);
					}
				}
				xmlFile.next();
			}
		} catch (XmlPullParserException e) {
			Log.e("MockPitchThread", "Bad Mock Data");
		} catch (IOException e) {
			Log.e("MockPitchThread", "IO Error");
		} catch (InterruptedException e) {
			return;
		}
	}

	// Note: This XML parser sucks
	private void handleEvent(XmlPullParser xmlFile)
			throws XmlPullParserException, IOException, InterruptedException {
		String currentTagName = null;
		int type = 1;
		double pitch = 0;
		double duration = 0;

		xmlFile.next();
		while (true) {
			if (xmlFile.getEventType() == XmlPullParser.START_TAG) {
				currentTagName = xmlFile.getName();
			} else if (xmlFile.getEventType() == XmlPullParser.TEXT) {
				if (currentTagName.equals("type")) {
					if (xmlFile.getText().equals("pitch")) {
						type = 1;
					}
				}

				else if (currentTagName.equals("val")) {
					pitch = Double.parseDouble(xmlFile.getText());
				}

				else if (currentTagName.equals("length")) {
					duration = Double.parseDouble(xmlFile.getText());
				}
			} else if (xmlFile.getEventType() == XmlPullParser.END_TAG) {
				// Totally bad practice. Sue me.
				if (xmlFile.getName().equals("event")) {
					break;
				}
			}
			xmlFile.next();
		}

		execEvent(type, pitch, duration);
	}

	private void execEvent(int type, double pitch, double duration)
			throws InterruptedException {
		switch (type) {
		case 1:
			Thread.sleep(Math.round(duration * 1000));
			onPitch(pitch, duration);
		}
	}

	public void onPitch(final double pitch, final double length) {
		receivingHandler.post(new Runnable() {

			@Override
			public void run() {
				notifyRecv.receivePitch(pitch, length);
			}

		});
	}

}