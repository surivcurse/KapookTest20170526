package com.app.me.kapooktest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.app.me.kapooktest.customclass.CategoryRcvAdapter;
import com.app.me.kapooktest.modelclass.CategoryModel;
import com.app.me.kapooktest.modelclass.ConstantModel;
import com.google.gson.Gson;

import org.json.JSONObject;

import static com.app.me.kapooktest.modelclass.ConstantModel.TO_INTENT;
import static com.app.me.kapooktest.modelclass.ConstantModel.getCategoryModel;

public class ChoiceCategoryActivity extends AppCompatActivity {
    AQuery aq;
    Gson gson = new Gson();
private int sentToIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_category);
        aq = new AQuery(this);
        final CategoryRcvAdapter categoryRcvAdapter = new CategoryRcvAdapter(getCategoryModel());
        if(ConstantModel.getCategoryModel().size() == 0){
            String urlCategory = "http://ts.entry.kapook.com/api/v1/entry/category";
            aq.ajax(urlCategory, JSONObject.class, new AjaxCallback<JSONObject>(){
                @Override
                public void callback(String url, JSONObject object, AjaxStatus status) {
                    super.callback(url, object, status);
                    CategoryModel categoryModel = gson.fromJson(object.toString(),CategoryModel.class);
                    ConstantModel.setDefaultCategory(categoryModel.getData());
                    categoryRcvAdapter.notifyDataSetChanged();
                    Log.d("ConstantModel", "callback: "+gson.toJson(object));
                }
            }.method(AQuery.METHOD_GET).header("Appserect","d^w,j[vdsivd]"));
        }

        Bundle bundle = getIntent().getExtras();
        TO_INTENT = bundle.getInt("INTENT_ID");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("เลือกหมวด");
        //aq = new AQuery(this);
        final GridLayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        final RecyclerView rcvItemCategory = (RecyclerView) findViewById(R.id.rcvItemCategory);

        rcvItemCategory.setHasFixedSize(true);
        rcvItemCategory.setLayoutManager(mLayoutManager);
        rcvItemCategory.setAdapter(categoryRcvAdapter);

    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
