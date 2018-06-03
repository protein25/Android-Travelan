package travelan.art.sangeun.travelan;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.kakao.auth.ApiErrorCode;
import com.kakao.auth.ApiResponseCallback;
import com.kakao.auth.AuthService;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.auth.network.response.AccessTokenInfoResponse;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.OptionalBoolean;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import travelan.art.sangeun.travelan.utils.ApiClient;

public class LoginActivity extends AppCompatActivity {
    private SessionCallback callback;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;

        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            getMe();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if(exception != null) {
                Logger.e(exception);
            }
        }

        public void getMe() {
            List<String> keys = new ArrayList<>();
            keys.add("properties.nickname");
            keys.add("properties.profile_image");
            keys.add("kakao_account.email");

            UserManagement.getInstance().me(keys, new MeV2ResponseCallback() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    String message = "failed to get user info. msg=" + errorResult;
                    Logger.d(message);

                    int result = errorResult.getErrorCode();
                    if (result == ApiErrorCode.CLIENT_ERROR_CODE) {
                        Toast.makeText(getApplicationContext(), "CLIENT_ERROR", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Logger.e("onSessionClosed");
                    Toast.makeText(getApplicationContext(), "SESSION_CLOSED", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(final MeV2Response result) {
                    if (result.hasSignedUp() == OptionalBoolean.FALSE) {
                        signUp();
                    } else {
                        Toast.makeText(getApplicationContext(), "KAKAO LINKED", Toast.LENGTH_SHORT).show();
                        // getAccessToken 을 서버에 보내서 해당 정보로 가입 / 로그인 하도록 구성.
                        // logout: https://developers.kakao.com/docs/android/user-management#로그아웃
                        // server token info: https://developers.kakao.com/docs/restapi/user-management#사용자-토큰-유효성-검사-및-정보-얻기
                        String token = Session.getCurrentSession().getTokenInfo().getAccessToken();
                        Log.i("KAKAO_LOGIN_SUCCESS", result.toString());
                        ApiClient.setToken(token);

                        ApiClient.login(new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                Toast.makeText(getApplicationContext(), "LOGIN SUCCESS", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                if (statusCode == 401) {
                                    String name = result.getNickname();
                                    String thumbnail = result.getProfileImagePath();
                                    String email = result.getKakaoAccount().getEmail();

                                    Intent intent = new Intent(context, RegisterActivity.class);
                                    intent.putExtra("name", name);
                                    intent.putExtra("thumbnail", thumbnail);
                                    intent.putExtra("email", email);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    startActivity(intent);
                                } else {
                                    try {
                                        Log.e("LOGIN ERROR", new String(responseBody, "UTF-8"));
                                    } catch (UnsupportedEncodingException e) {
                                        Log.e("ENCODE ERROR", e.getMessage());
                                    }
                                    Toast.makeText(context, "LOGIN ERROR", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            });
        }

        public void signUp() {
            UserManagement.getInstance().requestSignup(new ApiResponseCallback<Long>() {
                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Toast.makeText(getApplicationContext(), "SESSION_CLOSED", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNotSignedUp() {
                    Toast.makeText(getApplicationContext(), "NOT_SIGN_UP", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(Long result) {
                    Toast.makeText(getApplicationContext(), "SIGN_UP_SUCCESS", Toast.LENGTH_SHORT).show();
                    getMe();
                }
            }, null);
        }

        private void requestAccessTokenInfo() {
            AuthService.getInstance().requestAccessTokenInfo(new ApiResponseCallback<AccessTokenInfoResponse>() {
                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Toast.makeText(getApplicationContext(), "SESSION_CLOSED", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNotSignedUp() {
                    // not happened
                }

                @Override
                public void onFailure(ErrorResult errorResult) {
                    Logger.e("failed to get access token info. msg=" + errorResult);
                }

                @Override
                public void onSuccess(AccessTokenInfoResponse accessTokenInfoResponse) {
                    long userId = accessTokenInfoResponse.getUserId();
                    Logger.d("this access token is for userId=" + userId);

                    long expiresInMilis = accessTokenInfoResponse.getExpiresInMillis();
                    Logger.d("this access token expires after " + expiresInMilis + " milliseconds.");
                }
            });
        }
    }
}
