package com.app.me.kapooktest.modelclass;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by SuRiV on 4/5/2560.
 */

public class EntryModel {
    public static long HOWTO_ID = 0;
    public static boolean STATUS_CONTENT = true;
    public static Bitmap PIC_TITLE = null;
    public static int CATEGORY_ID = 1;
    public static String TITLE = "";
    public static String SUBJECT = "";

    public static ArrayList<EntryContentModel> entryContentList = new ArrayList<>();

    public static void swapContent(ArrayList arr,int fromContent,int toContent){
        arr.add(toContent,arr.get(fromContent));
        arr.remove(fromContent+1);
    }


    public static class EntryContentModel{
        private int numberContent;
        private String txtContent;
        private Bitmap picContent;

        public EntryContentModel() {
            this.numberContent = getSizeEntryContent()+1;
            this.txtContent = "";
            this.picContent = null;
        }

        public EntryContentModel(int numberContent, String txtContent, Bitmap picContent) {
            this.numberContent = numberContent;
            this.txtContent = txtContent;
            this.picContent = picContent;
        }

        public int getNumberContent() {
            return numberContent;
        }

        public void setNumberContent(int numberContent) {
            this.numberContent = numberContent;
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
