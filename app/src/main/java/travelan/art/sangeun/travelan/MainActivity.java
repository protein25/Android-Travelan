package travelan.art.sangeun.travelan;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

}
