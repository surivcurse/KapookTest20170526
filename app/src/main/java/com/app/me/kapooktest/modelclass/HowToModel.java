package com.app.me.kapooktest.modelclass;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by SuRiV on 4/5/2560.
 */

public class HowToModel {

    public enum LINK_TYPE {
        WEB_LINK,
        CLIP_LINK
    }
    public static String HOWTO_ID = "";
    public static int CONTENT_TYPE = 0;
    public static int STATUS_CONTENT = 1;
    public static Bitmap PIC_TITLE = null;
    public static MediaDetail mediaDetail = new MediaDetail();
    public static int CATEGORY_ID = 1;
    public static String CATEGORY_OBJ = "";
    public static String TITLE = "";
    public static String SUBJECT = "";

    private static final ArrayList<RenderingModel> renderingModelList = new ArrayList<>(Arrays.asList(new RenderingModel()));
    private static final ArrayList<DescriptionModel> descriptionModelList = new ArrayList<>(Arrays.asList(new DescriptionModel()));

    public static void addRenderingModelToList(RenderingModel object){
       renderingModelList.add(object);
    }

    public static void addDescriptionModelToList(DescriptionModel object){
        descriptionModelList.add(object);
    }

    public static void removeRenderingModelFromList(int index){
        renderingModelList.remove(index);
    }

    public static void removeDescriptionModelFromList(int index){
        descriptionModelList.remove(index);
    }

    public static void swapContent(ArrayList arr,int fromContent,int toContent){
        if(fromContent > toContent){
            arr.add(toContent,arr.get(fromContent));
            arr.remove(fromContent+1);
        }else{
            arr.add(toContent+1,arr.get(fromContent));
            arr.remove(fromContent);
        }
    }

    public static ArrayList<String> getContentArray(){
        ArrayList<String> st = new ArrayList<>();
        for (RenderingModel renderingModel:renderingModelList) {
            st.add(renderingModel.getTxtContent());
        }
        return st;
    }

    public static ArrayList<RenderingModel> getRenderingModelList() {
        return renderingModelList;
    }

    public static ArrayList<DescriptionModel> getDescriptionModelList() {
        return descriptionModelList;
    }

    public static class RenderingModel{
        private String txtContent;
        public RenderingModel() {
            this.txtContent = "";
        }

        public RenderingModel(String txtContent) {
            this.txtContent = txtContent;
        }
        public String getTxtContent() {
            return txtContent;
        }
        public void setTxtContent(String txtContent) {
            this.txtContent = txtContent;
        }

    }


    public static class DescriptionModel{
        private String txtContent;
        private Bitmap picContent;
        public MediaDetail mediaDetail = new MediaDetail();

        public DescriptionModel() {
            this.txtContent = "";
            this.picContent = null;
        }
        /*public void setMediaDetail(ImageLink imageLink){

            mediaDetail.imgurl = imageLink.getImages().get(0).getUrl();
            mediaDetail.thumbnail = imageLink.getImages().get(0).getUrl();

            if(imageLink.getTypeembed()){
                mediaDetail.clip  = imageLink.getOriginal_url();
                mediaDetail.clip_title = imageLink.getTitle();
                mediaDetail.clip_description = imageLink.getDescription();
            }else{
                mediaDetail.linkurl = imageLink.getOriginal_url();
            }
            mediaDetail.clip_provider = imageLink.getProvider();

        }*/

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
    public static int getSizeDescription(){

        try {
            return descriptionModelList.size();
        }catch (NullPointerException nuE){
            return 0;
        }
    }

    public static int getSizeRendering(){
        try {
            return renderingModelList.size();
        }catch (NullPointerException nuE){
            return 0;
        }

    }

    public static void refreshData(){
        descriptionModelList.clear();
        descriptionModelList.add(new DescriptionModel());

        renderingModelList.clear();
        renderingModelList.add(new RenderingModel());
    }


}
