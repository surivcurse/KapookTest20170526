package com.app.me.kapooktest.customclass;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.app.me.kapooktest.EntryActivity;
import com.app.me.kapooktest.HowtoActivity;
import com.app.me.kapooktest.MainActivity;
import com.app.me.kapooktest.R;
import com.app.me.kapooktest.modelclass.CategoryModel;
import com.app.me.kapooktest.modelclass.ConstantModel.*;

import java.util.ArrayList;

import static com.app.me.kapooktest.modelclass.ConstantModel.TO_INTENT;

/**
 * Created by SuRiV on 7/4/2560.
 */

public class CategoryRcvAdapter extends RecyclerView.Adapter<CategoryRcvAdapter.ViewHolder> {
    private ArrayList<CategoryModel.CategoryData> contents;
    private Context context;
    public AQuery aq;
    public CategoryRcvAdapter(ArrayList<CategoryModel.CategoryData> contents) {
        this.contents = contents;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgIconCategory;
        public TextView txtCategory;
        public LinearLayout linearLayoutItemCategory;

        public ViewHolder(View view) {
            super(view);

            imgIconCategory = (ImageView) view.findViewById(R.id.imgIconCategory);
            txtCategory = (TextView) view.findViewById(R.id.txtCategory);
            linearLayoutItemCategory = (LinearLayout) view.findViewById(R.id.layout_item_category);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        context = parent.getContext();
        aq = new AQuery(context);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final CategoryModel.CategoryData content = contents.get(position);
        String icon = "ic_"+content.getIcon();
        if(icon.length() < 4){
            aq.id(holder.imgIconCategory).image(content.getLink_icon(),true,true,80, 0);
        }else{
            holder.imgIconCategory.setImageResource(context.getResources().getIdentifier(icon,"drawable",context.getPackageName()));
        }

        //holder.imgIconCategory.setImageResource(context.getResources().getIdentifier());
        holder.txtCategory.setText(content.getName());
        holder.linearLayoutItemCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntentHowto(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    public void startIntentHowto(int position){

        Intent intentHowto = new Intent(context, HowtoActivity.class);
        switch (TO_INTENT){
            case 1:  intentHowto = new Intent(context, HowtoActivity.class);
                break;
            case 2:  intentHowto = new Intent(context, EntryActivity.class);
                break;
            case 3:  intentHowto = new Intent(context, HowtoActivity.class);
                break;
        }
        intentHowto.putExtra("ITEM_ID",position);
        context.startActivity(intentHowto);
    }

}
