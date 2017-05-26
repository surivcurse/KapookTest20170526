package com.app.me.kapooktest.customclass;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.app.me.kapooktest.EntryActivity;
import com.app.me.kapooktest.HowtoActivity;
import com.app.me.kapooktest.MainActivity;
import com.app.me.kapooktest.R;
import com.app.me.kapooktest.modelclass.ConstantModel.*;

import java.util.ArrayList;

import static com.app.me.kapooktest.modelclass.ConstantModel.TO_INTENT;

/**
 * Created by SuRiV on 7/4/2560.
 */

public class CategoryRcvAdapter extends RecyclerView.Adapter<CategoryRcvAdapter.ViewHolder> {
    private ArrayList<CategoryModel> contents;
    private Context context;

    public CategoryRcvAdapter(ArrayList<CategoryModel> contents) {
        this.contents = contents;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgIconCategory;
        public TextView txtCategory;
        public LinearLayout linearLayoutItemCategory;
        public AQuery aq;
        public ViewHolder(View view) {
            super(view);
            context = view.getContext();
            imgIconCategory = (ImageView) view.findViewById(R.id.imgIconCategory);
            txtCategory = (TextView) view.findViewById(R.id.txtCategory);
            linearLayoutItemCategory = (LinearLayout) view.findViewById(R.id.layout_item_category);
            aq = new AQuery(view);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final CategoryModel content = contents.get(position);
        holder.imgIconCategory.setImageResource(content.getIcon());
        holder.txtCategory.setText(content.getTitle());
        holder.linearLayoutItemCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntentHowto(content);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    public void startIntentHowto(CategoryModel content){

        Intent intentHowto = new Intent(context, HowtoActivity.class);
        switch (TO_INTENT){
            case 1:  intentHowto = new Intent(context, HowtoActivity.class);
                break;
            case 2:  intentHowto = new Intent(context, EntryActivity.class);
                break;
            case 3:  intentHowto = new Intent(context, HowtoActivity.class);
                break;
        }

        intentHowto.putExtra("ITEM_ID",content.getId());
        intentHowto.putExtra("ITEM_TITLE",content.getIcon());
        context.startActivity(intentHowto);
    }

}
