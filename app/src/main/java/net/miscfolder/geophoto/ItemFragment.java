package net.miscfolder.geophoto;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment object details for res/layout/fragment_item.xml
 */
public class ItemFragment extends Fragment {
	// TODO See doc note on ItemFragment::newInstance().  Should be able to safely delete these.
	private static final String ARG_COLUMN_COUNT = "column-count";
	private int mColumnCount = 1;

	private final DatabaseHelper helper;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ItemFragment() {
		helper = new DatabaseHelper(getContext());
	}

	/**
	 * Autogenerated instance creator.  Marked as unused by compiler.
	 * TODO check for safe delete in final
	 * @param columnCount   Should always be 1 for list IIRC
	 * @return              new ItemFragment with args bundle containing columnCount
	 */
	public static ItemFragment newInstance(int columnCount) {
		ItemFragment fragment = new ItemFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_COLUMN_COUNT, columnCount);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments() != null) {
			mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_item_list, container, false);

		// Set the adapter
		if (view instanceof RecyclerView) {
			Context context = view.getContext();
			RecyclerView recyclerView = (RecyclerView) view;
			if (mColumnCount <= 1) {
				recyclerView.setLayoutManager(new LinearLayoutManager(context));
			} else {
				recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
			}
			recyclerView.setAdapter(new GeoPhotoRecycleViewAdapter(GeoPhoto.load(helper)));
		}
		return view;
	}
}