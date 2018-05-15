package travelan.art.sangeun.travelan;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Toolbar toolbar;
    private TextView title;
    private Menu menu;
    private int menuSource;

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
        toolbar = findViewById(R.id.toolbar);
        title = toolbar.findViewById(R.id.title);

        navigator = findViewById(R.id.navigator);

        menuSource = R.menu.toolbar_newspeed;


        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        navigator.setOnNavigationItemSelectedListener(navListener);
        BottomNavigationViewHelper.disableShiftMode(navigator);

        readyFragement(FRAG_NEWSPEED);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(menuSource, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_newspeed:
                    readyFragement(FRAG_NEWSPEED);
                    menuSource = R.menu.toolbar_newspeed;
                    return true;
                case R.id.nav_plan:
                    readyFragement(FRAG_PLAN);
                    menuSource = R.menu.toolbar_plan;
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

    private void readyFragement(String fragment) {
        Fragment mFragment = getSupportFragmentManager().findFragmentByTag(fragment);
        if (null == mFragment) {
            if (fragment.equals(FRAG_NEWSPEED)) {
                title.setText(R.string.nav_newspeed);
                mFragment = new NewspeedFragment();
            }
            if (fragment.equals(FRAG_PLAN)) {
                title.setText(R.string.nav_plan);
                mFragment = new NewspeedFragment();

            }
            if (fragment.equals(FRAG_INFO)) {
                title.setText(R.string.nav_info);
                mFragment = new NewspeedFragment();
            }
            if (fragment.equals(FRAG_SETTINGS)) {
                title.setText(R.string.nav_settings);
                mFragment = new NewspeedFragment();
            }
        } else {
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.container, mFragment, fragment).commit();
    }

    private void setToolbar(int sort) {
        switch (sort) {
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
