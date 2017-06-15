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
import com.app.me.kapooktest.modelclass.ConstantModel
import com.app.me.kapooktest.modelclass.EntryViewModel
import com.google.gson.Gson
import com.parse.ParseUser
import org.json.JSONObject


class EntryViewActivity : AppCompatActivity() {

    private var rcvStepContrainer : RecyclerView? = null
    private var listContentViewAdapter: ListContentViewRcvAdapter? = null
    private var gson : Gson? = null
    private var aQuery : AQuery? = null

    private var jsonGetEntryViewModel = ""

    private val header :Map<String,CharSequence> = mapOf("Appserect" to "d^w,j[vdsivd]") //Map<String,String>{Map("Appserect" , "d^w,j[vdsivd]")}

    //http://ts.entry.kapook.com/api/v1/entry/topic/user/fd3dfcb12c16?dev=1994

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry_view)
        //user_objectId = currentUser.objectId
        jsonGetEntryViewModel = ConstantModel.KapookPostContent.getJsonViewModel(4)
        rcvStepContrainer  = findViewById(R.id.rcvContentContrainer) as RecyclerView
        val mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mLayoutManager.orientation = LinearLayoutManager.VERTICAL

        gson = Gson()
        aQuery = AQuery(this)


            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = "เอนทรี"


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
