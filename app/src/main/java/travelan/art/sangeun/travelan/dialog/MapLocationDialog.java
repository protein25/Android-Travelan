package travelan.art.sangeun.travelan.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
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
    public static final int SEARCH_PLACE = 0;
    public static final int SEARCH_ORIGIN = 1;
    public static final int SEARCH_DESTINATION = 2;

    private static View rootView;
    private SupportMapFragment mapFragment;
    private GoogleMap gMap;
    private OnMapSelectListener onMapSelectListener;
    private OnRouteSelect onRouteSelect;
    private LinearLayout destinationWrapper;
    private EditText keywordEdit, destinationEdit;
    private TextView route;
    private ImageButton searchBtn, destinationSearch;
    private RecyclerView resultList;
    private MapLocationListAdapter adapter;
    private InputMethodManager imm;

    private LatLng latlng;
    private LatLng destinationLatlng;
    private List<LatLng> polyline;

    private int mode = SEARCH_PLACE;

    public MapLocationDialog() {

    }

    public void setPoint(LatLng latlng) {
        this.latlng = latlng;
    }

    public void setPolyline(List<LatLng> polyline) {
        this.polyline = polyline;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setOnMapSelectedListener(OnMapSelectListener onMapSelectListener) {
        this.onMapSelectListener = onMapSelectListener;
    }

    public void setOnRouteSelect(OnRouteSelect onRouteSelect) {
        this.onRouteSelect = onRouteSelect;
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
        destinationEdit = rootView.findViewById(R.id.destinationEdit);
        resultList = rootView.findViewById(R.id.resultList);
        searchBtn = rootView.findViewById(R.id.searchBtn);
        destinationSearch = rootView.findViewById(R.id.destinationSearch);
        destinationWrapper = rootView.findViewById(R.id.destinationWrapper);
        route = rootView.findViewById(R.id.route);

        mapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        keywordEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocus) {
                if (isFocus && mode == SEARCH_DESTINATION) {
                    mode = SEARCH_ORIGIN;
                }
            }
        });

        destinationEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocus) {
                if (isFocus) {
                    mode = SEARCH_DESTINATION;
                }
            }
        });

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

        destinationSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                if (gMap == null) {
                    Toast.makeText(getContext(), "MAP NOT LOADED", Toast.LENGTH_SHORT).show();
                    return;
                }

                findLocation(destinationEdit.getText().toString());
            }
        });

        adapter = new MapLocationListAdapter(new ArrayList<MapLocation>(), new OnMapSelectListener() {
            @Override
            public void onSelect(MapLocation mapLocation) {
                gMap.moveCamera(CameraUpdateFactory.newLatLng(mapLocation.latlng));
                gMap.animateCamera(CameraUpdateFactory.zoomTo(15f));
            }
        });

        if (mode == SEARCH_ORIGIN) {
            destinationWrapper.setVisibility(View.VISIBLE);
        }

        resultList.setLayoutManager(new LinearLayoutManager(getContext()));
        resultList.setItemAnimator(new DefaultItemAnimator());
        resultList.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (gMap != null) {
            gMap.clear();
        }

        if (gMap != null && latlng != null) {
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

        if (mode == SEARCH_ORIGIN) {
            destinationWrapper.setVisibility(View.VISIBLE);
        } else {
            destinationWrapper.setVisibility(View.GONE);
        }

        route.setVisibility(View.GONE);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.clear();
        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (mode == SEARCH_ORIGIN) {
                    latlng = marker.getPosition();
                    destinationEdit.requestFocus();
                    return true;
                }

                if (mode == SEARCH_DESTINATION) {
                    destinationLatlng = marker.getPosition();
                    findRoute();
                    return true;
                }

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

        if (polyline != null) {
            gMap.addPolyline(new PolylineOptions().addAll(polyline));
        }
    }

    private void findLocation(String keyword) {
        resultList.setVisibility(View.VISIBLE);
        route.setVisibility(View.GONE);
        if (mode != SEARCH_DESTINATION) {
            gMap.clear();
        }

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

    private void findRoute() {
        resultList.setVisibility(View.GONE);

        if (latlng == null || destinationLatlng == null) {
            return;
        }

        ApiClient.findRoute(latlng, destinationLatlng, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    JSONObject bounds = response.getJSONObject("bounds");
                    final String startAddress = response.getString("start_address");
                    final JSONObject startLocation = response.getJSONObject("start_location");
                    final String endAddress = response.getString("end_address");
                    final JSONObject endLocation = response.getJSONObject("end_location");
                    final String summary = response.getString("summary");
                    final JSONArray points = response.getJSONArray("points");

                    route.setVisibility(View.VISIBLE);
                    route.setText(summary);

                    final List<LatLng> line = new ArrayList<>();
                    for(int i = 0; i < points.length(); i++) {
                        JSONArray pointArray = points.getJSONArray(i);
                        LatLng point = new LatLng(pointArray.getDouble(1), pointArray.getDouble(0));
                        line.add(point);
                    }

                    gMap.addPolyline(new PolylineOptions().addAll(line));
                    route.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                if (onRouteSelect != null) {
                                    onRouteSelect.onSelect(startAddress, new LatLng(startLocation.getDouble("lat"), startLocation.getDouble("lng")),
                                            endAddress, new LatLng(endLocation.getDouble("lat"), endLocation.getDouble("lng")),
                                            summary, line);
                                }
                            } catch (JSONException e) {
                                Toast.makeText(getContext(), "FAIL TO ON SELECT", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    JSONObject northeast = bounds.getJSONObject("northeast");
                    JSONObject southwest = bounds.getJSONObject("southwest");
                    LatLngBounds latLngBounds = LatLngBounds.builder()
                            .include(new LatLng(northeast.getDouble("lat"), northeast.getDouble("lng")))
                            .include(new LatLng(southwest.getDouble("lat"), southwest.getDouble("lng")))
                            .build();

                    gMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 20));
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "FAIL TO PARSE ROUTE", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.i("ERROR FIND ROUTE", errorResponse.toString());
                Toast.makeText(getContext(), "FAIL TO LOAD FIND ROUTE", Toast.LENGTH_SHORT).show();
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

    public interface OnRouteSelect {
        void onSelect(String startAddress, LatLng startLocation, String endAddress, LatLng endLocation, String summary, List<LatLng> points);
    }
}
