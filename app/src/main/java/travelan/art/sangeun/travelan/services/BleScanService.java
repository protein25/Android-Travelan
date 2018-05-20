package travelan.art.sangeun.travelan.services;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import travelan.art.sangeun.travelan.utils.BleScanListener;
import travelan.art.sangeun.travelan.utils.BleScanner;

public class BleScanService extends Service {
    private final int BLE_SCAN_PERIOD = 3 * 60 * 1000; // try scan every 3 minutes;
    private final int BLE_SCAN_DURATION = 60 * 1000; // scan for 1 minute;

    private Handler handler = new Handler();
    private Timer periodicTimer = new Timer();
    private TimerTask startTask;

    private BleScanListener bleScanListener;

    @Override
    public void onCreate() {
        super.onCreate();

        startTask = new TimerTask() {
            @Override
            public void run() {
                BleScanner.startScan(new BleScanListener() {
                    @Override
                    public void onScan(BluetoothDevice device) {
                        reportDeviceLocation(device);
                    }

                    @Override
                    public void onError(int errorCode) {

                    }
                });

                Runnable stop = new Runnable() {
                    @Override
                    public void run() {
                        BleScanner.stopScan();
                    }
                };

                handler.postDelayed(stop, BLE_SCAN_DURATION);
            }
        };
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        periodicTimer.schedule(startTask, 0, BLE_SCAN_PERIOD);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BleScanner.stopScan();
    }

    public void reportDeviceLocation(BluetoothDevice device) {
        Log.i("BLE SCANNED", device.getAddress());
    }
}
