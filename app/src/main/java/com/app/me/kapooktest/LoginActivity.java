package com.app.me.kapooktest;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;

import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.linecorp.linesdk.LineApiResponse;
import com.linecorp.linesdk.LineProfile;
import com.linecorp.linesdk.api.LineApiClient;
import com.linecorp.linesdk.api.LineApiClientBuilder;
import com.linecorp.linesdk.auth.LineLoginApi;
import com.linecorp.linesdk.auth.LineLoginResult;

import org.json.JSONObject;

import java.util.ArrayList;


public class LoginActivity extends FragmentActivity {
    private static final String TAG = "LOGIN";

    private static LineApiClient lineApiClient;
    private LineProfile lineProfile;
    private String LINE_CHANEL_ID;
    Button btnLineLogin;
    private String lineBtnTxt;

    CallbackManager callbackManager;
    LoginButton facebookLoginButton;
    AccessTokenTracker accessTokenTracker;
    AccessToken accessToken;

    ImageButton imgBtnHome;


    AQuery aq;
    public Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        LINE_CHANEL_ID = getResources().getString(R.string.line_channel_id);
        lineBtnTxt = getResources().getString(R.string.btn_login_line);
        btnLineLogin = (Button) findViewById(R.id.imgBtnLine);
        imgBtnHome = (ImageButton) findViewById(R.id.imgBtnHome);
        imgBtnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        });
        this.facebookLoginActivityCreate();
        btnLineLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lineLoginOnClickListener(v,LINE_CHANEL_ID);
            }
        });
        try {
            LineApiClientBuilder apiClientBuilder = new LineApiClientBuilder(getApplicationContext(), LINE_CHANEL_ID);
            lineApiClient = apiClientBuilder.build();
            if(lineApiClient != null){
                LineProfile profileInfo;

               new GetProfileTask().execute();


                //lineApiClient.logout();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Activity activity = this;
        //LineAuthManager authManager = null;

    }

    private void postLineLoginExecute(LineLoginResult result){

    }

    private void lineLoginOnClickListener(View viewBtn, String lineChanelID){
        final int REQUEST_CODE = 7758;
        if(lineProfile != null){
            new LineLogout().execute();
            return;
        }
        Intent loginIntent = LineLoginApi.getLoginIntent(viewBtn.getContext(),lineChanelID);
        startActivityForResult(loginIntent, REQUEST_CODE);

    }

    private void onLineLoginResult(Intent data){
        LineLoginResult result = LineLoginApi.getLoginResultFromIntent(data);
        switch (result.getResponseCode()) {

            case SUCCESS:
                btnLineLogin.setText("Log Out");
                Log.d(TAG, "onLineLoginResult: SUCCESS"+result.getLineProfile());
                lineProfile = result.getLineProfile();
                startActivity();
                //Intent lineIntent = new Intent(context, MainActivity.class);
                //lineIntent.putExtra("line_profile", result.getLineProfile());
                //lineIntent.putExtra("line_credential", result.getLineCredential());
                //startActivity(lineIntent);
                break;

            case CANCEL:
                Log.e(TAG, "LINE Login Canceled by user!!");
                break;

            default:
                Log.e(TAG, "LINE Login FAILED!");
                Log.e(TAG, result.getErrorData().toString());
        }

    }



    private void facebookLoginActivityCreate(){
        facebookLoginButton = (LoginButton) findViewById(R.id.login_button);
        facebookLoginButton.setReadPermissions("email");
        callbackManager = CallbackManager.Factory.create();
        accessToken = AccessToken.getCurrentAccessToken();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {

                if (currentAccessToken != null) {
                    Log.d("LOGIN", "currentAccessToken "+currentAccessToken.getUserId());
                    startActivity();
                    /*GraphRequest request = GraphRequest.newMeRequest(
                            currentAccessToken,
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(
                                        JSONObject object,
                                        GraphResponse response) {
                                    // Application code
                                    Gson gosn = new Gson();
                                    Log.d("LOGIN", "GraphRequest: "+ gosn.toJson(object));

                                }
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,link");
                    request.setParameters(parameters);
                    request.executeAsync();*/
                }

                if (oldAccessToken != null) {

                    Log.d("LOGIN", "oldAccessToken LoginActivity"+oldAccessToken.getUserId());
                }
            }
        };
        facebookLoginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code

                        if(accessToken != null){
                            startActivity();
                            Log.d(TAG, " f onSuccess: "+accessToken.getUserId());
                        }
                    }

                    @Override
                    public void onCancel() {
                        // App code

                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
        if(accessToken != null){
            Log.d("LOGIN", "accessToken "+accessToken.getUserId());
        }
    }

    private void startActivity(){
        Intent intent = new Intent(context,MainActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 7758){
            this.onLineLoginResult(data);
            Log.d(TAG, "onPostExecute: requestCode "+requestCode);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
        Log.d(TAG, "onDestroy: LoginActivity");
    }

    @Override
    protected void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        Log.d(TAG, "onStop: LoginActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public class LineLogout extends AsyncTask<Void, Void, LineApiResponse<?> > {


        final static String TAG = "LOGIN";

        protected void onPreExecute(){
            // lockScreenOrientation();
        }

        protected LineApiResponse<?>  doInBackground(Void... params) {
            return lineApiClient.logout();
        }

        @Override
        protected void onPostExecute(LineApiResponse<?>  apiResponse) {

            if(apiResponse.isSuccess()) {
                btnLineLogin.setText(lineBtnTxt);
                lineProfile = null;
                Toast.makeText(getApplicationContext(), "ออกจากระบบแล้ว", Toast.LENGTH_SHORT).show();
                /*
                 // newFragment.setProfileInfo(apiResponse.getResponseData());
                public void setProfileInfo(LineProfile profileInfo) {
                    this.profileInfo = profileInfo;
                }*/

                //ProfileDialogFragment newFragment = new ProfileDialogFragment();

                // newFragment.show(getFragmentManager(), null);
                // unlockScreenOrientation();
            } else {
                Toast.makeText(getApplicationContext(), "Failed to Line Logout.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Failed to Line Logout: " + apiResponse.getErrorData().toString());
            }
        }
    }


    public class GetProfileTask extends AsyncTask<Void, Void, LineApiResponse<LineProfile>> {


        final static String TAG = "LOGIN";

        protected void onPreExecute(){
           // lockScreenOrientation();
        }

        protected LineApiResponse<LineProfile> doInBackground(Void... params) {
            return lineApiClient.getProfile();
        }

        @Override
        protected void onPostExecute(LineApiResponse<LineProfile> apiResponse) {

            if(apiResponse.isSuccess()) {
                Log.d(TAG, "onPostExecute: "+apiResponse.getResponseData().getUserId());
                btnLineLogin.setText("Log Out");
                lineProfile = apiResponse.getResponseData();
                startActivity();
                /*
                 // newFragment.setProfileInfo(apiResponse.getResponseData());
                public void setProfileInfo(LineProfile profileInfo) {
                    this.profileInfo = profileInfo;
                }*/

                //ProfileDialogFragment newFragment = new ProfileDialogFragment();

               // newFragment.show(getFragmentManager(), null);
               // unlockScreenOrientation();
            } else {
                Log.e(TAG, "Failed to get Profile: " + apiResponse.getErrorData().toString());
            }
        }
    }
}



