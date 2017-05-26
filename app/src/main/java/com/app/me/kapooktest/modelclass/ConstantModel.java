package com.app.me.kapooktest.modelclass;

import android.content.Context;

import com.app.me.kapooktest.helper.TabsSqLiteHandle;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import static com.app.me.kapooktest.R.drawable.*;

/**
 * Created by SuRiV on 4/4/2560.
 */

public class ConstantModel {
    //private TabsSqLiteHandle datasource = new TabsSqLiteHandle();
    public static int TO_INTENT = 0;
    private static final ArrayList<CategoryModel> DEFAULT_CATEGORY = new ArrayList<>(Arrays.asList(
            new CategoryModel(1,ic_travel,"ท่องเที่ยว / ทริป")
            ,new CategoryModel(2,ic_eat,"กินดื่ม")
            ,new CategoryModel(3,ic_beauty,"แต่งหน้า")
            ,new CategoryModel(4,ic_hair,"ทำผม")
            ,new CategoryModel(5,ic_cooking,"ทำอาหาร / ขนม")
            ,new CategoryModel(6,ic_body,"แฟชั่น")
            ,new CategoryModel(7,ic_fitness,"ฟิตเนส")
            ,new CategoryModel(8,ic_camera,"ถ่ายรูป")
            ,new CategoryModel(9,ic_phone,"มือถือ")
            ,new CategoryModel(10,ic_car,"รถยนต์")
            ,new CategoryModel(11,ic_manage_home,"แต่งบ้านจัดสวน")
            ,new CategoryModel(12,ic_pets,"สัตว์เลี้ยง")
            ,new CategoryModel(13,ic_public,"อื่นๆ")
        )
    );

    public static CategoryModel getCategoryByID(int categoryID){
        CategoryModel categoryModel = null;
        for (CategoryModel cm : DEFAULT_CATEGORY){
            if(cm.getId() == categoryID){
                categoryModel = cm;
            }
        }
        return categoryModel;
    }


    //Default Start Value
    private static final ArrayList<TabsModel> DEFAULT_TABS = new ArrayList<>(Arrays.asList(
                    //Link 1
                    new TabsModel(PortalHomeModel.class,"หน้าแรก", "http://kapi.kapook.com/api_lite/api_lite.php?portal=home",false, ic_home_black_24dp)
                    //Link 2
                    ,new TabsModel(NewContentModel.class,"มาใหม่", "https://mportalapi.kapook.com/content_sql/list/17/40/",true,ic_update_black)
                    //Link 3
                    ,new TabsModel(HotContentModel.class,"มาแรง", "https://mportalapi.kapook.com/content_sql/list/17/30/?sortby=views",true,ic_trending_up_black)
                )
            );

    private static final ArrayList<TabsModel> CUSTOM_TABS = new ArrayList<>();

    private static final ArrayList<TabsModel> ALL_TABS = new ArrayList<>();

    public static boolean IS_Tabs_CHANGE = false;

    public static class TabsModel{
        private Class modelClass;
        private long _id;
        private String nameTabs;
        private String jsonUrl;
        private boolean isArraylistClass;
        private int icon;

        public TabsModel(Class modelClass, String nameTabs, String jsonUrl, boolean isArraylistClass, int icon){
            this.modelClass = modelClass;
            this.nameTabs = nameTabs;
            this.jsonUrl = jsonUrl;
            this.isArraylistClass = isArraylistClass;
            this.icon = icon;
        }

        public TabsModel(Class modelClass,String nameTabs,String jsonUrl,boolean isArraylistClass){
            this.modelClass = modelClass;
            this.nameTabs = nameTabs;
            this.jsonUrl = jsonUrl;
            this.isArraylistClass = isArraylistClass;
            this.icon = ic_bookmark_black;
        }

        public TabsModel(int _id, String _name) {
            this._id = _id;
            this.nameTabs = _name;
            this.modelClass = null;
            this.jsonUrl = null;
            this.isArraylistClass = false;
            this.icon = ic_bookmark_black;
        }


        public Class getModelClass(){
            return this.modelClass;
        }

        public String getNameTabs(){
            return this.nameTabs;
        }

        public String getJsonUrl(){
            return this.jsonUrl;
        }

        public boolean getIsArraylistClass(){
            return isArraylistClass;
        }

        public long getID() {
            return _id;
        }

        public void setID(long _id) {
            this._id = _id;
        }

        public int getIcon(){
            return icon;
        }

    }

    public static class CategoryModel{
        private int id;
        private int icon;
        private String title;

        public CategoryModel(int id,int icon,String title){
            setId(id);
            setIcon(icon);
            setTitle(title);
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }


    }

    public static ArrayList<CategoryModel> getCategoryModel(){
        return DEFAULT_CATEGORY;
    }

    public static int getSizeListTabs(){
        return ALL_TABS.size();
    }

    public static ArrayList<String> getListTabsKey(){
        ArrayList<String> keyList = new ArrayList<String>();

        for (TabsModel x : ALL_TABS) {
            keyList.add(x.getNameTabs());
        }

        return keyList;
    }

    /*public static String getTabsJsonUrl(int index){
        return DEFAULT_TABS.get(index).getJsonUrl();
    }*/

    /*public static Class getTabsModelClass(int index){
        return DEFAULT_TABS.get(index).getModelClass();
    }*/

    public static TabsModel getTabsModel(int index){
        return ALL_TABS.get(index);
    }

    public static TabsModel getCustomTab(int index){
        return  CUSTOM_TABS.get(index);
    }

    public static void setCustomTab(ArrayList<TabsModel> arrTabs){
        CUSTOM_TABS.clear();
        CUSTOM_TABS.addAll(arrTabs);
    }

    public static void addCustomTab(TabsModel tab){
        IS_Tabs_CHANGE = true;
        CUSTOM_TABS.add(tab);
    }

    public static ArrayList<TabsModel> getAllTabs(){

        return ALL_TABS;
    }

    public static int getSizeCustomTab(){
        return CUSTOM_TABS.size();
    }

    public static void removeCustomTab(int index){
        IS_Tabs_CHANGE = true;
        CUSTOM_TABS.remove(index);
    }

    public static void refreshTabs(Context context){
        String TAG = "ALL_TABS";
        if(ALL_TABS.size() > 0){
            ALL_TABS.clear();
        }

        ALL_TABS.addAll(DEFAULT_TABS);
        if (CUSTOM_TABS.size() > 0) {

            ALL_TABS.addAll(CUSTOM_TABS);
        }else{

            TabsSqLiteHandle datasource = new TabsSqLiteHandle(context);
            CUSTOM_TABS.addAll(datasource.getAllTabs());
            ALL_TABS.addAll(CUSTOM_TABS);
        }
        IS_Tabs_CHANGE = false;

    }
}
