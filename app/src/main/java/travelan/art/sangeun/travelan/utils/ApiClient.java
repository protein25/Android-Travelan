package travelan.art.sangeun.travelan.utils;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ApiClient {
    private static final AsyncHttpClient httpClient = new AsyncHttpClient();
//    private static final String HOST_URL = "http://18.191.11.177:3000";
    private static final String HOST_URL = "http://172.30.1.40:3000";
//    private static final String HOST_URL = "http://192.168.1.29:3000";
    private static String token = "";

    static public void setToken(String token) {
        ApiClient.token = token;
    }

    static public void get(String url, RequestParams params, AsyncHttpResponseHandler httpResponseHandler) {
        httpClient.addHeader("access-token", ApiClient.token);
        httpClient.get(HOST_URL + url, params, httpResponseHandler);
    }

    static public void post(String url, RequestParams params, AsyncHttpResponseHandler httpResponseHandler) {
        Log.i("ACCESS TOKEN", ApiClient.token);
        httpClient.addHeader("access-token", ApiClient.token);
        httpClient.post(HOST_URL + url, params, httpResponseHandler);
    }

    static public void getUserInfo(AsyncHttpResponseHandler httpHandler){
        ApiClient.post("/members/",null,httpHandler);
    }

    static public void login(AsyncHttpResponseHandler httpHandler) {
        ApiClient.post("/members/login", null, httpHandler);
    }

    static public void join(RequestParams params, AsyncHttpResponseHandler httpResponseHandler) {
        ApiClient.post("/members/join", params, httpResponseHandler);
    }

    static public void getFavs(AsyncHttpResponseHandler handler){
        ApiClient.post("/newspeed/favs",null,handler);
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

    static public void getLocations(AsyncHttpResponseHandler httpResponseHandler){
        ApiClient.post("/locations",null,httpResponseHandler);
    }

    static public void getMonthTravel(int year, int month, AsyncHttpResponseHandler httpResponseHandler) {
        ApiClient.get("/travel/" + year + "/" + month , null, httpResponseHandler);
    }

    static public void getDatePlan(int year, int month, int day, AsyncHttpResponseHandler httpResponseHandler) {
        ApiClient.get("/plan/" + year + "/" + month + "/" + day, null, httpResponseHandler);
    }

    static public void findLocation(String keyword, AsyncHttpResponseHandler httpResponseHandler) {
        RequestParams params = new RequestParams();
        params.put("keyword", keyword);
        ApiClient.get("/plan/findLocation", params, httpResponseHandler);
    }
}
