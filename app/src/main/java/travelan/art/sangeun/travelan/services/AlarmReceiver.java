package travelan.art.sangeun.travelan.services;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import travelan.art.sangeun.travelan.utils.BleScanListener;

public class AlarmReceiver extends BroadcastReceiver {
    private BleScanListener bleScanListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        bleScanListener = new BleScanListener() {
            @Override
            public void onScan(BluetoothDevice device) {
                reportDeviceLocation(device);
            }

            @Override
            public void onError(int errorCode) {

            }
        };
    }

    public void reportDeviceLocation(BluetoothDevice device) {
        Log.i("BLE SCANNED", device.getAddress());
    }
}
