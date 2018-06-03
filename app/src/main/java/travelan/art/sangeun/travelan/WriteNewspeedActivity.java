package travelan.art.sangeun.travelan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.synnapps.carouselview.CarouselView;

public class WriteNewspeedActivity extends AppCompatActivity {
    private static final String TAG = "WriteNewspeedActivity";
    private Button location;
    private CarouselView photoSlide;
    private EditText writeContet;
    private ImageButton selectPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_newspeed);
        location = findViewById(R.id.location);
        photoSlide = findViewById(R.id.photoSlide);
        writeContet = findViewById(R.id.writeContent);
        selectPhoto = findViewById(R.id.selected_photo);

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectLocation();
            }
        });
    }

    //여행 선택 dialog method
    private void selectLocation(){


    }
}
