package com.app.me.kapooktest;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import static com.app.me.kapooktest.modelclass.ConstantModel.*;

import android.support.v7.widget.DefaultItemAnimator;
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
import android.widget.Toast;

import com.app.me.kapooktest.customclass.HowToDescriptionRcvAdapter;
import com.app.me.kapooktest.customclass.HowToRenderingRcvAdapter;
import com.app.me.kapooktest.helper.KapookPostContentHelper;
import com.app.me.kapooktest.helper.LoadBitmap;
import com.app.me.kapooktest.helper.ManageFileHelper;
import com.app.me.kapooktest.modelclass.CategoryModel;
import com.app.me.kapooktest.modelclass.CreateContentModel;
import com.app.me.kapooktest.modelclass.HowToModel;
import com.app.me.kapooktest.modelclass.MediaDetail;
import com.google.gson.Gson;
import com.parse.ParseUser;


import static com.app.me.kapooktest.modelclass.HowToModel.*;

public class HowtoActivity extends AppCompatActivity {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

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
    private TextView txtPicTitle;
    private ImageButton imgBtnSetting;
    private Bitmap bm;
    private ManageFileHelper manageFileHelper;
    private static final int RESULT_SELECT_PICTURE = 9594;
    private ParseUser currentKapookUser;
    private Button btnPost;

    private TextView txtRendering;

    final HowToDescriptionRcvAdapter descriptionAdapter = new HowToDescriptionRcvAdapter(getDescriptionModelList());
    final HowToRenderingRcvAdapter renderingAdapter = new HowToRenderingRcvAdapter(getRenderingModelList());
    private boolean doubleBackToExitPressedOnce = false;
    private CategoryModel.CategoryData categoryModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_howto);
        refreshData();
        Gson gson = new Gson();
        currentKapookUser = ParseUser.getCurrentUser();
        Bundle bundle = getIntent().getExtras();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("How to . . .");
        CATEGORY_ID = bundle.getInt("ITEM_ID");
        CONTENT_TYPE = 6;
        categoryModel = getCategoryByIndex(CATEGORY_ID);
        CATEGORY_OBJ = categoryModel.getObjectId();
        Log.d("categoryModel", "onCreate: "+gson.toJson(categoryModel));
        verifyStoragePermissions(this);

        manageFileHelper = new ManageFileHelper(this,RESULT_SELECT_PICTURE);
        this.createHeaderView();
        this.createContentParagraph1();

        this.createContentParagraph2();
        this.createContentParagraph3(descriptionAdapter);

        btnPost =(Button) findViewById(R.id.btnPost);
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean someError = false;
                TITLE = edtTitle.getText().toString();
                SUBJECT = edtSubject.getText().toString();
                if(TITLE.length() < 1){
                    edtTitle.setHint("****กรุณาใส่หัวเรื่อง...****");
                    edtTitle.setHintTextColor(getColor(R.color.tw__composer_red));
                    someError = true;
                }
                if(SUBJECT.length() < 1){
                    edtSubject.setHint("****กรุณาใส่หัวเรื่อง...****");
                    edtSubject.setHintTextColor(getColor(R.color.tw__composer_red));
                    someError = true;
                }
                if(PIC_TITLE == null){
                    switchLayoutParagraph1(false);
                    txtPicTitle.setText("****กรุณาใส่รูปภาพหน้าปก...****");
                    txtPicTitle.setTextColor(getColor(R.color.tw__composer_red));
                    someError = true;
                }

                if(someError){
                    return;
                }

                KapookPostContentHelper kapookPostContentHelper = new KapookPostContentHelper(getApplicationContext(),currentKapookUser.getSessionToken());
                //kapookPostContentHelper.convertDataHowtoModel();
                int result = kapookPostContentHelper.postDataToServer();

                switch(result){
                    case 0:
                        Toast.makeText(v.getContext(),"Post content to server",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        renderingAdapter.setRenderSomeError();
                        break;
                    case 2:
                        descriptionAdapter.setDescriptionSomeError();
                        break;
                    case 3:
                        renderingAdapter.setRenderSomeError();
                        descriptionAdapter.setDescriptionSomeError();

                        break;

                }
                //Toast.makeText(v.getContext(),Result,Toast.LENGTH_LONG).show();


            }
        });

    }

    private void createHeaderView(){
        btnCategorySelect = (Button) findViewById(R.id.btnCategorySelect);
        btnCategorySelect.setText(categoryModel.getName());
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
            STATUS_CONTENT = 1;
        }else{
            btnSwitchPublic.setVisibility(View.GONE);
            btnSwitchDraft.setVisibility(View.VISIBLE);
            STATUS_CONTENT = 4;
        }

    }

    private void createContentParagraph1(){
        edtTitle = (EditText) findViewById(R.id.edtTitle);
        edtSubject = (EditText) findViewById(R.id.edtSubject);
        llAddImage = (LinearLayout) findViewById(R.id.llAddImage);
        rlImageTitle = (RelativeLayout) findViewById(R.id.rlImageTitle);
        imgTitle = (ImageView) findViewById(R.id.imgTitle);
        imgBtnSetting = (ImageButton) findViewById(R.id.imgBtnSetting);
        txtPicTitle = (TextView) findViewById(R.id.txtPicTitle);

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
                // Load Image From Device
                manageFileHelper.createChoicePicture();
                alertDialog.cancel();
            }
        });
        Button btnPhotoLink = (Button) dialoglayout.findViewById(R.id.btnPhotoLink);
        btnPhotoLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //bm = BitmapFactory.decodeResource(getResources(), R.drawable.pexels_photo_title);
                //setImageViewTitleHowTo(bm);
                showDialogInputLink("ลิ้งค์...");
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

            switch (requestCode){
                case RESULT_SELECT_PICTURE:
                    asynLoadBitmap(data,mediaDetail,manageFileHelper);
                    break;
                case HowToDescriptionRcvAdapter.RESULT_SELECT_PICTURE:
                    descriptionAdapter.onActivityResult(data);
                    break;

            }
        }
    }

    private void showDialogInputLink(String title) {
//https://siamblockchain.com/wp-content/uploads/2017/06/Bitcoin-vs-Ethereum-1.png
        final FrameLayout frameView = new FrameLayout(this);
        adb = new AlertDialog.Builder(this);
        adb.setIcon(R.drawable.ic_link_black);
        adb.setTitle(title);
        adb.setView(frameView);
        final AlertDialog alertDialog = adb.create();
        LayoutInflater inflater = alertDialog.getLayoutInflater();

        View dialoglayout = inflater.inflate(R.layout.dialog_input_link, frameView);
        final TextView txtHintInputLink = (TextView) dialoglayout.findViewById(R.id.txtHintInputLink);
        final EditText edtInputLink = (EditText) dialoglayout.findViewById(R.id.edtInputLink);
        Button btnInputLink = (Button) dialoglayout.findViewById(R.id.btnInputLink);
        btnInputLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtInputLink.getText().toString().length() < 1) {
                    alertDialog.cancel();
                    return;
                }

                if (edtInputLink.getText().toString().indexOf("http") == -1) {
                    txtHintInputLink.setVisibility(View.VISIBLE);
                    txtHintInputLink.setText("กรุณาใส่ http:// หรือ https:// ไว้ด้านหน้า url");
                    alertDialog.cancel();
                    return;
                }

                manageFileHelper.loadImageFromLink(edtInputLink.getText().toString(), imgTitle, new HowToModel());
                // holder.edtDescription.clearFocus();
                // holder.rlDescriptionView.setVisibility(View.VISIBLE);
                //holder.rlDescriptionEdit.setVisibility(View.GONE);
                // holder.btnAddPhoto.setVisibility(View.INVISIBLE);
                //  holder.imgBtnSetting.setVisibility(View.VISIBLE);
                //  holder.imgBtnDeletePic.setVisibility(View.VISIBLE);
                // holder.imgViewDescription.requestLayout();

                alertDialog.cancel();
            }
        });
        alertDialog.show();
    }
    private void asynLoadBitmap(Intent data, MediaDetail mediaDetail, final ManageFileHelper manageFileHelper){
        new LoadBitmap(data,mediaDetail,manageFileHelper){
            // Do Something When Done
            @Override
            public void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                PIC_TITLE = manageFileHelper.getBm();
                setImageViewTitleHowTo(PIC_TITLE);

            }
        }.execute();
    }

    public void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

}
