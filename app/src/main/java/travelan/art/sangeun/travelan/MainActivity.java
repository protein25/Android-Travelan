package travelan.art.sangeun.travelan;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import travelan.art.sangeun.travelan.services.BleScanService;
import travelan.art.sangeun.travelan.utils.BleScanner;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Toolbar toolbar;
    private TextView title;
    private FragmentManager fragmentManager;

    private BottomNavigationView navigator;

    private String FRAG_NEWSPEED = "newspeed_frag";
    private String FRAG_PLAN = "plan_frag";
    private String FRAG_INFO = "info_frag";
    private String FRAG_SETTINGS = "settings_frag";

    private final static int REQUEST_ENABLE_BT = 1;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initBleScanner();

        fragmentManager = getSupportFragmentManager();

        toolbar = findViewById(R.id.toolbar);
        title = toolbar.findViewById(R.id.title);

        navigator = findViewById(R.id.navigator);

        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        navigator.setOnNavigationItemSelectedListener(navListener);
        BottomNavigationViewHelper.disableShiftMode(navigator);

        readyFragement(FRAG_NEWSPEED);
        setToolbar(R.id.nav_newspeed);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int itemId = item.getItemId();
            setToolbar(itemId);
            switch (itemId) {
                case R.id.nav_newspeed:
                    readyFragement(FRAG_NEWSPEED);
                    return true;
                case R.id.nav_plan:
                    readyFragement(FRAG_PLAN);
                    return true;
                case R.id.nav_info:
                    readyFragement(FRAG_INFO);
                    return true;
                case R.id.nav_settings:
                    readyFragement(FRAG_SETTINGS);
                    return true;
            }


            return false;
        }
    };

    private void readyFragement(String fragmentTag) {
        Fragment mFragment = fragmentManager.findFragmentByTag(fragmentTag);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (null == mFragment) {
            if (fragmentTag.equals(FRAG_NEWSPEED)) {
                mFragment = new NewspeedFragment();
            }
            if (fragmentTag.equals(FRAG_PLAN)) {
                mFragment = new PlanFragment();
            }
            if (fragmentTag.equals(FRAG_INFO)) {
                mFragment = new InfoFragment();
            }
            if (fragmentTag.equals(FRAG_SETTINGS)) {
                mFragment = new SettingFragment();
            }
        }

        fragmentTransaction.replace(R.id.container, mFragment, fragmentTag).commit();
    }

    private void setToolbar(int itemId) {
        switch (itemId) {
            case R.id.nav_newspeed:
                title.setText(R.string.nav_newspeed);

                break;
            case R.id.nav_plan:
                title.setText(R.string.nav_plan);
                break;
            case R.id.nav_info:
                title.setText(R.string.nav_info);
                break;
            case R.id.nav_settings:
                title.setText(R.string.nav_settings);
                break;
        }
    }

    private void initBleScanner() {
        BleScanner.init(this);

        if (!BleScanner.isBluetoothEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent,REQUEST_ENABLE_BT);
        }

        checkPermission();

        Intent intent = new Intent(getApplicationContext(), BleScanService.class);

        if (isServiceRunning(BleScanService.class)) {
            stopService(intent);
        }

        startService(intent);
    }

    @TargetApi(23)
    private void checkPermission() {
        // Make sure we have access coarse location enabled, if not, prompt the user to enable it

        if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("This app needs location access");
            builder.setMessage("Please grant location access so this app can detect peripherals.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                }
            });
            builder.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    System.out.println("coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
        }
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
