package travelan.art.sangeun.travelan.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import travelan.art.sangeun.travelan.R;

public class SelectTravelAdapter extends RecyclerView.Adapter {
    public List<Object> items;
    private OnTravelSelectListener onTravelSelectListener;

    public SelectTravelAdapter(List<Object> items, OnTravelSelectListener onTravelSelectListener) {
        this.items = items;
        this.onTravelSelectListener = onTravelSelectListener;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_select_travel, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final Map<String, String> travel = (HashMap) items.get(position);

        viewHolder.travelTitle.setText("#" + travel.get("title"));

        if (travel.get("dateString") != null) {
            viewHolder.dateString.setVisibility(View.VISIBLE);
            viewHolder.dateString.setText(travel.get("dateString"));
        } else {
            viewHolder.dateString.setVisibility(View.GONE);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTravelSelectListener.onSelect(travel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView travelTitle, dateString;

        public ViewHolder(View itemView) {
            super(itemView);
            travelTitle = itemView.findViewById(R.id.travelTitle);
            dateString = itemView.findViewById(R.id.dateString);
        }
    }

    public interface OnTravelSelectListener {
        void onSelect(Map<String, String> selected);
    }
}
