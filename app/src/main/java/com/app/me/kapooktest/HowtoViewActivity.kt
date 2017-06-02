package com.app.me.kapooktest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.androidquery.AQuery
import com.androidquery.callback.AjaxCallback
import com.androidquery.callback.AjaxStatus
import com.app.me.kapooktest.customclass.ListContentViewRcvAdapter
import com.app.me.kapooktest.modelclass.EntryViewModel
import com.google.gson.Gson
import org.json.JSONObject


class HowtoViewActivity : AppCompatActivity() {

    private var rcvStepContrainer : RecyclerView? = null
    private var listContentViewAdapter: ListContentViewRcvAdapter? = null
    private var gson : Gson? = null
    private var aQuery : AQuery? = null
    private val jsonGetEntryViewModel = "http://ts.entry.kapook.com/api/v1/entry/topic/user/fd3dfcb12c16?content_type=6"
    private val header :Map<String,CharSequence> = mapOf("Appserect" to "d^w,j[vdsivd]")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_howto_view)

        rcvStepContrainer  = findViewById(R.id.rcvContentContrainer) as RecyclerView
        val mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mLayoutManager.orientation = LinearLayoutManager.VERTICAL

        gson = Gson()
        aQuery = AQuery(this)

            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = "ฮาวทู"

        aQuery!!.ajax(jsonGetEntryViewModel, JSONObject::class.java ,object : AjaxCallback<JSONObject>(){
            override fun callback(url:String , jsonObject:JSONObject , status: AjaxStatus) {

                super.callback(url,jsonObject,status)
                if(jsonObject!=JSONObject()) {
                    var allContentEntryViewModel: EntryViewModel =
                            gson!!.fromJson(jsonObject.toString(), EntryViewModel::class.java)

                    listContentViewAdapter = ListContentViewRcvAdapter(allContentEntryViewModel.data, allContentEntryViewModel.detail_user)
                    rcvStepContrainer!!.adapter = listContentViewAdapter
                    rcvStepContrainer!!.layoutManager = mLayoutManager
                    rcvStepContrainer!!.setHasFixedSize(false)
                    rcvStepContrainer!!.itemAnimator =  DefaultItemAnimator()


                    // Log.d("EntryViewModel",gson!!.toJson(allContentEntryViewModel))

                    //allContentEntryViewModel.detail_user.avatar
                    //allContentEntryViewModel.detail_user.display

                    //allContentEntryViewModel.data!!.get(0).createAt

                    //allContentEntryViewModel.data!!.get(0).media.thumbnail
                    //allContentEntryViewModel.data!!.get(0).title
                    //allContentEntryViewModel.data!!.get(0)._id
                    //allContentEntryViewModel.data!!.get(0).views
                    //allContentEntryViewModel.data!!.get(0).cat.name

                }
            }
        }.header("Appserect","d^w,j[vdsivd]").method(AQuery.METHOD_GET) )



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
