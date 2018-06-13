package travelan.art.sangeun.travelan.utils;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.Calendar;
import java.util.Date;

import travelan.art.sangeun.travelan.models.Plan;

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

    static public void addTravel(String title, AsyncHttpResponseHandler httpResponseHandler) {
        RequestParams params = new RequestParams();
        params.put("title", title);
        ApiClient.post("/travel", params, httpResponseHandler);
    }

    static public void addPlan(Plan plan, Date date, AsyncHttpResponseHandler httpResponseHandler) {
        RequestParams params = new RequestParams();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        params.put("sort", plan.attributeType);
        params.put("date", cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH));
        params.put("travelId", plan.travelId);
        params.put("title", plan.title);
        params.put("address", plan.address);
        params.put("time", plan.time);
        params.put("origin", plan.origin);
        params.put("destination", plan.destination);
        params.put("way", plan.way);
        params.put("route", plan.route);
        params.put("order", plan.order);

        ApiClient.post("/plan/write", params, httpResponseHandler);
    }

    static public void findLocation(String keyword, AsyncHttpResponseHandler httpResponseHandler) {
        RequestParams params = new RequestParams();
        params.put("keyword", keyword);
        ApiClient.get("/plan/findLocation", params, httpResponseHandler);
    }
}
