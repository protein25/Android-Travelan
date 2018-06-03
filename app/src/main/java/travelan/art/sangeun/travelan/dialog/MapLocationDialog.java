package travelan.art.sangeun.travelan.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import travelan.art.sangeun.travelan.R;

public class MapLocationDialog extends DialogFragment implements OnMapReadyCallback{
    private MapView map;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_map_location, container);

        map = rootView.findViewById(R.id.map);
        map.getMapAsync(this);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        map.onResume();
        super.onResume();
    }

    @Override
    public void onStart() {
        map.onStart();
        super.onStart();
    }

    @Override
    public void onStop() {
        map.onStop();
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    @Override
    public void onPause() {
        map.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        map.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        map.onLowMemory();
        super.onLowMemory();
    }
}
