package umich.pitchcoach.data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.ArrayList;

import umich.pitchcoach.LetterNotes;
import umich.pitchcoach.NotePlayer.Note;
import umich.pitchcoach.dataAdapt.IPitchSource;
import umich.pitchcoach.demo.RangeSelect;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;

public class EventStream implements IPitchSource {
	private final Context myContext;
	private static DatabaseHelper myDbHelper;
	private double minfreq, maxfreq;
	private String difficulty;
	private boolean rangeIsSet = false;
	private static final double DEFAULT_MIN_FREQ = 130;
	private static final double DEFAULT_MAX_FREQ = 523;

	// Initialize the EventStream
	public EventStream(Context context) {
		minfreq = DEFAULT_MIN_FREQ;
		maxfreq = DEFAULT_MAX_FREQ;
		difficulty = "e";
		this.myContext = context;
		myDbHelper = new DatabaseHelper(context);
		try {
			myDbHelper.createDataBase();
			myDbHelper.openDataBase();
		} catch (IOException ioe) {
			throw new Error("Unable to create database");
		}
	}	

	public Event getNextEvent(){
		myDbHelper.openDataBase();
		Event nextEvent = new Event();
		Cursor lessonType = selectLesson();
		if (lessonType.moveToFirst()){
			nextEvent.maxInterval = lessonType.getInt(lessonType.getColumnIndex("maxInterval"));
			setVocalRangeFromFile();
			Cursor pitch = myDbHelper.getAllPitchesInRange(minfreq, maxFreqWithInterval(nextEvent.maxInterval));
			if (pitch.moveToFirst()){
				setEvent(nextEvent, lessonType, pitch);
			} else {
				getNextEvent();
			}
		}
		myDbHelper.close();
		return nextEvent;
	}
	
	private void setEvent(Event nextEvent, Cursor lessonType, Cursor pitch){
		nextEvent.lessonId = lessonType.getLong(lessonType.getColumnIndex("_id"));
		nextEvent.pitchId = pitch.getLong(pitch.getColumnIndex("_id"));
		nextEvent.name = pitch.getString(pitch.getColumnIndex("name"))+" "+lessonType.getString(lessonType.getColumnIndex("name"));
		nextEvent.duration = lessonType.getInt(lessonType.getColumnIndex("duration"));
		int pitchCount = lessonType.getInt(lessonType.getColumnIndex("pitchCount"));
		ArrayList<String> pitchesToSing = new ArrayList<String>(pitchCount);
		ArrayList<Float> intervals = deserializeIntervalList(lessonType.getString(lessonType.getColumnIndex("intervals")));
		Note[] notesToPlay;
		if (pitchCount>1){
			notesToPlay = new Note[pitchCount+1];
		} else {
			notesToPlay = new Note[pitchCount];
		}
		double basePitch = pitch.getDouble(pitch.getColumnIndex("frequency"));
		pitchesToSing.add(LetterNotes.freqToNoteSpec(basePitch));
		notesToPlay[0] = new Note(basePitch, nextEvent.duration/(double) pitchCount);
		for (int i = 1; i < pitchCount; i++){
			int steps = (int) (intervals.get(i)*2);
			double freq = basePitch*Math.pow(LetterNotes.stepVal,steps); 
			pitchesToSing.add(LetterNotes.freqToNoteSpec(freq));
			notesToPlay[i] = new Note(freq, nextEvent.duration/(double) pitchCount);
		}
		if (pitchCount > 1){
			notesToPlay[pitchCount] = new Note(basePitch, 1);
		}
		nextEvent.notesToPlay = notesToPlay;
		nextEvent.pitchesToSing = pitchesToSing;
	}
	

	public void setVocalRange(double min, double max) {
		if (!rangeIsSet) {
			rangeIsSet = true;
		}
		minfreq = min;
		maxfreq = max;
	}

	private void setDifficultyFromFile() {
		StringBuilder inb = new StringBuilder();
		try {
			FileInputStream fis = myContext.openFileInput(RangeSelect.DIFFICULTY_FILENAME);
			int ch;
			while((ch = fis.read()) != -1)
				inb.append((char)ch);
			fis.close();
			difficulty = inb.toString();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	private void setVocalRangeFromFile() {
		StringBuilder inb = new StringBuilder();
		try {
			FileInputStream fis = myContext.openFileInput(RangeSelect.FILENAME);
			int ch;
			while((ch = fis.read()) != -1)
				inb.append((char)ch);
			fis.close();
			String delims = "[ ]+";
			String[] tokens = inb.toString().split(delims);
			if (tokens.length == 2){
				minfreq = Double.valueOf(tokens[0]);
				maxfreq = Double.valueOf(tokens[1]);
				rangeIsSet = true;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rangeIsSet = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rangeIsSet = false;
		}
	}

	public void clearVocalRange(){
		rangeIsSet = false;
	}

	public static void submitEventPerformance(String pitch, Event event, long rating){
		if (event!=null){
			myDbHelper.openDataBase();
			long perfId = myDbHelper.addPerformance(event.lessonId, event.pitchId, rating);
			if (perfId != -1) {
				Performance perf = new Performance();
				perf.performanceId = perfId;
				perf.lessonId = event.lessonId;
				perf.pitchId = event.pitchId;
				perf.performanceDate = new Timestamp(new Date().getTime()).toString();
				perf.performanceRating = rating;
				updateLessonTypePerformance(event.lessonId, rating, perf);
				updatePitchPerformance(event.pitchId, rating, perf);
			}
			myDbHelper.close();
		}
	}

	private static void updateLessonTypePerformance(long lessonId, long rating, Performance perf){
		Cursor lesson = myDbHelper.getLessonTypeById(lessonId);
		if (lesson.moveToFirst()) {
			double averageRating = lesson.getDouble(lesson.getColumnIndex("averageRating"));
			int perfCount = lesson.getInt(lesson.getColumnIndex("performanceCount"));
			averageRating = ((perfCount * averageRating) + rating)/(double) (perfCount + 1);
			myDbHelper.updateLessonType(lessonId, perf.performanceId, averageRating, perfCount, perf.performanceDate);
		}
	}

	private static void updatePitchPerformance(long pitchId, long rating, Performance perf){
		Cursor pitch = myDbHelper.getPitchById(pitchId);
		if (pitch.moveToFirst()) {
			double averageRating = pitch.getDouble(pitch.getColumnIndex("averageRating"));
			int perfCount = pitch.getInt(pitch.getColumnIndex("performanceCount"));
			averageRating = ((perfCount * averageRating) + rating)/(double) (perfCount + 1);
			myDbHelper.updatePitch(pitchId, perf.performanceId, averageRating, perfCount, perf.performanceDate);
		}
	}


	// Returns a Cursor whose first entity is the selected LessonType
	private Cursor selectLesson(){
		setDifficultyFromFile();
		if (difficulty.equalsIgnoreCase("e")){
			return myDbHelper.getLessonTypeEasy();
		} else if (difficulty.equalsIgnoreCase("m")){
			return myDbHelper.getLessonTypeMedium();
		} else {
			return myDbHelper.getAllLessonTypesOrderedRandom();
		}
	}
	
	public Cursor easyTest(){
		myDbHelper.openDataBase();
		return myDbHelper.getLessonTypeEasy();
	}

	public Cursor medTest(){
		myDbHelper.openDataBase();
		return myDbHelper.getLessonTypeMedium();
	}
	
	public Cursor hardTest(){
		myDbHelper.openDataBase();
		return myDbHelper.getAllLessonTypesOrderedRandom();
	}



	

	// Returns a Cursor whose first entity is the selected Pitch
	private Cursor selectPitch(){
		if (rangeIsSet){
			return myDbHelper.getAllPitchesInRange(minfreq, maxfreq);
		} else {
			return myDbHelper.getAllPitchesInRange(DEFAULT_MIN_FREQ, DEFAULT_MAX_FREQ);
		}
	}

	public String getNextPitch(){
		myDbHelper.openDataBase();
		Cursor pitches = selectPitch();
		if (pitches.moveToFirst()){
			myDbHelper.close();
			return pitches.getString(pitches.getColumnIndex("name"));
		}
		return null;
	}
	
	private double maxFreqWithInterval(int interval){
		return maxfreq*Math.pow(LetterNotes.stepVal,-interval); 
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
