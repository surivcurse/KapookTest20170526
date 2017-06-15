package com.app.me.kapooktest.modelclass

/**
 * Created by SuRiV on 14/6/2560.
 */

data class CategoryModel(
        var status:Int,
        var total:Int,
        var data:ArrayList<CategoryData> = ArrayList()

){

    data class CategoryData(
            var icon: String?="",
            var objectId: String?="",
            var name:String?="",
            var status: Int?=1,
            var link_icon:String?="",
            var sort:Int?=0,
            var createdAt:String,
            var updatedAt:String,
            var detail_howto:Detail_Howto

    ){
        data class Detail_Howto(
                var header_content:String,
                var holder_content:String,
                var header_step:String,
                var holder_step:String


        )

    }
}

