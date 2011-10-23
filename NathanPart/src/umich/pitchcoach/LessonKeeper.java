package umich.pitchcoach;

import umich.pitchcoach.data.entity.*;

/*
 * Interface for getting lessons and adding performance data.
 */
public interface LessonKeeper {
	// Suggest a lesson based on spaced repetition algorithm
	public Lesson getLesson();
	
	// Add performance data to history of the specified lesson
	public void addPerformance(long lesson_id, String timestamp, long rating);
};
