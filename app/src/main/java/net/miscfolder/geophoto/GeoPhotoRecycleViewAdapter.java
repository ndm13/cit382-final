package net.miscfolder.geophoto;

import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Responsible for generating and storing the elements of the list.
 */
public class GeoPhotoRecycleViewAdapter extends RecyclerView.Adapter<GeoPhotoRecycleViewAdapter.ViewHolder> {

	private final List<GeoPhoto> photoList;

	public GeoPhotoRecycleViewAdapter(List<GeoPhoto> items) {
		photoList = items;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.fragment_item, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {
		holder.photo = photoList.get(position);
		holder.imageContainer.setImageBitmap(BitmapFactory.decodeFile(holder.photo.getFileName()));
		holder.fileName.setText(holder.photo.getFileName());
		holder.fileInfo.setText(holder.photo.getInfoString());
	}

	@Override
	public int getItemCount() {
		return photoList.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		public final View view;
		public final ImageView imageContainer;
		public final TextView fileName;
		public final TextView fileInfo;
		public GeoPhoto photo;

		public ViewHolder(View view) {
			super(view);
			this.view = view;
			imageContainer = (ImageView) view.findViewById(R.id.imageContainer);
			fileName = (TextView) view.findViewById(R.id.fileName);
			fileInfo = (TextView) view.findViewById(R.id.fileInfo);
		}

		@Override
		public String toString() {
			return super.toString() + " '" + fileName.getText() + "'";
		}
	}
}
