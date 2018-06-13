package travelan.art.sangeun.travelan.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import travelan.art.sangeun.travelan.R;
import travelan.art.sangeun.travelan.adapters.SelectTravelAdapter;
import travelan.art.sangeun.travelan.utils.ApiClient;

public class SelectTravelDialog extends DialogFragment {
    private RecyclerView nearByTravel;
    private EditText newTravel;
    private Button addTravel;
    private SelectTravelAdapter.OnTravelSelectListener onTravelSelectListener;

    private List<Object> nearBy;
    private SelectTravelAdapter adapter;

    public SelectTravelDialog() {
    }

    public void init(List<Object> nearBy, SelectTravelAdapter.OnTravelSelectListener onTravelSelectListener) {
        this.nearBy = nearBy;
        this.onTravelSelectListener = onTravelSelectListener;
        adapter = new SelectTravelAdapter(this.nearBy, this.onTravelSelectListener);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_select_travel, container, false);

        nearByTravel = view.findViewById(R.id.nearByTravel);
        newTravel = view.findViewById(R.id.newTravel);
        addTravel = view.findViewById(R.id.addTravel);

        nearByTravel.setLayoutManager(new LinearLayoutManager(getContext()));
        nearByTravel.setItemAnimator(new DefaultItemAnimator());
        nearByTravel.setAdapter(adapter);

        addTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiClient.addTravel(newTravel.getText().toString(), new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            int travelId = response.getInt("id");
                            onTravelSelectListener.onSelect(travelId);
                        } catch (JSONException e) {
                            Log.i("TRAVEL PARSE ERROR", e.getMessage());
                            Toast.makeText(getContext(), "FAIL TO PARSE TRAVEL", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Toast.makeText(getContext(), "FAIL TO ADD TRAVEL", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return view;
    }
}
