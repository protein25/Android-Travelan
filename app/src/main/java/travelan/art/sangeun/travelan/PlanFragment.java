package travelan.art.sangeun.travelan;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import travelan.art.sangeun.travelan.dialog.MapLocationDialog;

/**
 * Created by sangeun on 2018-05-12.
 */

public class PlanFragment extends Fragment {
    private static final String TAG = "PlanFragment";
    private RecyclerView planList;
    private CalendarView calendarView;
    private MapLocationDialog mapLocationDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View v = inflater.inflate(R.layout.fragment_plan, container, false);

        planList = v.findViewById(R.id.planList);
        calendarView = v.findViewById(R.id.calendarView);

        mapLocationDialog = new MapLocationDialog();

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_plan, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addPlan:
                mapLocationDialog.show(getFragmentManager(), "dialog");
                return true;
            default:
                return false;
        }
    }
}
