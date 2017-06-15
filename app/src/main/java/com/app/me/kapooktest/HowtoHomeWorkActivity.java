package com.app.me.kapooktest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.app.me.kapooktest.customclass.CircleImageView;
import com.app.me.kapooktest.helper.LoadBitmap;
import com.app.me.kapooktest.helper.ManageFileHelper;

import static com.app.me.kapooktest.modelclass.HowToCardModel.*;


public class HowtoHomeWorkActivity extends AppCompatActivity {
    private AQuery aQuery;

    private CircleImageView imgProfile;
    private TextView txtProfileName;
    private Button btnSwitchPublic;
    private Button btnSwitchDraft;
    private Button btnCategorySelect;

    private LinearLayout llAddImage;
    private RelativeLayout rlImageTitle;
    private ImageView imgTitle;
    private ImageView imgBtnSetting;
    private EditText edtTitle;
    private EditText edtSubject;

    private ImageView imgContentTitle;
    private TextView txtContentName;
    private TextView txtContentTitle;

    private Button btnAddHomeWork;
    private AlertDialog.Builder adb;

    private ManageFileHelper manageFileHelper;
    private static final int RESULT_SELECT_PICTURE = 9598;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_howto_home_work);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("ส่งการบ้าน : "+CONTENT_NAME);

        aQuery = new AQuery(this);
        manageFileHelper = new ManageFileHelper(this,RESULT_SELECT_PICTURE);
        imgProfile = (CircleImageView)findViewById(R.id.imgProfile);
        txtProfileName= (TextView)findViewById(R.id.txtProfileName);
        btnSwitchPublic= (Button)findViewById(R.id.btnSwitchPublic);
        btnSwitchDraft= (Button)findViewById(R.id.btnSwitchDraft);
        btnCategorySelect= (Button)findViewById(R.id.btnCategorySelect);

        llAddImage= (LinearLayout)findViewById(R.id.llAddImage);
        rlImageTitle= (RelativeLayout)findViewById(R.id.rlImageTitle);
        imgTitle= (ImageView)findViewById(R.id.imgTitle);
        imgBtnSetting= (ImageView)findViewById(R.id.imgBtnSetting);
        edtTitle= (EditText)findViewById(R.id.edtTitle);
        edtSubject= (EditText)findViewById(R.id.edtSubject);

        imgContentTitle= (ImageView)findViewById(R.id.imgContentTitle);
        txtContentName= (TextView)findViewById(R.id.txtContentName);
        txtContentTitle= (TextView)findViewById(R.id.txtContentTitle);

        btnAddHomeWork = (Button)findViewById(R.id.btnAddHomeWork);

        this.showLayout();
        this.setOnClickListener();

    }

    private void showLayout(){

        btnCategorySelect.setVisibility(View.GONE);
        if(CONTENT_NAME.length() > 15){
            edtTitle.setHint("ส่งการบ้าน : "+ CONTENT_NAME.substring(0,15)+". . ." );
        }else{
            edtTitle.setHint("ส่งการบ้าน : "+ CONTENT_NAME );
        }

        edtSubject.setVisibility(View.GONE);
       // PROFILE_PIC
        if(PROFILE_PIC != null){
            aQuery.id(imgProfile).image(PROFILE_PIC,true,false);
        }
        txtContentName.setText(CONTENT_NAME);
        txtContentTitle.setText(CONTENT_SUBTITLE);
        imgContentTitle.setImageBitmap(BM_CONTENT_TITLE);
        txtProfileName.setText(PROFILE_NAME);


    }


    private void setOnClickListener(){
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
                //switchLayoutParagraph1(true);
                showDialogChoicePicture(true);
            }
        });

        btnAddHomeWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CARD_TEXT = edtTitle.getText().toString();

                // Post Data To Server


            }
        });
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
                BM_CARD_TITLE = BitmapFactory.decodeResource(getResources(), R.drawable.campusstar_line);
               // setImageViewTitleHowTo(BM_CARD_TITLE);
                alertDialog.cancel();
            }
        });
        Button btnPhotoLink = (Button) dialoglayout.findViewById(R.id.btnPhotoLink);
        btnPhotoLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BM_CARD_TITLE = BitmapFactory.decodeResource(getResources(), R.drawable.pexels_photo_title);
                setImageViewTitleHowTo(BM_CARD_TITLE);
                alertDialog.cancel();
            }
        });

        Button btnPhotoCamera = (Button) dialoglayout.findViewById(R.id.btnPhotoCamera);
        btnPhotoCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BM_CARD_TITLE = BitmapFactory.decodeResource(getResources(), R.drawable.picture_camera);
                setImageViewTitleHowTo(BM_CARD_TITLE);
                alertDialog.cancel();
            }
        });

        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();

    }

    private void setImageViewTitleHowTo(Bitmap picContent){
        imgTitle.setImageBitmap(picContent);

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == RESULT_SELECT_PICTURE){
                new LoadBitmap(data,IMAGE_DETAILS,manageFileHelper){
                    @Override
                    public void onPostExecute(Void aVoid){
                        super.onPostExecute(aVoid);
                        BM_CARD_TITLE = manageFileHelper.getBm();
                        setImageViewTitleHowTo(BM_CARD_TITLE);
                        //contents.get(addPictureHolder.getLayoutPosition()).setPicContent(addPictureHolder.bm);
                        // Log.d("ImageUpload", "onPostExecute: "+gson.toJson(contents));
                    }
                }.execute();


            }
        }
    }
}
