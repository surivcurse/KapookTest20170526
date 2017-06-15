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
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.app.me.kapooktest.modelclass.HowToModel;
import com.app.me.kapooktest.modelclass.ImagePath;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by SuRiV on 22/5/2560.
 */

public class ManageFileHelper  {

    private Gson gson = new Gson();
    private  AQuery aQuery;
    private  Context context;
    private Activity activity;
    private Uri select_image_uri;
    private Bitmap bm = null;
    private File picFile;
    private String mimeType;
    public int SELECT_PICTURE = 2222;

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
                String[] imagePath = {MediaStore.Images.Media.DATA};
                Cursor cursor = context.getContentResolver().query(select_image_uri, imagePath, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(imagePath[0]);
                String filePath = cursor.getString(columnIndex);

                cursor.close();
                final File f = new File(filePath);

                try {
                    picFile = f;
                    mimeType = getMimeType(picFile,context);
                    bm = ExifUtils.decodeFile(filePath);
                } catch (Exception e) {
                        e.printStackTrace();
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
        Log.d("IMAGEUPLOAD", "getMimeType: "+mimeType);
        return mimeType;
    }



    public  void loadImageFromLink(String url, final ImageView imageViewId, final Object objectModel){

        String image_url = "http://hilightad.kapook.com/lua/upload/entry?url="+url;
        aQuery.ajax(image_url, JSONObject.class,new AjaxCallback<JSONObject>(){
            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                super.callback(url, object, status);
                ImagePath imagePath = gson.fromJson(object.toString(),ImagePath.class);
                if(imagePath.getStatus() == 1){
                    if(objectModel instanceof HowToModel.DescriptionModel){
                        final HowToModel.DescriptionModel descriptionModel = (HowToModel.DescriptionModel)objectModel;
                        descriptionModel.mediaDetail.setMediaDetail(imagePath);
                        if(imageViewId != null){

                            aQuery.id(imageViewId).image("https://s359.kapook.com"+descriptionModel.mediaDetail.getImgurl(),true,true,0,0,new BitmapAjaxCallback(){
                                @Override
                                protected void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
                                    super.callback(url, iv, bm, status);
                                    Resources res= context.getResources();
                                    BitmapDrawable bDrawable = new BitmapDrawable(res, bm);
                                    float xdify = ((float)bDrawable.getIntrinsicWidth()/(float)bDrawable.getIntrinsicHeight());
                                    if(xdify<1.2){
                                        int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 240, context.getResources().getDisplayMetrics());
                                        iv.getLayoutParams().height = dimensionInDp;
                                    }else{
                                        iv.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                                    }
                                    Log.d("IMG", "setImageViewDescription: ");

                                    descriptionModel.setPicContent(bm);
                                }
                            });

                        }

                    }
                }//End Of DescriptionModel

                if(objectModel instanceof HowToModel){
                    final HowToModel howToModel = (HowToModel)objectModel;
                    HowToModel.mediaDetail.setMediaDetail(imagePath);
                    aQuery.id(imageViewId).image("https://s359.kapook.com"+ HowToModel.mediaDetail.getImgurl(),true,true,0,0,new BitmapAjaxCallback(){
                        @Override
                        protected void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
                            super.callback(url, iv, bm, status);
                            HowToModel.PIC_TITLE = bm;

                        }
                    });
                }//End Of HowToModel

            }

        });
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

        private static int getExifOrientation(String src) throws IOException {
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
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
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




}
