package com.app.me.kapooktest.modelclass;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.gms.common.api.Api;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * Created by SuRiV on 4/5/2560.
 */

public class HowToModel {
    public static long HOWTO_ID = 0;
    public static boolean STATUS_CONTENT = true;
    public static Bitmap PIC_TITLE = null;
    public static int CATEGORY_ID = 1;
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

    public static ArrayList<RenderingModel> getRenderingModelList() {
        return renderingModelList;
    }

    public static ArrayList<DescriptionModel> getDescriptionModelList() {
        return descriptionModelList;
    }

    public static class RenderingModel{
        private int numberContent;
        private String txtContent;

        public RenderingModel() {
            this.txtContent = "";
            this.numberContent = getSizeRendering()+1;
        }

        public RenderingModel(String txtContent,int numberContent) {
            this.txtContent = txtContent;
            this.numberContent = numberContent;
        }

        public String getTxtContent() {
            return txtContent;
        }

        public void setTxtContent(String txtContent) {
            this.txtContent = txtContent;
        }

        public int getNumberContent() {
            return numberContent;
        }

        public void setNumberContent(int numberContent) {
            this.numberContent = numberContent;
        }

    }

    public static class DescriptionModel{
        private int numberContent;
        private String txtContent;
        private Bitmap picContent;

        public DescriptionModel() {
            this.numberContent = getSizeDescription()+1;
            this.txtContent = "";
            this.picContent = null;
        }

        public DescriptionModel(int numberContent, String txtContent, Bitmap picContent) {
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
    private static int getSizeDescription(){

        try {
            return descriptionModelList.size();
        }catch (NullPointerException nuE){
            return 0;
        }
    }

    private static int getSizeRendering(){
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
