package com.app.me.kapooktest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.app.me.kapooktest.customclass.CategoryRcvAdapter;

import static com.app.me.kapooktest.modelclass.ConstantModel.TO_INTENT;
import static com.app.me.kapooktest.modelclass.ConstantModel.getCategoryModel;

public class ChoiceCategoryActivity extends AppCompatActivity {
    //AQuery aq;
private int sentToIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_category);

        Bundle bundle = getIntent().getExtras();
        TO_INTENT = bundle.getInt("INTENT_ID");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("เลือกหมวด");
        //aq = new AQuery(this);
        final GridLayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        final RecyclerView rcvItemCategory = (RecyclerView) findViewById(R.id.rcvItemCategory);

        rcvItemCategory.setHasFixedSize(true);
        rcvItemCategory.setLayoutManager(mLayoutManager);

        final CategoryRcvAdapter categoryRcvAdapter = new CategoryRcvAdapter(getCategoryModel());
        rcvItemCategory.setAdapter(categoryRcvAdapter);

    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
