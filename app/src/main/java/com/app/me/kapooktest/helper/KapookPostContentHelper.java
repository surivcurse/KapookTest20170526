package com.app.me.kapooktest.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.app.me.kapooktest.modelclass.CreateContentModel;
import com.app.me.kapooktest.modelclass.EntryCardModel;
import com.app.me.kapooktest.modelclass.EntryModel;
import com.app.me.kapooktest.modelclass.HowtoCardModel;
import com.app.me.kapooktest.modelclass.HowToModel;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

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

    public int postCardDataToServer(){
        int result;
            result = this.convertDataCardModel();
        String url = "http://ts.entry.kapook.com/api/v1/entry/card";
            this.ajaxPostContentModel(contentModel,url,contentModel.content_type);

        return result;
    }

    private int postEntryCardDataToServer(){
        int result;
        result = this.convertDataEntryCardModel();
        String url = "http://ts.entry.kapook.com/api/v1/entry/card";
        this.ajaxPostContentModel(contentModel,url,contentModel.content_type);

        return result;
    }

    private int convertDataEntryModel(){
        int someError = 0;
        int lessSize = 1;

        if(EntryModel.getEntryContentList().size() <= lessSize){

            try{
                if(EntryModel.getEntryContentList().get(0).getTxtContent().equals("")){
                    someError += lessSize;
                }
            }catch (NullPointerException nue){
                someError += lessSize;
            }

        }

        if(someError > 0){
            return someError;
        }
        contentModel = new CreateContentModel();
        contentModel.setMediaDescription(HowToModel.mediaDetail.getImgurl(),HowToModel.mediaDetail.getThumbnail());
        contentModel.content_type = EntryModel.CONTENT_TYPE;
        if(EntryModel.STATUS_CONTENT){
            contentModel.status = 1;
        }else{
            contentModel.status = 4;
        }

        contentModel.title = EntryModel.TITLE;
        contentModel.description = EntryModel.SUBJECT;
        contentModel.category = EntryModel.CATEGORY_OBJ;

        return someError;
    }


    private int convertDataEntryCardModel(){
        int lessText = 1;
        int nullContentId = 2;
        if(EntryCardModel.CONTENT_ID.equals("")){
            return nullContentId;
        }
        contentModel = new CreateContentModel(EntryCardModel.CONTENT_ID, EntryCardModel.SORT);

        if(EntryCardModel.IMAGE_DETAILS.getImgurl().length() < lessText && EntryCardModel.LINK_DETAILS == null && EntryCardModel.CARD_TEXT.length() < lessText){
            return lessText;
        }
        contentModel.status = EntryCardModel.STATUS;
        contentModel.content_type = EntryCardModel.CONTENT_TYPE;
        contentModel.description = EntryCardModel.CARD_TEXT;
        contentModel.type = EntryCardModel.TYPE;
        contentModel.checkin = false;

        if(contentModel.type == 1 || contentModel.type==6){
            contentModel.setMediaDescription(EntryCardModel.LINK_DETAILS);
        }

        if(contentModel.type == 2){
            contentModel.setMediaDescription(EntryCardModel.IMAGE_DETAILS.getImgurl(),EntryCardModel.IMAGE_DETAILS.getThumbnail());
        }

        return 0;
    }

    private int convertDataCardModel(){

        int lessText = 1;
        int nullContentId = 2;
        if(HowtoCardModel.CONTENT_ID.equals("")){
            return nullContentId;
        }

        contentModel = new CreateContentModel(HowtoCardModel.CONTENT_ID, HowtoCardModel.SORT);

        if(HowtoCardModel.CARD_TEXT.length() < lessText && HowtoCardModel.IMAGE_DETAILS.getImgurl().length() < lessText){
            return lessText;
        }else{
            contentModel.status = HowtoCardModel.STATUS;
            contentModel.content_type = HowtoCardModel.CONTENT_TYPE;
            contentModel.title = HowtoCardModel.CARD_TEXT;
            contentModel.setMediaDescription(HowtoCardModel.IMAGE_DETAILS.getImgurl(), HowtoCardModel.IMAGE_DETAILS.getThumbnail());
            HowtoCardModel.initTypeContent();
            contentModel.type = HowtoCardModel.TYPE;
        }

        return 0;
    }

    private int convertDataHowtoModel(){
        int someError = 0;
        int lessSize = 1;

        if(HowToModel.getSizeRendering() <= lessSize){

            try{
                if(getRenderingModelList().get(0).getTxtContent().equals("")){
                    someError += lessSize;
                }
            }catch (NullPointerException nue){
                someError += lessSize;
            }

        }

        if(HowToModel.getSizeDescription() <= lessSize){

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
        contentModel.content_type = HowToModel.CONTENT_TYPE;
        contentModel.status = HowToModel.STATUS_CONTENT;
        contentModel.title = HowToModel.TITLE;
        contentModel.description = HowToModel.SUBJECT;
        contentModel.category = HowToModel.CATEGORY_OBJ;


        for (HowToModel.RenderingModel rdm : HowToModel.getRenderingModelList() ) {
            contentModel.content.add(rdm.getTxtContent());
        }

        for (HowToModel.DescriptionModel dm : HowToModel.getDescriptionModelList() ) {
            contentModel.step.add(new CreateContentModel.Step(dm.mediaDetail.getImgurl(),dm.mediaDetail.getThumbnail(),dm.getTxtContent()));
        }

        return someError;
    }
    public int postEntryDataToServer(){
        return postDataToServer(EntryModel.CONTENT_TYPE);
    }
    public int postDataToServer(){
        return postDataToServer(HowToModel.CONTENT_TYPE);
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

                this.ajaxPostContentModel(contentModel,content_type);

                //aQuery.ajax()
                break;
            case 4:
                result = this.convertDataEntryModel();
                if(result > 0){
                    Log.d("SET ERROR", "postDataToServer: "+result);
                    return result;
                }

                this.ajaxPostContentModel(contentModel,CONTENT_TYPE);

                //aQuery.ajax()
                break;
        }
       return result;
    }

    private void ajaxPostContentModel(CreateContentModel contentModel,String postURL,int content_type){
        String json = gson.toJson(contentModel);
        Log.d("postDataToServer", "callback: "+json);
        Log.d("Member_Token", "callback: "+Member_Token);
        aQuery.ajax(postURL,JSONObject.class,new AjaxCallback<JSONObject>(){
                    @Override
                    public void callback(String url, JSONObject object, AjaxStatus status) {
                        super.callback(url, object, status);
                        try {
                            if(object.get("status").toString() == "200"){
                                showDialogResultPost("เพิ่มข้อมูลเรียบร้อย");
                            }else{
                                showDialogResultPost(object.get("message").toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d("postDataToServer", "callback: "+gson.toJson(object));
                    }
                }.param("data",json).method(AQuery.METHOD_POST)
                        .header("Appserect",Appserect)
                        .header("Member-Token",Member_Token)
                        .header("Content-type","application/x-www-form-urlencoded")
        );

    }

    private void ajaxPostContentModel(CreateContentModel contentModel,int content_type){

        ajaxPostContentModel(contentModel,postURL,content_type);
    }

    public void ajaxDeleteContent(final String object_id){

        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setTitle("ยืนยันลบข้อมูลนี้ ?");
        adb.setMessage("");
        adb.setNegativeButton("NO", null);
        adb.setPositiveButton("YES", new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                // TODO Auto-generated method stub
                aQuery.ajax(postURL+"/"+object_id,JSONObject.class,new AjaxCallback<JSONObject>(){
                            @Override
                            public void callback(String url, JSONObject object, AjaxStatus status) {
                                super.callback(url, object, status);

                                Log.d("postDataToServer", "callback: "+gson.toJson(object));
                                try {
                                    if(object.get("status").toString() == "200"){
                                        showDialogResultPost("ลบข้อมูลเรียบร้อย");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }.method(AQuery.METHOD_DELETE)
                                .header("Appserect",Appserect)
                                .header("Member-Token",Member_Token)
                );
            }
        });
        adb.show();


    }

    private String setMessageResult(int ErrorCode){
        return "";
    }

    private void showDialogResultPost(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        //Show Activity ...
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


}
