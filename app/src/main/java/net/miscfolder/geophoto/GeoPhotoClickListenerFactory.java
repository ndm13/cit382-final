package net.miscfolder.geophoto;

import android.view.View;

/**
 * Created by Nathaniel on 4/20/2017.
 */

public class GeoPhotoClickListenerFactory {
	private final GeoPhotoRecycleViewAdapter.ViewHolder viewHolder;

	public GeoPhotoClickListenerFactory(GeoPhotoRecycleViewAdapter.ViewHolder viewHolder){
		this.viewHolder = viewHolder;
	}

	public View.OnClickListener onImageClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO open viewHolder.photo for viewing
		}
	};

	public View.OnClickListener onMainTextClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO switch to map and focus on location
		}
	};

	public View.OnClickListener onShareClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO start image sharing intent
		}
	};

	public View.OnClickListener onDeleteClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO prompt to confirm and if true, delete file
		}
	};
}
