package travelan.art.sangeun.travelan.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import travelan.art.sangeun.travelan.R;
import travelan.art.sangeun.travelan.adapters.LocationListAdapter;

/**
 * Created by sangeun on 2018-06-03.
 */

public class SelectLocationDialog extends DialogFragment {
    private RecyclerView listLocation;
    private LocationListAdapter adapter;
    private List<String> items;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.select_location, container);
        listLocation = rootView.findViewById(R.id.listLocation);
        listLocation.setLayoutManager(new LinearLayoutManager(getContext()));
        listLocation.setItemAnimator(new DefaultItemAnimator());

        adapter = new LocationListAdapter(items, getContext());

        listLocation.setAdapter(adapter);

        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
