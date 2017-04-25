package net.miscfolder.geophoto;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.AsyncTask;
import android.text.format.DateUtils;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Representation of an image with metadata, specifically a file name, coordinates, and a date.
 * Preferred construction is via the literal constructor, since the on-the-fly constructor throws
 * an exception.
 * @author Nathaniel
 */
public class GeoPhoto {
	private static final DateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

	private final String fileName;
	private final LatLng coordinates;
	private final Date date;

	/**
	 * Simple wrapper for literal constructor that splits LatLng into two doubles.
	 * @param fileName      Full path of file
	 * @param latitude      Latitude where photo was taken
	 * @param longitude     Longitude where photo was taken
	 * @param date          Date photo was taken
	 */
	public GeoPhoto(String fileName, double latitude, double longitude, Date date){
		this(fileName, new LatLng(latitude, longitude), date);
	}

	/**
	 * Used for generating the object literally.
	 * @param fileName      Full path of file
	 * @param coordinates   Coordinates where photo was taken
	 * @param date          Date photo was taken
	 */
	public GeoPhoto(String fileName, LatLng coordinates, Date date){
		this.fileName = fileName;
		this.coordinates = coordinates;
		this.date = date;
	}

	public String getFileName() {
		return fileName;
	}

	public LatLng getCoordinates() {
		return coordinates;
	}

	public Date getDate() {
		return date;
	}

	public String getInfoString() {
		return FORMATTER.format(date) + " - " + coordinates.latitude + " - " + coordinates.longitude;
	}

	// Database methods
	private static String[] SELECT_COLS = {
			DatabaseHelper.Photos.COLUMN_NAME_FILENAME,
			DatabaseHelper.Photos.COLUMN_NAME_LATITUDE,
			DatabaseHelper.Photos.COLUMN_NAME_LONGITUDE,
			DatabaseHelper.Photos.COLUMN_NAME_DATE
	};

	/**
	 * Tries to load everything from the database into a List using the provided helper.
	 * @param helper    The DatabaseHelper to use
	 * @return          A List of all GeoPhoto objects in the database.
	 */
	public static List<GeoPhoto> load(DatabaseHelper helper){
		SQLiteDatabase database = helper.getReadableDatabase();
		Cursor cursor = database.query(DatabaseHelper.Photos.TABLE_NAME, SELECT_COLS, null, null,
				null, null, DatabaseHelper.Photos.COLUMN_NAME_DATE + "DESC");
		List<GeoPhoto> geoPhotoList = new LinkedList<>();
		while (cursor.moveToNext()){
			try {
				geoPhotoList.add(new GeoPhoto(
						cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.Photos.COLUMN_NAME_FILENAME)),
						cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.Photos.COLUMN_NAME_LATITUDE)),
						cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.Photos.COLUMN_NAME_LONGITUDE)),
						FORMATTER.parse(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.Photos.COLUMN_NAME_DATE)))));
			} catch (ParseException e) {
				Log.e(GeoPhoto.class.getCanonicalName(), "load: Error loading photo [invalid date]", e);
			} catch (IllegalArgumentException e) {
				Log.e(GeoPhoto.class.getCanonicalName(), "load: Error loading photo [invalid column data]", e);
			}
		}
		return geoPhotoList;
	}

	/**
	 * Writes the current GeoPhoto to the database using the provided helper object.
	 * @param helper    The DatabaseHelper to use
	 * @return          true if nothing went wrong, false otherwise [specifically db.insert(...) != -1]
	 */
	public synchronized boolean save(DatabaseHelper helper){
		SQLiteDatabase database = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.Photos.COLUMN_NAME_FILENAME, this.fileName);
		values.put(DatabaseHelper.Photos.COLUMN_NAME_LATITUDE, this.coordinates.latitude);
		values.put(DatabaseHelper.Photos.COLUMN_NAME_LONGITUDE, this.coordinates.longitude);
		values.put(DatabaseHelper.Photos.COLUMN_NAME_DATE, FORMATTER.format(this.date));
		return database.insert(DatabaseHelper.Photos.TABLE_NAME, null, values) != -1;
	}

	/**
	 * Deletes the current GeoPhoto from the database using the provided helper object.
	 * @param helper    The DatabaseHelper to use
	 * @return          true if at least one row was deleted, false otherwise
	 */
	public synchronized boolean delete(DatabaseHelper helper){
		SQLiteDatabase database = helper.getWritableDatabase();
		return database.delete(DatabaseHelper.Photos.TABLE_NAME, "? = ?", new String[]{
				DatabaseHelper.Photos.COLUMN_NAME_FILENAME, this.getFileName()}) > 0;
	}
}
