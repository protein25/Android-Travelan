package travelan.art.sangeun.travelan;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import travelan.art.sangeun.travelan.adapters.NewspeedListAdapter;
import travelan.art.sangeun.travelan.models.Newspeed;
import travelan.art.sangeun.travelan.models.User;

/**
 * Created by sangeun on 2018-05-12.
 */

public class NewspeedFragment extends Fragment {
    private static final String TAG = "NewspeedFragment";
    private RecyclerView newspeedList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View rootView = inflater.inflate(R.layout.fragment_newspeed, container, false);
        newspeedList = rootView.findViewById(R.id.newspeedList);

        List<Newspeed> items = makeDummy(); // 이후  api에서 비동기로 처리 하도록 수정 필요
        Log.i("item size", Integer.toString(items.size()));

        NewspeedListAdapter adapter = new NewspeedListAdapter(getContext(), items);
        newspeedList.setLayoutManager(new LinearLayoutManager(getContext()));
        newspeedList.setItemAnimator(new DefaultItemAnimator());
        newspeedList.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_newspeed, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private List<Newspeed> makeDummy() {
        List<Newspeed> items = new ArrayList<>();

        Newspeed item1 = new Newspeed();
        item1.location = "#FUKUOKA";
        item1.isFav = true;
        item1.contents = "FUKUOKA\nI'm lovin' it!\nMc Donalds";
        item1.images = new ArrayList<>();
        item1.images.add("https://mblogthumb-phinf.pstatic.net/20151215_146/rlatnals8712_1450141030738pC6eR_PNG/20151215_094901.png?type=w2");
        item1.images.add("https://mblogthumb-phinf.pstatic.net/20151215_291/rlatnals8712_1450141111641dSSbu_PNG/20151215_094947.png?type=w2");
        item1.planId = 1;
        item1.user = new User();
        item1.user.thumbnail = "https://mblogthumb-phinf.pstatic.net/MjAxODA1MjVfNjQg/MDAxNTI3MjQyMzU5NTIw.dETfi7WgQg-qgW4EdMxNfte7wxrCCI0Ugo7xO1cc8Ikg.erElLYVslFCldx_x9yerThdiwSVLdcF87Q7h9WINXBIg.JPEG.pvoqhpot65v/%EC%95%88%EB%85%95%281%29.JPG?type=w800";
        item1.user.userId = "tkddms1015@hanmail.net";

        Newspeed item2 = new Newspeed();
        item2.location = "#OSAKA";
        item2.isFav = false;
        item2.contents = "OSAKA\nGAGO\nSIPDDA";
        item2.images = new ArrayList<>();
        item2.images.add("https://thumb-wishbeen.akamaized.net/RIv8BY368OCQ92mhTlrLQXClFic=/880x/smart/filters:no_upscale()/img-wishbeen.akamaized.net/post/1460296826601_20160410_203817.jpg");
        item2.images.add("https://thumb-wishbeen.akamaized.net/5adZ94s0dMQbZYhGEMYCHZ_6HcM=/880x/smart/filters:no_upscale()/img-wishbeen.akamaized.net/post/1460296831757_20160410_203826.jpg");
        item2.planId = 1;
        item2.user = new User();
        item2.user.thumbnail = "https://mblogthumb-phinf.pstatic.net/MjAxODA1MjVfNjQg/MDAxNTI3MjQyMzU5NTIw.dETfi7WgQg-qgW4EdMxNfte7wxrCCI0Ugo7xO1cc8Ikg.erElLYVslFCldx_x9yerThdiwSVLdcF87Q7h9WINXBIg.JPEG.pvoqhpot65v/%EC%95%88%EB%85%95%281%29.JPG?type=w800";
        item2.user.userId = "tkddms1015@hanmail.net";

        items.add(item1);
        items.add(item2);

        return items;
    }
}
