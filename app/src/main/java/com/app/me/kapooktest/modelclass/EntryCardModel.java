package com.app.me.kapooktest.modelclass;

import static com.app.me.kapooktest.modelclass.ConstantModel.KapookPostContent.CURRENT_USER;

/**
 * Created by SuRiV on 5/6/2560.
 */

public class EntryCardModel {
    //public static String PARENT_ID;

    public static String CONTENT_ID;
    public static final int CONTENT_TYPE=4;
    public static int TYPE;
//                ---> 1: link (fetch link)
//                ---> 2: image (upload image only or upload image and text)
//                ---> 3: text (text only)
//                ---> 6: clip (fetch clip เช่น youtube)

    public static final int STATUS = 1;
    public static int SORT;

    public static MediaDetail IMAGE_DETAILS = new MediaDetail(); //
    public static ImageLink LINK_DETAILS; // LINK_DETAILS และ IMAGE_DETAILS จะมีพร้อมกันไม่ได้ สำคัญมาก !!
    public static String CARD_TEXT;

    public static void initTypeContent(){
        if(LINK_DETAILS != null){
            if(LINK_DETAILS.getTypeembed()){
                TYPE = 6;
            }else{
                TYPE = 1;
            }
            return;
        }

        if(IMAGE_DETAILS.getImgurl().length() > 0){
            TYPE = 2;
            return;
        }
        if(CARD_TEXT.length() > 0){
            TYPE = 3;
            return;
        }
    }

    public static void resetModel(){

    }

}
