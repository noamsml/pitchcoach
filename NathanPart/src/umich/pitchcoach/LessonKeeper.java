package umich.pitchcoach;

/*
 * Interface to receive other components' requests for
 * 'lessons' in string representation as well as accept and
 * update performance history of each lesson.
 */
public interface LessonKeeper {
	// Suggest a lesson based on spaced repetition algorithms
	public String suggestLesson();
	// Retrieve human-readable form of the specified lesson
	public String getLesson(String lesson);
	// Take performance data and associate with a lesson
	public void updatePerformanceHistory(String lesson, int rating);
	// Give performance data for a lesson
	public String getPerformanceHistory(String lesson);
};