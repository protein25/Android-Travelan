package travelan.art.sangeun.travelan.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import travelan.art.sangeun.travelan.R;
import travelan.art.sangeun.travelan.adapters.MapLocationListAdapter;
import travelan.art.sangeun.travelan.models.MapLocation;
import travelan.art.sangeun.travelan.utils.ApiClient;
import travelan.art.sangeun.travelan.utils.OnMapSelectListener;

public class MapLocationDialog extends DialogFragment implements OnMapReadyCallback {
    private static View rootView;
    private SupportMapFragment mapFragment;
    private GoogleMap gMap;
    private OnMapSelectListener onMapSelectListener;
    private EditText keywordEdit;
    private ImageButton searchBtn;
    private RecyclerView resultList;
    private MapLocationListAdapter adapter;
    private InputMethodManager imm;

    private LatLng latlng;

    public MapLocationDialog() {

    }

    public void setPoint(LatLng latlng) {
        this.latlng = latlng;
    }

    public void setOnMapSelectedListener(OnMapSelectListener onMapSelectListener) {
        this.onMapSelectListener = onMapSelectListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) parent.removeView(rootView);
        }
        try {
            rootView = inflater.inflate(R.layout.dialog_map_location, container, false);
        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }

        keywordEdit = rootView.findViewById(R.id.keywordEdit);
        resultList = rootView.findViewById(R.id.resultList);
        searchBtn = rootView.findViewById(R.id.searchBtn);

        mapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                if (gMap == null) {
                    Toast.makeText(getContext(), "MAP NOT LOADED", Toast.LENGTH_SHORT).show();
                    return;
                }

                findLocation(keywordEdit.getText().toString());
            }
        });

        adapter = new MapLocationListAdapter(new ArrayList<MapLocation>(), new OnMapSelectListener() {
            @Override
            public void onSelect(MapLocation mapLocation) {
                gMap.moveCamera(CameraUpdateFactory.newLatLng(mapLocation.latlng));
                gMap.animateCamera(CameraUpdateFactory.zoomTo(15f));
            }
        });

        resultList.setLayoutManager(new LinearLayoutManager(getContext()));
        resultList.setItemAnimator(new DefaultItemAnimator());
        resultList.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (gMap != null && latlng != null) {
            gMap.clear();
            gMap.addMarker(new MarkerOptions().position(latlng));
            gMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
            gMap.animateCamera(CameraUpdateFactory.zoomTo(15f));
        }

        if (latlng != null) {
            keywordEdit.setVisibility(View.GONE);
            searchBtn.setVisibility(View.GONE);
            resultList.setVisibility(View.GONE);
        } else {
            keywordEdit.setVisibility(View.VISIBLE);
            searchBtn.setVisibility(View.VISIBLE);
            resultList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (onMapSelectListener != null) {
                    MapLocation mapLocation = (MapLocation)marker.getTag();
                    onMapSelectListener.onSelect(mapLocation);
                    return true;
                }

                return false;
            }
        });

        if (latlng != null) {
            gMap.addMarker(new MarkerOptions().position(latlng));
            gMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
            gMap.animateCamera(CameraUpdateFactory.zoomTo(15f));
        }
    }

    private void findLocation(String keyword) {
        final List<MapLocation> items = new ArrayList<>();

        ApiClient.findLocation(keyword, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject object = response.getJSONObject(i);
                        MapLocation mapLocation = new MapLocation();

                        mapLocation.poi = object.getString("poi");
                        mapLocation.address = object.getString("address");

                        JSONObject coordinates = object.getJSONObject("coordinates");
                        mapLocation.latlng = new LatLng(coordinates.getDouble("lat"), coordinates.getDouble("lng"));

                        items.add(mapLocation);
                    }

                    setMarkers(items);
                    adapter.items = items;
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "FAIL TO PARSE RESULT", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getContext(), "FAIL TO LOAD RESULT", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setMarkers(List<MapLocation> mapLocations) {
        if (mapLocations.size() <= 0) return;

        gMap.moveCamera(CameraUpdateFactory.newLatLng(mapLocations.get(0).latlng));
        gMap.animateCamera(CameraUpdateFactory.zoomTo(15f));

        for(MapLocation mapLocation: mapLocations) {
            gMap.addMarker(new MarkerOptions().position(mapLocation.latlng)).setTag(mapLocation);
        }
    }
}
