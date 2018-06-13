package travelan.art.sangeun.travelan.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import travelan.art.sangeun.travelan.R;
import travelan.art.sangeun.travelan.dialog.OnLocationSelectListener;

/**
 * Created by sangeun on 2018-06-03.
 */

public class LocationListAdapter extends RecyclerView.Adapter {
    private List<String> list;
    private OnLocationSelectListener selectListener;

    public LocationListAdapter(List list){
        this.list = list;
    }

    public void setOnLocationSelectListener(OnLocationSelectListener selectListener){
        this.selectListener = selectListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_only_text, parent ,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ViewHolder mViewHolder = (ViewHolder)holder;
        mViewHolder.listText.setText(list.get(position));
        mViewHolder.listText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectListener.onItemClick(list.get(position));
            }
        });
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
