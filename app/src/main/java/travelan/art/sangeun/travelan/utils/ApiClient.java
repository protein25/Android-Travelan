package travelan.art.sangeun.travelan.utils;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ApiClient {
    private static final AsyncHttpClient httpClient = new AsyncHttpClient();
    private static final String HOST_URL = "http://18.191.11.177:3000";
//    private static final String HOST_URL = "http://192.168.1.29:3000";
    private static String token = "";

    static public void setToken(String token) {
        ApiClient.token = token;
    }

    static public void get(String url, RequestParams params, AsyncHttpResponseHandler httpResponseHandler) {
        httpClient.addHeader("accessToken", ApiClient.token);
        httpClient.get(HOST_URL + url, params, httpResponseHandler);
    }

    static public void post(String url, RequestParams params, AsyncHttpResponseHandler httpResponseHandler) {
        Log.i("ACCESS TOKEN", ApiClient.token);
        httpClient.addHeader("access-token", ApiClient.token);
        httpClient.post(HOST_URL + url, params, httpResponseHandler);
    }

    static public void login(AsyncHttpResponseHandler httpHandler) {
        ApiClient.post("/members/login", null, httpHandler);
    }

    static public void join(RequestParams params, AsyncHttpResponseHandler httpResponseHandler)


    {
        ApiClient.post("/members/join", params, httpResponseHandler);
    }

    static public void getNewspeeds(int page, AsyncHttpResponseHandler httpResponseHandler) {
        RequestParams params = new RequestParams();
        params.put("page", page);
        ApiClient.get("/newspeed", params, httpResponseHandler);
    }

    static public void getInformations(int page, AsyncHttpResponseHandler httpResponseHandler) {
        RequestParams params = new RequestParams();
        params.put("page", page);
        ApiClient.get("/informations", params, httpResponseHandler);
    }

    static public void getLocations(int memberId, AsyncHttpResponseHandler httpResponseHandler){
        RequestParams params = new RequestParams();
        params.put("memberId",memberId);

        ApiClient.post("/locations",params,httpResponseHandler);
    }
}
