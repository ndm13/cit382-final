package net.miscfolder.geophoto;

import android.view.View;

import static android.view.View.*;

/**
 * This class is a wrapper for four OnClickListeners:
 * - onImageClickListener: runs when the thunbnail image is clicked.
 * - onMainTextClickListener: runs when the photo information is clicked.
 * - onShareClickListener: runs when the share button is clicked.
 * - onDeleteClickListener: runs when the delete button is clicked.
 */
public class GeoPhotoClickListenerFactory {
	private final GeoPhotoRecycleViewAdapter.ViewHolder viewHolder;

	public GeoPhotoClickListenerFactory(GeoPhotoRecycleViewAdapter.ViewHolder viewHolder){
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
		}
	};

	public final OnClickListener onShareClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO start image sharing intent
		}
	};

	public final OnClickListener onDeleteClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO prompt to confirm and if true, delete file
		}
	};
}
