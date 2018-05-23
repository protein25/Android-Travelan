package travelan.art.sangeun.travelan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ImageButton;

public class WriteNewsfeedActivity extends AppCompatActivity {
    private static final String TAG = "WriteNewsfeedActivity";
    private Toolbar toolbar;
    private ImageButton btnAddPhoto;
    private EditText inputContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_newsfeed);

        toolbar = findViewById(R.id.toolbar);
        btnAddPhoto = findViewById(R.id.btnAddPhoto);
        inputContent = findViewById(R.id.inputContent);


    }
}
