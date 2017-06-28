package com.app.me.kapooktest.modelclass;

import android.graphics.Bitmap;

import static com.app.me.kapooktest.modelclass.ConstantModel.KapookPostContent.*;

/**
 * Created by SuRiV on 5/6/2560.
 */

public class HowtoCardModel {
    //public static String PARENT_ID;

    public static String CONTENT_NAME;
    public static String CONTENT_ID;
    public static int CONTENT_TYPE=6;
    public static int TYPE;
//                ---> 1: link (fetch link)
//                ---> 2: image (upload image only or upload image and text)
//                ---> 3: text (text only)
//                ---> 6: clip (fetch clip เช่น youtube)


    public static int STATUS = 1;
    public static int SORT;
    public static String CONTENT_SUBTITLE;
    public static Bitmap BM_CONTENT_TITLE;

    public static String PROFILE_PIC = CURRENT_USER == null ? null : CURRENT_USER.getString("avatar") ;
    public static String PROFILE_NAME = CURRENT_USER == null ? "User" : CURRENT_USER.getString("display") ;
    public static MediaDetail IMAGE_DETAILS = new MediaDetail();
    public static Bitmap BM_CARD_TITLE;
    public static String CARD_TEXT;

    public static void initTypeContent(){
        if(IMAGE_DETAILS.getImgurl().length() > 0){
            TYPE = 2;
            return;
        }
        if(CARD_TEXT.length() > 0){
            TYPE = 3;
            return;
        }
    }

}
