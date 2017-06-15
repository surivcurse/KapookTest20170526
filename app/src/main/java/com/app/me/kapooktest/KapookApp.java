package com.app.me.kapooktest;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseConfig;
import com.parse.ParseObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;


/**
 * Created by SuRiV on 6/6/2560.
 */

public class KapookApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        /*Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);*/

        //ParseObject.registerSubclass(LoginActivity.class);
        Parse.enableLocalDatastore(this);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.parse_app_id))
                .server(getString(R.string.parse_server_url))
                .build());



        Log.d("Test", "onCreate: " );
    }


}
