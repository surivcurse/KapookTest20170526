package com.app.me.kapooktest.customclass;

import android.animation.ObjectAnimator;
import android.app.Activity;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.app.me.kapooktest.R;
import com.app.me.kapooktest.helper.LoadBitmap;
import com.app.me.kapooktest.helper.ManageFileHelper;
import com.app.me.kapooktest.modelclass.HowToModel;
import com.app.me.kapooktest.modelclass.ImagePath;
import com.app.me.kapooktest.modelclass.MediaDetail;
import com.google.gson.Gson;

import static com.app.me.kapooktest.modelclass.HowToModel.*;

import java.io.File;

import java.io.IOException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.client.methods.CloseableHttpResponse;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.mime.HttpMultipartMode;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;
import cz.msebera.android.httpclient.entity.mime.content.ContentBody;
import cz.msebera.android.httpclient.entity.mime.content.FileBody;
import cz.msebera.android.httpclient.impl.client.CloseableHttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.util.EntityUtils;

/**
 * Created by SuRiV on 7/4/2560.
 */

public class HowToDescriptionRcvAdapter extends RecyclerView.Adapter<HowToDescriptionRcvAdapter.ViewHolder> {
    private ArrayList<DescriptionModel> contents;
    private Context context;
    private View rootView;
    private AlertDialog.Builder adb;
    private static int currentDragPosition;
    private ViewHolder currentHolder;
    private ViewHolder addPictureHolder;
    public static boolean isAddNewItem = false;
    private static boolean isEntered = false;
    private static int lastEnter = 0;
    private RecyclerView mRecyclerView;
    private static int[] VIEW_LOCATION = new int[2];
    public static int[] RECYCLER_LOCATION = new int[2];
    private ManageFileHelper manageFileHelper;
    private AQuery aQuery;
    private Gson gson = new Gson();
    public static final int RESULT_SELECT_PICTURE = 9595;


    public HowToDescriptionRcvAdapter(ArrayList<DescriptionModel> contents) {
        this.contents = contents;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Layout Content_Paragraph3
        private LinearLayout llRenderingContrainer;

        // Layout For Edit Content
        private Bitmap bm;
        private RelativeLayout rlDescriptionEdit;
        private TextView txtEditNumber;
        private ImageButton btnDelete;
        private ImageButton btnDragItem;
        private EditText edtDescription;
        private Button btnAddPhoto;
        private ImageButton btnDragUp;
        private ImageButton btnDragDown;

        // Layout For View Content
        private RelativeLayout rlDescriptionView;
        private TextView txtViewNumber;
        private ImageButton btnDeleteView;
        private TextView txtViewDescription;
        private ImageView imgViewDescription;
        private ImageButton imgBtnSetting;
        private ImageButton imgBtnDeletePic;
        private boolean isTyping;
        // End Layout Content_Paragraph3
        private ImageView imageView2;

        public ViewHolder(View view) {
            super(view);

            rootView = view;
            bm=null;
            settingLayoutEditContent(view);
            settingLayoutViewContent(view);



        }

        private void settingLayoutViewContent(View view){
            llRenderingContrainer = (LinearLayout) view.findViewById(R.id.llRenderingContrainer);
            rlDescriptionView = (RelativeLayout)  view.findViewById(R.id.rlDescriptionView);
            txtViewNumber = (TextView)  view.findViewById(R.id.txtViewNumber);
            btnDeleteView = (ImageButton)  view.findViewById(R.id.btnDeleteView);
            txtViewDescription = (TextView)  view.findViewById(R.id.txtViewDescription);
            imgViewDescription = (ImageView)  view.findViewById(R.id.imgViewDescription);
            imgBtnSetting = (ImageButton)  view.findViewById(R.id.imgBtnSetting);
            imgBtnDeletePic = (ImageButton)  view.findViewById(R.id.imgBtnDeletePic);
        }

        private void settingLayoutEditContent(View view){
            //Generate Layout For Edit Content
            rlDescriptionEdit  = (RelativeLayout)  view.findViewById(R.id.rlDescriptionEdit);
            txtEditNumber = (TextView)  view.findViewById(R.id.txtEditNumber);
            btnDelete = (ImageButton)  view.findViewById(R.id.btnDelete);
            btnDragItem= (ImageButton)  view.findViewById(R.id.btnDragItem);
            edtDescription = (EditText)  view.findViewById(R.id.edtDescription);
            btnAddPhoto = (Button)  view.findViewById(R.id.btnAddPhoto);

            imageView2 = (ImageView) view.findViewById(R.id.imageView2);

            btnDragUp = (ImageButton)  view.findViewById(R.id.btnDragUp);
            btnDragDown = (ImageButton)  view.findViewById(R.id.btnDragDown);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_description_howto, parent, false);
        mRecyclerView = (RecyclerView) parent;
        context = parent.getContext();
        aQuery = new AQuery(context);
        manageFileHelper = new ManageFileHelper(context,RESULT_SELECT_PICTURE);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final DescriptionModel content = contents.get(position);
        int index = position+1;
        holder.bm = null;
        holder.txtViewNumber.setText(String.valueOf(index));
        holder.txtEditNumber.setText(String.valueOf(index));
        layoutStart(holder,content);
        setActionOnClickListener(holder,content);
        setActionOnFocusListener(holder,content);
        setActionOnDragEvent(holder,content);
        holder.llRenderingContrainer.setTag("");

        if(getItemCount() == 1){
            currentHolder = holder;
        }
        if(isAddNewItem && holder.getLayoutPosition() == getItemCount()-1){
            if(getItemCount() == 2){

                layoutStart(currentHolder,null);
            }
            switchLayout(holder,true,content);
        }

        // watchIsTyping(holder,content);

    }

    public void layoutStart(final ViewHolder holder, DescriptionModel content){
        if(content != null){
            holder.txtViewDescription.setText(content.getTxtContent());
            holder.edtDescription.setText(content.getTxtContent());
            setImageViewDescription(holder,content.getPicContent());
        }

        if(getItemCount() > 1){
            holder.btnDeleteView.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnDragItem.setVisibility(View.VISIBLE);
            holder.btnDragUp.setVisibility(View.VISIBLE);
            holder.btnDragDown.setVisibility(View.VISIBLE);
        }else{
            holder.btnDeleteView.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.GONE);
            holder.btnDragItem.setVisibility(View.GONE);
            holder.btnDragUp.setVisibility(View.INVISIBLE);
            holder.btnDragDown.setVisibility(View.INVISIBLE);
        }



    }

    public void resetValue(ViewHolder holder){
        holder.txtViewDescription.setText("");
        holder.edtDescription.setText("");
        holder.bm=null;
    }

    public void setActionOnClickListener(final ViewHolder holder,final DescriptionModel content){
        holder.rlDescriptionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchLayout(holder,true,content);
            }
        });

        holder.btnDeleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(holder,content);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(holder,content);
            }
        });

        holder.btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogChoicePicture(holder,content);

            }
        });

        holder.imgBtnDeletePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.bm = null;
                setImageViewDescription(holder,holder.bm);
                content.setPicContent(holder.bm);
            }
        });

        holder.imgBtnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogChoicePicture(holder,content);
            }
        });



    }

    private void showDialogChoicePicture(final ViewHolder holder,final DescriptionModel content){

        final FrameLayout frameView = new FrameLayout(context);
        adb = new AlertDialog.Builder(context);
        adb.setIcon(R.drawable.ic_picture_black);
        adb.setTitle("เพิ่มรูปภาพ");
        adb.setView(frameView);
        final AlertDialog alertDialog = adb.create();
        LayoutInflater inflater = alertDialog.getLayoutInflater();

        View dialoglayout = inflater.inflate(R.layout.dialog_choice_picture, frameView);

        Button btnPhotoDevice = (Button) dialoglayout.findViewById(R.id.btnPhotoDevice);
        btnPhotoDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // holder.bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.campusstar_line);
                switchLayout(holder,false,content);
                addPictureHolder = holder;
                manageFileHelper.createChoicePicture();
                alertDialog.cancel();
            }
        });
        Button btnPhotoLink = (Button) dialoglayout.findViewById(R.id.btnPhotoLink);
        btnPhotoLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //holder.bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.pexels_photo_title);

                showDialogInputLink(holder,content,"เพิ่มลิงก์...");
                alertDialog.cancel();
            }
        });

        Button btnPhotoCamera = (Button) dialoglayout.findViewById(R.id.btnPhotoCamera);
        btnPhotoCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.picture_camera);
                switchLayout(holder,false,content);
                alertDialog.cancel();
            }
        });

        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();

    }

    private void showDialogInputLink(final ViewHolder holder, final DescriptionModel content, String title){

        final FrameLayout frameView = new FrameLayout(context);
        adb = new AlertDialog.Builder(context);
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
                if(edtInputLink.getText().toString().length() < 1){
                    alertDialog.cancel();
                    return;
                }

                if(edtInputLink.getText().toString().indexOf("http") == -1){
                    txtHintInputLink.setVisibility(View.VISIBLE);
                    txtHintInputLink.setText("กรุณาใส่ http:// หรือ https:// ไว้ด้านหน้า url");
                    alertDialog.cancel();
                    return;
                }

                manageFileHelper.loadImageFromLink(edtInputLink.getText().toString(),holder.imgViewDescription,content);
                holder.edtDescription.clearFocus();
                holder.rlDescriptionView.setVisibility(View.VISIBLE);
                holder.rlDescriptionEdit.setVisibility(View.GONE);
                holder.btnAddPhoto.setVisibility(View.INVISIBLE);
                holder.imgBtnSetting.setVisibility(View.VISIBLE);
                holder.imgBtnDeletePic.setVisibility(View.VISIBLE);
                holder.imgViewDescription.requestLayout();

                alertDialog.cancel();
            }
        });

        edtInputLink.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    txtHintInputLink.setVisibility(View.VISIBLE);
                }else{
                    txtHintInputLink.setVisibility(View.INVISIBLE);
                }
            }
        });
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();


    }

    private void setImageViewDescription(final ViewHolder holder, Bitmap picContent){
        if(picContent != null){
            Resources res= context.getResources();
            BitmapDrawable bDrawable = new BitmapDrawable(res, picContent);
            float xdify = ((float)bDrawable.getIntrinsicWidth()/(float)bDrawable.getIntrinsicHeight());
            if(xdify<1.2){
                int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 240, context.getResources().getDisplayMetrics());
                holder.imgViewDescription.getLayoutParams().height = dimensionInDp;
            }else{
                holder.imgViewDescription.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            }
            Log.d("IMG", "setImageViewDescription: ");

            holder.imgViewDescription.requestLayout();

            holder.btnAddPhoto.setVisibility(View.INVISIBLE);
            holder.imgBtnSetting.setVisibility(View.VISIBLE);
            holder.imgBtnDeletePic.setVisibility(View.VISIBLE);
        }else{
            int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, context.getResources().getDisplayMetrics());
            holder.imgViewDescription.getLayoutParams().height = dimensionInDp;
            holder.imgViewDescription.requestLayout();
            holder.btnAddPhoto.setVisibility(View.VISIBLE);
            holder.imgBtnSetting.setVisibility(View.INVISIBLE);
            holder.imgBtnDeletePic.setVisibility(View.INVISIBLE);
        }

        holder.imgViewDescription.setImageBitmap(picContent);

    }

    private void removeItem(final ViewHolder holder,final DescriptionModel content){
        adb = new AlertDialog.Builder(context);
        adb.setTitle("ยืนยันลบขั้นตอนนี้?");
        adb.setMessage("");
        adb.setNegativeButton("NO", null);
        adb.setPositiveButton("YES", new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                // TODO Auto-generated method stub
                content.setPicContent(null);
                contents.remove(content);
                resetValue(holder);
                holder.bm = null;
                holder.imgViewDescription.setImageBitmap(null);
                notifyDataSetChanged();
            }
        });
        adb.show();
    }

    private void setActionOnFocusListener(final ViewHolder holder,final DescriptionModel content){
        holder.edtDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    switchLayout(holder,false,content);
                }
            }
        });

    }

    private void switchLayout(ViewHolder holder,boolean isEdit,DescriptionModel content){

        if(isEdit){
            if(currentHolder != null){
                switchLayout(currentHolder,false,content);
            }
            currentHolder = holder;
            holder.rlDescriptionView.setVisibility(View.GONE);
            holder.rlDescriptionEdit.setVisibility(View.VISIBLE);
            holder.edtDescription.setFocusable(true);
            holder.edtDescription.requestFocus();
        }else{
            holder.edtDescription.clearFocus();
            holder.rlDescriptionView.setVisibility(View.VISIBLE);
            holder.rlDescriptionEdit.setVisibility(View.GONE);

            if(content != null){
                whenSwitchToView(holder,content);
            }
        }
    }

    private void whenSwitchToView(ViewHolder holder, DescriptionModel content){
        content.setTxtContent(holder.edtDescription.getText().toString());

        holder.txtViewDescription.setText(content.getTxtContent());

        if(holder.bm != null){

            content.setPicContent(holder.bm);
            setImageViewDescription(holder, content.getPicContent());
            holder.btnAddPhoto.setVisibility(View.INVISIBLE);
        }else{
            holder.btnAddPhoto.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    public void onBackPressed(){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
        switchLayout(currentHolder,false,null);

    }

    protected  void itemMove(final ViewHolder holder,int upPosition,int downPosition){
        ViewHolder vHolder = (ViewHolder)mRecyclerView.findViewHolderForLayoutPosition(downPosition);


        vHolder.txtEditNumber.setText(String.valueOf(upPosition+1));
        vHolder.txtViewNumber.setText(String.valueOf(upPosition+1));
        holder.txtEditNumber.setText(String.valueOf(downPosition+1));
        holder.txtViewNumber.setText(String.valueOf(downPosition+1));

        notifyItemMoved(upPosition,downPosition);
        swapContent(contents,upPosition,downPosition);

    }

    private void setActionOnDragEvent(final ViewHolder holder, final DescriptionModel content){

        holder.btnDragUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int focusPosition = holder.getLayoutPosition();
                int upPosition = focusPosition-1;


                if(focusPosition != 0){
                    //Select Upper Item In RecyclerView
                    itemMove(holder,focusPosition,upPosition);
                    // mRecyclerView.scrollToPosition(focusPosition);
                    int scrollTo = 0;
                    NestedScrollView nestedView = (NestedScrollView) mRecyclerView.getParent().getParent().getParent().getParent();
                    scrollTo = nestedView.getScrollY() - (holder.itemView.getHeight());
                    ObjectAnimator.ofInt(nestedView, "scrollY", scrollTo).setDuration(1000).start();
                }
            }
        });
        holder.btnDragDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int focusPosition = holder.getLayoutPosition();
                int downPosition = focusPosition+1;
                if(focusPosition != getItemCount()-1){
                    //Select Upper Item In RecyclerView
                    itemMove(holder,focusPosition,downPosition);
                    int scrollTo = 0;
                    NestedScrollView nestedView = (NestedScrollView) mRecyclerView.getParent().getParent().getParent().getParent();
                    scrollTo = nestedView.getScrollY() + (holder.itemView.getMeasuredHeight());
                    ObjectAnimator.ofInt(nestedView, "scrollY", scrollTo).setDuration(1000).start();
                }
            }
        });

        holder.btnDragItem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                    ClipData.Item item = new ClipData.Item(holder.llRenderingContrainer.getTag().toString());
                    ClipData dragData = new ClipData(holder.llRenderingContrainer.getTag().toString(),mimeTypes,item);
                    View.DragShadowBuilder myShadow = new View.DragShadowBuilder(holder.itemView);
                    myShadow.getView().setAlpha(1);
                    //switchLayout(holder,false,content);

                    currentHolder = holder;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        holder.llRenderingContrainer.startDragAndDrop(dragData, myShadow  , null, 0);
                    } else {
                        holder.llRenderingContrainer.startDrag(dragData, myShadow, null, 0);
                    }
                }

                if(event.getAction() == MotionEvent.ACTION_CANCEL){
                    currentDragPosition = holder.getLayoutPosition();

                    onBackPressed();
                }

                return false;
            }
        });

        holder.btnDragItem.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                currentHolder = holder;

                return false;
            }
        });

        holder.rlDescriptionEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("Eventz", "onTouch: "+event.getAction());
                if(event.getAction()==MotionEvent.ACTION_DOWN){

                    currentHolder = holder;
                }
                if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    currentDragPosition = holder.getLayoutPosition();
                }
                return false;
            }
        });

        holder.rlDescriptionEdit.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(getItemCount() == 1){
                    return false;
                }
                String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                ClipData.Item item = new ClipData.Item(holder.llRenderingContrainer.getTag().toString());
                ClipData dragData = new ClipData(holder.llRenderingContrainer.getTag().toString(),mimeTypes,item);
                View.DragShadowBuilder myShadow = new View.DragShadowBuilder(holder.itemView);
                myShadow.getView().setAlpha(1);
                // switchLayout(holder,false,content);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    holder.llRenderingContrainer.startDragAndDrop(dragData, myShadow  , null, 0);
                } else {
                    holder.llRenderingContrainer.startDrag(dragData, myShadow, null, 0);
                }

                return true;
            }
        });

        holder.llRenderingContrainer.setOnDragListener(new View.OnDragListener() {

            @Override
            public boolean onDrag(View v, DragEvent event) {
                if(currentHolder == null){
                    return false;
                }

                final int fromPosition = currentHolder.getLayoutPosition();
                final int toPosition = holder.getLayoutPosition();

                NestedScrollView nestedView = (NestedScrollView) mRecyclerView.getParent().getParent().getParent().getParent();
                int ItemHeight = holder.itemView.getMeasuredHeight();
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        return true;

                    case DragEvent.ACTION_DRAG_ENTERED:

                        if(fromPosition != toPosition){
                            isEntered = true;
                            lastEnter = toPosition;
                            mRecyclerView.getLocationOnScreen(RECYCLER_LOCATION);
                            v.getLocationOnScreen(VIEW_LOCATION);
                            new Handler().postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    notifyItemMoved(fromPosition,toPosition);
                                    holder.txtEditNumber.setText(String.valueOf(fromPosition+1));
                                    currentHolder.txtEditNumber.setText(String.valueOf(toPosition+1) );

                                    holder.txtViewNumber.setText(String.valueOf(fromPosition+1) );
                                    currentHolder.txtViewNumber.setText(String.valueOf(toPosition+1));

                                }
                            }, 100);
                        }
                        return true;

                    case DragEvent.ACTION_DROP:
                        //Log.d("RenderView", "onDrag: ACTION_DROP "+currentDragPosition+" To "+toPosition);
                        isEntered = false;
                        swapContent(contents,currentDragPosition,toPosition);
                        switchLayout(holder,false,content);
                        return true;
                    case DragEvent.ACTION_DRAG_ENDED:

                        if(lastEnter == toPosition){
                            if(isEntered ){
                                Log.d("RenderView", "onDrag: ACTION_DRAG_ENDED "+currentDragPosition+" To "+toPosition+"\n");
                                swapContent(contents,currentDragPosition,toPosition);
                                isEntered = false;
                            }
                            switchLayout(holder,true,content);
                        }
                        return false;

                    case DragEvent.ACTION_DRAG_LOCATION:
                        int translatedY = VIEW_LOCATION[1];
                        int rec_translatedY = RECYCLER_LOCATION[1];

                        int threshold = ItemHeight/2;

                        if (translatedY < threshold && rec_translatedY < threshold) {

                            // make a scroll up by 30 px
                            if(toPosition < fromPosition ){

                                int scrollTo;
                                if(toPosition==0){
                                    scrollTo= nestedView.getScrollY() - threshold;
                                }else{
                                    scrollTo = nestedView.getScrollY() - ItemHeight;
                                }
                                ObjectAnimator.ofInt(nestedView,"scrollY",scrollTo).setDuration(1000).start();
                            }
                        } else {
                            // make a autoscrolling down due y has passed the 500 px border
                            if (toPosition > fromPosition) {
                                Log.d("RenderView", "translatedY + threshold : "+(translatedY + threshold)+" > (ItemHeight+450) "+(ItemHeight+450));
                                if (translatedY + threshold > (ItemHeight + 450)) {
                                    int scrollTo;
                                    // make a scroll down by 30 px
                                    if (toPosition == getItemCount() - 1) {
                                        scrollTo = nestedView.getScrollY() + (threshold);
                                    } else {
                                        scrollTo = nestedView.getScrollY() + ItemHeight;
                                    }
                                    ObjectAnimator.ofInt(nestedView, "scrollY", scrollTo).setDuration(1000).start();
                                }
                            }
                        }

                        break;
                }
                return false;
            }
        });
    }

    public void onActivityResult(Intent data){
        final MediaDetail mediaDetail = contents.get(addPictureHolder.getLayoutPosition()).mediaDetail;

        if(data != null){
            new LoadBitmap(data,mediaDetail,manageFileHelper){
                @Override
                public void onPostExecute(Void aVoid){
                    super.onPostExecute(aVoid);
                    addPictureHolder.bm = manageFileHelper.getBm();
                    setImageViewDescription(addPictureHolder, addPictureHolder.bm);
                    contents.get(addPictureHolder.getLayoutPosition()).setPicContent(addPictureHolder.bm);
                }
            }.execute();
        }

    }

    public void setDescriptionSomeError(){
        if(currentHolder.getLayoutPosition() == 0){
            if(currentHolder.txtViewDescription.getText().length() < 1){
                currentHolder.txtViewDescription.setHint("****กรุณากรอกข้อมูล****");
                currentHolder.txtViewDescription.setHintTextColor(context.getColor(R.color.tw__composer_red));
            }
        }
    }



}