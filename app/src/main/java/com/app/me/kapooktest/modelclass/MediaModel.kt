package com.app.me.kapooktest.modelclass

import android.util.Log

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
        var status : Int,
        var title : String,
        var description : String,
        var original_url : String,
        var favicon : String,
        var provider: String,
        var scriptembed: String,
        var embed: String,
        var typeembed: Boolean,
        var embed_url: String,
        var images : ArrayList<MyImages>  = ArrayList()

){
    data class MyImages(
            var caption : String,
            var url : String,
            var height : Int,
            var width : Int,
            var color : String,
            var entropy : Int,
            var size : Int

            )
}

data class MediaDetail( var imgurl : String ?= "",
                        var thumbnail : String ?= "") {

    fun setMediaDetail(imagePath: ImagePath) {
        Log.d("ImagePath", "setMediaDetail: " + imagePath.path["0"])
        this.imgurl = imagePath.path["0"]
        this.thumbnail = imagePath.path_thumb["0"]
    }
}

