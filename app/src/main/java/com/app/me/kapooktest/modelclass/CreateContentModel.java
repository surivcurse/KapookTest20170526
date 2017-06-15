package com.app.me.kapooktest.modelclass;

import java.util.ArrayList;

/**
 * Created by SuRiV on 7/6/2560.
 */

public class CreateContentModel {
    public int status=0;
    public int content_type=0;

    public String img="";
    public String thumbnail="";
    public String link="";
    public String clip="";
    public String clip_title="";
    public String clip_description="";
    public String clip_provider="";
    public boolean checkin = false;
    public String place_name;
    public float latitude;
    public float longitude;

    public String title="";
    public String description="";
    public String category="";
    public ArrayList<String> content= new ArrayList<>();
    public ArrayList<Step> step = new ArrayList<>();

    public static class Step{
        public String img;
        public String thumbnail;
        public String title;

        public Step(String img,String thumbnail,String title) {
            this.img = img;
            this.thumbnail = thumbnail;
            this.title = title;
        }
    }



    public void setMediaDescription(String img,String thumbnail){
        this.img = img;
        this.thumbnail = thumbnail;
    }

    public void setMediaDescription(String original_url,String title,String images,String provider,String description,boolean isClip){
        this.img = images;
        this.title = title;
        this.description = description;

        if(isClip){
            clip = original_url;
            clip_title = title;
            clip_description = description;
            clip_provider = provider;
        }else{
            link = original_url;

        }


    }

}
