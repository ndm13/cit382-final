package net.miscfolder.geophoto;

import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;

/**
 * Responsible for generating and storing the elements of the list.
 */
public class GeoPhotoRecyclerViewAdapter extends RecyclerView.Adapter<GeoPhotoRecyclerViewAdapter.ViewHolder> {

	public final List<GeoPhoto> photoList;

	public GeoPhotoRecyclerViewAdapter(List<GeoPhoto> items) {
		photoList = items;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.fragment_item, parent, false);
		return new ViewHolder(view);
	}

	// Clean all elements of the recycler
	public void clear() {
		photoList.clear();
		notifyDataSetChanged();
	}

	// Add items
	public void addAll(List<GeoPhoto> items) {
		photoList.addAll(items);
		notifyDataSetChanged();
	}


	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {
		holder.photo = photoList.get(position);
		holder.imageContainer.setImageBitmap(BitmapFactory.decodeFile(holder.photo.getFileName()));
		holder.coordinates.setText(holder.photo.getCoordinatesAsText());
		holder.infoText.setText(holder.photo.getInfoText());
	}

	@Override
	public int getItemCount() {
		return photoList.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		public final View view;
		public final ImageView imageContainer;
		public final LinearLayout mainText;
		public final TextView coordinates, infoText;
		public final ImageButton share, delete;
		public GeoPhoto photo;

		public ViewHolder(View view) {
			super(view);
			this.view = view;
			imageContainer = (ImageView) view.findViewById(R.id.imageContainer);
			mainText = (LinearLayout) view.findViewById(R.id.mainText);
			coordinates = (TextView) view.findViewById(R.id.coordinates);
			infoText = (TextView) view.findViewById(R.id.infoText);
			share = (ImageButton) view.findViewById(R.id.shareButton);
			delete = (ImageButton) view.findViewById(R.id.deleteButton);

			GeoPhotoClickListenerFactory factory = new GeoPhotoClickListenerFactory(this);
			imageContainer.setOnClickListener(factory.onImageClickListener);
			mainText.setOnClickListener(factory.onMainTextClickListener);
			share.setOnClickListener(factory.onShareClickListener);
			delete.setOnClickListener(factory.onDeleteClickListener);
		}

		@Override
		public String toString() {
			return super.toString() + " '" + coordinates.getText() + "'";
		}
	}
}
