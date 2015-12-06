package com.feedthekitty.cmsc436.feedthekitty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Arrays;

/**
 * Created by davidgreene on 12/1/15.
 */
public class FacebookActivity extends Activity {

    private CallbackManager callbackManager;
    private LoginButton login_button;
    public static final String AUTH_ID = "A";
    public static final Integer RESULT_ERROR = -1;
    public static final String TAG = "FacebookActivity";
    Firebase ref = new Firebase(MainActivity.firebaseUrl);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getIntent();

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_facebook_login);

        callbackManager = CallbackManager.Factory.create();

        login_button = (LoginButton) findViewById(R.id.login_button);
        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {


            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i(TAG, "Successfully logged in to Facebook with result: " + loginResult.toString());
                Log.i(TAG, "Access token: " + loginResult.getAccessToken());

                Intent resultIntent = new Intent();
                AccessToken accessToken = loginResult.getAccessToken();

                if (accessToken != null) {
                    returnUserData(accessToken);
                } else {
                    FacebookActivity.this.setResult(RESULT_ERROR);
                    FacebookActivity.this.finish();
                }
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "Canceled Facebook Login");
                Intent resultIntent = new Intent();
                FacebookActivity.this.setResult(RESULT_CANCELED, resultIntent);
                FacebookActivity.this.finish();
            }

            @Override
            public void onError(FacebookException e) {
                Log.i(TAG, "Error logging into Facebook");
                e.printStackTrace();

                Intent resultIntent = new Intent();
                FacebookActivity.this.setResult(RESULT_ERROR, resultIntent);
                FacebookActivity.this.finish();
            }
        });


        // if already logged in, just send back uid
        if (isLoggedIn()) {
            Intent resultIntent = new Intent();
            AccessToken currAccessToken = AccessToken.getCurrentAccessToken();

            if (currAccessToken != null) {
                returnUserData(AccessToken.getCurrentAccessToken());
            }
        }
    }

    public void returnUserData(AccessToken accessToken) {

        UserData userData = new UserData();
        userData.setUserId(accessToken.getUserId());

        //TODO get fullName = first + " " + last
        userData.setFullName(null);
        Intent intent = userData.packageIntoIntent();
        setResult(RESULT_OK, intent);

        finish();
    }

    private boolean isLoggedIn() {
        Log.i(TAG, "Checking if already logged in");
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
/*
    private void onFacebookAccessTokenChange(AccessToken token) {
        if(token != null){
            ref.authWithOAuthToken("facebook", token.getToken(), new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticated(AuthData authdata){

                }

                @Override
                public void onAuthenticationError(FirebaseError error) {

                }
            });
        }
        else {
            ref.unauth();
        }
    }
*/


}