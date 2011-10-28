package umich.pitchcoach.data.entity;

/*
 * Get-Setters for Lesson.
 */
public class Lesson {
	private long   lesson_id;
	private String lesson_name;
	private String lesson_content;
	private long performance_count;
	private double average_rating;
	private String last_accessed; // SQLite supports timestrings
	
	public static final int ID                = 0; 
	public static final int NAME              = 1;
	public static final int CONTENT           = 2;
	public static final int PERFORMANCE_COUNT = 3;
	public static final int AVERAGE_RATING    = 4;
	public static final int LAST_ACCESSED     = 5;
 
	public static final String[] ALL_COLUMNS     = {"_id", 
													"Name", 
													"Content", 
													"PerformanceCount", 
													"AverageRating", 
													"LastAccessed"};
	
	public long getLessonId(){
		return lesson_id;
	}

	public void setLessonId(long id){
		lesson_id = id;
	}

	public String getLessonName(){
		return lesson_name;
	}

	public void setLessonName(String name){
		lesson_name = name;
	}

	public String getLessonContent(){
		return lesson_content;
	}

	public void setLessonContent(String content){
		lesson_content = content;
	}

	public long getPerformanceCount(){
		return performance_count;
	}

	public void setPerformanceCount(long l){
		performance_count = l;
	}

	public double getAverageRating(){
		return average_rating;
	}

	public void setAverageRating(double average){
		average_rating = average;
	}

	public String getLastAccessed(){
		return last_accessed;
	}

	public void setLastAccessed(String date){
		last_accessed = date;
	}
}
