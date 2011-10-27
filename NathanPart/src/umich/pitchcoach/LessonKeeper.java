package umich.pitchcoach;

import java.io.IOException;

import android.content.Context;
import android.database.Cursor;
import umich.pitchcoach.data.entity.*;
import umich.pitchcoach.data.access.*;

/*
 * Interface for getting lessons and adding performance data.
 */
public class LessonKeeper {
	private PitchCoachDbHelper dbHelper;
	
	public LessonKeeper(Context context) throws Exception 
	{
		dbHelper = new PitchCoachDbHelper(context);
		dbHelper.createDatabase();
	}
	
	// Suggest a lesson based on spaced repetition algorithm
	public Lesson getLessonById(long id) throws Exception {
		dbHelper.openDatabase();
		Cursor c = dbHelper.getLessonById(id);
		c.moveToFirst();
		Lesson lesson = new Lesson();
		lesson.setLessonId(c.getLong(0));
		lesson.setLessonName(c.getString(1));
		lesson.setLessonContent(c.getString(2));
		lesson.setPerformanceCount(c.getLong(3));
		lesson.setAverageRating(c.getDouble(4));
		lesson.setLastAccessed(c.getString(5));
		return lesson;
	}
	
	// Add performance data to history of the specified lesson
	public long addPerformance(long lessonId, String dateAccessed, long rating){
		return 0;
	}
};
