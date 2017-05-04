package net.miscfolder.geophoto;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;

import java.io.File;

import static android.view.View.OnClickListener;

/**
 * This class is a wrapper for four OnClickListeners:
 * - onImageClickListener: runs when the thunbnail image is clicked.
 * - onMainTextClickListener: runs when the photo information is clicked.
 * - onShareClickListener: runs when the share button is clicked.
 * - onDeleteClickListener: runs when the delete button is clicked.
 */
public class GeoPhotoClickListenerFactory{
	private final GeoPhoto photo;

	public GeoPhotoClickListenerFactory(GeoPhoto photo) {
		this.photo = photo;
	}

	public final OnClickListener onImageClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.parse("file://" + photo.getFileName()), "image/*");
			MainActivity.activity.startActivity(intent);
		}
	};

	public final OnClickListener onMainTextClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			TabHost tabHost = (TabHost)MainActivity.activity.findViewById(R.id.tabHost);
			tabHost.setCurrentTab(1);
			MainActivity.activity.scheduleCameraUpdate(CameraUpdateFactory.newLatLng(photo.getCoordinates()));
		}
	};

	public final OnClickListener onShareClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Uri fileName = Uri.parse("file://" + photo.getFileName());
			Intent shareIntent = new Intent();
			shareIntent.setAction(Intent.ACTION_SEND);
			shareIntent.putExtra(Intent.EXTRA_STREAM, fileName);
			shareIntent.setType("image/*");
			MainActivity.activity
					.startActivityForResult(Intent.createChooser(shareIntent, "Share image"),
							MainActivity.SHARE_INTENT);
		}
	};

	public final OnClickListener onDeleteClickListener = new OnClickListener() {
		@Override
		public void onClick(final View v) {
			AlertDialog.Builder confirmDelete = new AlertDialog.Builder(v.getContext());
			confirmDelete.setTitle("Delete file");
			confirmDelete.setMessage("Are you sure you want to delete?");
			confirmDelete.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					File file = new File(photo.getFileName());
					if(file.exists() && !file.delete()){
						Toast.makeText(v.getContext(), "File was not deleted (system error).", Toast.LENGTH_LONG).show();
					}
					if(!photo.delete(MainActivity.DATABASE_HELPER)){
						Toast.makeText(v.getContext(), "File was not removed from database (system error).", Toast.LENGTH_LONG).show();
					}
					MainActivity.activity.refresh();
				}
			});
			confirmDelete.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// Close
					dialog.cancel();
				}
			});
			confirmDelete.show();
		}
	};
}
