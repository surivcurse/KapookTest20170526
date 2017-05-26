package com.app.me.kapooktest.helper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by SuRiV on 22/5/2560.
 */

public class ManageFileHelper  {

    private Context context;
    private Uri select_image_uri;
    private Bitmap bm = null;
    public static final int SELECT_PICTURE = 2222;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public ManageFileHelper(Context context){
        select_image_uri = null;
        this.context = context;
        verifyStoragePermissions((Activity)context);
    }

    public void createChoicePicture(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        ((Activity)context).startActivityForResult(intent, SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == SELECT_PICTURE){
            select_image_uri = data.getData();
            if(select_image_uri != null){
                //final BitmapFactory.Options options = new BitmapFactory.Options();
                String[] imagePath = {MediaStore.Images.Media.DATA};
                Cursor cursor = context.getContentResolver().query(select_image_uri, imagePath, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(imagePath[0]);
                String filePath = cursor.getString(columnIndex);

                cursor.close();
                final File f = new File(filePath);
                //bm = BitmapFactory.decodeFile(filePath);
                //options.inSampleSize = 2;
                try {

                   // bm = BitmapFactory.decodeStream(new FileInputStream(f),null,options);
                    bm = ExifUtils.decodeFile(filePath);
                } catch (Exception e) {
                        e.printStackTrace();
                }
                if(bm != null)
                Log.d("ActivityResult", "onActivityResult: "+f.getAbsolutePath());
            }
        }
    }

    public Bitmap getBm() {
        return bm;
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
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

        public static Bitmap decodeFile(String filePath) {

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
