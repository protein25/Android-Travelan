package travelan.art.sangeun.travelan.utils;

import android.bluetooth.BluetoothDevice;

import java.util.List;

public interface BleScanListener {
    void onScan(BluetoothDevice device);
    void onError(int errorCode);
}
