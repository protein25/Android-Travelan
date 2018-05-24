package travelan.art.sangeun.travelan;

import android.app.Application;

import com.kakao.auth.KakaoSDK;

import travelan.art.sangeun.travelan.utils.KakaoSDKAdapter;

public class TravelanApplication extends Application {
    private static volatile TravelanApplication instance = null;

    public static TravelanApplication getGlobalApplicationContext() {
        if(instance == null)
            throw new IllegalStateException("this application does not Travelan application");
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        KakaoSDK.init(new KakaoSDKAdapter());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
    }
}
