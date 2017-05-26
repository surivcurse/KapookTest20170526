package com.app.me.kapooktest.modelclass;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by SuRiV on 4/4/2560.
 */

public class PortalHomeModel {
    @SerializedName("data")
    public ArrayList<DataClass> portalHomeData;
    public class DataClass{

        public Detail detail;

        public ArrayList<Content> content;

        public class Detail{
            public int template; //1

            @SerializedName("zone_name") //"Highlight Decoration"
            public String zoneName;

            @SerializedName("zone_id") //"286"
            public String zoneID;

            @SerializedName("portal_name") //"home"
            public String portalName;

            @SerializedName("flag_view_all")  //1
            public int flagViewAll;

            public String type; //""
        }

        public class Content{

            public String slug; //"home-59224"
            public String ts; //1364199421
            public String link; //"https://home.kapook.com/view59224.html"
            public String title; //"9 แอร์เคลื่อนที่รุ่นใหม่ ทางเลือกคลายร้อน กระจายลมเย็นได้ทุกมุมบ้าน "

            @SerializedName("image")
            public ImageData imageData;

            public class ImageData{
                public String large;
                public String medium;
                public String small;
            }
        }

    }


}
