package com.app.me.kapooktest.modelclass
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
/**
 * Created by SuRiV on 7/6/2560.
 */

data class ImagePath(
        var path : HashMap<String,String>,
        var status : Int?= 0,
        var message : String?=null,
        var path_thumb : HashMap<String,String>
)

data class ImageLink(
        var status : Int ?= 1,
        var title : String ?= "",
        var description : String ?= "",
        var original_url : String ?= "",
        var favicon : String ?= "",
        var provider: String ?= "",
        var scriptembed: String ?= "",
        var embed: String ?= "",
        var typeembed: Boolean ?= false,
        var embed_url: String ?= "",
        @SerializedName("images") @Expose var images : ArrayList<MyImages> ,
        var type : Int ?= 0,
        var subtype :Int ?= 0

){
    data class MyImages(
            @SerializedName("caption")
            @Expose
            var caption : String,
            @SerializedName("url")
            @Expose
            var url : String,
            @SerializedName("height")
            @Expose
            var height : Int,
            @SerializedName("width")
            @Expose
            var width : Int,
            @SerializedName("color")
            @Expose
            var color : String,
            @SerializedName("entropy")
            @Expose
            var entropy : Int,
            @SerializedName("size")
            @Expose
            var size : Int
    )

}

data class MediaLinkDetail(
        var img :  String ?= "",
        var link : String ?= ""
        //var scriptembed : String ?= ""
){
    fun setMediaLinkDetail(imageLink: ImageLink){

        this.img = imageLink.images[0].url
        link = imageLink.original_url
       // scriptembed = imageLink.scriptembed
    }
}

data class MediaDetail( var imgurl : String ?= "",
                        var thumbnail : String ?= "") {

    fun setMediaDetail(imagePath: ImagePath) {
        this.imgurl = imagePath.path["0"]
        this.thumbnail = imagePath.path_thumb["0"]
    }
}

