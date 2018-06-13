package travelan.art.sangeun.travelan.dialog;

import android.widget.AdapterView;

/**
 * Created by sangeun on 2018-06-13.
 */

public interface OnLocationSelectListener extends AdapterView.OnItemClickListener {
    void onItemClick(String location);
}
