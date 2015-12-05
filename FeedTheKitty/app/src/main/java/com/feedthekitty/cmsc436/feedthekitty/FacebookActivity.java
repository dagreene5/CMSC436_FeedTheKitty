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

    private TextView username;
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

<<<<<<< HEAD:FeedTheKitty/app/src/main/java/com/feedthekitty/cmsc436/feedthekitty/FacebookActivity.java
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_facebook_login);
=======
        username = (TextView) findViewById(R.id.username);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("email", "user_friends"));
>>>>>>> 768923f8189ee0ede80896cb2c846ad4ae8bfcbd:FeedTheKitty/app/src/main/java/com/feedthekitty/cmsc436/feedthekitty/FacebookLogin.java

        callbackManager = CallbackManager.Factory.create();


        login_button = (LoginButton) findViewById(R.id.login_button);
        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i(TAG, "Successfully logged in to Facebook with result: " + loginResult.toString());
                Log.i(TAG, "Access token: " + loginResult.getAccessToken());

                Intent resultIntent = new Intent();
                resultIntent.putExtra(AUTH_ID, loginResult.getAccessToken().getApplicationId());
                setResult(RESULT_OK, resultIntent);
                finish();
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "Canceled Facebook Login");
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();
            }

            @Override
            public void onError(FacebookException e) {
                Log.i(TAG, "Error logging into Facebook");
                e.printStackTrace();

                Intent resultIntent = new Intent();
                setResult(RESULT_ERROR, resultIntent);
                finish();
            }
        });

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
