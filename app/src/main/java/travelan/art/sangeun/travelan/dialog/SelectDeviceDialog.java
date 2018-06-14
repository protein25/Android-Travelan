package travelan.art.sangeun.travelan.dialog;

import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import travelan.art.sangeun.travelan.R;
import travelan.art.sangeun.travelan.adapters.DeviceListAdapter;
import travelan.art.sangeun.travelan.utils.ApiClient;
import travelan.art.sangeun.travelan.utils.BleScanListener;
import travelan.art.sangeun.travelan.utils.BleScanner;

public class SelectDeviceDialog extends DialogFragment {
    private RecyclerView deviceList;
    private DeviceListAdapter adapter;
    private DialogInterface.OnDismissListener onDismissListener;
    private ProgressBar loader;

    public SelectDeviceDialog() {

    }

    public void setOnDismissListner(DialogInterface.OnDismissListener onDismissListner) {
        this.onDismissListener = onDismissListner;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_devices, container, false);
        deviceList = view.findViewById(R.id.deviceList);
        loader = view.findViewById(R.id.loader);

        adapter = new DeviceListAdapter(new DeviceListAdapter.OnDeviceSelectListener() {
            @Override
            public void onSelect(String device) {
                addDevice(device);
            }
        });

        deviceList.setLayoutManager(new LinearLayoutManager(getContext()));
        deviceList.setItemAnimator(new DefaultItemAnimator());
        deviceList.setAdapter(adapter);

        starScan();

        return view;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        onDismissListener.onDismiss(dialog);
    }

    private void starScan() {
        if (BleScanner.isScanning) {
            BleScanner.stopScan();
        }

        BleScanner.startScan(new BleScanListener() {
            @Override
            public void onScan(BluetoothDevice device) {
                BleScanner.connectedDevices.put(device.getAddress(), System.currentTimeMillis());
                loader.setVisibility(View.GONE);
                adapter.items.add(device.getAddress());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int errorCode) {

            }
        });
    }

    private void addDevice(String mac) {
        ApiClient.addDevice(mac, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    Toast.makeText(getContext(), errorResponse.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "FAIL TO ADD DEVICE", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
