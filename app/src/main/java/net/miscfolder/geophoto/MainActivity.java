package net.miscfolder.geophoto;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
		GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
	public static GoogleApiClient GOOGLE_API_CLIENT;

	/**
	 * onClick handler for R.id.floatingActionButton
	 *
	 * Should launch a photo capture intent, generate a GeoPhoto, and save the photo to the gallery
	 * and the database.  Then the RecyclerView should be refreshed.
	 *
	 * @param view     FloatingActionButton
	 */
	public void capturePhoto(View view) {
		// TODO
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
