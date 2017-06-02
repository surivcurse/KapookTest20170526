package com.app.me.kapooktest.modelclass
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
/**
 * Created by SuRiV on 26/5/2560.
 */
data class EntryViewContent(
        @SerializedName("message") var message:String?="",
        @SerializedName("status") var status:Int?=0,
        @SerializedName("total") var total:Int?=0,
        @SerializedName("next") var next:String?="",
        @SerializedName("prev") var prev:String?="",
        @SerializedName("info") var data_info:DataDetail?=null,
        @SerializedName("data") var data:ArrayList<ContentDetail>?=null

){

    data class ContentDetail(
            @SerializedName("_id") var _id:String, //"c0b3f9f0ebd1"
            @SerializedName("title") var title:String, //""
            @SerializedName("description") var description:String, //"เริ่มต้นบนเครื่องบินเล็ก มีใบพัด สั่นมากกกก เอาซะเครื่องในรวมกันหมดเลย"
            @SerializedName("type") var type:Int, //2
            @SerializedName("parent") var parent: String, //"c07b5af7c729"
            @SerializedName("status") var status:Int, //1
            @SerializedName("sort") var sort:Int, // 1 2 3 4
            @SerializedName("views") var views:Int,//0
            @SerializedName("createAt") var createAt:String, //"2017-03-08T08:32:51Z"
            @SerializedName("createAt_ts") var createAt_ts:Long, //1488961971
            @SerializedName("updateAt") var updateAt:String, //"2017-03-08T08:32:51Z"
            @SerializedName("updateAt_ts") var updateAt_ts:Long, //1488961971
            @SerializedName("detail_user") var detail_user:DetailUser,
            @SerializedName("media") var media:DataMedia,
            @SerializedName("myposition") var myposition:MyPosition,
            @SerializedName("parent_content_type") var parent_content_type:Int,
            @SerializedName("slug") var slug:String,
            @SerializedName("link") var link:String
    )

}

data class EntryViewModel(

        var message:String?="", //"message": ""
        var status:Int?=0,
        var total:Int?=0,
        var next:String="",
        var prev:String?="",
        var post:StatusContent?=null,
        var howto:StatusContent?=null,
        var entry:StatusContent?=null,
        var detail_user:DetailUser?=null,
        @SerializedName("data")
        var data:ArrayList<DataDetail>?=null
){
    data class StatusContent(
            @SerializedName("publish")
            var publish:Int,
            @SerializedName("draft")
            var draft:Int
    )
}

data class DetailUser(
        var objectId:String, //"fd3dfcb12c16"
        var avatar:String, //"https://graph.facebook.com/100001129486307/picture?type=square&width=250&height=250"
        var display:String, //"Bank Pachara"
        @SerializedName("email")
        var email: String, //"neo_2265@hotmail.com"
        var facebookId: String, //"100001129486307"
        var username: String, //"neo_2265@hotmail.com"
        var screenname: String //"fb_100001129486307"
)

data class DataDetail(
        var _id:String, //"c07b5af7c729",
        var createAt:String, //"2017-03-08T08:28:15Z"
        var createAt_ts:Long, //1488961695
        var updateAt:String, //"2017-05-26T06:57:59Z",
        var updateAt_ts:Long, //1495781879
        var status:Int, //1
        var cat:Cat,
        var slug:String, //"review-c07b5af7c729"
        var views:Int,
        var detail_user:DetailUser,
        var title:String, //"รีวิว : ทริปเชียงคาน - ภูเรือ"
        var description:String, //"นึกอยากจะไปก็ไปกัน 2 คน ซัก 3 วัน 2 คืน หมดทริปนี้ไปเกือบหมื่น "
        var media:DataMedia,
        @SerializedName("content") @Expose var content_list: ArrayList<String>,
        @SerializedName("step") var step:List<Step>?=ArrayList<Step>(),
        var content_type:Int,  //4
        var number_card:Int, //0
        @SerializedName("link")
        var link:String, //"http://member.kapook.com/entry/c07b5af7c729",
        var myposition:MyPosition

) {
    data class Cat(
            @SerializedName("objectId")
            var objectId:String, //"eb69171dd430"
            @SerializedName("name")
            var name:String, //"ท่องเที่ยว / ทริป"
            @SerializedName("status")
            var status:Int, //1
            @SerializedName("icon")
            var icon:String, //"beach_access"
            @SerializedName("link_icon")
            var link_icon:String, //""
            @SerializedName("sort")
            var sort:Int, //1 2 3 4
            @SerializedName("createAt")
            var createAt:String, //"2016-12-28T09:14:12Z"
            @SerializedName("updateAt")
            var updateAt:String, //"2016-12-28T09:14:12Z"
            @SerializedName("detail_howto")
            var detail_howto:DetailHowto
    ){
        data class DetailHowto(
                @SerializedName("header_content")
                var header_content:String, //"ค่าใช้จ่าย"
                @SerializedName("holder_content")
                var holder_content:String, //"ใส่ค่าใช้จ่ายในการเดินทาง"
                @SerializedName("header_step")
                var header_step:String, //"วิธีการเดินทาง"
                @SerializedName("holder_step")
                var holder_step:String //"เขียนขั้นตอน/วิธีการเดินทาง"
        )
    }

    data class Step(
            @SerializedName("title") var title:String?=null,
            @SerializedName("img") var img:String?=null,
            @SerializedName("thumbnail") var thumbnail:String?=null)
}


data class DataMedia(
        @SerializedName("img") var img:String, //"/entry/4a58d583-7e0c-42b4-acee-a5c4c584c1f6.jpg"
        @SerializedName("thumbnail") var thumbnail:String, //"/entry/thumb_4a58d583-7e0c-42b4-acee-a5c4c584c1f6.jpg"
        @SerializedName("clip") var clip:String,
        @SerializedName("rec") var rec:String,
        @SerializedName("checkin") var checkin:Boolean, //true
        @SerializedName("link") var link:String,
        @SerializedName("clip_title") var clip_title:String,
        @SerializedName("clip_provider") var clip_provider:String,
        @SerializedName("clip_urlembed") var clip_urlembed:String,
        @SerializedName("clip_description") var clip_description:String,
        @SerializedName("full_img") var full_img:String, //"https://s359.kapook.com/entry/4a58d583-7e0c-42b4-acee-a5c4c584c1f6.jpg"
        @SerializedName("full_thumbnail") var full_thumbnail:String, //"https://s359.kapook.com/entry/thumb_4a58d583-7e0c-42b4-acee-a5c4c584c1f6.jpg"
        @SerializedName("place_name") var place_name:String //"Chiang Khan District"
)

data class  MyPosition(
        @SerializedName("__type") var __type:String,
        @SerializedName("latitude") var latitude:Double,
        @SerializedName("longitude") var longitude:Double
)


