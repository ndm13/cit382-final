package net.miscfolder.geophoto;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment object details for res/layout/fragment_item.xml
 */
public class ItemFragment extends Fragment {

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ItemFragment() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_item_list, container, false);

		// Set the adapter
		if (view instanceof RecyclerView) {
			Context context = view.getContext();
			RecyclerView recyclerView = (RecyclerView) view;
			recyclerView.setLayoutManager(new LinearLayoutManager(context));
			recyclerView.setAdapter(new GeoPhotoRecyclerViewAdapter(
					GeoPhoto.load(new DatabaseHelper(context))));
		}
		return view;
	}
}
