package travelan.art.sangeun.travelan;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageButton;

public class CommentActivity extends AppCompatActivity {
    private static final String TAG = "CommentActivity";
    private Toolbar toolbar;
    private RecyclerView commentList;
    private TextInputEditText inputText;
    private ImageButton submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        toolbar = findViewById(R.id.toolbar);
        commentList = findViewById(R.id.commentList);
        inputText = findViewById(R.id.inputComment);
        submit = findViewById(R.id.submit);

        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.homeAsUp){
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
