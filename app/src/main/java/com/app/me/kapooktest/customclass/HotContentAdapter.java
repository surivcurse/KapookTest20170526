package com.app.me.kapooktest.customclass;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.app.me.kapooktest.R;
import com.app.me.kapooktest.helper.ShareHandle;
import com.app.me.kapooktest.modelclass.HotContentModel;
import com.app.me.kapooktest.modelclass.NewContentModel;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.google.gson.Gson;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by SuRiV on 7/4/2560.
 */

public class HotContentAdapter extends RecyclerView.Adapter<HotContentAdapter.ViewHolder> {
    private ArrayList<HotContentModel> hotContents;
    private Gson gson = new Gson();
    private ShareButton shareButton;
    private ShareLinkContent shareContent;
    public Context context;
    public HotContentAdapter(ArrayList<HotContentModel> hotContents) {
        this.hotContents = hotContents;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgHotContent;
        private TextView txtHotTitle;
        private TextView txtDate;
        private TextView txtView;
        //private ImageButton imgBtnLineShare;
        private ImageButton imgBtnShare;

        private AQuery aq;

        public ViewHolder(View view) {
            super(view);
            //shareButton = (ShareButton)view.findViewById(R.id.fb_shareButton);
            txtDate = (TextView) view.findViewById(R.id.txtDate);
            txtView = (TextView) view.findViewById(R.id.txtView);
            imgBtnShare = (ImageButton) view.findViewById(R.id.imgBtnShare);
            context  = view.getContext();
            imgHotContent = (ImageView) view.findViewById(R.id.imgHotContent);
            txtHotTitle = (TextView) view.findViewById(R.id.txtHotTitle);
            aq = new AQuery(view);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hotcontents, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Log.d("Runing", "onBindViewHolder: position = "+position);
        final HotContentModel content = hotContents.get(position);
        holder.aq.id(holder.txtHotTitle).text(content.subject);
        holder.aq.id(holder.imgHotContent).image(content.img,true,false);
        String[] splitDate = content.createDate.split(" ");
        Date nowdate;
        String strDate;
        try {
            nowdate = new SimpleDateFormat("yyyy-MM-dd").parse(splitDate[0]);
            strDate = new SimpleDateFormat("dd MMMMM yyyy").format(nowdate);
            holder.txtDate.setText(strDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.txtView.setText(String.valueOf(content.views));

        holder.imgBtnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View viewClick = v;
                //creating a popup menu
                PopupMenu popup = new PopupMenu(v.getContext(), holder.imgBtnShare);
                //inflating menu from xml resource
                popup.inflate(R.menu.menu_in_item);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_share:
                                openBottomSheet(viewClick,content);

                                break;
                            case R.id.action_subscribe:
                                //handle action_subscribe click
                                break;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return hotContents.size();
    }


    private  void openBottomSheet(View v, final HotContentModel content) {

        final ShareHandle share = new ShareHandle(null,content.subject,content.title,content.linkHtml,context);
        Context context=v.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View view = inflater.inflate (R.layout.bottom_layout_share, null);

        ShareButton fbShare = (ShareButton)view.findViewById(R.id.facebookShare);
        fbShare.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        fbShare.setBackgroundResource(R.drawable.facebook_icon);
        fbShare.setShareContent(setShareFacebookContent(content));

        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(context);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(view.findViewById(R.id.layout_menu_bottom));

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        mBottomSheetDialog.setContentView (view);
        mBottomSheetDialog.setCancelable (true);
        mBottomSheetDialog.show ();

        ImageButton imgBtnGmailShare = (ImageButton) view.findViewById(R.id.imgBtnGmailShare);
        imgBtnGmailShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareGmail(share);
            }
        });

        ImageButton imgBtnLineShare = (ImageButton) view.findViewById(R.id.imgBtnLineShare);
        imgBtnLineShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareLine(share);
            }
        });

        ImageButton imgBtnTweetShare = (ImageButton) view.findViewById(R.id.imgBtnTweetShare);
        imgBtnTweetShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TweetComposer.Builder builder = new TweetComposer.Builder(v.getContext())
                        .text(content.subject+"\n"+content.linkHtml)
                        .image(Uri.parse(content.img));
                builder.show();
            }
        });
    }

    private ShareContent setShareFacebookContent(HotContentModel content){
        shareContent = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(content.linkHtml))
                //.setImageUrl(Uri.parse(content.img))
                //.setContentTitle(content.subject)
                //.setContentDescription(content.title)
                .build();

        return shareContent;
    }




    private void shareGmail(ShareHandle share){
        share.startShareActivity(ShareHandle.GMAIL_ACTIVITY_INFO_NAME);
    }

    private void shareLine(ShareHandle share){
        share.startShareActivity(ShareHandle.LINECORP_ACTIVITY_INFO_NAME);
    }


}
