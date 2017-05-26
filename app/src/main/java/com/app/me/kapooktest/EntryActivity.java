package com.app.me.kapooktest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import com.app.me.kapooktest.customclass.EntryRcvAdapter;
import static com.app.me.kapooktest.modelclass.EntryModel.*;
import static com.app.me.kapooktest.modelclass.ConstantModel.*;

public class EntryActivity extends AppCompatActivity {
    private boolean doubleBackToExitPressedOnce = false;
    private CategoryModel categoryModel;
    private AlertDialog.Builder adb;

    private RecyclerView rcvEntryContent;

    private Button btnCategorySelect;
    private Button btnSwitchPublic;
    private Button btnSwitchDraft;
    private Button btnAddDescription;

    private LinearLayout llAddImage;
    private RelativeLayout rlImageTitle;
    private ImageView imgTitle;
    private EditText edtTitle;
    private EditText edtSubject;
    private ImageButton imgBtnSetting;
    final private EntryRcvAdapter entryRcvAdapter = new EntryRcvAdapter(getEntryContentList());
    private TextView txtRendering;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        refreshData();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("เขียนเอนทรีที่คุณสนใจ");
        Bundle bundle = getIntent().getExtras();
        CATEGORY_ID = bundle.getInt("ITEM_ID");
        categoryModel = getCategoryByID(CATEGORY_ID);


        this.createContentParagraph1();
        this.createParagraph2(entryRcvAdapter);
        this.createHeaderView();
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

    private void createParagraph2(final EntryRcvAdapter entryRcvAdapter){
        btnAddDescription = (Button) findViewById(R.id.btnAddDescription);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);


        rcvEntryContent = (RecyclerView) findViewById(R.id.rcvEntryContrainer);
        rcvEntryContent.setHasFixedSize(true);
        rcvEntryContent.setLayoutManager(mLayoutManager);
        rcvEntryContent.setAdapter(entryRcvAdapter);
        rcvEntryContent.setItemAnimator(new DefaultItemAnimator());

        btnAddDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //entryRcvAdapter.onBackPressed();
                entryContentList.add(new EntryContentModel());
                entryRcvAdapter.notifyItemInserted(entryRcvAdapter.getItemCount()-1);
                EntryRcvAdapter.isNotifyChange = true;
                rcvEntryContent.scrollToPosition(entryRcvAdapter.getItemCount()-1);

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
                PIC_TITLE = BitmapFactory.decodeResource(getResources(), R.drawable.campusstar_line);
                setImageViewTitleHowTo(PIC_TITLE);
                alertDialog.cancel();
            }
        });
        Button btnPhotoLink = (Button) dialoglayout.findViewById(R.id.btnPhotoLink);
        btnPhotoLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PIC_TITLE = BitmapFactory.decodeResource(getResources(), R.drawable.pexels_photo_title);
                setImageViewTitleHowTo(PIC_TITLE);
                alertDialog.cancel();
            }
        });

        Button btnPhotoCamera = (Button) dialoglayout.findViewById(R.id.btnPhotoCamera);
        btnPhotoCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PIC_TITLE = BitmapFactory.decodeResource(getResources(), R.drawable.picture_camera);
                setImageViewTitleHowTo(PIC_TITLE);
                alertDialog.cancel();
            }
        });

        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();

    }
    private void setImageViewTitleHowTo(Bitmap picContent){
        imgTitle.setImageBitmap(picContent);

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

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {

        entryRcvAdapter.onBackPressed();

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
            entryRcvAdapter.onActivityResult(requestCode, resultCode, data);
        }

    }
}
