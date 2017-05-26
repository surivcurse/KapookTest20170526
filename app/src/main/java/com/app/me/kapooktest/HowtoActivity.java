package com.app.me.kapooktest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import static com.app.me.kapooktest.modelclass.ConstantModel.*;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.me.kapooktest.customclass.HowToDescriptionRcvAdapter;
import com.app.me.kapooktest.customclass.HowToRenderingRcvAdapter;
import com.app.me.kapooktest.helper.ManageFileHelper;


import static com.app.me.kapooktest.modelclass.HowToModel.*;

public class HowtoActivity extends AppCompatActivity {
    private Button btnCategorySelect;
    private Button btnSwitchPublic;
    private Button btnSwitchDraft;
    private Button btnAddDescription;
    private RecyclerView rcvRendering;
    private RecyclerView rcvDescription;
    private AlertDialog.Builder adb;

    private LinearLayout llAddImage;
    private RelativeLayout rlImageTitle;
    private ImageView imgTitle;
    private EditText edtTitle;
    private EditText edtSubject;
    private ImageButton imgBtnSetting;
    private Bitmap bm;

    private TextView txtRendering;

    final HowToDescriptionRcvAdapter descriptionAdapter = new HowToDescriptionRcvAdapter(getDescriptionModelList());

    private boolean doubleBackToExitPressedOnce = false;
    private CategoryModel categoryModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_howto);
        refreshData();
        Bundle bundle = getIntent().getExtras();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("How to . . .");
        CATEGORY_ID = bundle.getInt("ITEM_ID");
        categoryModel = getCategoryByID(CATEGORY_ID);
        this.createHeaderView();
        this.createContentParagraph1();

        this.createContentParagraph2();
        this.createContentParagraph3(descriptionAdapter);
    }

    private void createHeaderView(){
        btnCategorySelect = (Button) findViewById(R.id.btnCategorySelect);
        btnCategorySelect.setText(categoryModel.getTitle());
        btnCategorySelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doubleBackToExitPressedOnce = true;
                onBackPressed();
            }
        });
        btnSwitchPublic = (Button) findViewById(R.id.btnSwitchPublic);
        btnSwitchPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchStatusContent(false);
            }
        });

        btnSwitchDraft = (Button) findViewById(R.id.btnSwitchDraft);
        btnSwitchDraft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchStatusContent(true);
            }
        });
    }

    private void switchStatusContent(boolean status){
        if(status){
            btnSwitchPublic.setVisibility(View.VISIBLE);
            btnSwitchDraft.setVisibility(View.GONE);
        }else{
            btnSwitchPublic.setVisibility(View.GONE);
            btnSwitchDraft.setVisibility(View.VISIBLE);
        }
        STATUS_CONTENT = status;
    }

    private void createContentParagraph1(){
        edtTitle = (EditText) findViewById(R.id.edtTitle);
        edtSubject = (EditText) findViewById(R.id.edtSubject);
        llAddImage = (LinearLayout) findViewById(R.id.llAddImage);
        rlImageTitle = (RelativeLayout) findViewById(R.id.rlImageTitle);
        imgTitle = (ImageView) findViewById(R.id.imgTitle);
        imgBtnSetting = (ImageButton) findViewById(R.id.imgBtnSetting);

        txtRendering = (TextView) findViewById(R.id.txtRendering);

        edtTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    TITLE = edtTitle.getText().toString();
                }
            }
        });

        edtSubject.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    SUBJECT = edtSubject.getText().toString();
                }
            }
        });

        llAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchLayoutParagraph1(true);
                showDialogChoicePicture();

            }
        });

        imgBtnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogChoicePicture(true);
            }
        });
    }

    private void showDialogChoicePicture(){
        showDialogChoicePicture(false);
    }

    private void showDialogChoicePicture(boolean isEdit){
        String strTitle = "เพิ่มรูปภาพ";
        if(isEdit){
            strTitle = "แก้ไขรูปภาพ";
        }

        final FrameLayout frameView = new FrameLayout(this);
        adb = new AlertDialog.Builder(this);
        adb.setIcon(R.drawable.ic_picture_black);
        adb.setTitle(strTitle);
        adb.setView(frameView);
        final AlertDialog alertDialog = adb.create();
        LayoutInflater inflater = alertDialog.getLayoutInflater();

        View dialoglayout = inflater.inflate(R.layout.dialog_choice_picture, frameView);

        Button btnPhotoDevice = (Button) dialoglayout.findViewById(R.id.btnPhotoDevice);
        btnPhotoDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bm = BitmapFactory.decodeResource(getResources(), R.drawable.campusstar_line);
                setImageViewTitleHowTo(bm);
                alertDialog.cancel();
            }
        });
        Button btnPhotoLink = (Button) dialoglayout.findViewById(R.id.btnPhotoLink);
        btnPhotoLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bm = BitmapFactory.decodeResource(getResources(), R.drawable.pexels_photo_title);
                setImageViewTitleHowTo(bm);
                alertDialog.cancel();
            }
        });

        Button btnPhotoCamera = (Button) dialoglayout.findViewById(R.id.btnPhotoCamera);
        btnPhotoCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bm = BitmapFactory.decodeResource(getResources(), R.drawable.picture_camera);
                setImageViewTitleHowTo(bm);
                alertDialog.cancel();
            }
        });

        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();

    }

    private void switchLayoutParagraph1(boolean isImage) {
        if(isImage){

            llAddImage.setVisibility(View.GONE);
            rlImageTitle.setVisibility(View.VISIBLE);

        }else{
            rlImageTitle.setVisibility(View.GONE);
            llAddImage.setVisibility(View.VISIBLE);
        }
    }

    private void setImageViewTitleHowTo(Bitmap picContent){
        imgTitle.setImageBitmap(picContent);

    }

    private void createContentParagraph2(){
        setAdapterRecyclerViewRendering();
    }

    private void createContentParagraph3(final HowToDescriptionRcvAdapter descriptionAdapter){

        btnAddDescription = (Button) findViewById(R.id.btnAddDescription);
        setRecyclerViewDescription(descriptionAdapter);

        btnAddDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                descriptionAdapter.onBackPressed();
                addDescriptionModelToList(new DescriptionModel());
                HowToDescriptionRcvAdapter.isAddNewItem = true;
                descriptionAdapter.notifyItemInserted(getDescriptionModelList().size()-1);
                rcvDescription.scrollToPosition(getDescriptionModelList().size()-1);

            }
        });


    }

    private void setRecyclerViewDescription(HowToDescriptionRcvAdapter descriptionAdapter){
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rcvDescription = (RecyclerView) findViewById(R.id.rcvDescriptionContrainer);
        rcvDescription.setLayoutManager(mLayoutManager);
        rcvDescription.setHasFixedSize(true);
        rcvDescription.setAdapter(descriptionAdapter);
        rcvDescription.setItemAnimator(new DefaultItemAnimator());


    }

    private void setAdapterRecyclerViewRendering(){
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        final HowToRenderingRcvAdapter renderingAdapter = new HowToRenderingRcvAdapter(getRenderingModelList());


        rcvRendering = (RecyclerView) findViewById(R.id.rcvRenderingContrainer);
        rcvRendering.setLayoutManager(mLayoutManager);
        rcvRendering.setHasFixedSize(false);
        rcvRendering.setAdapter(renderingAdapter);
        rcvRendering.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {

        descriptionAdapter.onBackPressed();

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == ManageFileHelper.SELECT_PICTURE){
                descriptionAdapter.onActivityResult(requestCode,resultCode,data);
            }
        }
    }

}
