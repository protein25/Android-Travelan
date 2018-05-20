package travelan.art.sangeun.travelan.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import travelan.art.sangeun.travelan.R;
import travelan.art.sangeun.travelan.utils.BleScanListener;
import travelan.art.sangeun.travelan.utils.BleScanner;

public class BleScanService extends Service {
    private final int NOTIFICATION_ID = 0;
    private final int BLE_SCAN_PERIOD = 3 * 60 * 1000; // try scan every 3 minutes;
    private final int BLE_SCAN_DURATION = 60 * 1000; // scan for 1 minute;

    private Handler handler = new Handler();
    private Timer periodicTimer = new Timer();
    private TimerTask startTask;

    private Notification fixedNotification;
    private NotificationManager notificationManager;

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

        notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        periodicTimer.schedule(startTask, 0, BLE_SCAN_PERIOD);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        fixedNotification = new Notification.Builder(getApplicationContext())
                .setContentTitle("Travelan")
                .setContentText("Scanning BLE devices for location")
                .setSmallIcon(R.drawable.ic_settings_black_24dp)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .build();

        notificationManager.notify(NOTIFICATION_ID, fixedNotification);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BleScanner.stopScan();
        notificationManager.cancel(NOTIFICATION_ID);
    }

    public void reportDeviceLocation(BluetoothDevice device) {
        Log.i("BLE SCANNED", device.getAddress());
    }
}
