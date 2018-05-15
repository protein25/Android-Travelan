package travelan.art.sangeun.travelan;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by sangeun on 2018-05-12.
 */

public class NewspeedFragment extends Fragment {
    private static final String TAG = "NewspeedFragment";
    private RecyclerView newspeedList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_newspeed, container, false);
        newspeedList = v.findViewById(R.id.newspeedList);

        return v;
    }
}
