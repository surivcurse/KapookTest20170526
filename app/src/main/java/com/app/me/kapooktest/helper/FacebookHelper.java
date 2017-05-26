package com.app.me.kapooktest.helper;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.app.me.kapooktest.R;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by SuRiV on 25/4/2560.
 */

public class FacebookHelper{
    private static final int isFacebookRequestCode = FacebookSdk.getCallbackRequestCodeOffset();
    private static final CallbackManager callbackManager = CallbackManager.Factory.create();
    private static AccessToken accessToken;
    private static final AccessTokenTracker accessTokenTracker= new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(
                AccessToken oldAccessToken,
                AccessToken currentAccessToken) {

            if (currentAccessToken != null) {
                Log.d("LOGIN", "currentAccessToken "+currentAccessToken.getUserId());
                //getGraphRequest(currentAccessToken);
                accessToken = AccessToken.getCurrentAccessToken();
            }

            if (oldAccessToken != null) {

                Log.d("LOGIN", "oldAccessToken FacebookHelper"+oldAccessToken.getUserId());
            }
        }
    };

    private static LoginButton facebookLoginButton;

    public static void loadUserProfile(AccessToken currentAccessToken, final View navigationView){
        final ArrayList<JSONObject> arrjson = new ArrayList<>();
        final TextView txtProfileName = (TextView) navigationView.findViewById(R.id.txtProfileName);
        txtProfileName.setVisibility(View.VISIBLE);
        final Button btnShowLogin = (Button) navigationView.findViewById(R.id.btnShowLogin);
        if(currentAccessToken != null){
            GraphRequest request = GraphRequest.newMeRequest(
                    currentAccessToken,
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {
                            // Application code
                            Gson gson = new Gson();

                            try {
                                txtProfileName.setText(object.getString("name").toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            btnShowLogin.setText("SIGN OUT");

                            arrjson.add(object);
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,link");
            request.setParameters(parameters);
            request.executeAsync();

        }

    }

    public static void logout(){
        LoginManager.getInstance().logOut();
        accessTokenStopTracker();
        AccessToken.setCurrentAccessToken(null);
        AccessToken.setCurrentAccessToken(null);
        accessToken = AccessToken.getCurrentAccessToken();
        if(accessToken == null){
            Log.d("LOGIN", "logout: NULLLLLLLNULLLLLLLNULLLLLLLNULLLLLLLNULLLLLLL ");
        }
    }

    public static void loadProfilePicture(AccessToken currentAccessToken,View navigationView){
        final ImageView imgProfile = (ImageView) navigationView.findViewById(R.id.imgProfile);
        final AQuery aq = new AQuery(navigationView);
        final Bitmap profilePic = null;
        if(currentAccessToken != null){
            GraphRequest request = GraphRequest.newMeRequest(
                    currentAccessToken,
                    new GraphRequest.GraphJSONObjectCallback(){
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            if (response != null) {
                                try {

                                    if (object.has("picture")) {
                                        String profilePicUrl = object.getJSONObject("picture").getJSONObject("data").getString("url");

                                        aq.id(imgProfile).image(profilePicUrl,true,true);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "picture.type(normal){url}");
            request.setParameters(parameters);
            request.executeAsync();


        }

    }

    public static AccessToken getAccessToken() {
        accessToken = AccessToken.getCurrentAccessToken();
        return accessToken;
    }

    public static CallbackManager getCallbackManager() {
        return callbackManager;
    }

    public static LoginButton connectFacebookLoginButton(View root){
        facebookLoginButton = (LoginButton) root.findViewById(R.id.login_button);
        facebookLoginButton.setReadPermissions("email");
        facebookLoginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code

                        if(accessToken != null){
                            Log.d("LoginFace", " f onSuccess: "+accessToken.getUserId());
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

        return facebookLoginButton;
    }


    public static void accessTokenStopTracker(){
        accessTokenTracker.stopTracking();
    }

    public static boolean isFacebookRequestCode(int requestCode){
        return FacebookSdk.isFacebookRequestCode(requestCode);
    }
}
