package com.app.me.kapooktest.customclass;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.app.me.kapooktest.R;
import com.app.me.kapooktest.modelclass.EntryViewContent;

import java.util.ArrayList;


/**
 * Created by SuRiV on 7/4/2560.
 */

public class EntryContentViewRcvAdapter extends RecyclerView.Adapter<EntryContentViewRcvAdapter.ViewHolder> {
    private ArrayList<EntryViewContent.ContentDetail> contents;
    private Context context;
    private AQuery aQuery;


    public EntryContentViewRcvAdapter(ArrayList<EntryViewContent.ContentDetail> contents) {
        this.contents = contents;


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtViewEntryContent;
        private ImageView imgViewDescription;
        private TextView txtLocation;
        private ImageView imgFooter;
        private LinearLayout llLocation;

        public ViewHolder(View view) {
            super(view);
            txtViewEntryContent = (TextView) view.findViewById(R.id.txtDescriptionContent);
            imgViewDescription = (ImageView) view.findViewById(R.id.imgViewDescription);
            txtLocation = (TextView) view.findViewById(R.id.txtLocation);
            imgFooter= (ImageView) view.findViewById(R.id.imgFooter);
            llLocation = (LinearLayout) view.findViewById(R.id.llLocation);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_entry_step_view, parent, false);
        context = parent.getContext();
        aQuery = new AQuery(context);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {

        final EntryViewContent.ContentDetail content = contents.get(position);
        holder.txtViewEntryContent.setText(content.getDescription());
        String imageUrl = content.getMedia().getFull_thumbnail() != "" ? content.getMedia().getFull_thumbnail() : content.getMedia().getImg();
        String lacation = content.getMedia().getPlace_name() ;
        if(!lacation.equals("")){
            holder.txtLocation.setText(lacation);
        }else{
            holder.imgFooter.setVisibility(View.GONE);
            holder.llLocation.setVisibility(View.GONE);
        }
        aQuery.id(holder.imgViewDescription).image(imageUrl,true,false);
        setOnClickListener(holder,content);

    }

    private void setOnClickListener(final ViewHolder holder, final EntryViewContent.ContentDetail content){



    }



    @Override
    public int getItemCount() {
        return contents.size();
    }



}
