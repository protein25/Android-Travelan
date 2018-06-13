package travelan.art.sangeun.travelan.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import travelan.art.sangeun.travelan.R;

public class MapLocationDialog extends DialogFragment implements OnMapReadyCallback {
    private static View rootView;
    private SupportMapFragment mapFragment;
    private GoogleMap gMap;
    private OnMapSelectListener onMapSelectListener;

    public MapLocationDialog() {

    }

    public void setOnMapSelectedListener(OnMapSelectListener onMapSelectListener) {
        this.onMapSelectListener = onMapSelectListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {
            rootView = inflater.inflate(R.layout.dialog_map_location, container, false);
        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }

        mapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (onMapSelectListener != null) {
                    MapLocation mapLocation = new MapLocation();
                    mapLocation.latlng = marker.getPosition();
                    mapLocation.address = marker.getTag().toString();
                    mapLocation.poi = marker.getTitle();

                    onMapSelectListener.onSelect(mapLocation);
                }

                return false;
            }
        });
    }
}
