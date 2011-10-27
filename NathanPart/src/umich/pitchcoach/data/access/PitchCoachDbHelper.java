package umich.pitchcoach.data.access;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import umich.pitchcoach.data.entity.*;

/*
 * A helper class for SQLite database creation and access.
 */
public class PitchCoachDbHelper extends SQLiteOpenHelper {

	private static String DB_PATH = "/data/data/umich.pitchcoach/databases/";
	private static String DB_NAME = "pitchcoach.db";
	private final Context context;
	private SQLiteDatabase pitchcoachdb;

	/*
	 * A constructor that keeps a reference to the 
	 * passed context
	 */
	public PitchCoachDbHelper(Context context) 
	{
		super(context, "pitchcoach", null, 1);
		this.context = context;
	}

	/*
	 * Create an empty database and overwrite with
	 * prepared database in /assets
	 */
	public void createDatabase() throws Exception {
		boolean dbExists = checkDatabase();
		if (!dbExists) {
			SQLiteDatabase dbRead = this.getReadableDatabase();
			dbRead.close();
			try {
				copyDatabase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}

	public void openDatabase() throws SQLiteException{
		String myPath = DB_PATH + DB_NAME;
		pitchcoachdb = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
	}


	/*
	 * Return true if the database already exists
	 */
	public boolean checkDatabase() throws Exception
	{
		SQLiteDatabase checkDB = null;
		try{
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
		}catch(SQLiteException e){}
		return checkDB != null;
	}

	/*
	 * Copies database from assets folder to system folder
	 */
	private void copyDatabase() throws IOException
	{
		InputStream source = context.getAssets().open("pitchcoach.db");
		String dbfilepath = DB_PATH + DB_NAME;
		OutputStream destination = new FileOutputStream(dbfilepath);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = source.read(buffer))>0){
			destination.write(buffer, 0, length);
		}
		destination.flush();
		destination.close();
		source.close();
	}

	@Override
	public synchronized void close() {
		if(pitchcoachdb != null)
			pitchcoachdb.close();
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

	public Cursor getLessonById(long id) throws Exception{
		return pitchcoachdb.query("Lesson", Lesson.ALL_COLUMNS, "_id like ?", new String[] {""+id}, null, null, null);
	}
}
