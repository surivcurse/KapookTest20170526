package com.app.me.kapooktest.helper;

import android.content.Context;
import android.util.Log;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.app.me.kapooktest.modelclass.CreateContentModel;
import com.app.me.kapooktest.modelclass.HowToModel;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import static com.app.me.kapooktest.modelclass.HowToModel.CATEGORY_ID;
import static com.app.me.kapooktest.modelclass.HowToModel.CATEGORY_OBJ;
import static com.app.me.kapooktest.modelclass.HowToModel.CONTENT_TYPE;
import static com.app.me.kapooktest.modelclass.HowToModel.STATUS_CONTENT;
import static com.app.me.kapooktest.modelclass.HowToModel.SUBJECT;
import static com.app.me.kapooktest.modelclass.HowToModel.TITLE;
import static com.app.me.kapooktest.modelclass.HowToModel.getDescriptionModelList;
import static com.app.me.kapooktest.modelclass.HowToModel.getRenderingModelList;

/**
 * Created by SuRiV on 7/6/2560.
 */

public class KapookPostContentHelper {
    private CreateContentModel contentModel;
    private Gson gson = new Gson();
    private final String postURL = "http://ts.entry.kapook.com/api/v1/entry/topic";
    private final String Appserect = "d^w,j[vdsivd]";
    private String Member_Token;
    private Context context;
    private AQuery aQuery;
    public KapookPostContentHelper(Context context,String member_token) {
        this.context = context;
        this.Member_Token = member_token;
        aQuery = new AQuery(context);
    }

    private int convertDataHowtoModel(){
        int someError = 0;

        if(HowToModel.getSizeRendering() <= 1){

            try{
                if(getRenderingModelList().get(0).getTxtContent().equals("")){
                    someError += 1;
                }
            }catch (NullPointerException nue){
                someError += 1;
            }

        }

        if(HowToModel.getSizeDescription() <= 1){

            try{
                if(getDescriptionModelList().get(0).getTxtContent().equals("") && getDescriptionModelList().get(0).getPicContent() == null){
                    someError += 2;
                }
            }catch (NullPointerException nue){
                someError += 2;
            }

        }

        if(someError > 0){
            return someError;
        }
        contentModel = new CreateContentModel();
        contentModel.setMediaDescription(HowToModel.mediaDetail.getImgurl(),HowToModel.mediaDetail.getThumbnail());
        contentModel.content_type = CONTENT_TYPE;
        contentModel.status = STATUS_CONTENT;
        contentModel.title = TITLE;
        contentModel.description = SUBJECT;
        contentModel.category = CATEGORY_OBJ;


        for (HowToModel.RenderingModel rdm : HowToModel.getRenderingModelList() ) {
            contentModel.content.add(rdm.getTxtContent());
        }

        for (HowToModel.DescriptionModel dm : HowToModel.getDescriptionModelList() ) {
            contentModel.step.add(new CreateContentModel.Step(dm.mediaDetail.getImgurl(),dm.mediaDetail.getThumbnail(),dm.getTxtContent()));
        }

        return someError;
    }
    public int postDataToServer(){
        return postDataToServer(CONTENT_TYPE);
    }
    private int postDataToServer(int content_type){
        int result = 0;
        switch (content_type){
            case 6:
                result = this.convertDataHowtoModel();
                if(result > 0){
                    Log.d("SET ERROR", "postDataToServer: "+result);
                    return result;
                }

                String json = gson.toJson(contentModel);
                Log.d("postDataToServer", "callback: "+json);
                Log.d("Member_Token", "callback: "+Member_Token);
                aQuery.ajax(postURL,JSONObject.class,new AjaxCallback<JSONObject>(){
                    @Override
                    public void callback(String url, JSONObject object, AjaxStatus status) {
                        super.callback(url, object, status);
                        Log.d("postDataToServer", "callback: "+gson.toJson(object));
                    }
                }.param("data",json).method(AQuery.METHOD_POST)
                        .header("Appserect",Appserect)
                        .header("Member-Token",Member_Token)
                        .header("Content-type","application/x-www-form-urlencoded")
                         );
                //aQuery.ajax()
                break;
        }
       return result;
    }


}
