package umich.pitchcoach.data.entity;

/*
 * Get-Setters for Performance.
 */
public class Performance{
	private long lesson_id;
	private long performance_id;
	private String date_accessed;
	private long rating;
	
	public static enum e_Indices {ID, LESSON_ID, DATE_ACCESSED, RATING};
	public static final String[] ALL_COLUMNS = {"_id",
												"LessonId",
												"DateAccessed",
												"Rating"};
	public long getLessonId(){
		return lesson_id;
	}
	
	public void setLessonId(long id){
		lesson_id = id;
	}
	
	public long getPerformanceId(){
		return performance_id;
	}
	
	public void setPerformanceId(long id){
		performance_id = id;
	}
	
	public String getDateAccessed(){
		return date_accessed;
	}
	
	public void setDateAccessed(String date){
		date_accessed = date;
	}
	
	public long getRating(){
		return rating;
	}
	
	public void setRating(long value){
		rating = value;
	}
}
