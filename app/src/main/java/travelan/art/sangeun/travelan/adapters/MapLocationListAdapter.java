package travelan.art.sangeun.travelan.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import travelan.art.sangeun.travelan.R;
import travelan.art.sangeun.travelan.models.MapLocation;
import travelan.art.sangeun.travelan.utils.OnMapSelectListener;

public class MapLocationListAdapter extends RecyclerView.Adapter {
    public List<MapLocation> items;
    private OnMapSelectListener mapSelectListener;

    public MapLocationListAdapter(List<MapLocation> items, OnMapSelectListener mapSelectListener) {
        this.items = items;
        this.mapSelectListener = mapSelectListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_map_location_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder)holder;
        final MapLocation item = items.get(position);
        viewHolder.poi.setText(item.poi);
        viewHolder.address.setText(item.address);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapSelectListener.onSelect(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView poi, address;

        public ViewHolder(View itemView) {
            super(itemView);

            poi = itemView.findViewById(R.id.poi);
            address = itemView.findViewById(R.id.address);
        }
    }
}
