package travelan.art.sangeun.travelan;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import travelan.art.sangeun.travelan.adapters.MyDeviceListAdapter;
import travelan.art.sangeun.travelan.dialog.SelectDeviceDialog;
import travelan.art.sangeun.travelan.models.Device;
import travelan.art.sangeun.travelan.utils.ApiClient;
import travelan.art.sangeun.travelan.utils.BaseFragment;
import travelan.art.sangeun.travelan.utils.BleScanner;

/**
 * Created by sangeun on 2018-05-12.
 */

public class SettingFragment extends BaseFragment {
    private static final String TAG = "SettingFragment";
    private RecyclerView recyclerView;
    private MyDeviceListAdapter adapter;

    private ImageView thumbnail;
    private TextView email;
    private TextView name;
    private TextView emergency;
    private FloatingActionButton addBtn;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setting, container, false);
        recyclerView = v.findViewById(R.id.settingList);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new MyDeviceListAdapter(this);
        recyclerView.setAdapter(adapter);

        thumbnail = v.findViewById(R.id.thumbnail);
        email = v.findViewById(R.id.email);
        name = v.findViewById(R.id.name);
        emergency = v.findViewById(R.id.emergency);
        addBtn = v.findViewById(R.id.addBtn);

        ApiClient.getUserInfo(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Picasso.get().load(response.getString("thumb")).into(thumbnail);
                    email.setText(response.getString("userId"));
                    name.setText(response.getString("name"));
                    emergency.setText(response.getString("emergency"));
                }catch(JSONException e){
                    Log.e("FAIL TO PARSE DATA", e.getMessage());
                }
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectDeviceDialog selectDeviceDialog = new SelectDeviceDialog();
                selectDeviceDialog.setOnDismissListner(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        BleScanner.stopScan();
                        getMyDevices();
                    }
                });
                selectDeviceDialog.show(getFragmentManager(), "selectDeviceDialog");
            }
        });

        getMyDevices();

        return v;
    }

    @Override
    public void onFocus() {
        setHasOptionsMenu(false);
    }

    private void getMyDevices() {
        BleScanner.myDevices.clear();
        ApiClient.getMyDevices(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                adapter.items.clear();
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject object = response.getJSONObject(i);
                        Device device = new Device();
                        device.id = object.getInt("id");
                        device.imageUrl = object.isNull("imageUrl") ? null : object.getString("imageUrl");
                        device.mac = object.getString("mac");
                        device.name = object.getString("name");
                        device.isReported = object.getBoolean("isReported");

                        if (!object.isNull("lastLocation")) {
                            JSONObject location = object.getJSONObject("lastLocation");
                            JSONArray coordinates = location.getJSONArray("coordinates");
                            device.lastLocation = new LatLng(coordinates.getDouble(1), coordinates.getDouble(0));
                        }

                        BleScanner.myDevices.add(device.mac);
                        adapter.items.add(device);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "FAIL TO PARSE DEVICES", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getContext(), "FAIL TO LOAD DEVICES", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
