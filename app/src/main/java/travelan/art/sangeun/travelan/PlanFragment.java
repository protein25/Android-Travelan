package travelan.art.sangeun.travelan;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import travelan.art.sangeun.travelan.adapters.BottomSheetListener;
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
    private ConstraintLayout selectAttributeType;
    private LinearLayout attributeTransport;
    private LinearLayout attributeAttraction;
    private LinearLayout attributeAccommodate;
    private FrameLayout selectBackground;
    public Date selectedDate = new Date();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(false);

        View v = inflater.inflate(R.layout.fragment_plan, container, false);

        planList = v.findViewById(R.id.planList);
        calendarView = v.findViewById(R.id.calendarView);
        addBtn = v.findViewById(R.id.addBtn);
        selectAttributeType = v.findViewById(R.id.selectAttributeType);
        attributeTransport = v.findViewById(R.id.transport);
        attributeAccommodate = v.findViewById(R.id.accommodate);
        attributeAttraction = v.findViewById(R.id.attraction);
        selectBackground = v.findViewById(R.id.selectBackground);

        getMonthTravel(selectedDate);

        calendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                selectedDate = dateClicked;
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

        selectBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAttributeType.setVisibility(View.GONE);
            }
        });

        planList.setLayoutManager(new LinearLayoutManager(getContext()));
        planList.setItemAnimator(new DefaultItemAnimator());

        adapter = new PlanAdapter(this, new ArrayList<Plan>());
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

    public void getMonthTravel(final Date date) {
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
                        int id = travelObj.getInt("id");
                        String title = travelObj.getString("title");
                        JSONArray dates = travelObj.getJSONArray("dates");
                        for(int j = 0; j < dates.length(); j++) {
                            Map<String, String> map = new HashMap<>();
                            map.put("id", ""+id);
                            map.put("title", title);

                            calendarView.addEvent(new Event(Color.GRAY, dates.getLong(j), map));
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "FAIL TO PARSE MONTH TRAVEL", Toast.LENGTH_SHORT).show();
                }

                getDatePlans(date);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getContext(), "FAIL TO GET MONTH TRAVEL", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getDatePlans(Date date) {
        adapter.items.clear();
        adapter.notifyDataSetChanged();

        List<Event> events = calendarView.getEvents(date);
        if (events.size() <= 0) {
            return;
        }

        Map<String, String> data = (HashMap) events.get(0).getData();
        final int travelId = Integer.parseInt(data.get("id"));
        String travelTitle = data.get("title");
        if (getActivity() != null) {
            ((MainActivity) getActivity()).title.setText("#" + travelTitle);
        }

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
                        plan.travelId = travelId;
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
                            if (!object.isNull("coordinates")) {
                                JSONObject point = object.getJSONObject("coordinates");
                                JSONArray coordinates = point.getJSONArray("coordinates");
                                plan.coordinates = new LatLng(coordinates.getDouble(1), coordinates.getDouble(0));
                            }

                            plan.title = object.getString("title");
                            plan.address = object.getString("address");
                        }

                        plans.add(plan);
                    }

                    adapter.items = plans;
                    adapter.notifyDataSetChanged();

                    if (!addBtn.isShown()) editPlans();
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

        double firstOrder = 0;
        int travelId = -1;

        if (plans.size() > 0) {
            firstOrder = plans.get(0).order - 2;
            travelId = plans.get(0).travelId;
        }

        editablePlans.add(Plan.getAddInstance(travelId, firstOrder));

        for(int i = 0; i < plans.size(); i++) {
            Plan plan = plans.get(i);
            editablePlans.add(plans.get(i));
            double nextOrder = i + 1 < plans.size() ? (plan.order + plans.get(i + 1).order) / 2 : plan.order + 2;
            editablePlans.add(Plan.getAddInstance(travelId, nextOrder));
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

    public final void openAttributeSelect(final BottomSheetListener bottomSheetListener) {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAttributeType.setVisibility(View.GONE);
                bottomSheetListener.onSelect(view.getId());
            }
        };

        attributeTransport.setOnClickListener(clickListener);
        attributeAccommodate.setOnClickListener(clickListener);
        attributeAttraction.setOnClickListener(clickListener);

        selectAttributeType.setVisibility(View.VISIBLE);
    }

    public List<Object> getNearByTravel() {
        List<Object> nearBy = new ArrayList<>();

        calendar.setTime(selectedDate);

        calendar.add(Calendar.DATE, -1);
        Date yesterday = new Date(calendar.getTimeInMillis());
        List<Event> yesterdayEvents = calendarView.getEvents(yesterday);

        if (yesterdayEvents.size() > 0) {
            nearBy.add(yesterdayEvents.get(0).getData());
        }

        calendar.add(Calendar.DATE, 2);
        Date tomorrow = new Date(calendar.getTimeInMillis());
        List<Event> tomorrowEvents = calendarView.getEvents(tomorrow);

        if (tomorrowEvents.size() > 0) {
            nearBy.add(tomorrowEvents.get(0).getData());
        }

        return nearBy;
    }
}
