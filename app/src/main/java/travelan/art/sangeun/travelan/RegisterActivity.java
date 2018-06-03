package travelan.art.sangeun.travelan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import cz.msebera.android.httpclient.Header;
import travelan.art.sangeun.travelan.utils.ApiClient;

public class RegisterActivity extends AppCompatActivity {
    private EditText idInput, nameInput;
    private ImageView userThumb;
    private Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        idInput = findViewById(R.id.idInput);
        nameInput = findViewById(R.id.nameInput);
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
