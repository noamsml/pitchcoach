package umich.pitchcoach.data;

import java.io.IOException;
import java.util.Date;
import java.util.ArrayList;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;

public class EventStream {
	private final Context myContext;
	private final DatabaseHelper myDbHelper;

	// Initialize the EventStream
	public EventStream(Context context) {
		this.myContext = context;
		myDbHelper = new DatabaseHelper(context);
		try {
			myDbHelper.createDataBase();
		} catch (IOException ioe) {
			throw new Error("Unable to create database");
		}
	}	
	
	public Event nextEvent(){
		myDbHelper.openDataBase();
		Event nextEvent = new Event();
		Cursor lessonType = selectLesson();
		if (lessonType.moveToFirst()){
			Cursor pitch = selectPitchForInterval(lessonType.getInt(lessonType.getColumnIndex("maxInterval")));
			if (pitch.moveToFirst()){
				nextEvent.lessonId = lessonType.getLong(lessonType.getColumnIndex("_id"));
				nextEvent.pitchId = pitch.getLong(pitch.getColumnIndex("_id"));
				nextEvent.name = pitch.getString(pitch.getColumnIndex("name"))+" "+lessonType.getString(lessonType.getColumnIndex("name"));
				nextEvent.duration = lessonType.getInt(lessonType.getColumnIndex("duration"));
				ArrayList<Float> pitchesToSing = new ArrayList<Float>(lessonType.getInt(lessonType.getColumnIndex("pitchCount")));
				ArrayList<Float> intervals = deserializeIntervalList(lessonType.getString(lessonType.getColumnIndex("intervals")));
				for (int i = 0; i < lessonType.getInt(lessonType.getColumnIndex("pitchCount")); i++){
					pitchesToSing.add(new Float(pitch.getFloat(pitch.getColumnIndex("frequency"))+intervals.get(i)));
				}
				nextEvent.pitchesToSing = pitchesToSing;
				// TODO pitchesToPlay
			}
		}
		myDbHelper.close();
		return nextEvent;
	}
	
	// Returns a Cursor whose first entity is the selected LessonType
	private Cursor selectLesson(){
		return myDbHelper.getAllLessonTypesOrderedLeastRecent();
	}
	
	// Returns a Cursor whose first entity is the selected Pitch
	private Cursor selectPitch(){
		return myDbHelper.getAllPitchesOrderedLeastRecent();
	}
	
	// Given a LessonType's maxInterval, and the configured vocal range,
	// Returns a suitable Pitch
	private Cursor selectPitchForInterval(int maxInterval){
		float max = (float) 1046.5;
		float min = (float) 82.41;
		return myDbHelper.getAllPitchesInRange(min, max);
	}
	
	// Takes comma separated sequence of intervals
	// Returns in an ArrayList object 
	private ArrayList<Float> deserializeIntervalList(String intervals){
		String delims = "[, ]+";
		String[] tokens = intervals.split(delims);
		ArrayList<Float> intervalList = new ArrayList<Float>(tokens.length);
		for (int i = 0; i < tokens.length; i++){
		    intervalList.add(new Float(tokens[i]));
		}
		return intervalList;
	}
}
