package net.miscfolder.geophoto;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionApi;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.IllegalFormatCodePointException;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {
	public static final DateFormat FILE_TIMESTAMP_FORMATTER =
			new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US);
	public static final File FILE_STORAGE_DIR =
			Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
	private static final int PHOTO_INTENT = 1;
	public static final int SHARE_INTENT = 2;
	public static DatabaseHelper DATABASE_HELPER;

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
					handlePhotoData(data);
				} else {
					Toast.makeText(this, "Camera didn't return an image!", Toast.LENGTH_SHORT).show();
					if (lastCapturedFile.exists()) lastCapturedFile.delete();
					lastCapturedFile = null;
				}
				break;
			case SHARE_INTENT:
				if(resultCode == RESULT_OK){
					Toast.makeText(this, "Shared photo!", Toast.LENGTH_SHORT).show();
				}
				break;
		}

	}

	private void handlePhotoData(Intent data) {
		// Get location
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			throw new IllegalStateException("Permissions not available!");
		}
		PendingResult<PlaceLikelihoodBuffer> pendingResult = Places.PlaceDetectionApi.getCurrentPlace(apiClient, null);
		pendingResult.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
			@Override
			public void onResult(@NonNull PlaceLikelihoodBuffer placeLikelihoods) {
				Place place = placeLikelihoods.get(0).getPlace();
				// Generate and save photo
				GeoPhoto photo = new GeoPhoto(lastCapturedFile.getAbsolutePath(), place.getLatLng(), new Date());
				photo.save(DATABASE_HELPER);
				// Update library
				Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
				Uri contentUri = Uri.fromFile(lastCapturedFile);
				mediaScanIntent.setData(contentUri);
				getApplicationContext().sendBroadcast(mediaScanIntent);
				// TODO refresh RecyclerView
			}
		});
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//TODO Link tabs with corresponding fragments
		TabHost host = (TabHost)findViewById(R.id.tabHost);
		host.setup();

		//Tab 1
		TabHost.TabSpec spec = host.newTabSpec("MapFragment");
		spec.setContent(R.id.tab1);
		spec.setIndicator("MapFragment");
		host.addTab(spec);

		//Tab 2
		spec = host.newTabSpec("ListFragment");
		spec.setContent(R.id.tab2);
		spec.setIndicator("ListFragment");
		host.addTab(spec);

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
