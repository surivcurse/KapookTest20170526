package com.app.me.kapooktest.customclass;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.app.me.kapooktest.R;
import com.app.me.kapooktest.modelclass.PortalHomeModel;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by SuRiV on 7/4/2560.
 */

public class HorizontalRecAdapter extends RecyclerView.Adapter<HorizontalRecAdapter.ViewHolder> {
    private ArrayList<PortalHomeModel.DataClass.Content> contents;

    public HorizontalRecAdapter(ArrayList<PortalHomeModel.DataClass.Content> contents) {
        this.contents = contents;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgContent;
        public TextView txtTitle;
        public AQuery aq;
        public ViewHolder(View view) {
            super(view);
            //imgContent = (ImageView) view.findViewById(R.id.imgContent);
            txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            aq = new AQuery(view);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemcontent, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        PortalHomeModel.DataClass.Content content = contents.get(position);
        holder.txtTitle.setText(content.title);
        holder.aq.id(R.id.imgContent).image(content.imageData.small,true,false);
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

}
