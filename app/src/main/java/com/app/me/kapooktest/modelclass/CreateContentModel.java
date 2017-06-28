package com.app.me.kapooktest.modelclass;

import java.util.ArrayList;

/**
 * Created by SuRiV on 7/6/2560.
 */

public class CreateContentModel {

    public CreateContentModel(){

    }

    //Use this when CreateContent For Card Content
    public CreateContentModel(String parent,int sort){
        this.parent = parent; // Parent is ObjectID Content
        this.sort = sort;
        this.category = null;
        this.step = null;
        this.content = null;
    }

    public String parent; // Parent is ObjectID Content
    public int sort; // Count Number Card +1
    public int type;
//                ---> 1: link (fetch link)
//                ---> 2: image (upload image only or upload image and text)
//                ---> 3: text (text only)
//                ---> 6: clip (fetch clip เช่น youtube)

    public int status=0;
//                ---> 1: Public
//                ---> 4: Draft

    public int content_type=0;
//                ---> 4: Entry
//                ---> 5: Post
//                ---> 6: Howto

    // ----- Media Detail ----- //
    public String img="";
    public String thumbnail="";

    public String link="";
    public String clip="";
    public String clip_title="";
    public String clip_description="";
    public String clip_provider="";
    public boolean checkin = false; // Now set Default False Because Web site is not function
    public String place_name; // If Check in false = null
    public float latitude; // If Check in false = null
    public float longitude; // If Check in false = null
    // ----- End Media Detail ----- //

    public String title="";
    public String description=""; // is use for CARD_TITLE in EntryCardModel
    public String category="";
    public ArrayList<String> content= new ArrayList<>();
    public ArrayList<Step> step = new ArrayList<>();

    //IS Step Content Picture and Image
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

    public void setMediaDescription(ImageLink imageLink){
            if(this.type == 1){
                this.link = imageLink.getOriginal_url();
            }else{
                this.clip = imageLink.getOriginal_url();
            }
            this.clip_title  = imageLink.getTitle();
            this.clip_description  = imageLink.getDescription();
            this.clip_provider  = imageLink.getProvider();
    }

    public void setMediaDescription(String img,String thumbnail){
        this.img = img;
        this.thumbnail = thumbnail;
    }

    /*public void setMediaDescription(String original_url,String title,String images,String provider,String description,boolean isClip){
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


    }*/

}
