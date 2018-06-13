package travelan.art.sangeun.travelan;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import travelan.art.sangeun.travelan.adapters.NewspeedListAdapter;
import travelan.art.sangeun.travelan.models.Newspeed;
import travelan.art.sangeun.travelan.models.User;
import travelan.art.sangeun.travelan.utils.ApiClient;

public class MyPageActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private NewspeedListAdapter adapter;
    private List<Newspeed> items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.favsList);
        items = new ArrayList<>();

        this.setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.MyPage);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        ApiClient.getFavs(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray resultArray) {
                try {
                    for (int i = 0; i < resultArray.length(); i++) {
                        JSONObject object = resultArray.getJSONObject(i);

                        Newspeed item = new Newspeed();
                        item.location = "#FUKUOKA";
                        item.isFav = false;
                        item.contents = object.getString("content");

                        item.images = new ArrayList<>();
                        JSONArray images = object.getJSONArray("images");
                        for (int imageIndex = 0; imageIndex < images.length(); imageIndex++) {
                            item.images.add(images.getString(imageIndex));
                        }

                        if (!object.isNull("planId")) {
                            item.planId = object.getInt("planId");
                        }

                        item.user = new User();
                        JSONObject member = object.getJSONObject("member");
                        item.user.thumbnail = member.getString("thumb");
                        item.user.userId = member.getString("userId");

                        items.add(item);
                    }
                    adapter = new NewspeedListAdapter(items);
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    Log.e("FAIL TO PARSE DATA", e.getMessage());
                }
            }
        });

    }
}
