package umich.pitchcoach.data;

import java.util.Collection;
import java.util.Date;

public class Event {
	// Unique identifier
	public long lessonId;
	
	// Human readable, descriptive name
	public String name;
	
	public static enum eventType {SINGLE, INTERVAL, SCALE, SLIDE, MESSAGE};
	
	// Let the UI decide how to handle this Event
	public eventType type; 
	
	public float averageRating;
	
	// Event duration in seconds, to be distributed evenly among pitches
	public long duration;
	
	// Cannot be empty
	public Collection<Float> pitchesToSing;
	
	// Cannot be empty, will typically have one member
	public Collection<Float> pitchesToPlay;	
	
	// -1 if never performed
	public long lastPerformanceId;
	
	public Date lastPerformanceDate;
	
	public int performanceCount;
	
	// Optional, mostly for MESSAGE type events
	public String message;
}
