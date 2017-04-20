package net.miscfolder.geophoto;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
		GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
	public static GoogleApiClient GOOGLE_API_CLIENT;
	public static final DateFormat FILE_TIMESTAMP_FORMATTER =
			new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US);
	public static final File FILE_STORAGE_DIR =
			Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
	public static DatabaseHelper DATABASE_HELPER;

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
		startActivityForResult(cameraIntent, 1);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Handle camera activity result
		if (requestCode == 1 && resultCode == RESULT_OK &&
				lastCapturedFile != null && lastCapturedFile.length() > 0) {
			// Generate and save photo
			GeoPhoto photo = new GeoPhoto(lastCapturedFile.getAbsolutePath(), GOOGLE_API_CLIENT);
			photo.save(DATABASE_HELPER);
			// Update library
			Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			Uri contentUri = Uri.fromFile(lastCapturedFile);
			mediaScanIntent.setData(contentUri);
			getApplicationContext().sendBroadcast(mediaScanIntent);
			// TODO refresh RecyclerView
		}else{
			Toast.makeText(this, "Camera didn't return an image!", Toast.LENGTH_SHORT).show();
			if(lastCapturedFile.exists()) lastCapturedFile.delete();
			lastCapturedFile = null;
		}
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Create an instance of GoogleAPIClient.
		if (GOOGLE_API_CLIENT == null) {
			GOOGLE_API_CLIENT = new GoogleApiClient.Builder(this)
					.addConnectionCallbacks(this)
					.addOnConnectionFailedListener(this)
					.addApi(LocationServices.API)
					.build();
		}

		// Create the database helper
		DATABASE_HELPER = new DatabaseHelper(this);
	}

	@Override
	protected void onStart() {
		// Required to ensure availability of location services
		GOOGLE_API_CLIENT.connect();
		super.onStart();
	}

	@Override
	protected void onStop() {
		// Prevents lingering connections
		GOOGLE_API_CLIENT.disconnect();
		super.onStop();
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		// TODO initialize map
	}

	@Override
	public void onConnected(@Nullable Bundle bundle) {
		Log.d(this.getClass().getCanonicalName(), "onConnected: Connected to Google API Client.");
	}

	@Override
	public void onConnectionSuspended(int i) {
		Log.d(this.getClass().getCanonicalName(),
				"onConnectionSuspended: Google API Client connection suspended [code " + i + "]");
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
		Log.d(this.getClass().getCanonicalName(),
				"onConnectionFailed: Google API Client connecction failed [result: "
						+ connectionResult.getErrorCode() + ": "
						+ connectionResult.getErrorMessage() + "]");
		Toast.makeText(this, "Google API Services unreachable!", Toast.LENGTH_LONG).show();
	}
}
