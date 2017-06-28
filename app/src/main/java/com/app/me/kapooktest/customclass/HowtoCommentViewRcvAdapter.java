package com.app.me.kapooktest.customclass;

import android.content.Context;
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
import com.app.me.kapooktest.modelclass.DataDetail;
import com.app.me.kapooktest.modelclass.EntryViewContent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by SuRiV on 7/4/2560.
 */

public class HowtoCommentViewRcvAdapter extends RecyclerView.Adapter<HowtoCommentViewRcvAdapter.ViewHolder> {
    private ArrayList<EntryViewContent.ContentDetail> contents;
    private Context context;
    private AQuery aQuery;


    public HowtoCommentViewRcvAdapter(ArrayList<EntryViewContent.ContentDetail> contents) {
        this.contents = contents;


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtDescriptionContent;
        private ImageView imgViewDescription;
        private ImageButton imgBtnSettingHeaderContent;

        private CircleImageView imgProfile;
        private TextView txtProfileName;
        private TextView txtTimePost;


        public ViewHolder(View view) {
            super(view);
            txtDescriptionContent = (TextView) view.findViewById(R.id.txtDescriptionContent);
            imgViewDescription = (ImageView) view.findViewById(R.id.imgViewDescription);
            imgBtnSettingHeaderContent = (ImageButton)   view.findViewById(R.id.imgBtnSettingHeaderContent);
            imgBtnSettingHeaderContent.setVisibility(View.VISIBLE);

            imgProfile = (CircleImageView) view.findViewById(R.id.imgProfile);
            txtProfileName = (TextView) view.findViewById(R.id.txtProfileName);
            txtTimePost = (TextView) view.findViewById(R.id.txtTimePost);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment_content, parent, false);
        context = parent.getContext();
        aQuery = new AQuery(context);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {

        final EntryViewContent.ContentDetail content = contents.get(position);
        //holder.txtViewNumber.setText(String.valueOf(position+1));
        holder.txtDescriptionContent.setText(content.getDescription());
        String imageUrl = content.getMedia().getFull_thumbnail() != "" ? content.getMedia().getFull_thumbnail() : content.getMedia().getImg();
        String strDiff = dateDifferent(content.getCreateAt_ts());
        aQuery.id(holder.imgViewDescription).image(imageUrl,true,false);

        aQuery.id(holder.imgProfile).image(content.getDetail_user().getAvatar(),true,false);
        holder.txtProfileName.setText(content.getDetail_user().getDisplay());

        if(!strDiff.equals("")){
            holder.txtTimePost.setText(strDiff);
        }else{
            String strDate ="";
            Date nowDate;
            try {//2017-03-08T08:28:15Z
                nowDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(content.getCreateAt());
                strDate = new SimpleDateFormat("dd MMMM yyyy kk:mm:ss").format(nowDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            holder.txtTimePost.setText(strDate);

        }

        setOnClickListener(holder,content);

    }

    public String dateDifferent(long create_ts){

        long currentTime = System.currentTimeMillis()/1000;
        Log.d("TIME","currentTime "+ currentTime+" dateDifferent: "+create_ts);
        long diff = currentTime-create_ts;
        String strDiff = "";
//        currentTime 1497604504 dateDifferent: 1497627627
//        dateDifferent: -23123
        long diffSeconds = diff  % 60;
        long diffMinutes = (diff / 60) % 60;
        long diffHours = diff / (60 * 60) % 24;
        long diffDays = diff / (24 * 60 * 60) %30;
        long diffMonths = diff / (24 * 60 * 60 * 30)%12;
        long diffYears = diff / (24 * 60 * 60 * 30 * 12);

        Log.d("TIME", "dateDifferent: "+diff);
        if(diffYears > 0) {
            strDiff = diffYears + " ปีที่แล้ว";
        }
        else if(diffMonths > 0){
            strDiff = diffMonths+" เดือนที่แล้ว";
        }else if(diffDays > 0){
            strDiff = diffDays+" วันที่แล้ว";
        }else if(diffHours > 0){
            strDiff = diffHours+" ชั่วโมงที่แล้ว";
        }else if(diffMinutes > 0){
            strDiff = diffMinutes+" นาทีที่แล้ว";
        }else if(diffSeconds > 0){
            strDiff = "ไม่กี่วินาทีที่แล้ว";
        }
        Log.d("TIME", "dateDifferent: "+strDiff);
        return strDiff;
    }
    private void setOnClickListener(final ViewHolder holder, final EntryViewContent.ContentDetail content){
        holder.imgBtnSettingHeaderContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(v.getContext(), holder.imgBtnSettingHeaderContent);
                popup.inflate(R.menu.menu_card_homework);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_alert:
                                // openBottomSheet(viewClick,content);

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
        return contents.size();
    }



}
