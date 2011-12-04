package umich.pitchcoach.flow;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogPromise extends Promise {
	
	private Activity activity;
	private AlertDialog messagebox;

	public DialogPromise(Activity activity, String message) {
		this.activity = activity;
		this.messagebox = new AlertDialog.Builder(activity).create();
		this.messagebox.setMessage(message);
		this.messagebox.setTitle("Message");
		final Promise that = this; 
		this.messagebox.setButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				that.done();
			}
		});
		
		this.messagebox.setCancelable(false);
	}
	
	
	public void go() {
		this.messagebox.show();
	}
	
}
