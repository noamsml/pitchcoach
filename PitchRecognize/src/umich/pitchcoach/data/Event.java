package umich.pitchcoach.data;

import java.util.Collection;
import java.util.Date;

public class Event {
	// Lesson unique identifier
	public long lessonId;
	
	// Pitch unique identifier
	public long pitchId;

	// Descriptive name
	public String name;
			
	// Event duration in seconds, to be distributed evenly among pitches
	public long duration;
	
	// Cannot be empty
	public Collection<Float> pitchesToSing;
	
	public Collection<Float> pitchesToPlay;	
	
	// Optional, mostly for MESSAGE type events
	public String message;
}
