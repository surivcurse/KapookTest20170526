package com.app.me.kapooktest.customclass;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.me.kapooktest.R;
import com.app.me.kapooktest.helper.KapookPostContentHelper;
import com.app.me.kapooktest.helper.LoadBitmap;
import com.app.me.kapooktest.helper.ManageFileHelper;
import com.app.me.kapooktest.helper.VideoEnabledWebChromeClient;
import com.app.me.kapooktest.helper.VideoEnabledWebView;
import com.app.me.kapooktest.modelclass.ConstantModel;
import com.app.me.kapooktest.modelclass.MediaDetail;

import static com.app.me.kapooktest.modelclass.EntryModel.*;



import java.util.ArrayList;

/**
 * Created by SuRiV on 7/4/2560.
 */

public class EntryRcvAdapter extends RecyclerView.Adapter<EntryRcvAdapter.ViewHolder> {
    private ArrayList<EntryContentModel> contents;
    private static ViewHolder addPictureHolder;
    private static ViewHolder currentViewHolder;
    public static boolean isNotifyChange = true;
    private static int currentDragPosition;
    private Context context;
    private AlertDialog.Builder adb;
    private View rootView;
    public static int MAX_HEIGHT = 0;
    public  int NEW_LAYOUT = 0;
    private static final String TAG = "Entry";
    private RecyclerView mRecyclerView;
    public static int[] VIEW_LOCATION = new int[2];
    public static int[] RECYCLER_LOCATION = new int[2];
    private static boolean isEntered = false;
    private static int lastEnter = 0;
    public static final int RESULT_SELECT_PICTURE = 9596;
    private KapookPostContentHelper kapookPostContentHelper;

    NestedScrollView  nestedView;
    private ManageFileHelper manageFileHelper;

    public EntryRcvAdapter(ArrayList<EntryContentModel> contents) {
        this.contents = contents;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout llRenderingContrainer;

        private LinearLayout llEntryContent;
        private LinearLayout llButtonAddItem;
        private ImageButton btnAddPhoto;
        private ImageButton btnAddVideo;
        private ImageButton btnAddLink;
        private ImageButton btnDragUp;
        private ImageButton btnDragDown;

        private RelativeLayout rlEntryEdit;
        private ImageButton btnDragItem;
        private ImageButton btnDelete;
        private TextView txtEditNumber;
        private EditText edtEntryContent;

        private RelativeLayout rlEntryView;
        private TextView txtViewNumber;
        private TextView txtViewEntryContent;

        private RelativeLayout rlEntryImageView;
        private ImageView imgViewDescription;
        private ImageButton imgBtnDeletePic;

        private VideoEnabledWebView webViewDescription;
        private VideoEnabledWebChromeClient webChromeClient;
        private RelativeLayout rlWebview;
        private RelativeLayout rlEntryWebView;
        private ImageButton imgBtnDeleteWeb;

        private boolean isTyping = false;

        private boolean currentViewIsEdit = false;

        public ViewHolder(View view) {
            super(view);
            rootView = view;
            llRenderingContrainer = (LinearLayout) view.findViewById(R.id.llRenderingContrainer);

            llButtonAddItem = (LinearLayout) view.findViewById(R.id.llButtonAddItem);
            btnAddPhoto = (ImageButton) view.findViewById(R.id.btnAddPhoto);
            btnAddVideo = (ImageButton) view.findViewById(R.id.btnAddVideo);
            btnAddLink = (ImageButton) view.findViewById(R.id.btnAddLink);
            btnDragUp = (ImageButton)  view.findViewById(R.id.btnDragUp);
            btnDragDown = (ImageButton)  view.findViewById(R.id.btnDragDown);

            llEntryContent = (LinearLayout) view.findViewById(R.id.llDescriptionContent);

            rlEntryEdit = (RelativeLayout) view.findViewById(R.id.rlEntryEdit);
            txtEditNumber = (TextView) view.findViewById(R.id.txtEditNumber);
            btnDragItem = (ImageButton) view.findViewById(R.id.btnDragItem);
            btnDelete = (ImageButton) view.findViewById(R.id.btnDelete);
            edtEntryContent = (EditText) view.findViewById(R.id.edtEntryContent);

            rlEntryView = (RelativeLayout) view.findViewById(R.id.rlEntryView);
            txtViewNumber = (TextView) view.findViewById(R.id.txtViewNumber);
            txtViewEntryContent = (TextView) view.findViewById(R.id.txtDescriptionContent);

            rlEntryImageView = (RelativeLayout) view.findViewById(R.id.rlEntryImageView);
            imgViewDescription = (ImageView) view.findViewById(R.id.imgViewDescription);
            webViewDescription = (VideoEnabledWebView) view.findViewById(R.id.webView);
            rlWebview = (RelativeLayout) view.findViewById(R.id.rlWebview);
            rlEntryWebView = (RelativeLayout) view.findViewById(R.id.rlEntryWebView);
// Initialize the VideoEnabledWebChromeClient and set event handlers
            View nonVideoLayout = view.findViewById(R.id.nonVideoLayout); // Your own view, read class comments
            ViewGroup videoLayout = (ViewGroup) view.findViewById(R.id.videoLayout); // Your own view, read class comments
            //noinspection all
            View loadingView =  LayoutInflater.from(context).inflate(R.layout.view_loading_video, null); // Your own view, read class comments
            webChromeClient = new VideoEnabledWebChromeClient(nonVideoLayout, videoLayout, loadingView, webViewDescription) // See all available constructors...
            {
                // Subscribe to standard events, such as onProgressChanged()...
                @Override
                public void onProgressChanged(WebView view, int progress)
                {
                    // Your code...
                }
            };
            webChromeClient.setOnToggledFullscreen(new VideoEnabledWebChromeClient.ToggledFullscreenCallback()
            {
                @Override
                public void toggledFullscreen(boolean fullscreen)
                {
                    //Log.d("cr_Ime", "toggledFullscreen: fullscreen  = "+fullscreen);
                    // Your code to handle the full-screen change, for example showing and hiding the title bar. Example:
                    if (fullscreen)
                    {
                        WindowManager.LayoutParams attrs = ((Activity)context).getWindow().getAttributes();
                        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                        attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                        ((Activity)context).getWindow().setAttributes(attrs);
                        if (android.os.Build.VERSION.SDK_INT >= 14)
                        {
                            //noinspection all
                            ((Activity)context).getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
                        }
                    }
                    else
                    {
                        WindowManager.LayoutParams attrs = ((Activity)context).getWindow().getAttributes();
                        attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
                        attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                        ((Activity)context).getWindow().setAttributes(attrs);
                        if (android.os.Build.VERSION.SDK_INT >= 14)
                        {
                            //noinspection all
                            ((Activity)context).getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                        }
                    }

                }
            });

            // webView.getSettings().setUserAgentString("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/534.36 (KHTML, like Gecko) Chrome/13.0.766.0 Safari/534.36");
            webViewDescription.setWebChromeClient(webChromeClient);
            // Call private class InsideWebViewClient
            webViewDescription.setWebViewClient(new InsideWebViewClient());
            webViewDescription.getSettings().setDefaultTextEncodingName("utf-8");

            imgBtnDeletePic = (ImageButton) view.findViewById(R.id.imgBtnDeletePic);

            itemView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

            NEW_LAYOUT = itemView.getMeasuredHeight();

            Log.d("LinearLayout", "onBindViewHolder "+NEW_LAYOUT);


        }
    }

    private class InsideWebViewClient extends WebViewClient {
        @Override
        // Force links to be opened inside WebView and not in Default Browser
        // Thanks http://stackoverflow.com/a/33681975/1815624
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_description_entry, parent, false);
        mRecyclerView = (RecyclerView) parent;
        context = parent.getContext();
        manageFileHelper = new ManageFileHelper(context,RESULT_SELECT_PICTURE);
        nestedView = (NestedScrollView) mRecyclerView.getParent().getParent().getParent();
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        EntryContentModel content = contents.get(position);
        holder.txtEditNumber.setText((position+1)+"");
        holder.txtViewNumber.setText((position+1)+"");
        onCreateHolder(holder,content);
        setOnClickListener(holder,content);
        setOnFocusChangeListener(holder,content);
        setActionOnDragEvent(holder,content);

        if(getItemCount() == 1){
            currentViewHolder = holder;
        }


        if(position == getItemCount()-1 && getItemCount() > 1){
            if(isNotifyChange){
                isNotifyChange = false;
                switchLayout(true,holder);
                currentViewHolder.btnDragUp.setVisibility(View.VISIBLE);
                currentViewHolder.btnDragDown.setVisibility(View.VISIBLE);
            }
        }




        //holder.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
    }

    public void switchLayout(boolean isEdit, ViewHolder holder){
        //
        holder.currentViewIsEdit = isEdit;
        layoutColorChange(isEdit,holder);
        if(isEdit){
            if(currentViewHolder != null && getItemCount() > 1){
                switchLayout(false,currentViewHolder);
            }
             currentViewHolder = holder;
             switchLayoutToEdit(holder);
        }else{
            switchLayoutToView(holder);
        }
        if(getItemCount() > 1){
            holder.btnDragItem.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnDragUp.setVisibility(View.VISIBLE);
            holder.btnDragDown.setVisibility(View.VISIBLE);
        }else{
            holder.btnDragItem.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.GONE);
            holder.btnDragUp.setVisibility(View.INVISIBLE);
            holder.btnDragDown.setVisibility(View.INVISIBLE);
        }
    }

    private void switchLayoutToEdit(ViewHolder holder){
        holder.rlEntryEdit.setVisibility(View.VISIBLE);
        holder.rlEntryView.setVisibility(View.GONE);
        holder.edtEntryContent.setFocusable(true);
        holder.edtEntryContent.requestFocus();
    }

    private void switchLayoutToView(ViewHolder holder){
        holder.edtEntryContent.clearFocus();
        holder.rlEntryEdit.clearFocus();

        holder.rlEntryEdit.setVisibility(View.GONE);
        holder.rlEntryView.setVisibility(View.VISIBLE);
        holder.txtViewEntryContent.setText(holder.edtEntryContent.getText());
    }

    private void layoutColorChange(boolean isEdit,ViewHolder holder){
        Log.d(TAG, "layoutColorChange: "+isEdit+" Holder : "+holder.getLayoutPosition());
        if(isEdit){
            holder.llButtonAddItem.setBackgroundColor(context.getColor(R.color.buttonOn));
            holder.llEntryContent.setBackgroundColor(context.getColor(R.color.bg_layout_howto_content));
            holder.btnAddPhoto.setImageResource(R.drawable.ic_camera_wt);
            holder.btnAddVideo.setImageResource(R.drawable.ic_video_white);
            holder.btnAddLink.setImageResource(R.drawable.ic_link_white);
            holder.btnDragDown.setImageResource(R.drawable.ic_down_white);
            holder.btnDragUp.setImageResource(R.drawable.ic_up_white);
        }else{
            holder.llButtonAddItem.setBackgroundColor(context.getColor(R.color.bgAddImage));
            holder.llEntryContent.setBackgroundColor(context.getColor(R.color.tw__solid_white));
            holder.btnAddPhoto.setImageResource(R.drawable.ic_camera_blue);
            holder.btnAddVideo.setImageResource(R.drawable.ic_video_blue);
            holder.btnAddLink.setImageResource(R.drawable.ic_link_blue);
            holder.btnDragDown.setImageResource(R.drawable.ic_down_blue);
            holder.btnDragUp.setImageResource(R.drawable.ic_up_blue);
        }

        holder.itemView.requestLayout();
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    private void refreshContent(ViewHolder holder,EntryContentModel content){
        content.setTxtContent(holder.edtEntryContent.getText().toString());
       // content.setPicContent(content.getPicContent());

    }

    private void onCreateHolder(ViewHolder holder,EntryContentModel content){
        setEntryImageView(holder,content);
        holder.llRenderingContrainer.setTag("");
        holder.edtEntryContent.setText(content.getTxtContent());
        holder.txtViewEntryContent.setText(content.getTxtContent());
        if(getItemCount() > 1){
            holder.btnDragUp.setVisibility(View.VISIBLE);
            holder.btnDragDown.setVisibility(View.VISIBLE);
        }else{
            holder.btnDragUp.setVisibility(View.INVISIBLE);
            holder.btnDragDown.setVisibility(View.INVISIBLE);
        }
        watchIsTyping(holder,content);
    }

    private void setEntryImageView(final ViewHolder holder, final EntryContentModel content){
        if(content.getPicContent() != null){
            //holder.bm = content.getPicContent();
            holder.rlEntryImageView.setVisibility(View.VISIBLE);
            holder.imgViewDescription.setImageBitmap(content.getPicContent());
        }else{
            holder.rlEntryImageView.setVisibility(View.GONE);
            if(content.loadData.length() > 1){
                holder.rlEntryWebView.setVisibility(View.VISIBLE);
            }else{
                holder.rlEntryWebView.setVisibility(View.GONE);
                holder.webViewDescription.loadData(content.loadData,"","");
            }
        }
    }

    private void setOnClickListener(final ViewHolder holder, final EntryContentModel content){
        holder.llButtonAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!holder.currentViewIsEdit){
                    switchLayout(true,holder);
                }
            }
        });

        holder.rlEntryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switchLayout(true,holder);
            }
        });

        holder.imgBtnDeleteWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //holder.bm = null;
                //content.setPicContent(null);
                content.mediaLinkDetail = null;
                refreshContent(holder,content);
                setEntryImageView(holder,content);
            }
        });

        holder.imgBtnDeletePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //holder.bm = null;
                content.setPicContent(null);
                content.mediaDetail = new MediaDetail();
                refreshContent(holder,content);
                setEntryImageView(holder,content);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(holder);
            }
        });


        holder.btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogChoicePicture(holder,content);
            }
        });

        holder.btnAddLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialogAddLink(holder,content,"เพิ่มลิงก์...",1);
                //showDialogPictureLink(holder,content,"เพิ่มลิงก์...");
            }
        });

        holder.btnAddVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //"เพิ่มลิงก์คลิปวิดีโอ"
                showDialogAddLink(holder,content,"เพิ่มลิงก์คลิปวิดีโอ",2);
                //showDialogPictureLink(holder,content,"เพิ่มลิงก์คลิปวิดีโอ");
            }
        });
    }

    private void removeItem(final ViewHolder holder){
        adb = new AlertDialog.Builder(context);
        adb.setTitle("ยืนยันลบขั้นตอนนี้?");
        adb.setMessage("");
        adb.setNegativeButton("NO", null);
        adb.setPositiveButton("YES", new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                // TODO Auto-generated method stub
                //content.setPicContent(null);
                Log.d(TAG, "onClick: holder.getLayoutPosition() = "+holder.getLayoutPosition());
                contents.remove(holder.getLayoutPosition());
                holder.imgViewDescription.setImageBitmap(null);
                switchLayout(false,holder);
                notifyItemRemoved(holder.getLayoutPosition());
                notifyDataSetChanged();
                isNotifyChange = true;
            }
        });
        adb.show();
    }

    private void setOnFocusChangeListener(final ViewHolder holder, final EntryContentModel content){
        holder.edtEntryContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    refreshContent(holder,content);
                    switchLayout(false,holder);
                }
            }
        });
    }

    private void showDialogChoicePicture(final ViewHolder holder,final EntryContentModel content){

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
              //  holder.bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.campusstar_line);
                addPictureHolder = holder;
                refreshContent(holder,content);
                manageFileHelper.createChoicePicture();
                switchLayout(false,holder);
                //setEntryImageView(holder,content);
                alertDialog.cancel();
            }
        });
        Button btnPhotoLink = (Button) dialoglayout.findViewById(R.id.btnPhotoLink);
        btnPhotoLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // holder.bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.pexels_photo_title);
                //switchLayout(false,holder);
                //refreshContent(holder,content);
                //setEntryImageView(holder,content);

                showDialogPictureLink(holder,content);
                alertDialog.cancel();
            }
        });

        Button btnPhotoCamera = (Button) dialoglayout.findViewById(R.id.btnPhotoCamera);
        btnPhotoCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.setPicContent(BitmapFactory.decodeResource(context.getResources(), R.drawable.picture_camera));
                switchLayout(false,holder);
                //refreshContent(holder,content);
                //setEntryImageView(holder,content);
                alertDialog.cancel();
            }
        });

        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }

    private void showDialogAddLink(final ViewHolder holder,final EntryContentModel content,String title,int typeLink){
        switchLayout(false,holder);
        final AlertDialog alertDialogLink = manageFileHelper.createDialogInputLink(holder.webViewDescription,content.mediaLinkDetail,typeLink);
        if(title != null || title != ""){
            alertDialogLink.setTitle(title);
        }
        alertDialogLink.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // switchLayoutParagraph1(manageFileHelper.PICTURE_STATUS);
                if(manageFileHelper.PICTURE_STATUS){
                    Log.d(TAG, "onCancel: rlWebview is Visible");
                    holder.rlEntryImageView.setVisibility(View.GONE);
                    holder.rlEntryWebView.setVisibility(View.VISIBLE);
                    manageFileHelper.PICTURE_STATUS = false;
                    content.setPicContent(null);
                    content.mediaDetail = new MediaDetail();
                }
            }
        });
        alertDialogLink.show();
    }

    private void showDialogPictureLink(final ViewHolder holder,final EntryContentModel content,String title){
        switchLayout(false,holder);
        final AlertDialog alertDialogLink = manageFileHelper.createDialogInputLink(holder.imgViewDescription,content.mediaDetail);
        if(title != null || title != ""){
            alertDialogLink.setTitle(title);
        }
        alertDialogLink.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // switchLayoutParagraph1(manageFileHelper.PICTURE_STATUS);
                if(manageFileHelper.PICTURE_STATUS){
                    holder.rlEntryImageView.setVisibility(View.VISIBLE);
                    manageFileHelper.PICTURE_STATUS = false;
                }
            }
        });
        alertDialogLink.show();
    }

    private void showDialogPictureLink(final ViewHolder holder,final EntryContentModel content){
        showDialogPictureLink(holder,content,null);
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

    private void setActionOnDragEvent(final ViewHolder holder, final EntryContentModel content) {

        holder.btnDragUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int focusPosition = holder.getLayoutPosition();
                int upPosition = focusPosition-1;

                if(focusPosition != 0){
                    //Select Upper Item In RecyclerView
                    itemMove(holder,focusPosition,upPosition);
                    // mRecyclerView.scrollToPosition(focusPosition);
                    int scrollTo = nestedView.getScrollY() - (holder.rlEntryEdit.getMeasuredHeight());
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
                    int scrollTo = nestedView.getScrollY() + (holder.itemView.getMeasuredHeight());
                    ObjectAnimator.ofInt(nestedView, "scrollY", scrollTo).setDuration(1000).start();
                }
            }
        });

        holder.btnDragItem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                    ClipData.Item item = new ClipData.Item(holder.llRenderingContrainer.getTag().toString());
                    ClipData dragData = new ClipData(holder.llRenderingContrainer.getTag().toString(), mimeTypes, item);
                    View.DragShadowBuilder myShadow = new View.DragShadowBuilder(holder.itemView);
                    myShadow.getView().setAlpha(1);
                    //holder.switchView(false);


                    currentViewHolder = holder;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        holder.llRenderingContrainer.startDragAndDrop(dragData, myShadow, null, 0);
                    } else {
                        holder.llRenderingContrainer.startDrag(dragData, myShadow, null, 0);
                    }
                }

                if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    currentDragPosition = holder.getLayoutPosition();
                }

                return false;
            }
        });

        holder.btnDragItem.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                currentViewHolder = holder;

                return false;
            }
        });

        holder.llRenderingContrainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    currentViewHolder = holder;
                }
                if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    currentDragPosition = holder.getLayoutPosition();
                }
                return false;
            }
        });

        holder.llRenderingContrainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                ClipData.Item item = new ClipData.Item(holder.llRenderingContrainer.getTag().toString());
                ClipData dragData = new ClipData(holder.llRenderingContrainer.getTag().toString(), mimeTypes, item);
                View.DragShadowBuilder myShadow = new View.DragShadowBuilder(holder.itemView);
                myShadow.getView().setAlpha(1);
                //holder.switchView(false);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    holder.llRenderingContrainer.startDragAndDrop(dragData, myShadow, null, 0);
                } else {
                    holder.llRenderingContrainer.startDrag(dragData, myShadow, null, 0);
                }
                return false;
            }
        });

        holder.llRenderingContrainer.setOnDragListener(new View.OnDragListener() {

            @Override
            public boolean onDrag(View v, DragEvent event) {
                final int fromPosition = currentViewHolder.getLayoutPosition();
                final int toPosition = holder.getLayoutPosition();

                int ItemHeight = holder.itemView.getMeasuredHeight();
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:

                        return true;

                    case DragEvent.ACTION_DRAG_ENTERED:
                        if(fromPosition != toPosition){
                            isEntered = true;
                            lastEnter = toPosition;
                            Log.d(TAG, "onDrag: ACTION_DRAG_ENTERED =====================");
                            mRecyclerView.getLocationOnScreen(RECYCLER_LOCATION);
                            v.getLocationOnScreen(VIEW_LOCATION);

                            new Handler().postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    notifyItemMoved(fromPosition,toPosition);
                                    holder.txtEditNumber.setText(String.valueOf(fromPosition+1));
                                    currentViewHolder.txtEditNumber.setText(String.valueOf(toPosition+1) );

                                    holder.txtViewNumber.setText(String.valueOf(fromPosition+1) );
                                    currentViewHolder.txtViewNumber.setText(String.valueOf(toPosition+1));
                                }
                            }, 100);


                        }
                        return true;

                    case DragEvent.ACTION_DROP:
                        isEntered = false;
                        swapContent(contents,currentDragPosition,toPosition);
                        viewHolderSwitchLayout(holder,content,false);
                        return true;

                    case DragEvent.ACTION_DRAG_ENDED:
                        if(isEntered && lastEnter == toPosition){
                            swapContent(contents,currentDragPosition,toPosition);
                            isEntered = false;
                        }
                        return false;

                    case DragEvent.ACTION_DRAG_LOCATION:
                        //EntryRcvAdapter adapter = (EntryRcvAdapter) recyclerView.getAdapter();
                        int translatedY = VIEW_LOCATION[1];
                        int rec_translatedY = RECYCLER_LOCATION[1];

                        int threshold = ItemHeight/2;
                        Log.d(TAG, "onDrag: [ItemHeight : "+ItemHeight+"] [rec_translatedY : "+rec_translatedY+"] [rec_translatedY : "+nestedView.getScrollY()+"]");
                        // make a scrolling up due the y has passed the threshold
                        if (translatedY < threshold && rec_translatedY < threshold) {
                            // make a scroll up by 30 px
                            if(toPosition < fromPosition){
                                int scrollTo;
                                if(toPosition==0){
                                    scrollTo= nestedView.getScrollY() - (int)(threshold/1.5);
                                }else{
                                    scrollTo = nestedView.getScrollY() - ItemHeight;
                                }
                                ObjectAnimator.ofInt(nestedView,"scrollY",scrollTo).setDuration(1000).start();

                            }

                        } else
                            // make a autoscrolling down due y has passed the 500 px border
                        if(toPosition > fromPosition){
                            if (translatedY + threshold > (ItemHeight+500) ) {
                                int scrollTo;
                                // make a scroll down by 30 px
                                if(toPosition==getItemCount()-1){
                                    scrollTo = nestedView.getScrollY()+(threshold/2);
                                }else{
                                    scrollTo = nestedView.getScrollY() + ItemHeight+100;
                                }
                                ObjectAnimator.ofInt(nestedView,"scrollY",scrollTo).setDuration(1000).start();

                            }
                        }

                        break;
                }
                return false;
            }
        });
    }

    private void watchIsTyping(final ViewHolder holder,final EntryContentModel content){

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            Rect r = new Rect();
            @Override
            public void onGlobalLayout() {

                //r will be populated with the coordinates of your view that area still visible.
                rootView.getWindowVisibleDisplayFrame(r);
                int heightDiff = rootView.getRootView().getHeight() - (r.bottom - r.top);

                if (heightDiff > 200) { // if more than 100 pixels, its probably a keyboard...

                    holder.isTyping = true;
                } else {
//to make sure this will call only once when keyboard is hide.
                    if(holder.isTyping){
                        holder.isTyping = false;
                        viewHolderSwitchLayout(holder,content,holder.isTyping);
                    }
                }
            }
        });
    }

    private void viewHolderSwitchLayout(final ViewHolder holder,final EntryContentModel content,boolean isEdit){
        refreshContent(holder,content);
        switchLayout(isEdit,holder);
    }

    public void onBackPressed(){
        // Notify the VideoEnabledWebChromeClient, and handle it ourselves if it doesn't handle it

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
    }


    public void onActivityResult(int requestCode , Intent data) {
        if(requestCode == RESULT_SELECT_PICTURE){
            if(data != null){
                EntryContentModel content = contents.get(addPictureHolder.getLayoutPosition());

                asynLoadBitmap(data,content.mediaDetail,manageFileHelper);
                //new LoadBitmap(requestCode,resultCode,data).execute();
            }
        }
    }

    private void asynLoadBitmap(Intent data, MediaDetail mediaDetail, final ManageFileHelper manageFileHelper){
        new LoadBitmap(data,mediaDetail,manageFileHelper){
            // Do Something When Done
            @Override
            public void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                //PIC_TITLE = manageFileHelper.getBm();
                EntryContentModel content = contents.get(addPictureHolder.getLayoutPosition());
                content.setPicContent(manageFileHelper.getBm());
                setEntryImageView(addPictureHolder,content);
                content.mediaLinkDetail = null;
                content.loadData = "";
                //setEntryImageView(PIC_TITLE);
            }
        }.execute();
    }


}
