package umich.pitchcoach.data;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{

	private static String DB_PATH = "/data/data/umich.pitchcoach/databases/";
	private static String DB_NAME = "PitchCoach.sqlite";

	private SQLiteDatabase myDatabase; 

	private final Context myContext;

	/**
	 * Constructor
	 * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
	 * @param context
	 */
	public DatabaseHelper(Context context) {

		super(context, DB_NAME, null, 1);
		this.myContext = context;
	}	

	/**
	 * Creates a empty database on the system and rewrites it with your own database.
	 */
	public void createDataBase() throws IOException{

		boolean dbExist = checkDataBase();

		if(dbExist){
			//do nothing - database already exist
		}else{

			//By calling this method and empty database will be created into the default system path
			//of your application so we are gonna be able to overwrite that database with our database.
			this.getReadableDatabase();

			try {

				copyDataBase();

			} catch (IOException e) {

				throw new Error("Error copying database");

			}
		}

	}

	/**
	 * Check if the database already exist to avoid re-copying the file each time you open the application.
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase(){

		SQLiteDatabase checkDB = null;

		try{
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

		}catch(SQLiteException e){

			//database does't exist yet.

		}

		if(checkDB != null){

			checkDB.close();

		}

		return checkDB != null ? true : false;
	}

	/**
	 * Copies your database from your local assets-folder to the just created empty database in the
	 * system folder, from where it can be accessed and handled.
	 * This is done by transferring byte stream.
	 * */
	private void copyDataBase() throws IOException{

		//Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(DB_NAME);

		// Path to the just created empty db
		String outFileName = DB_PATH + DB_NAME;

		//Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		//transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer))>0){
			myOutput.write(buffer, 0, length);
		}

		//Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	public void openDataBase() throws SQLException{
		//Open the database
		String myPath = DB_PATH + DB_NAME;
		if (myDatabase==null || !myDatabase.isOpen()){
			myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
		}
	}

	@Override
	public synchronized void close() {

		if(myDatabase != null)
			myDatabase.close();

		super.close();

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}


	// Add your public helper methods to access and get content from the database.
	// You could return cursors by doing "return myDatabase.query(....)" so it'd be easy
	// to you to create adapters for your views.
	public long countPitches(){
		Cursor dataCount = myDatabase.rawQuery("select * from Pitch", null);
		return dataCount.getCount();
	}
	
	public long countLessonTypes(){
		Cursor dataCount = myDatabase.rawQuery("select * from LessonType", null);
		return dataCount.getCount();
	}
	
	public long countPerformances(){
		Cursor dataCount = myDatabase.rawQuery("select * from Performance", null);
		return dataCount.getCount();
	}
	
	public Cursor getPitchById(long id){
		Cursor pitch = myDatabase.rawQuery("select * from Pitch where _id = "+id, null);
		return pitch;
	}


	public long getPitchIdFromFreq(double freq){
		Cursor pitches = myDatabase.rawQuery("select * from Pitch where frequency = "+freq, null);
		pitches.moveToFirst();
		return pitches.getLong(pitches.getColumnIndex("_id"));
	}
	
	public Cursor getAllPitchesOrderedRandom(){
		Cursor pitches = myDatabase.rawQuery("select * from Pitch order by RANDOM()", null);
		return pitches;
	}
	
	public Cursor getAllPitchesOrderedLeastRecent(){
		Cursor pitches = myDatabase.rawQuery("select * from Pitch order by 'latestPerformanceDate' ASC", null);
		return pitches;
	}
	
	public Cursor getAllPitchesOrderedLowestRating(){
		Cursor pitches = myDatabase.rawQuery("select * from Pitch order by 'averageRating' ASC", null);
		return pitches;
	}
	
	public Cursor getAllPitchesInRange(double min, double max){
		Cursor pitches = myDatabase.rawQuery("select * from Pitch where frequency >= "+min+" AND frequency <= "+max+" order by RANDOM()", null);
		return pitches;
	}
	
	public Cursor getRecentPitchesInRange(double min, double max){
		Cursor pitches = myDatabase.rawQuery("select * from Pitch where frequency >= "+min+" AND frequency <= "+max+" order by latestPerformanceDate asc", null);
		return pitches;
	}

	
	public void updateLessonType(long id, long perfId, double averageRating, long perfCount, String date){
		ContentValues pv = new ContentValues();
		pv.put("latestPerformance", perfId);
		pv.put("averageRating", averageRating);
		pv.put("performanceCount", perfCount);
		pv.put("latestPerformanceDate", date);
		myDatabase.update("LessonType", pv, "_id=?", new String[]{""+id});
	}
	
	public void updatePitch(long id, long perfId, double averageRating, long perfCount, String date){
		ContentValues pv = new ContentValues();
		pv.put("latestPerformance", perfId);
		pv.put("averageRating", averageRating);
		pv.put("performanceCount", perfCount);
		pv.put("latestPerformanceDate", date);
		myDatabase.update("Pitch", pv, "_id=?", new String[]{""+id});
	}

	
	public Cursor getLessonTypeById(long id){
		Cursor lessonTypes = myDatabase.rawQuery("select * from LessonType where _id = "+id, null);
		return lessonTypes;
	}
	
	public Cursor getAllLessonTypesOrderedRandom(){
		Cursor lessonTypes = myDatabase.rawQuery("select * from LessonType order by RANDOM()", null);
		return lessonTypes;
	}
	
	public Cursor getAllLessonTypesOrderedLeastRecent(){
		Cursor lessonTypes = myDatabase.rawQuery("select * from LessonType order by 'latestPerformanceDate' asc", null);
		return lessonTypes;
	}
	
	public Cursor getAllLessonTypesOrderedLowestRating(){
		Cursor lessonTypes = myDatabase.rawQuery("select * from LessonType order by 'averageRating' ASC", null);
		return lessonTypes;
	}

	public Cursor getLessonTypeByName(String name){
		Cursor lessonType = myDatabase.rawQuery("select * from LessonType where 'name' = "+name, null);
		return lessonType;
	}
	
	public Cursor getLessonTypeNote(){
		Cursor lessonType = myDatabase.rawQuery("select * from LessonType where name like '%Note%' order by ", null);
		return lessonType;
	}
	
	public Cursor getLessonTypeInterval(){
		Cursor lessonType = myDatabase.rawQuery("select * from LessonType where name not like '%Note%' and name not like '%Chord%' and name not like '%Scale%'", null);
		return lessonType;
	}
	
	public Cursor getLessonTypeChord(){
		Cursor lessonType = myDatabase.rawQuery("select * from LessonType where name like '%Chord%'", null);
		return lessonType;
	}

	public Cursor getLessonTypeScale(){
		Cursor lessonType = myDatabase.rawQuery("select * from LessonType where name like '%Scale%'", null);
		return lessonType;
	}
	
	public long addPerformance(long lessonId, long pitchId, double rating){
		ContentValues performancevalues = new ContentValues();
		performancevalues.put("rating", rating);
		performancevalues.put("lessonType", lessonId);
		performancevalues.put("pitch", pitchId);
		performancevalues.put("performanceDate", new Timestamp(new Date().getTime()).toString());
		return myDatabase.insert("Performance", null, performancevalues);
	}
}
