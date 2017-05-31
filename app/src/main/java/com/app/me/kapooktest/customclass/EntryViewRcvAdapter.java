package com.app.me.kapooktest.customclass;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;

import com.app.me.kapooktest.R;
import com.app.me.kapooktest.modelclass.DataDetail;
import com.app.me.kapooktest.modelclass.DetailUser;
import com.app.me.kapooktest.modelclass.EntryViewModel;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by SuRiV on 7/4/2560.
 */

public class EntryViewRcvAdapter extends RecyclerView.Adapter<EntryViewRcvAdapter.ViewHolder> {
    private ArrayList<DataDetail> contents;
    private Context context;
    private DetailUser detailUser;
    private AQuery aQuery;

    public EntryViewRcvAdapter(ArrayList<DataDetail> contents, DetailUser detailUser) {
        this.contents = contents;
        this.detailUser = detailUser;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgTitle;
        private ImageView imgProfile;

        private TextView txtCategoryName;
        private TextView txtCountContent;
        private TextView txtTitle;
        private TextView txtCountView;
        private TextView txtProfileName;
        private TextView txtTimePost;

        private ImageButton imgBtnSetting;
        private LinearLayout llEntryViewContainer;


        public ViewHolder(View view) {
            super(view);
            imgTitle = (ImageView) view.findViewById(R.id.imgTitle);
            imgProfile = (ImageView) view.findViewById(R.id.imgProfile);
            txtProfileName = (TextView) view.findViewById(R.id.txtProfileName);
            txtTimePost = (TextView) view.findViewById(R.id.txtTimePost);
            txtCategoryName = (TextView) view.findViewById(R.id.txtCategoryName);
            txtCountContent = (TextView) view.findViewById(R.id.txtCountContent);
            txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            txtCountView = (TextView) view.findViewById(R.id.txtCountView);
            imgBtnSetting = (ImageButton) view.findViewById(R.id.imgBtnSetting);
            llEntryViewContainer = (LinearLayout) view.findViewById(R.id.llEntryViewContainer);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_description_entry_view, parent, false);
        aQuery = new AQuery(parent.getContext());
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final DataDetail content = contents.get(position);
        holder.txtTitle.setText(content.getTitle());
        holder.txtCategoryName.setText(content.getCat().getName());
        holder.txtCountView.setText(String.valueOf(content.getViews()));
        holder.txtProfileName.setText(detailUser.getDisplay());

        Date nowDate;
        String strDate = null;
        try {//2017-03-08T08:28:15Z
            nowDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(content.getCreateAt());
            strDate = new SimpleDateFormat("dd MMMM yyyy kk:mm:ss").format(nowDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String imgTitle = content.getMedia().getFull_thumbnail()!="" ?
                content.getMedia().getFull_thumbnail() :
                content.getMedia().getImg();


        aQuery.id(holder.imgTitle).image(imgTitle,true,false);
        aQuery.id(holder.imgProfile).image(detailUser.getAvatar(),true,false);

        holder.txtTimePost.setText(strDate);

        setOnClickListener(holder,content);

    }

    private void setOnClickListener(final ViewHolder holder, final DataDetail content){
        holder.imgBtnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View viewClick = v;
                PopupMenu popup = new PopupMenu(v.getContext(), holder.imgBtnSetting);
                popup.inflate(R.menu.menu_in_entry_view);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_delete:
                                // openBottomSheet(viewClick,content);

                                break;
                            case R.id.action_edit:
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
        View.OnClickListener onClickData = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
        holder.imgTitle.setOnClickListener(onClickData);
        holder.txtTitle.setOnClickListener(onClickData);
    }



    @Override
    public int getItemCount() {
        return contents.size();
    }



}
