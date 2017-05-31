package com.app.me.kapooktest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.androidquery.AQuery
import com.androidquery.callback.AjaxCallback
import com.androidquery.callback.AjaxStatus
import com.app.me.kapooktest.customclass.EntryViewRcvAdapter
import com.app.me.kapooktest.modelclass.EntryViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.contrainer_entry_view_layout.*
import org.json.JSONObject


class EntryViewActivity : AppCompatActivity() {

    private var recyclerEntryViewModel : RecyclerView? = null
    private var entryViewAdapter : EntryViewRcvAdapter? = null
    private var gson : Gson? = null
    private var aQuery : AQuery? = null
    private var id_content = "c07b5af7c729"
    private val jsonGetEntryViewModel = "http://ts.entry.kapook.com/api/v1/entry/topic/user/fd3dfcb12c16?content_type=4"

    private val header :Map<String,CharSequence> = mapOf("Content-Type" to "application/json" ,"Appserect" to "d^w,j[vdsivd]") //Map<String,String>{Map("Appserect" , "d^w,j[vdsivd]")}

    //http://ts.entry.kapook.com/api/v1/entry/topic/user/fd3dfcb12c16?dev=1994

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry_view)

        recyclerEntryViewModel  = findViewById(R.id.rcvEntryContrainer) as RecyclerView
        val mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mLayoutManager.orientation = LinearLayoutManager.VERTICAL

        gson = Gson()
        aQuery = AQuery(this)

        if(this!=null){
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = "เอนทรี"
        }

        aQuery!!.ajax(jsonGetEntryViewModel, JSONObject::class.java ,object : AjaxCallback<JSONObject>(){
            override fun callback(url:String , jsonObject:JSONObject , status: AjaxStatus) {

                super.callback(url,jsonObject,status)
                if(jsonObject!=null) {
                    var allContentEntryViewModel: EntryViewModel =
                            gson!!.fromJson(jsonObject.toString(), EntryViewModel::class.java)

                    entryViewAdapter = EntryViewRcvAdapter(allContentEntryViewModel.data,allContentEntryViewModel.detail_user)
                    rcvEntryContrainer.adapter = entryViewAdapter
                    rcvEntryContrainer.layoutManager = mLayoutManager
                    rcvEntryContrainer.setHasFixedSize(false)
                    rcvEntryContrainer.itemAnimator =  DefaultItemAnimator()


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
