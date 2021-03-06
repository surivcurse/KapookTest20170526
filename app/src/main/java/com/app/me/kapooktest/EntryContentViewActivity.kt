package com.app.me.kapooktest

import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.*
import com.androidquery.AQuery
import com.androidquery.callback.AjaxCallback
import com.androidquery.callback.AjaxStatus
import com.app.me.kapooktest.customclass.*

import com.app.me.kapooktest.modelclass.DataDetail
import com.app.me.kapooktest.modelclass.EntryViewContent
import com.facebook.share.model.ShareContent
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareButton
import com.google.api.client.util.DateTime
import com.google.gson.Gson
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class EntryContentViewActivity : AppCompatActivity() {

    private var recyclerEntryViewContent : RecyclerView? = null
    private var gson : Gson? = null
    private var aQuery : AQuery? = null
    private var content_id : String? = ""
    private val jsonGetEntryViewContent = "http://ts.entry.kapook.com/api/v1/entry/topic/"
    private val header :Map<String,String> = mapOf("Appserect" to "d^w,j[vdsivd]") //Map<String,String>{Map("Appserect" , "d^w,j[vdsivd]")}

    private var imgTitle : ImageView? = null
    private var imgFooterForEntryView : ImageView? = null
    private var imgProfile : CircleImageView? = null

    private var txtTitle : TextView? = null
    private var txtCountLike : TextView? = null
    private var txtProfileName : TextView? = null
    private var txtTimePost : TextView? = null
    private var txtDescription : TextView? = null
    private var txtCountView : TextView? = null
    private var txtLocation : TextView? = null


    private var imgBtnSave : ImageButton? = null
    private var imgBtnSubscribe: ImageButton? = null
    private var tglBtnLike : ToggleButton? = null
    private var shareFacebook : ShareButton? = null
    private var shareTweet : Button? = null
    private var imgBtnSettingHeaderContent : ImageButton? = null

    private var shareContent: ShareLinkContent? = null
    private var entryContentAdapter :EntryContentViewRcvAdapter? = null

    private var llEntryView : LinearLayout? = null
    private var llLocation : LinearLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry_view_content)

        val bundle = intent.extras
        content_id = bundle.getString("CONTENT_ID")

        imgTitle = findViewById(R.id.imgTitle) as ImageView
        imgFooterForEntryView = findViewById(R.id.imgFooterForEntryView) as ImageView

        txtTitle = findViewById(R.id.txtTitle) as TextView
        txtProfileName = findViewById(R.id.txtProfileName) as TextView
        txtCountLike = findViewById(R.id.txtCountLike) as TextView
        txtTimePost = findViewById(R.id.txtTimePost) as TextView
        txtDescription = findViewById(R.id.txtDescription) as TextView
        txtCountView = findViewById(R.id.txtCountView) as TextView
        txtLocation = findViewById(R.id.txtLocation) as TextView

        imgProfile  = findViewById(R.id.imgProfile) as CircleImageView

        imgBtnSave  = findViewById(R.id.imgBtnSave) as ImageButton
        imgBtnSubscribe  = findViewById(R.id.imgBtnSave) as ImageButton
        tglBtnLike  = findViewById(R.id.tglBtnLike) as ToggleButton

        shareFacebook = findViewById(R.id.shareFacebook) as ShareButton

        shareTweet = findViewById(R.id.shareTweet) as Button
        imgBtnSettingHeaderContent = findViewById(R.id.imgBtnSettingHeaderContent) as ImageButton

        llEntryView = findViewById(R.id.llEntryView) as LinearLayout
        llLocation = findViewById(R.id.llLocation) as LinearLayout
        recyclerEntryViewContent  = findViewById(R.id.rcvStepContainer) as RecyclerView
        val mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mLayoutManager.orientation = LinearLayoutManager.VERTICAL
        //val dividerItemDecoration = DividerItemDecoration(this, mLayoutManager.getOrientation())

        recyclerEntryViewContent!!.layoutManager = mLayoutManager
        recyclerEntryViewContent!!.itemAnimator = DefaultItemAnimator()
        recyclerEntryViewContent!!.setHasFixedSize(true)
       // recyclerEntryViewContent!!.addItemDecoration(dividerItemDecoration)
        gson = Gson()
        aQuery = AQuery(this)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "เอนทรี"

        aQuery!!.ajax(jsonGetEntryViewContent+""+content_id, JSONObject::class.java ,object : AjaxCallback<JSONObject>(){

            override fun callback(url: String?, jsonObject: JSONObject?, status: AjaxStatus?) {
                super.callback(url, jsonObject, status)
                Log.d("JSON",gson!!.toJson(jsonObject))

                if(jsonObject != JSONObject()){
                    val entryContentData :
                            EntryViewContent = gson!!.fromJson(jsonObject.toString(), EntryViewContent::class.java)
                    val data_info = entryContentData.data_info!!

                    val imageUrl = if(data_info.media.full_thumbnail != "")
                                        data_info.media.full_thumbnail
                                    else
                                        data_info.media.img
                    txtCountView!!.text = data_info.views.toString()
                    aQuery!!.id(imgTitle).image(imageUrl,true,false)
                    aQuery!!.id(imgProfile).image(data_info.detail_user.avatar,true,false)
                    txtTitle!!.text = data_info.title
                    txtDescription!!.text = data_info.description
                    txtProfileName!!.text = data_info.detail_user.display
                    val lacation = data_info.media.place_name
                    if (lacation != "") {
                        llLocation!!.visibility = View.VISIBLE
                        txtLocation!!.text = lacation
                    }

                    //txtRendering!!.text=entryContentData.data_info!!.cat.detail_howto.header_step
                    val nowDate: Date
                    var strDate: String? = null
                    try {//2017-03-08T08:28:15Z
                        nowDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(entryContentData.data_info!!.createAt)
                        strDate = SimpleDateFormat("dd MMMM yyyy kk:mm:ss").format(nowDate)
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }
                    txtTimePost!!.text = strDate
                    shareFacebook!!.shareContent = setShareFacebookContent(entryContentData.data_info)
                    entryContentAdapter =EntryContentViewRcvAdapter(entryContentData.data)

                    recyclerEntryViewContent!!.adapter = entryContentAdapter

                }

            }

        }.header("Appserect","d^w,j[vdsivd]").method(AQuery.METHOD_GET))
    }

    private fun setShareFacebookContent(content: DataDetail?): ShareContent<*, *> {
        shareContent = ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(content!!.link))

                .build()

        return shareContent as ShareLinkContent
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}
