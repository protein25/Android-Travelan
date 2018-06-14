package travelan.art.sangeun.travelan.adapters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import gun0912.tedbottompicker.TedBottomPicker;
import travelan.art.sangeun.travelan.R;
import travelan.art.sangeun.travelan.dialog.MapLocationDialog;
import travelan.art.sangeun.travelan.models.Device;
import travelan.art.sangeun.travelan.utils.Alert;
import travelan.art.sangeun.travelan.utils.ApiClient;
import travelan.art.sangeun.travelan.utils.BleScanner;

public class MyDeviceListAdapter extends RecyclerView.Adapter {
    public List<Device> items = new ArrayList<>();
    private Fragment fragment;

    public MyDeviceListAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_setting, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;

        final Device item = items.get(position);

        if (item.imageUrl != null && item.imageUrl.length() > 0) {
            Picasso.get().load(item.imageUrl).into(viewHolder.deviceImage);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (item.lastLocation == null) {
                        Alert.show(fragment.getContext(), "등록 된 위치가 없습니다.");
                        return;
                    }
                    MapLocationDialog mapDialog = new MapLocationDialog();
                    mapDialog.setPoint(item.lastLocation);
                    mapDialog.show(fragment.getFragmentManager(), "mapDialog");
                }
            });
        } else {
            viewHolder.deviceImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    TedBottomPicker bottomSheetDialogFragment = new TedBottomPicker.Builder(fragment.getContext())
                            .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                                @Override
                                public void onImageSelected(Uri uri) {
                                    Picasso.get().load(uri).into((ImageView)view);

                                    try {
                                        ApiClient.addDeviceImage(item.id, uri, new JsonHttpResponseHandler());
                                    } catch (Exception e) {

                                    }
                                }
                            })
                            .create();

                    bottomSheetDialogFragment.show(fragment.getFragmentManager());
                }
            });
        }

        viewHolder.deviceId.setText(item.name);
        viewHolder.switchDevice.setChecked(item.isReported);

        boolean isConnected = false;

        for(String connected: BleScanner.connectedDevices.keySet()) {
            if(connected.equals(item.mac)) {
                isConnected = true;
                break;
            }
        }

        if (!isConnected) {
            viewHolder.deviceDiabled.setVisibility(View.VISIBLE);
        } else {
            viewHolder.deviceDiabled.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

class ViewHolder extends RecyclerView.ViewHolder {
    public ImageView deviceImage;
    public View deviceDiabled;
    public TextView deviceId;
    public Switch switchDevice;

    public ViewHolder(View itemView) {
        super(itemView);

        deviceImage = itemView.findViewById(R.id.deviceImage);
        deviceDiabled = itemView.findViewById(R.id.deviceDisabled);
        deviceId = itemView.findViewById(R.id.deviceId);
        switchDevice = itemView.findViewById(R.id.switchDevice);
    }
}