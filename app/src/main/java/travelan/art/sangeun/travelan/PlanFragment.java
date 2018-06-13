package travelan.art.sangeun.travelan;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import travelan.art.sangeun.travelan.adapters.PlanAdapter;
import travelan.art.sangeun.travelan.models.Plan;
import travelan.art.sangeun.travelan.utils.ApiClient;

/**
 * Created by sangeun on 2018-05-12.
 */

public class PlanFragment extends Fragment {
    private static final String TAG = "PlanFragment";
    private CompactCalendarView calendarView;
    private RecyclerView planList;
    private Calendar calendar = Calendar.getInstance();
    private PlanAdapter adapter;
    private FloatingActionButton addBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(false);

        View v = inflater.inflate(R.layout.fragment_plan, container, false);

        planList = v.findViewById(R.id.planList);
        calendarView = v.findViewById(R.id.calendarView);
        addBtn = v.findViewById(R.id.addBtn);

        Date currentDate = calendarView.getFirstDayOfCurrentMonth();
        getMonthTravel(currentDate);

        calendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                doneEditPlans();
                getDatePlans(dateClicked);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                getMonthTravel(firstDayOfNewMonth);
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBtn.hide();
                editPlans();
                setHasOptionsMenu(true);
            }
        });

        planList.setLayoutManager(new LinearLayoutManager(getContext()));
        planList.setItemAnimator(new DefaultItemAnimator());

        adapter = new PlanAdapter(getFragmentManager(), new ArrayList<Plan>());
        planList.setAdapter(adapter);

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
                doneEditPlans();
                return true;
            default:
                return false;
        }
    }

    private void getMonthTravel(Date date) {
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        calendarView.removeAllEvents();

        ApiClient.getMonthTravel(year, month, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject travelObj = response.getJSONObject(i);
                        String title = travelObj.getString("title");
                        JSONArray dates = travelObj.getJSONArray("dates");
                        for(int j = 0; j < dates.length(); j++) {
                            calendarView.addEvent(new Event(Color.GRAY, dates.getLong(j), title));
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "FAIL TO PARSE MONTH TRAVEL", Toast.LENGTH_SHORT).show();
                }

                getDatePlans(new Date());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getContext(), "FAIL TO GET MONTH TRAVEL", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDatePlans(Date date) {
        adapter.items.clear();
        adapter.notifyDataSetChanged();

        List<Event> events = calendarView.getEvents(date);
        if (events.size() <= 0) {
            return;
        }

        String travelTitle = events.get(0).getData().toString();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(travelTitle);

        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        final List<Plan> plans = new ArrayList<>();

        ApiClient.getDatePlan(year, month, day, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    for(int i = 0; i < response.length(); i++) {
                        JSONObject object = response.getJSONObject(i);
                        String attributeType = object.getString("attributeType");
                        Plan plan = new Plan();
                        plan.id = object.getInt("id");
                        plan.order = object.getDouble("order");
                        plan.attributeType = attributeType;

                        if (attributeType.equals("transportation")) {
                            plan.time = object.getString("time");
                            plan.origin = object.getString("origin");
                            plan.destination = object.getString("destination");
                            plan.way = object.getString("way");
                            plan.route = object.getString("route");
                        } else {
                            plan.title = object.getString("title");
                            plan.address = object.getString("address");
                            plan.tel = object.getString("tel");
                        }

                        plans.add(plan);
                    }

                    adapter.items = plans;
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.i("FAIL TO PARSE", e.getMessage());
                    Toast.makeText(getContext(), "FAIL TO PARSE DATE PLANS", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    public void editPlans() {
        List<Plan> editablePlans = new ArrayList<>();
        List<Plan> plans = adapter.items;

        double firstOrder = plans.size() > 0 ? plans.get(0).order - 2 : 0;

        editablePlans.add(Plan.getAddInstance(firstOrder));

        for(int i = 0; i < plans.size(); i++) {
            Plan plan = plans.get(i);
            editablePlans.add(plans.get(i));
            double nextOrder = i + 1 < plans.size() ? (plan.order + plans.get(i + 1).order) / 2 : plan.order + 2;
            editablePlans.add(Plan.getAddInstance(nextOrder));
        }

        adapter.items = editablePlans;
        adapter.notifyDataSetChanged();
    }

    public void doneEditPlans() {
        setHasOptionsMenu(false);
        addBtn.show();

        List<Plan> plans = new ArrayList<>();

        for(Plan plan: adapter.items) {
            Log.i("plan", plan.toString());
            if (plan.id > 0) {
                plans.add(plan);
            }
        }

        adapter.items = plans;
        adapter.notifyDataSetChanged();
    }
}
