package net.miscfolder.geophoto;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Nathaniel on 4/13/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper{
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "GeoPhoto.db";

	private static final String SQL_CREATE_ENTRIES =
			"CREATE TABLE " + Photos.TABLE_NAME + " (" +
					Photos.COLUMN_NAME_FILENAME + " TEXT PRIMARY KEY," +
					Photos.COLUMN_NAME_LATITUDE + " DOUBLE, " +
					Photos.COLUMN_NAME_LONGITUDE + " DOUBLE, " +
					Photos.COLUMN_NAME_DATE + " TEXT)";

	private static final String SQL_DELETE_ENTRIES =
			"DROP TABLE IF EXISTS " + Photos.TABLE_NAME;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_ENTRIES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Until there's a new version, this should never be called.  Back up the old DB and
		// initialize the new one.
		// TODO backup old database
		db.execSQL(SQL_DELETE_ENTRIES);
		onCreate(db);
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// See note in DatabaseHelper:onUpgrade(...)
		onUpgrade(db, oldVersion, newVersion);
	}

	public static class Photos implements BaseColumns{
		public static final String TABLE_NAME = "photos";
		public static final String COLUMN_NAME_FILENAME = "filename";
		public static final String COLUMN_NAME_LATITUDE = "latitude";
		public static final String COLUMN_NAME_LONGITUDE = "longitude";
		public static final String COLUMN_NAME_DATE = "date";
	}
}
