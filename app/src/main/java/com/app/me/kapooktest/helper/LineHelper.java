package com.app.me.kapooktest.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.app.me.kapooktest.LoginActivity;
import com.app.me.kapooktest.R;
import com.linecorp.linesdk.LineApiResponse;
import com.linecorp.linesdk.LineProfile;
import com.linecorp.linesdk.api.LineApiClient;
import com.linecorp.linesdk.api.LineApiClientBuilder;
import com.linecorp.linesdk.auth.LineLoginApi;
import com.linecorp.linesdk.auth.LineLoginResult;

/**
 * Created by SuRiV on 26/4/2560.
 */

public class LineHelper  {
    public static final int LINE_REQUEST_CODE = 7758;
    public static final String LINE_CHANEL_ID = "1510059629";
    private String TAG = "LOGIN";
    private static LineApiClient lineApiClient;
    private static LineProfile lineProfile;
    public static ImageView imgProfile;
    public static TextView txtProfile;
    public static AQuery aq;
    public static Button btnShowLogin;

    //public static String profileName;

    private void onLineLoginResult(Intent data){
        LineLoginResult result = LineLoginApi.getLoginResultFromIntent(data);
        switch (result.getResponseCode()) {

            case SUCCESS:
                //btnLineLogin.setText("Log Out");
                Log.d(TAG, "onLineLoginResult: SUCCESS"+result.getLineProfile());
                //lineProfile = result.getLineProfile();
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

    public static void lineLoginOnClickListener(View v, FragmentActivity fragmentActivity){

        if(lineApiClient != null){
            new LineLogout().execute();
            return;
        }
        Intent loginIntent = LineLoginApi.getLoginIntent(v.getContext(),LINE_CHANEL_ID);
        fragmentActivity.startActivityForResult(loginIntent, LINE_REQUEST_CODE);
    }

    public static void lineLogout(){
        if(lineProfile != null){
            new LineLogout().execute();
            return;
        }
    }

    public static class LineLogout extends AsyncTask<Void, Void, LineApiResponse<?> > {


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

                lineProfile = null;

            } else {
               // Toast.makeText(getApplicationContext(), "Failed to Line Logout.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Failed to Line Logout: " + apiResponse.getErrorData().toString());
            }
        }
    }

    public static void loadProfileUser(Context context,View navigationView){

        txtProfile = (TextView) navigationView.findViewById(R.id.txtProfileName);
        btnShowLogin = (Button) navigationView.findViewById(R.id.btnShowLogin);
        imgProfile = (ImageView) navigationView.findViewById(R.id.imgProfile);
        aq = new AQuery(navigationView);
        txtProfile.setVisibility(View.VISIBLE);
        try {
            LineApiClientBuilder apiClientBuilder = new LineApiClientBuilder(context, LINE_CHANEL_ID);
            lineApiClient = apiClientBuilder.build();
            if(lineApiClient != null){

                new GetProfileTask().execute();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static class GetProfileTask extends AsyncTask<Void, Void, LineApiResponse<LineProfile>> {

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
                lineProfile = apiResponse.getResponseData();
                txtProfile.setText(lineProfile.getDisplayName());
                aq.id(imgProfile).image(lineProfile.getPictureUrl().toString(),true,true);
                btnShowLogin.setText("SIGN OUT");


            } else {
                Log.e(TAG, "Failed to get Profile: " + apiResponse.getErrorData().toString());
            }
        }
    }

    public static boolean checkStatusLineLogin(Context context){
        LineApiClientBuilder apiClientBuilder = new LineApiClientBuilder(context, LINE_CHANEL_ID);
        lineApiClient = apiClientBuilder.build();
        return lineApiClient.getCurrentAccessToken() != null;
    }
}
