package com.app.me.kapooktest.customclass;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.app.me.kapooktest.R;
import com.app.me.kapooktest.modelclass.PortalHomeModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import static android.content.ContentValues.TAG;

/**
 * Created by SuRiV on 7/4/2560.
 */

public class VertialRecAdapter  extends RecyclerView.Adapter<VertialRecAdapter.ViewHolder>{
    private PortalHomeModel portalHomeModel;

    private LinearLayoutManager horizontalLayoutManagaer;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtZoneName;
        public RecyclerView horizontalContent;
        public HorizontalRecAdapter horizontalRecAdapter;
        public AQuery aq;
        public ViewHolder(View view) {
            super(view);
            horizontalContent = (RecyclerView) view.findViewById(R.id.recHorizontalContent);

            txtZoneName = (TextView) view.findViewById(R.id.txtZoneName);
            aq = new AQuery(view);

            try {
                horizontalLayoutManagaer
                        = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
                horizontalContent.setLayoutManager(horizontalLayoutManagaer);
                //horizontalContent.setRecycledViewPool(new RecyclerView.RecycledViewPool());
            }catch (Exception e){
                Log.w(TAG, "onBindViewHolder: ",e );
            }

        }
    }

    public VertialRecAdapter(PortalHomeModel portalHomeModel){
        this.portalHomeModel = portalHomeModel;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gird_main, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Gson gson = new Gson();
        Log.d(TAG, "onBindViewHolder: "+ portalHomeModel.portalHomeData.get(position).detail.zoneName);
        try {
            holder.aq.id(holder.txtZoneName).text(portalHomeModel.portalHomeData.get(position).detail.zoneName);
            holder.horizontalRecAdapter = new HorizontalRecAdapter(portalHomeModel.portalHomeData.get(position).content);

            holder.horizontalContent.setAdapter(holder.horizontalRecAdapter);
        }catch (Exception e){

            Log.w(TAG, "horizontalContent setAdapter Error: ",e );
        }



    }

    @Override
    public int getItemCount() {
        return portalHomeModel.portalHomeData.size();
    }
}
