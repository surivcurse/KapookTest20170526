package com.app.me.kapooktest.modelclass;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by SuRiV on 4/5/2560.
 */

public class EntryModel {
    public static long HOWTO_ID = 0;
    public static final int CONTENT_TYPE=4;
    public static boolean STATUS_CONTENT = true;
    public static Bitmap PIC_TITLE = null;
    public static int CATEGORY_ID = 1;
    public static String TITLE = "";
    public static String SUBJECT = "";
    public static String CATEGORY_OBJ = "";
    public static MediaDetail mediaDetail = new MediaDetail();

    public static ArrayList<EntryContentModel> entryContentList = new ArrayList<>();

    public static void swapContent(ArrayList arr,int fromContent,int toContent){
        arr.add(toContent,arr.get(fromContent));
        arr.remove(fromContent+1);
    }


    public static class EntryContentModel{
        private String txtContent;
        private Bitmap picContent;
        public MediaDetail mediaDetail = new MediaDetail();
        public String loadData = "";
        public ImageLink mediaLinkDetail;
        public EntryContentModel() {
            this.txtContent = "";
            this.picContent = null;
        }

        public String getTxtContent() {
            return txtContent;
        }
        public void setTxtContent(String txtContent) {
            this.txtContent = txtContent;
        }
        public Bitmap getPicContent() {
            return picContent;
        }
        public void setPicContent(Bitmap picContent) {
            this.picContent = picContent;
        }

    }

    public static ArrayList<EntryContentModel> getEntryContentList() {
        return entryContentList;
    }

    private static int getSizeEntryContent(){
        return entryContentList.size();
    }

    public static void refreshData(){
        entryContentList.clear();
        entryContentList.add(new EntryContentModel());
    }


}
