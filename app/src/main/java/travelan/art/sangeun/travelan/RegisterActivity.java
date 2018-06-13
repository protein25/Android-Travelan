package travelan.art.sangeun.travelan;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import cz.msebera.android.httpclient.Header;
import travelan.art.sangeun.travelan.utils.ApiClient;

public class RegisterActivity extends AppCompatActivity {
    private TextInputLayout idInputLayout, nameInputLayout, ageInputLayout, emergencInputyLayout;
    private EditText idInput, nameInput, ageInput, emergencyInput;
    private RadioGroup radioGroup;
    private RadioButton female, male;
    private ImageView userThumb;
    private Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        idInputLayout = findViewById(R.id.idInputLayout);
        nameInputLayout = findViewById(R.id.nameInputLayout);
        ageInputLayout = findViewById(R.id.ageInputLayout);
        emergencInputyLayout = findViewById(R.id.emergencyInputLayout);

        idInput = findViewById(R.id.idInput);
        nameInput = findViewById(R.id.nameInput);
        ageInput = findViewById(R.id.ageInput);
        emergencyInput = findViewById(R.id.emergencyInput);

        radioGroup = findViewById(R.id.sexInput);
        female = findViewById(R.id.sexFemale);
        male = findViewById(R.id.sexMale);

        userThumb = findViewById(R.id.userThumb);
        registerBtn = findViewById(R.id.registerBtn);

        Intent intent = getIntent();

        idInput.setText(intent.getStringExtra("email"));
        nameInput.setText(intent.getStringExtra("name"));

        final String thumbnailUrl = intent.getStringExtra("thumbnail");
        if (!thumbnailUrl.equals("")) {
            Picasso.get().load(thumbnailUrl).into(userThumb);
        }

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestParams params = new RequestParams();
                params.add("userId", idInput.getText().toString());
                params.add("name", nameInput.getText().toString());

                if (ageInput.getText() != null) {
                    params.add("age",ageInput.getText().toString());
                }

                if (emergencyInput.getText() != null) {
                    params.add("emergency", emergencyInput.getText().toString());
                }

                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.sexFemale:
                        params.add("sex", "F");
                        break;
                    case R.id.sexMale:
                        params.add("sex", "M");
                        break;
                    default:
                        params.add("sex", null);
                        break;
                }

                if (!thumbnailUrl.equals("")) {
                    params.add("thumb", idInput.getText().toString());
                }

                ApiClient.join(params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Intent toMain = new Intent(RegisterActivity.this, MainActivity.class);
                        toMain.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(toMain);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(RegisterActivity.this, "Register Fail please retry", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
