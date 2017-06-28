package com.app.me.kapooktest.helper;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.app.me.kapooktest.R;
import com.app.me.kapooktest.modelclass.HowToModel;
import com.app.me.kapooktest.modelclass.ImageLink;
import com.app.me.kapooktest.modelclass.ImagePath;
import com.app.me.kapooktest.modelclass.MediaDetail;
import com.app.me.kapooktest.modelclass.MediaLinkDetail;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

//import static com.app.me.kapooktest.helper.ManageFileHelper.TYPE_LINK.DEFAULT_LINK_IMAGE;

/**
 * Created by SuRiV on 22/5/2560.
 */

public class ManageFileHelper  {
     enum TYPE_LINK{
        DEFAULT_LINK_IMAGE,
        LINK_WEB_SITE,
        LINK_VDO
    }

    private AlertDialog.Builder adb;
    private Gson gson = new Gson();
    private  AQuery aQuery;
    private  Context context;
    private Activity activity;
    private Uri select_image_uri;
    private Bitmap bm = null;
    private File picFile;
    private String mimeType;
    public boolean PICTURE_STATUS = false;
    public int SELECT_PICTURE = 2222;
    public static final String TAG = "ajaxLoadedFromLink";

    public ManageFileHelper(Context context,int resultCode){
        select_image_uri = null;
        this.context = context;
        aQuery = new AQuery(context);
        SELECT_PICTURE = resultCode;
    }

    public ManageFileHelper(Activity act,int resultCode){
        select_image_uri = null;
        this.context = act.getApplicationContext();
        this.activity = act;
        aQuery = new AQuery(context);
        SELECT_PICTURE = resultCode;
    }

    public void createChoicePicture(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");

        try {
            ((Activity)context).startActivityForResult(intent, SELECT_PICTURE);
        }catch (ClassCastException cce){
            activity.startActivityForResult(intent, SELECT_PICTURE);
        }

    }

    public void onActivityResult(Intent data){
            select_image_uri = data.getData();
            if(select_image_uri != null){
                int columnIndex;
                String filePath;
                String[] imagePath = {MediaStore.Images.Media.DATA};
                Cursor cursor = context.getContentResolver().query(select_image_uri, imagePath, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    columnIndex = cursor.getColumnIndex(imagePath[0]);
                    filePath = cursor.getString(columnIndex);
                    final File f = new File(filePath);

                    try {
                        picFile = f;
                        mimeType = getMimeType(picFile,context);
                        bm = ExifUtils.decodeFile(filePath);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    cursor.close();
                }
            }
    }

    public Bitmap getBm() {
        return bm;
    }
    public File getPicFile(){ return  picFile;}
    public String getMimeType(){return  mimeType;}

    private  String getMimeType(File file, Context context)
    {
        Uri uri = Uri.fromFile(file);
        String mimeType;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = context.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }


    private class LoadMedia {
        String url;
        ImageView imageViewId;
        Object objectModel;
        VideoEnabledWebView webView;


        public LoadMedia(String url, View viewId, Object objectModel){
            if(viewId instanceof ImageView){
                imageViewId = (ImageView) viewId;
            } else if(viewId instanceof VideoEnabledWebView){
                webView = (VideoEnabledWebView)viewId;
            }else{
                return;
            }
            this.url = url;
            this.objectModel = objectModel;
        }



        public void fromWeb(final AlertDialog alertDialog ,final TextView txtShowError){
            ajaxLoadedFromLink(alertDialog,txtShowError,1);
        }

        private void ajaxLoadedFromLink(final AlertDialog alertDialog ,final TextView txtShowError,final int typeMedia){
            String image_url = "http://ts.entry.kapook.com/api/v1/entry/url?dev=1994&url="+url;
            aQuery.ajax(image_url, JSONObject.class,new AjaxCallback<JSONObject>(){
                @Override
                public void callback(String url, JSONObject object, AjaxStatus status) {
                    super.callback(url, object, status);
                    Log.d(TAG, "callback: "+gson.toJson(object));
                    int intStatus = 0;
                    try {
                        intStatus = (int)object.get("status");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "callback: intStatus = "+intStatus);
                    controlAlertDialog(alertDialog,intStatus,txtShowError);
                    if(intStatus != 200){
                        return;
                    }
                    ImageLink imageLink = gson.fromJson(object.toString(),ImageLink.class);
                    if(objectModel instanceof ImageLink){
                        objectModel = imageLink;
                        //final MediaLinkDetail descriptionModel = (MediaLinkDetail)objectModel;
                        //descriptionModel.setMediaLinkDetail(imageLink);
                        // aQuery.id(imageViewId).image()
                        switch(typeMedia){
                            case 1:
                                String web_mask = createWebViewByWebLink(imageLink);
                                Log.d(TAG, "callback: "+web_mask);
                                webView.loadData(web_mask,"text/html; charset=utf-8","UTF-8");
                                break;
                            case 2:
                                String view_mask="";
                                if(imageLink.getTypeembed()){
                                    view_mask = createWebViewByVDOLink(imageLink.getEmbed_url());
                                }else{
                                    view_mask= createWebViewByWebLink(imageLink);
                                }
                                webView.loadData(view_mask,"text/html; charset=utf-8","UTF-8");
                                break;
                        }

                        //String

                    }

                }
            });


        }

        private String createImageSrc(Bitmap bm){
            String html="<img src='{IMAGE_PLACEHOLDER}' />";

// Convert bitmap to Base64 encoded image for web
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String imgageBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
            String image = "data:image/png;base64," + imgageBase64;

// Use image for the img src parameter in your html and load to webview
            html = html.replace("{IMAGE_PLACEHOLDER}", image);
            return html;
        }

        private String createWebViewByWebLink(ImageLink imageLink){
            String image = imageLink.getImages().get(0).getUrl();
            Log.d(TAG, "createWebViewByWebLink: "+imageLink.getImages().get(0).getUrl());
            String imgDiv = "";
            if(image.length() > 1){
                Log.d(TAG, "createWebViewByWebLink: "+gson.toJson(imageLink.getImages().get(0)));
                if(image.indexOf("http") == -1){
                    if(image.startsWith("//")){
                        image = "http:"+image;
                    }else{
                        image = "http://"+image;
                    }

                }

                imgDiv="<div height=\"55%\" wight=\"100%\"> <img width=\"100%\" height=\"auto\" src=\""+image+"\"/></div>";
            }

           // String image = imghtml;
            String title = imageLink.getTitle();
            String description = imageLink.getDescription();
            String provider = imageLink.getProvider();
            String html =   "<a><div height=\"auto\" wight=\"100%\">"+
                                "<div height=\"auto\" wight=\"100%\">"+
                                    imgDiv +
                                "</div>"+
                                "<div style=\"display: inline-block; wight:auto; height:auto; padding:0 5px 0 10px;\">"+
                                    "<h3 style=\"width:100%; overflow:hidden; height:50px;line-height:25px; margin:0;\">"+title+"</h3>"+
                                    "<div style=\"width:auto;overflow:hidden; height:50px;line-height:25px;\">"+description+"</div>"+
                                    "<font size=\"2\" color=\"#999\">"+provider+"</font>"+
                                "</div>"+
                            "</div></a>";
            return html;
        }
        private String createWebViewByVDOLink(String embededUrl){
            String video = "<div ><span> <iframe width=\"100%\" height=\"100%\" src=\""+embededUrl+"\" frameborder=\"0\" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe> </span></div> ";
            return video;
        }

        public void fromLink(final AlertDialog alertDialog ,final TextView txtShowError){
            String image_url = "http://hilightad.kapook.com/lua/upload/entry?url="+url;
            aQuery.ajax(image_url, JSONObject.class,new AjaxCallback<JSONObject>(){
                @Override
                public void callback(String url, JSONObject object, AjaxStatus status) {
                    super.callback(url, object, status);
                    int intStatus = 0;

                    try {
                        intStatus = (int)object.get("status");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    controlAlertDialog(alertDialog,intStatus,txtShowError);
                    if(intStatus != 1){
                        return;
                    }
                    ImagePath imagePath = gson.fromJson(object.toString(),ImagePath.class);
                    if(objectModel instanceof HowToModel.DescriptionModel){
                        final HowToModel.DescriptionModel descriptionModel = (HowToModel.DescriptionModel)objectModel;
                        descriptionModel.mediaDetail.setMediaDetail(imagePath);

                        if(imageViewId != null){
                            aQuery.id(imageViewId).image("https://s359.kapook.com"+descriptionModel.mediaDetail.getImgurl(),true,false,0,0,new BitmapAjaxCallback(){
                                @Override
                                protected void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
                                    super.callback(url, iv, bm, status);
                                    Resources res= context.getResources();
                                    BitmapDrawable bDrawable = new BitmapDrawable(res, bm);
                                    float xdify = ((float)bDrawable.getIntrinsicWidth()/(float)bDrawable.getIntrinsicHeight());
                                    if(xdify<1.2){
                                        iv.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 240, context.getResources().getDisplayMetrics());
                                    }else{
                                        iv.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                                    }
                                    descriptionModel.setPicContent(bm);
                                }
                            });

                        }

                    }//End Of DescriptionModel

                    if(objectModel instanceof HowToModel){
                        final HowToModel howToModel = (HowToModel)objectModel;
                        HowToModel.mediaDetail.setMediaDetail(imagePath);
                        aQuery.id(imageViewId).image("https://s359.kapook.com"+ HowToModel.mediaDetail.getImgurl(),false,false,0,0,new BitmapAjaxCallback(){
                            @Override
                            protected void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
                                super.callback(url, iv, bm, status);
                                HowToModel.PIC_TITLE = bm;

                            }
                        });
                    }//End Of HowToModel

                    if(objectModel instanceof MediaDetail){
                        MediaDetail mediaDetail = (MediaDetail) objectModel;
                        mediaDetail.setMediaDetail(imagePath);
                        aQuery.id(imageViewId).image("https://s359.kapook.com"+ mediaDetail.getImgurl(),false,false,0,0,new BitmapAjaxCallback(){
                            @Override
                            protected void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
                                super.callback(url, iv, bm, status);
                            }
                        });
                    }

                }

            });
        }

    }

    public AlertDialog createDialogInputLink(final View viewTitle, final Object objectModel, final int linkType){
        if(activity != null){
            adb = new AlertDialog.Builder(activity);
        }else{
            adb = new AlertDialog.Builder(context);
        }

        final FrameLayout frameView = new FrameLayout(context);
        adb.setIcon(R.drawable.ic_link_black);
        adb.setTitle("ลิ้งค์...");
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
                    txtHintInputLink.setVisibility(View.VISIBLE);
                    txtHintInputLink.setText("กรุณาใส่ http:// หรือ https:// และตามด้วย url");
                    return;
                }

                if (edtInputLink.getText().toString().indexOf("http") == -1) {
                    txtHintInputLink.setVisibility(View.VISIBLE);
                    txtHintInputLink.setText("กรุณาใส่ http:// หรือ https:// ไว้ด้านหน้า url");
                    return;
                }
                LoadMedia loadMedia;
                String url = edtInputLink.getText().toString();
                TYPE_LINK type_link = TYPE_LINK.values()[linkType];
                Log.d("linkType", "onClick: "+linkType);
                Log.d("TYPE_LINK", "onClick: "+TYPE_LINK.values()[linkType]);
                switch (type_link){
                    case DEFAULT_LINK_IMAGE :
                            loadMedia = new LoadMedia(url,viewTitle,objectModel);
                            loadMedia.fromLink(alertDialog,txtHintInputLink);
                        break;

                    case LINK_WEB_SITE:
                        Log.d("LINK_WEB_SITE", "onClick: LoadMedia");
                        loadMedia = new LoadMedia(url,viewTitle,objectModel);
                        loadMedia.fromWeb(alertDialog,txtHintInputLink);
                        break;

                    case LINK_VDO:

                        break;
                }

                // switchLayoutParagraph1(true);
            }
        });

        alertDialog.setCanceledOnTouchOutside(true);

        return alertDialog;
    }

    public AlertDialog createDialogInputLink(final ImageView imgTitle,final Object objectModel) {
        //https://siamblockchain.com/wp-content/uploads/2017/06/Bitcoin-vs-Ethereum-1.png
        return createDialogInputLink(imgTitle,objectModel,0);
    }


    private static class ExifUtils {

        public static Bitmap rotateBitmap(String src, Bitmap bitmap) {
            try {
                int orientation = getExifOrientation(src);

                if (orientation == 1) {
                    return bitmap;
                }

                Matrix matrix = new Matrix();
                switch (orientation) {
                    case 2:
                        matrix.setScale(-1, 1);
                        break;
                    case 3:
                        matrix.setRotate(180);
                        break;
                    case 4:
                        matrix.setRotate(180);
                        matrix.postScale(-1, 1);
                        break;
                    case 5:
                        matrix.setRotate(90);
                        matrix.postScale(-1, 1);
                        break;
                    case 6:
                        matrix.setRotate(90);
                        break;
                    case 7:
                        matrix.setRotate(-90);
                        matrix.postScale(-1, 1);
                        break;
                    case 8:
                        matrix.setRotate(-90);
                        break;
                    default:
                        return bitmap;
                }

                try {
                    Bitmap oriented = Bitmap.createBitmap(bitmap, 0, 0,
                            bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    bitmap.recycle();
                    return oriented;
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                    return bitmap;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        private static int getExifOrientation(String src) throws IOException, SecurityException, IllegalArgumentException {
            int orientation = 1;

            try {
                /**
                 * if your are targeting only api level >= 5 ExifInterface exif =
                 * new ExifInterface(src); orientation =
                 * exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                 */
                if (Build.VERSION.SDK_INT >= 5) {
                    Class<?> exifClass = Class
                            .forName("android.media.ExifInterface");
                    Constructor<?> exifConstructor = exifClass
                            .getConstructor(String.class);
                    Object exifInstance = exifConstructor
                            .newInstance(src);
                    Method getAttributeInt = exifClass.getMethod("getAttributeInt",
                            String.class, int.class);
                    Field tagOrientationField = exifClass
                            .getField("TAG_ORIENTATION");
                    String tagOrientation = (String) tagOrientationField.get(null);
                    orientation = (Integer) getAttributeInt.invoke(exifInstance,
                            tagOrientation, 1);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }

            return orientation;
        }

        private static Bitmap decodeFile(String filePath) {

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, o);

            // The new size we want to scale to
            final int REQUIRED_SIZE = 1024;

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            Bitmap b1 = BitmapFactory.decodeFile(filePath, o2);
            Bitmap b= ExifUtils.rotateBitmap(filePath, b1);

            // image.setImageBitmap(bitmap);
            return b;
        }
    }


    private void controlAlertDialog(AlertDialog alertDialog, int loadStatus , TextView txtShowError){
        if(alertDialog == null){
            return;
        }

        switch (loadStatus){
            case 1:
                PICTURE_STATUS = true;
                alertDialog.cancel();
                return;
            case 200:
                PICTURE_STATUS = true;
                alertDialog.cancel();
                return;
            case 501:
                if(txtShowError != null){
                    txtShowError.setText("ไม่สามารถดึงข้อมูลลิ้งก์นี้ได้ กรุณาตรวจสอบ url");
                    txtShowError.setVisibility(View.VISIBLE);
                    txtShowError.requestLayout();
                }
                PICTURE_STATUS = false;
            case 3:
                if(txtShowError != null){
                    txtShowError.setText("ไม่สามารถดึงข้อมูลลิ้งก์นี้ได้ กรุณาตรวจสอบ url");
                    txtShowError.setVisibility(View.VISIBLE);
                    txtShowError.requestLayout();
                }
                PICTURE_STATUS = false;
                return;
        }

    }


}
