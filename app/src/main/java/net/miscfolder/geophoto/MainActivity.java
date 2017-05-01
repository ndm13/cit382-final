package net.miscfolder.geophoto;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {
	public static final DateFormat FILE_TIMESTAMP_FORMATTER =
			new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US);
	public static final File FILE_STORAGE_DIR =
			Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
	private static final int PHOTO_INTENT = 1;
	public static final int SHARE_INTENT = 2;
	private static final int PLACE_INTENT = 3;
	public static DatabaseHelper DATABASE_HELPER;
	public static Activity activity;

	private GoogleApiClient apiClient;
	private File lastCapturedFile;

	/**
	 * onClick handler for R.id.floatingActionButton
	 *
	 * Should launch a photo capture intent, generate a GeoPhoto, and save the photo to the gallery
	 * and the database.  Then the RecyclerView should be refreshed.
	 *
	 * @param view     FloatingActionButton
	 */
	public void capturePhoto(View view) {
		String timeStamp = FILE_TIMESTAMP_FORMATTER.format(new Date());
		String imageFileName = timeStamp + ".jpg";
		lastCapturedFile = new File(FILE_STORAGE_DIR, imageFileName);
		Uri outputFileUri = Uri.fromFile(lastCapturedFile);
		Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
		startActivityForResult(cameraIntent, PHOTO_INTENT);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case PHOTO_INTENT:
				// Handle camera activity result
				if (resultCode == RESULT_OK && lastCapturedFile != null && lastCapturedFile.length() > 0) {
					try {
						Intent placeIntent = new PlacePicker.IntentBuilder().build(this);
						startActivityForResult(placeIntent, PLACE_INTENT);
					} catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
						e.printStackTrace();
					}

				} else {
					Toast.makeText(this, "Camera didn't return an image!", Toast.LENGTH_SHORT).show();
					if (lastCapturedFile.exists())
						if(!lastCapturedFile.delete())
							Toast.makeText(this, "Error deleting temporary file!", Toast.LENGTH_LONG).show();
					lastCapturedFile = null;
				}
				break;
			case SHARE_INTENT:
				if(resultCode == RESULT_OK){
					Toast.makeText(this, "Shared photo!", Toast.LENGTH_SHORT).show();
				}
				break;
			case PLACE_INTENT:
				if(resultCode == RESULT_OK){
					Place place = PlacePicker.getPlace(this, data);
					// Generate and save photo
					GeoPhoto photo = new GeoPhoto(lastCapturedFile.getAbsolutePath(), place.getLatLng(), new Date());
					photo.save(DATABASE_HELPER);
					// Update library
					Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
					Uri contentUri = Uri.fromFile(lastCapturedFile);
					mediaScanIntent.setData(contentUri);
					getApplicationContext().sendBroadcast(mediaScanIntent);
					// TODO refresh RecyclerView
					Toast.makeText(this, "Photo added!", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(this, "Could not access PlacePicker API!", Toast.LENGTH_LONG).show();
				}
				break;
		}

	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		activity = this;

		// Set the tab contents
		TabHost host = (TabHost)findViewById(R.id.tabHost);
		host.setup();

		TabHost.TabSpec spec = host.newTabSpec("photo_list");
		spec.setContent(R.id.photo_list);
		spec.setIndicator("Photo List");
		host.addTab(spec);

		spec = host.newTabSpec("map_view");
		spec.setContent(R.id.map_view);
		spec.setIndicator("Map View");
		host.addTab(spec);

		// Create the API client
		apiClient = new GoogleApiClient
				.Builder(this)
				.addApi(Places.GEO_DATA_API)
				.addApi(Places.PLACE_DETECTION_API)
				.enableAutoManage(this, this)
				.build();

		// Create the database helper
		DATABASE_HELPER = new DatabaseHelper(this);
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		// TODO initialize map
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
		Log.e(MainActivity.class.getCanonicalName(),
				"API connection couldn't be established: " + connectionResult.getErrorMessage());
	}
}
