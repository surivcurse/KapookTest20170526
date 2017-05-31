package com.app.me.kapooktest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.androidquery.AQuery
import com.androidquery.callback.AjaxCallback
import com.androidquery.callback.AjaxStatus

import com.app.me.kapooktest.customclass.EntryViewRcvAdapter
import com.google.gson.Gson
import org.json.JSONObject


class EntryContentViewActivity : AppCompatActivity() {

    private var recyclerEntryViewContent : RecyclerView? = null
    private var entryViewAdapter : EntryViewRcvAdapter? = null
    private var gson : Gson? = null
    private var aQuery : AQuery? = null
    private var id_content = "c07b5af7c729"
    private val jsonGetEntryViewContent = "http://ts.entry.kapook.com/api/v1/entry/topic/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry_view_content)

        val bundle = intent.extras
        val content_id = bundle.getString("CONTENT_ID")

        recyclerEntryViewContent  = findViewById(R.id.rcvEntryContainer) as RecyclerView
        val mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mLayoutManager.orientation = LinearLayoutManager.VERTICAL

        gson = Gson()
        aQuery = AQuery(this)

        if(this!=null){
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = "เอนทรี"
        }

        aQuery!!.ajax(jsonGetEntryViewContent, JSONObject::class.java ,object : AjaxCallback<JSONObject>(){

            override fun callback(url: String?, `object`: JSONObject?, status: AjaxStatus?) {
                super.callback(url, `object`, status)
                

            }

        })

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
