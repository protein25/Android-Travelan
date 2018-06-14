package travelan.art.sangeun.travelan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import travelan.art.sangeun.travelan.adapters.CommentAdapter;
import travelan.art.sangeun.travelan.models.Comment;
import travelan.art.sangeun.travelan.models.Newspeed;
import travelan.art.sangeun.travelan.models.User;
import travelan.art.sangeun.travelan.utils.ApiClient;

public class CommentActivity extends AppCompatActivity {
    private Toolbar toolbar;

    private RecyclerView commentList;
    private EditText commentInput;
    private List<Comment> comments = new ArrayList<>();
    private CommentAdapter adapter;
    private Newspeed newspeed = new Newspeed();
    private Comment writerComment = new Comment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        commentList = findViewById(R.id.commentList);
        commentInput = findViewById(R.id.commentInput);

        commentList.setItemAnimator(new DefaultItemAnimator());
        commentList.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();

        int id = intent.getIntExtra("id", 0);
        String location = intent.getStringExtra("location");
        List<String> images = intent.getStringArrayListExtra("images");
        boolean isFav = intent.getBooleanExtra("isFav", false);

        newspeed.id = id;
        newspeed.location = location;
        newspeed.images = images;
        newspeed.isFav = isFav;

        User writer = new User();
        writer.thumbnail = intent.getStringExtra("thumbnail");
        writer.userId = intent.getStringExtra("userId");

        newspeed.user = writer;

        String comment = intent.getStringExtra("comment");
        writerComment.user = writer;
        writerComment.content = comment;

        comments.add(null); // null for header
        comments.add(writerComment);
        adapter = new CommentAdapter(newspeed, comments);
        commentList.setAdapter(adapter);

        commentInput.setOnEditorActionListener(new EditText.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    write();
                    return true;
                }
                return false;
            }
        });
        getComments();
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

    public void write() {
        String content = commentInput.getText().toString();
        ApiClient.writeComment(newspeed.id, content, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                getComments();
                commentInput.setText("");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(CommentActivity.this, "FAIL TO WRITE COMMENTS", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getComments() {
        final List<Comment> list = new ArrayList<>();
        list.add(null); // null for header
        list.add(writerComment);

        ApiClient.getComments(newspeed.id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    for(int i = 0; i < response.length(); i++) {
                        JSONObject object = response.getJSONObject(i);
                        Comment comment = new Comment();
                        comment.content = object.getString("content");
                        JSONObject memberObj = object.getJSONObject("member");
                        User user = new User();
                        user.thumbnail = memberObj.getString("thumb");
                        user.userId = memberObj.getString("userId");
                        comment.user = user;

                        list.add(comment);
                    }

                    adapter.items = list;
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.i("FAIL TO PARSE", e.getMessage());
                    Toast.makeText(CommentActivity.this, "FAIL TO PARSE COMMENTS", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(CommentActivity.this, "FAIL TO LOAD COMMENTS", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
