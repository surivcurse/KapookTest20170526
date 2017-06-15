package com.app.me.kapooktest.modelclass;

import android.graphics.Bitmap;

import static com.app.me.kapooktest.modelclass.ConstantModel.KapookPostContent.*;

/**
 * Created by SuRiV on 5/6/2560.
 */

public class HowToCardModel {
    public static String CONTENT_NAME;
    public static String CONTENT_ID;
    public static int STATUS;
    public static String CONTENT_SUBTITLE;
    public static Bitmap BM_CONTENT_TITLE;

    public static String PROFILE_PIC = CURRENT_USER == null ? null : CURRENT_USER.getString("avatar") ;
    public static String PROFILE_NAME = CURRENT_USER == null ? "User" : CURRENT_USER.getString("display") ;
    public static MediaDetail IMAGE_DETAILS;
    public static Bitmap BM_CARD_TITLE;
    public static String CARD_TEXT;

}
