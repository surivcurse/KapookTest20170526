package com.app.me.kapooktest.customclass;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.app.me.kapooktest.R;
import com.app.me.kapooktest.modelclass.DataDetail;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by SuRiV on 7/4/2560.
 */

public class HowtoStepViewRcvAdapter extends RecyclerView.Adapter<HowtoStepViewRcvAdapter.ViewHolder> {
    private ArrayList<DataDetail.Step> contents;
    private Context context;
    private AQuery aQuery;


    public HowtoStepViewRcvAdapter(List<DataDetail.Step> contents) {
        this.contents = (ArrayList<DataDetail.Step>)contents;


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtViewNumber;
        private TextView txtViewEntryContent;
        private ImageView imgViewDescription;


        public ViewHolder(View view) {
            super(view);
            txtViewNumber = (TextView) view.findViewById(R.id.txtViewNumber);
            txtViewEntryContent = (TextView) view.findViewById(R.id.txtDescriptionContent);
            imgViewDescription = (ImageView) view.findViewById(R.id.imgViewDescription);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_description_step_view, parent, false);
        context = parent.getContext();
        aQuery = new AQuery(context);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {

        final DataDetail.Step content = contents.get(position);
        holder.txtViewNumber.setText(String.valueOf(position+1));
        holder.txtViewEntryContent.setText(content.getTitle());
        String imageUrl = "https://s359.kapook.com"+ (content.getThumbnail() != "" ? content.getThumbnail() : content.getImg());

        aQuery.id(holder.imgViewDescription).image(imageUrl,true,false);
        setOnClickListener(holder,content);

    }

    private void setOnClickListener(final ViewHolder holder, final DataDetail.Step content){



    }



    @Override
    public int getItemCount() {
        return contents.size();
    }



}
