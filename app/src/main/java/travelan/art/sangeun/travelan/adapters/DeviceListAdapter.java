package travelan.art.sangeun.travelan.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import travelan.art.sangeun.travelan.R;

public class DeviceListAdapter extends RecyclerView.Adapter{
    public List<String> items = new ArrayList<>();
    private OnDeviceSelectListener onDeviceSelectListener;

    public DeviceListAdapter(OnDeviceSelectListener onDeviceSelectListener) {
        this.onDeviceSelectListener = onDeviceSelectListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_only_text, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.listText.setText(items.get(position));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeviceSelectListener.onSelect(items.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView listText;

        public ViewHolder(View itemView) {
            super(itemView);

            listText = itemView.findViewById(R.id.listText);
        }
    }

    public interface OnDeviceSelectListener {
        void onSelect(String device);
    }
}
