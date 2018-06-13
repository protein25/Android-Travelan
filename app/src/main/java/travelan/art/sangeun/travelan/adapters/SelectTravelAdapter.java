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
        Map<String, String> travel = (HashMap) items.get(position);

        final int travelId = Integer.parseInt(travel.get("id"));
        viewHolder.travelTitle.setText("#" + travel.get("title"));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTravelSelectListener.onSelect(travelId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView travelTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            travelTitle = itemView.findViewById(R.id.travelTitle);
        }
    }

    public interface OnTravelSelectListener {
        void onSelect(int travelId);
    }
}
