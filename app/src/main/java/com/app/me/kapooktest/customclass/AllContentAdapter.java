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
import com.app.me.kapooktest.modelclass.AllContentModel;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by SuRiV on 3/5/2560.
 */

public class AllContentAdapter  extends RecyclerView.Adapter<AllContentAdapter.ViewHolder>{
    private ArrayList<AllContentModel> allContents;

    public AllContentAdapter(ArrayList<AllContentModel> allContents) {
        this.allContents = allContents;

    }

    ShareLinkContent shareContent;
    public Context context;

    public class ViewHolder extends RecyclerView.ViewHolder /*implements View.OnCreateContextMenuListener*/ {
        private ImageView imgContent;
        private TextView txtTitle;
        private TextView txtDate;
        private TextView txtView;

        private ImageButton imgBtnShare;
        private AQuery aq;

        public ViewHolder(View view) {
            super(view);

            context = view.getContext();

            txtDate = (TextView) view.findViewById(R.id.txtDate);
            txtView = (TextView) view.findViewById(R.id.txtView);
            imgBtnShare = (ImageButton) view.findViewById(R.id.imgBtnShare);
            imgContent = (ImageView) view.findViewById(R.id.imgContent);
            txtTitle = (TextView) view.findViewById(R.id.txtTitle);

            aq = new AQuery(view);
        }

    }

    @Override
    public AllContentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_allcontent, parent, false);

        return new AllContentAdapter.ViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(final AllContentAdapter.ViewHolder holder, int position) {
        final AllContentModel content = allContents.get(position);
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
        Log.d("Runing ",""+content.img+" Load");
        holder.aq.id(holder.txtTitle).text(content.subject);
        holder.aq.id(holder.imgContent).image(content.img,true,false);

        holder.imgBtnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final View viewClick = v;
                PopupMenu popup = new PopupMenu(v.getContext(), holder.imgBtnShare);

                popup.inflate(R.menu.menu_in_item);

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
        return allContents.size();
    }

    private  void openBottomSheet(View v, final AllContentModel content) {

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

    private ShareContent setShareFacebookContent(AllContentModel content){
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
