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
import com.app.me.kapooktest.modelclass.EntryViewContent;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by SuRiV on 7/4/2560.
 */

public class HowtoContentViewRcvAdapter extends RecyclerView.Adapter<HowtoContentViewRcvAdapter.ViewHolder> {
    private ArrayList<String> contents;
    private Context context;
    private AQuery aQuery;


    public HowtoContentViewRcvAdapter(ArrayList<String> contents) {
        this.contents = contents;


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtContentView;


        public ViewHolder(View view) {
            super(view);
            txtContentView = (TextView) view.findViewById(R.id.txtContentView);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_description_content_view, parent, false);
        context = parent.getContext();

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {

        final String content = contents.get(position);

        holder.txtContentView.setText("\u25CF"+" "+content);

    }


    @Override
    public int getItemCount() {
        return contents.size();
    }



}
