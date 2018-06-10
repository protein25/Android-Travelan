package travelan.art.sangeun.travelan.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import travelan.art.sangeun.travelan.R;
import travelan.art.sangeun.travelan.adapters.LocationListAdapter;
import travelan.art.sangeun.travelan.utils.ApiClient;

/**
 * Created by sangeun on 2018-06-03.
 */

public class SelectLocationDialog extends DialogFragment {
    private static final String TAG = "SelectLocationDialog";
    private RecyclerView listLocation;
    private LocationListAdapter adapter;
    private static List<String> items;

    public static SelectLocationDialog newInstance(int memberId){
        SelectLocationDialog dialog = new SelectLocationDialog();
        items = new ArrayList<>();

        ApiClient.getLocations(memberId,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject object = response.getJSONObject(i);
                        String location = object.getString("location");

                        items.add(location);
                    }
                } catch (JSONException e) {
                    Log.e("FAIL TO PARSE DATA", e.getMessage());
                }
            }
        });

        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        adapter = new LocationListAdapter(items, getContext());

        View rootView = inflater.inflate(R.layout.select_location, null);

        listLocation = rootView.findViewById(R.id.listLocation);

        listLocation.setLayoutManager(new LinearLayoutManager(getContext()));
        listLocation.setItemAnimator(new DefaultItemAnimator());
        listLocation.setAdapter(adapter);

        builder.setView(rootView).setPositiveButton("완료", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });

        return builder.create();
    }

}
