package umich.pitchcoach.NotePlayer;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.media.MediaPlayer;

public class NotePlayer extends MediaPlayer {
	
	@SuppressWarnings("unused")
	private File targetNote;
	
	
	
	public NotePlayer(){
		super();
		targetNote = null;
	}	

	// play .wav/.mid from the resources folder
	public void playNote(Note note) throws IOException, FileNotFoundException{	
		// look for correct note file
		
		String f = "res/raw/boing_spring.wav";
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(f);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FileDescriptor fd = null;
		try {
			fd = fis.getFD();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.setDataSource( fd );
		this.prepare();
		this.start();
	}
	
	public void playNote(String note) throws IOException{	
		Note target = convertNoteStringToNoteEnum( note );
		this.playNote(target);
	}

	private Note convertNoteStringToNoteEnum(String note) {
		Note noteReturn = null;
		
		// convert string to enum
		
		
		return noteReturn;
	}
	
}

	
	
	
	
	
	