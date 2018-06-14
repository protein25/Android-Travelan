package travelan.art.sangeun.travelan.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BleScanner {
    private static BluetoothManager btManager;
    private static BluetoothAdapter btAdapter;
    private static BluetoothLeScanner btScanner;
    public static boolean isScanning = false;
    public static final Map<String, Long> connectedDevices = new HashMap<>();
    public static final Set<String> myDevices = new HashSet<>();

    public static final void init(Context context) {
        btManager = (BluetoothManager)context.getSystemService(Context.BLUETOOTH_SERVICE);
        btAdapter = btManager.getAdapter();
        btScanner = btAdapter.getBluetoothLeScanner();
    }

    public static final boolean isBluetoothEnabled() {
        return btAdapter != null && !btAdapter.isEnabled();
    }

    public static final void startScan(final BleScanListener scanListener) {
        if (isScanning) {
            scanListener.onError(ScanCallback.SCAN_FAILED_ALREADY_STARTED);
            return;
        }

        isScanning = true;

        final List<BluetoothDevice> scannedDevices = new ArrayList<>();

        Log.i("SCAN_STARTED", "BLE");
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                btScanner.startScan(new ScanCallback() {
                    @Override
                    public void onScanResult(int callbackType, ScanResult result) {
                        boolean isScanned = true;
                        BluetoothDevice scannedDevice = result.getDevice();

                        if (scannedDevice.getName() == null || !scannedDevice.getName().equals("Travelan")) {
                            return;
                        }

                        for(int i = 0; i < scannedDevices.size(); i++) {
                            if (scannedDevices.get(i).getAddress().equals(scannedDevice.getAddress())) {
                                isScanned = false;
                            }
                        }

                        if (isScanned) {
                            scannedDevices.add(scannedDevice);
                            scanListener.onScan(scannedDevice);
                        }
                    }

                    @Override
                    public void onScanFailed(int errorCode) {
                        // errorCodes: https://developer.android.com/reference/android/bluetooth/le/ScanCallback
                        scanListener.onError(errorCode);
                    }
                });
            }
        });
    }

    public static final void stopScan() {
        if (!isScanning) {
            return;
        }

        isScanning = false;

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                btScanner.stopScan(new ScanCallback() {
                    @Override
                    public void onScanResult(int callbackType, ScanResult result) {
                        super.onScanResult(callbackType, result);
                    }
                });
            }
        });
    }
}
