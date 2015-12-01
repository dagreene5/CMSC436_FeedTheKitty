package com.feedthekitty.cmsc436.feedthekitty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

/**
 * Created by davidgreene on 12/1/15.
 */
public class FacebookLogin extends Activity {

    private TextView username;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private Intent intent;
    public static final String AUTH_ID = "A";
    public static final Integer RESULT_ERROR = -1;
    public static final String TAG = "FacebookLogin";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intent = getIntent();
        //LoginButton
        setContentView(R.layout.activity_facebook_login);

        username = (TextView) findViewById(R.id.username);
        loginButton = (LoginButton) findViewById(R.id.fb_login_button);
        loginButton.setReadPermissions(Arrays.asList("email", "user_friends"));

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i(TAG, "Successfully logged in to Facebook with result: " + loginResult.toString());
                Log.i(TAG, "Access token: " + loginResult.getAccessToken());

                intent.putExtra(AUTH_ID,  loginResult.getAccessToken().getApplicationId());
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "Canceled Facebook Login");
                setResult(RESULT_CANCELED);
                finish();
            }

            @Override
            public void onError(FacebookException e) {
                Log.i(TAG, "Error logging into Facebook");
                e.printStackTrace();

                setResult(RESULT_ERROR);
                finish();
            }
        });

    }

}
