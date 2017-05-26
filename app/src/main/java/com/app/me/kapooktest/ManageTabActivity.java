package com.app.me.kapooktest;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.me.kapooktest.customclass.ListTabsRecAdapter;
import com.app.me.kapooktest.helper.TabsSqLiteHandle;
import com.app.me.kapooktest.modelclass.ConstantModel;

import java.util.List;

public class ManageTabActivity extends AppCompatActivity {
    private TabsSqLiteHandle datasource;
    private LayoutManagerType mCurrentLayoutManagerType;
    private Button btnAddTab;
    private EditText edtNameTab;
    private ListTabsRecAdapter listTabsRecAdapter;
    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_tab);

        datasource = new TabsSqLiteHandle(this);



        final RecyclerView recContainer = (RecyclerView) findViewById(R.id.rec_item_tab);
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listTabsRecAdapter = new ListTabsRecAdapter(datasource);
        //recContainer.setHasFixedSize(true);
        recContainer.setLayoutManager(mLayoutManager);
        recContainer.setAdapter(listTabsRecAdapter);
        recContainer.setRecycledViewPool(new RecyclerView.RecycledViewPool());

        edtNameTab =  (EditText) findViewById(R.id.edtNameTab) ;
        final String textDefault_edtNameTab = edtNameTab.getText().toString();
        btnAddTab = (Button) findViewById(R.id.btnAddTab);

        btnAddTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtNameTab.getText().toString().equals(textDefault_edtNameTab)){
                    Log.d("ALL_TABS", "onClick: TabModel"+edtNameTab.getText().toString().length());
                    ConstantModel.TabsModel newTab = new ConstantModel.TabsModel(0,edtNameTab.getText().toString());
                    newTab.setID(datasource.addTabs(newTab));
                    ConstantModel.addCustomTab(newTab);

                    listTabsRecAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(),"Item Added",Toast.LENGTH_SHORT).show();
                    edtNameTab.setText("");

                }
            }


        });




    }
}
