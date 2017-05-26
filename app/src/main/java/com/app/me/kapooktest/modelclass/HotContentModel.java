package com.app.me.kapooktest.modelclass;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SuRiV on 4/4/2560.
 */

public class HotContentModel {

    @SerializedName("zone_id")
    public int zoneID;          // 129
    @SerializedName("id")
    public long ID;              // 168980

    public String img;          // "https://my.thaibuffer.com/r/320/auto/imagescontent/fb_img/487/s_168980_9301.jpg"
    public String linkAbs;      // "http://home.kapook.com"
    @SerializedName("link")
    public String linkHtml;         // "http://home.kapook.com/view168980.html"

    @SerializedName("cd")
    public String createDate;   // "2017-04-04 17:20:08"

    public String subject;      // "ส่องอีกครั้ง บ้านเจ เจตริน สร้างเสร็จแล้ว 50 ล้าน สระน้ำ - สนามกอล์ฟ มาครบ",
    public String title;        // "บ้านเจ เจตริน สร้างเสร็จพร้อมอยู่ มาครบทุกความต้องการ สระน้ำ สนามกอล์ฟขนาดย่อม สวนสวย น่าอยู่สุด ๆ",
    @SerializedName("portal_id")
    public int portalID;       // 17,
    public int views;           // 14189,
    public int likes;           // 0,
    public String review;       // "no",
    public int comments;        // 0

}
