package net.miscfolder.geophoto;

import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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

	// TODO create a dynamic loader.  e.g. public static List<GeoPhoto> loadFromDatabase(handle){}

	/**
	 * Used for generating the object on-the-fly.
	 * @param fileName      Full path of the file
	 * @param apiClient     An instantiated GoogleApiClient to poll location from
	 * @throws SecurityException
	 *                      if we don't have location access (requested in manifest)
	 */
	public GeoPhoto(String fileName, GoogleApiClient apiClient) throws SecurityException{
		this.fileName = fileName;
		Location location = LocationServices.FusedLocationApi.getLastLocation(apiClient);
		this.coordinates = new LatLng(location.getLatitude(), location.getLongitude());
		this.date = new Date();
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
}
