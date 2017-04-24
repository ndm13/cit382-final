package net.miscfolder.geophoto;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;

import static android.view.View.*;

/**
 * This class is a wrapper for four OnClickListeners:
 * - onImageClickListener: runs when the thunbnail image is clicked.
 * - onMainTextClickListener: runs when the photo information is clicked.
 * - onShareClickListener: runs when the share button is clicked.
 * - onDeleteClickListener: runs when the delete button is clicked.
 */
public class GeoPhotoClickListenerFactory extends FragmentActivity {
	private final GeoPhotoRecycleViewAdapter.ViewHolder viewHolder;

	public GeoPhotoClickListenerFactory(GeoPhotoRecycleViewAdapter.ViewHolder viewHolder) {
		this.viewHolder = viewHolder;
	}

	public final OnClickListener onImageClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO open viewHolder.photo for viewing
		}
	};

	public final OnClickListener onMainTextClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO switch to map and focus on location

			Fragment switchMap = new Fragment();
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.map, switchMap)
					.commit();

		}



		public final OnClickListener onShareClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO complete image sharing intent
			/* Not sure how to find the image to be sent.
			Intent shareIntent = new Intent();
			shareIntent.setAction(Intent.ACTION_SEND);
			shareIntent.putExtra(Intent.EXTRA_STREAM,);
			shareIntent.setType("image/*);
			startActivity(Intent.createChooser(shareIntent, "Share image"));
			*/
			}
		};

		public final OnClickListener onDeleteClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO fix up prompt to confirm and delete file
			/*
				AlertDialog.Builder confirmDelete = new AlertDialog.Builder();
				confirmDelete.setTitle("Delete file");
				confirmDelete.setMessage("Are you sure you want to delete?");
				confirmDelete.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// TODO delete file
					}
				});
				confirmDelete.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						//Close
						dialog.cancel();
					}
				});
				confirmDelete.show();
			*/
			}
		};
	};
}
