package travelan.art.sangeun.travelan.services;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;
import travelan.art.sangeun.travelan.R;
import travelan.art.sangeun.travelan.utils.ApiClient;
import travelan.art.sangeun.travelan.utils.BleScanListener;
import travelan.art.sangeun.travelan.utils.BleScanner;

public class BleScanService extends Service {
    private final int NOTIFICATION_ID = 0;
    private final int BLE_SCAN_PERIOD = 60 * 1000; // try scan every 1 minutes;
    private final int BLE_SCAN_DURATION = 30 * 1000; // scan for 30 seconds;

    private Handler handler = new Handler();
    private Timer periodicTimer = new Timer();
    private Timer disconnectChecker = new Timer();
    private TimerTask startTask, checker;

    private Notification fixedNotification;
    private NotificationManager notificationManager;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    public void onCreate() {
        super.onCreate();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        checker = new TimerTask() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();

                for(String key: BleScanner.connectedDevices.keySet()) {
                    if (currentTime - BleScanner.connectedDevices.get(key) > BLE_SCAN_PERIOD) {
                        BleScanner.connectedDevices.remove(key);
                    }
                }
            }
        };

        startTask = new TimerTask() {
            @Override
            public void run() {
                BleScanner.startScan(new BleScanListener() {
                    @Override
                    public void onScan(BluetoothDevice device) {
                        String mac = device.getAddress();
                        BleScanner.connectedDevices.put(mac, System.currentTimeMillis());
                        reportDeviceLocation(device);
                    }

                    @Override
                    public void onError(int errorCode) {
                        notificationManager.cancel(NOTIFICATION_ID);
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

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        periodicTimer.schedule(startTask, 0, BLE_SCAN_PERIOD);
        disconnectChecker.schedule(checker, BLE_SCAN_DURATION, BLE_SCAN_PERIOD);

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

    public void reportDeviceLocation(final BluetoothDevice device) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location == null) {
                    Log.i("LOCATION NULL", location.toString());
                    return;
                }

                double lat = location.getLatitude();
                double lng = location.getLongitude();
                Log.i("LOCATION", location.toString() + " " + lat + " " + lng);

                ApiClient.reportDevice(device.getAddress(), lat, lng, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }
                });
            }
        });

        Log.i("BLE SCANNED", device.getAddress());
    }
}
