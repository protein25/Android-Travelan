package travelan.art.sangeun.travelan.utils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ApiClient {
    private static final AsyncHttpClient httpClient = new AsyncHttpClient();

    private static final String HOST_URL = "http://18.191.11.177:3000/";

    public void login(String userId, String password, AsyncHttpResponseHandler httpHandler) {
        RequestParams params = new RequestParams();
        params.put("userId", userId);
        params.put("password", "password");

        httpClient.post("/members/login", params, httpHandler);
    }
}
