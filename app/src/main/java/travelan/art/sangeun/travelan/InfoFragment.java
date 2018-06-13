package travelan.art.sangeun.travelan;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import travelan.art.sangeun.travelan.adapters.InformationListAdapter;
import travelan.art.sangeun.travelan.models.Information;
import travelan.art.sangeun.travelan.utils.ApiClient;

/**
 * Created by sangeun on 2018-05-12.
 */

public class InfoFragment extends Fragment {
    private static final String TAG = "InfoFragment";
    private RecyclerView recyclerView;
    private InformationListAdapter adapter;
    private List<Information> items;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_info, container, false);
        setHasOptionsMenu(true);

        getList();

        recyclerView = v.findViewById(R.id.infoList);
        adapter = new InformationListAdapter(items);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_information, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void getList() {
        items = new ArrayList<>();

        ApiClient.getInformations(0, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject object = (JSONObject) response.get(i);
                        Information item = new Information();
                        item.title = object.getString("title");
                        item.wrtDt = object.getString("wrtDt");
                        item.content = object.getString("content");
                        item.flagImage = object.getString("flagImage");
                        item.countryName = object.getString("countryEnName");

                        items.add(item);
                    }

                    adapter.items = items;
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e(TAG, "onSuccess: " + e.getMessage());
                    Toast.makeText(getContext(), "FAIL LOAD INFORMATION LIST", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e(TAG, "onFailure: ", throwable);
                Toast.makeText(getContext(), "FAIL TO PARSE DATA", Toast.LENGTH_LONG);

            }
        });
    }
}
