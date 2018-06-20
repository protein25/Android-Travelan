package travelan.art.sangeun.travelan;

import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import gun0912.tedbottompicker.TedBottomPicker;
import travelan.art.sangeun.travelan.adapters.SelectTravelAdapter;
import travelan.art.sangeun.travelan.dialog.SelectTravelDialog;
import travelan.art.sangeun.travelan.utils.Alert;
import travelan.art.sangeun.travelan.utils.ApiClient;

public class WriteNewspeedActivity extends AppCompatActivity {
    private static final String TAG = "WriteNewspeedActivity";
    private Toolbar toolbar;
    private Button location;
    private CarouselView photoSlide;
    private EditText writeContet;
    private String selectedLocation;
    private List<Uri> imageUriList = new ArrayList<>();
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        setContentView(R.layout.activity_write_newspeed);
        toolbar = findViewById(R.id.toolbar);
        location = findViewById(R.id.location);
        photoSlide = findViewById(R.id.photoSlide);
        writeContet = findViewById(R.id.writeContent);

        setSupportActionBar(toolbar);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectLocation();
            }
        });
        setImageCarousel(imageUriList);
    }

    private void setImageCarousel(List<Uri> uriList) {
        for(Uri uri: uriList) {
            imageUriList.add(uri);
        }

        photoSlide.setPageCount(imageUriList.size() + 1);
        photoSlide.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                if (position < imageUriList.size()) {
                    Picasso.get().load(imageUriList.get(position)).into(imageView);
                } else {
                    imageView.setScaleType(ImageView.ScaleType.CENTER);
                    imageView.setImageResource(R.drawable.ic_add_black_24dp);
                }
            }
        });
        photoSlide.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {
                TedBottomPicker bottomSheetDialogFragment = new TedBottomPicker.Builder(context)
                        .setOnMultiImageSelectedListener(new TedBottomPicker.OnMultiImageSelectedListener() {
                            @Override
                            public void onImagesSelected(ArrayList<Uri> uriList) {
                                setImageCarousel(uriList);
                            }
                        })
                        .create();

                bottomSheetDialogFragment.show(getSupportFragmentManager());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.done:
                write();
                return true;
            default:
                return false;
        }
    }

    public void write() {
        if (location.getTag() == null) {
            Alert.show(context, "location 을 선택 해 주세요.");
            return;
        }

        if (imageUriList.size() == 0) {
            Alert.show(context, "이미지를 1장 이상 등록 해 주세요.");
            return;
        }

        if (writeContet.getText().length() == 0) {
            Alert.show(context, "글을 입력 해 주세요.");
            return;
        }

        int travelId = Integer.parseInt(location.getTag().toString());

        try {
            ApiClient.writeNewspeed(travelId, writeContet.getText().toString(), imageUriList, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    finish();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Toast.makeText(context, "FAIL TO WRITE", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (FileNotFoundException e) {
            Log.i("FILE UPLOAD ERROR", "ERROR: " + e.getLocalizedMessage());
            Toast.makeText(context, "FAIL TO UPLOAD FILE", Toast.LENGTH_SHORT).show();
        } catch (URISyntaxException e) {
            Log.i("FILE UPLOAD ERROR", "ERROR: " + e.getReason());
            Toast.makeText(context, "FAIL TO UPLOAD FILE", Toast.LENGTH_SHORT).show();
        }
    }

    //여행 선택 dialog method
    private void selectLocation() {
        final List<Object> travels = new ArrayList<>();

        ApiClient.getTravels(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        Map<String, String> travel = new HashMap<>();
                        JSONObject object = response.getJSONObject(i);
                        travel.put("id", object.getString("id"));
                        travel.put("title", object.getString("title"));
                        travel.put("dateString", object.getString("dateString"));

                        travels.add(travel);
                    }

                    final SelectTravelDialog selectTravelDialog = new SelectTravelDialog();
                    selectTravelDialog.init(travels, new SelectTravelAdapter.OnTravelSelectListener() {
                        @Override
                        public void onSelect(Map<String, String> selected) {
                            selectTravelDialog.dismiss();
                            location.setText("#" + selected.get("title"));
                            location.setTag(selected.get("id"));
                        }
                    });
                    selectTravelDialog.setAddAble(false);
                    selectTravelDialog.show(getSupportFragmentManager(), "selectTravelDialog");
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "FAIL TO PARSE TRAVELS", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getApplicationContext(), "FAIL TO LOAD TRAVELS", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
