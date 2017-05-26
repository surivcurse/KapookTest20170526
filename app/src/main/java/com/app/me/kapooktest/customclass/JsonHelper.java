package com.app.me.kapooktest.customclass;

import android.os.AsyncTask;
import android.util.Log;

import com.app.me.kapooktest.modelclass.ConstantModel;
import com.app.me.kapooktest.modelclass.NewContentModel;
import com.app.me.kapooktest.modelclass.PortalHomeModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.HttpStatus;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.CloseableHttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;

/**
 * Created by SuRiV on 4/4/2560.
 */

public class JsonHelper {

    public ConstantModel.TabsModel getModel() {
        return TabsModel;
    }

    public void setModel(ConstantModel.TabsModel model) {
        this.TabsModel = model;
    }

    private ConstantModel.TabsModel TabsModel;

    public JsonHelper(ConstantModel.TabsModel model){
        this.TabsModel = model;

    }

    public Object runGetJson(){

        return new asyncGetJson().execute(TabsModel);
    }

    class asyncGetJson extends AsyncTask<ConstantModel.TabsModel, Void, Object> {

        @Override
        protected Object doInBackground(ConstantModel.TabsModel[] params) {
            String url = params[0].getJsonUrl();
            Class model = params[0].getModelClass();
            boolean isArraylistClass = params[0].getIsArraylistClass();
            // do above Server call here
            return gsonParse(loadJson(url),model,isArraylistClass);
        }

        @Override
        protected void onPostExecute(Object result){

        }

    }

    public Object gsonParse(JsonElement json, Class model,boolean isArraylistClass) {

        Gson gson = new GsonBuilder().create();
        Log.d("Model ",model.getName());
        Log.d("Json",json.toString());
        try {
            if(isArraylistClass) {
                Object[] result = (Object[]) java.lang.reflect.Array.newInstance(model, 1);
                //Type type = new TypeToken<ArrayList<NewContentModel>>() {}.getType();
                //ArrayList<NewContentModel> result;
                result = gson.fromJson(json, result.getClass());//result.getClass()


                Log.d("Object Json ",gson.toJson(result));
                return result;
            }else {
                Object result = model;
                result = gson.fromJson(json, model);

                PortalHomeModel portal = new PortalHomeModel();
                portal = (PortalHomeModel)result;
                //Log.d("Object Json ",portal.dataClass.get(0).detail.zoneName);
                return result;
            }

        }catch (com.google.gson.JsonParseException jpEx) {
            Log.w(getClass().getSimpleName(), "Error for JsonParse " + model.getName(), jpEx);
        }

        return null;

    }

    private JsonElement loadJson(String url) {
        HttpGet getRequest = new HttpGet(url);
        getRequest.addHeader("content-type","application/json");

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try{

            HttpResponse getResponse = httpClient.execute(getRequest);
            final int statusCode = getResponse.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                Log.w(getClass().getSimpleName(),
                        "Error " + statusCode + " for URL " + url);
                return null;
            }

            HttpEntity getResponseEntity = getResponse.getEntity();

            InputStream source = getResponseEntity.getContent();
            Reader reader = new InputStreamReader(source);
            JsonElement elem = new JsonParser().parse(reader);
            source.close();
            reader.close();
            return elem;

        } catch (IOException e) {
            getRequest.abort();
            Log.w(getClass().getSimpleName(), "Error for URL " + url, e);

        }
        return null;
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
