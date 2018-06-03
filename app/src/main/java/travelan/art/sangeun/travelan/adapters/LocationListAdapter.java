package travelan.art.sangeun.travelan.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import travelan.art.sangeun.travelan.R;

/**
 * Created by sangeun on 2018-06-03.
 */

public class LocationListAdapter extends RecyclerView.Adapter {
    private List<String> list;
    private Context context;

    public LocationListAdapter(List list, Context context){
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_only_text, parent ,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder mViewHolder = (ViewHolder)holder;
        mViewHolder.listText.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        public TextView listText;
        public ViewHolder(View itemView) {
            super(itemView);
            listText = itemView.findViewById(R.id.listText);
        }
    }

}
