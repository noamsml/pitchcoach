package umich.pitchcoach;

import android.content.Context;
import android.database.Cursor;
import umich.pitchcoach.data.entity.*;
import umich.pitchcoach.data.access.*;

/*
 * Class for getting lessons and adding performance data.
 */
public class LessonKeeper {
	private PitchCoachDbHelper dbHelper;
	
	public LessonKeeper(Context context) throws Exception 
	{
		dbHelper = new PitchCoachDbHelper(context);
		dbHelper.createDatabase();
	}

	// Suggest a lesson based on "spaced repetition algorithm"
	// Cycle through the 10 built-in pitches
	private static long DUMMY = 0;
	public Lesson getLesson() throws Exception 
	{
		DUMMY%=10;
		DUMMY++;
		dbHelper.openDatabase();
		Cursor c = dbHelper.getLessonById(DUMMY);
		Lesson lesson = new Lesson();
		if (!c.moveToFirst()){
			return null;
		}
		lesson.setLessonId(c.getLong(Lesson.ID));
		lesson.setLessonName(c.getString(Lesson.NAME));
		lesson.setLessonContent(c.getString(Lesson.CONTENT));
		lesson.setPerformanceCount(c.getLong(Lesson.PERFORMANCE_COUNT));
		lesson.setAverageRating(c.getDouble(Lesson.AVERAGE_RATING));
		lesson.setLastAccessed(c.getString(Lesson.LAST_ACCESSED));
		dbHelper.close();
		return lesson;
	}
	
	public Lesson getLessonById(long id) throws Exception 
	{
		dbHelper.openDatabase();
		Cursor c = dbHelper.getLessonById(id);
		c.moveToFirst();
		Lesson lesson = new Lesson();
		lesson.setLessonId(c.getLong(Lesson.ID));
		lesson.setLessonName(c.getString(Lesson.NAME));
		lesson.setLessonContent(c.getString(Lesson.CONTENT));
		lesson.setPerformanceCount(c.getLong(Lesson.PERFORMANCE_COUNT));
		lesson.setAverageRating(c.getDouble(Lesson.AVERAGE_RATING));
		lesson.setLastAccessed(c.getString(Lesson.LAST_ACCESSED));
		dbHelper.close();
		return lesson;
	}
	
	// Add performance data to history of the specified lesson.
	// Returns long rowid or -1 in case of failure.
	public long addPerformance(long lessonId, String dateAccessed, long rating)
	{
		long rowid;
		dbHelper.openDatabase();
		rowid = dbHelper.insertPerformance(lessonId, dateAccessed, rating);
		dbHelper.close();
		return rowid;
	}
};
