package travelan.art.sangeun.travelan.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import travelan.art.sangeun.travelan.R;
import travelan.art.sangeun.travelan.models.Plan;

public class PlanAdapter extends RecyclerView.Adapter {
    private static final int ATTRIBUTE_COMMON = 0;
    private static final int ATTRIBUTE_TRANSPORT = 1;

    public List<Plan> items;

    public PlanAdapter(List<Plan> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        switch (viewType) {
            case ATTRIBUTE_TRANSPORT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_plan_transportation, parent, false);
                return new ViewHolderTransport(view);
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_plan_common, parent, false);
                return new ViewHolderCommon(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Plan item = items.get(position);

        if (getItemViewType(position) == ATTRIBUTE_COMMON) {
            ViewHolderCommon viewHolder = (ViewHolderCommon) holder;
            viewHolder.title.setText(item.title);
            viewHolder.address.setText(item.address);
            viewHolder.tel.setText(item.tel);
        } else {
            ViewHolderTransport viewHolder = (ViewHolderTransport) holder;
            viewHolder.time.setText(item.time);
            viewHolder.way.setText(item.way);
            viewHolder.origin.setText(item.origin);
            viewHolder.route.setText(item.route);
            viewHolder.destination.setText(item.destination);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Plan item = items.get(position);
        if (item.attributeType.equals("transportation")) {
            return ATTRIBUTE_TRANSPORT;
        }

        return ATTRIBUTE_COMMON;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolderCommon extends RecyclerView.ViewHolder {
        TextView title, address, tel;

        public ViewHolderCommon(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            address = itemView.findViewById(R.id.address);
            tel = itemView.findViewById(R.id.tel);
        }
    }

    class ViewHolderTransport extends RecyclerView.ViewHolder {
        TextView time, way, origin, route, destination;

        public ViewHolderTransport(View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            way = itemView.findViewById(R.id.way);
            origin = itemView.findViewById(R.id.origin);
            route = itemView.findViewById(R.id.route);
            destination = itemView.findViewById(R.id.destination);
        }
    }
}
