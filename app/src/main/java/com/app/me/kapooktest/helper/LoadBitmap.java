package com.app.me.kapooktest.helper;

import android.content.Intent;
import android.os.AsyncTask;

import com.app.me.kapooktest.modelclass.ImagePath;
import com.app.me.kapooktest.modelclass.MediaDetail;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.client.methods.CloseableHttpResponse;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.mime.HttpMultipartMode;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;
import cz.msebera.android.httpclient.entity.mime.content.ContentBody;
import cz.msebera.android.httpclient.entity.mime.content.FileBody;
import cz.msebera.android.httpclient.impl.client.CloseableHttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.util.EntityUtils;

/**
 * Created by SuRiV on 13/6/2560.
 */

public class  LoadBitmap  extends AsyncTask<Void, Void, Void> {
    Gson gson = new Gson();
    Intent data;
    MediaDetail mediaDetail;
    ManageFileHelper manageFileHelper;

    public LoadBitmap(Intent data,MediaDetail mediaDetail,ManageFileHelper manageFileHelper) {

        this.data = data;
        this.mediaDetail = mediaDetail;
        this.manageFileHelper = manageFileHelper;
    }

    @Override
    protected Void doInBackground(Void... params) {
        manageFileHelper.onActivityResult(data);
        return null;
    }

    @Override
    public void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        UploadFileToServer uploadFileToServer = new UploadFileToServer();
        uploadFileToServer.execute();
    }
    private class UploadFileToServer extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            return ImageUpload(manageFileHelper.getPicFile());
        }

        @Override
        protected void onPostExecute(String message) {
            ImagePath imagepath;
            imagepath = gson.fromJson(message,ImagePath.class);
            mediaDetail.setMediaDetail(imagepath);

        }

        public String ImageUpload(File picfile) {

            String uploadPicURL = "http://hilightad.kapook.com/lua/upload/entry";
            String result = "";
            String fileName = picfile.getName();

            try {
                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                CloseableHttpClient httpclient = HttpClientBuilder.create().build();
                HttpPost httpPost = new HttpPost(uploadPicURL);
                builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

                ContentType contentType = ContentType.create(manageFileHelper.getMimeType());
                ContentBody contentBody = new FileBody(picfile,contentType,fileName);

                builder.addPart("picfile",contentBody );
                HttpEntity httpEntity = builder.build();
                httpPost.setEntity(httpEntity);

                CloseableHttpResponse response = httpclient.execute(httpPost);
                result = EntityUtils.toString(response.getEntity());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
            // check the response and do what is required
        }
    }//Class UploadFile
}

