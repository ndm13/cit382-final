package net.miscfolder.geophoto;

import android.app.AlertDialog;
import android.app.Application;
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

import java.io.File;

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
	};

	public final OnClickListener onShareClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
		// TODO test sharing intent
		Uri fileName = Uri.parse(viewHolder.photo.getFileName());

		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.putExtra(Intent.EXTRA_STREAM, fileName);
		shareIntent.setType("image/*");
		startActivityForResult(Intent.createChooser(shareIntent, "Share image"), MainActivity.SHARE_INTENT);
		}
	};

	public final OnClickListener onDeleteClickListener = new OnClickListener() {
		@Override
		public void onClick(final View v) {
			// TODO test delete file
			AlertDialog.Builder confirmDelete = new AlertDialog.Builder(v.getContext());
			confirmDelete.setTitle("Delete file");
			confirmDelete.setMessage("Are you sure you want to delete?");
			confirmDelete.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					File file = new File(viewHolder.photo.getFileName());
					if(file.exists() && !file.delete()){
						Toast.makeText(v.getContext(), "File was not deleted (system error).", Toast.LENGTH_LONG).show();
					}
					if(!viewHolder.photo.delete(MainActivity.DATABASE_HELPER)){
						Toast.makeText(v.getContext(), "File was not removed from database (system error).", Toast.LENGTH_LONG).show();
					}
					// TODO refresh RecyclerView
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
