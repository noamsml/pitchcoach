package umich.pitchcoach.data;

import java.util.Collection;
import java.util.Date;

import umich.pitchcoach.NotePlayer.Note;

public class Event {
	
	public Event(){
	}
	
	public Event(Event event){
		lessonId = event.lessonId;
		pitchId = event.pitchId;
		name = event.name;
		duration = event.duration;
		notesToPlay = event.notesToPlay;
		basePitch = event.basePitch;
		pitchesToSing = event.pitchesToSing;
		maxInterval = event.maxInterval;
	}
	
	// Lesson unique identifier
	public long lessonId;
	
	// Pitch unique identifier
	public long pitchId;
	
	public int maxInterval;

	// Descriptive name
	public String name;
			
	// Event duration in seconds, to be distributed evenly among pitches
	public long duration;
	
	public Note[] notesToPlay;
	
	public double basePitch;
	
	// Cannot be empty
	public Collection<String> pitchesToSing;	
}
