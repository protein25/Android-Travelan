package travelan.art.sangeun.travelan;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import travelan.art.sangeun.travelan.utils.ApiClient;

/**
 * Created by sangeun on 2018-05-12.
 */

public class SettingFragment extends Fragment {
    private static final String TAG = "SettingFragment";
    private RecyclerView recyclerView;

    private ImageView thumbnail;
    private TextView email;
    private TextView name;
    private TextView emergency;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setting, container, false);
        recyclerView = v.findViewById(R.id.settingList);

        thumbnail = v.findViewById(R.id.thumbnail);
        email = v.findViewById(R.id.email);
        name = v.findViewById(R.id.name);
        emergency = v.findViewById(R.id.emergency);

        ApiClient.getUserInfo(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Picasso.get().load(response.getString("thumbnail")).into(thumbnail);
                    email.setText(response.getString("userId"));
                    name.setText(response.getString("name"));
                    emergency.setText(response.getString("emergency"));
                }catch(JSONException e){
                    Log.e("FAIL TO PARSE DATA", e.getMessage());
                }

            }
        });

        return v;
    }
}
