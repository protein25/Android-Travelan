package travelan.art.sangeun.travelan;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

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
    private TextView title;
    private RecyclerView recyclerView;
    private NewspeedListAdapter adapter;
    private List<Newspeed> items = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);
        toolbar = findViewById(R.id.toolbar);
        title = toolbar.findViewById(R.id.title);
        setSupportActionBar(toolbar);
        title.setText(R.string.MyPage);

        recyclerView = findViewById(R.id.favsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        ApiClient.getFavs(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray resultArray) {
                try {
                    for (int i = 0; i < resultArray.length(); i++) {
                        JSONObject object = resultArray.getJSONObject(i);

                        Newspeed item = new Newspeed();
                        item.location = "#" + object.getJSONObject("travel").getString("title");
                        item.isFav = object.getBoolean("isFav");
                        item.contents = object.getString("content");

                        item.images = new ArrayList<>();
                        JSONArray images = object.getJSONArray("images");
                        for (int imageIndex = 0; imageIndex < images.length(); imageIndex++) {
                            JSONObject image = images.getJSONObject(imageIndex);
                            String imageUrl = image.getString("serverName") + image.getString("originName");
                            item.images.add(imageUrl);
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
                    adapter = new NewspeedListAdapter(MyPageActivity.this, items);
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    Log.e("FAIL TO PARSE DATA", e.getMessage());
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
