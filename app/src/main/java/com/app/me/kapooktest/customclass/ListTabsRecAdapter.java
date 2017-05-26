package com.app.me.kapooktest.customclass;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.app.me.kapooktest.R;
import com.app.me.kapooktest.helper.TabsSqLiteHandle;
import com.app.me.kapooktest.modelclass.ConstantModel;

import java.util.List;

/**
 * Created by SuRiV on 7/4/2560.
 */

public class ListTabsRecAdapter extends RecyclerView.Adapter<ListTabsRecAdapter.ViewHolder> {

    private TabsSqLiteHandle dataSource;
    public ListTabsRecAdapter(TabsSqLiteHandle dataSource) {
        this.dataSource = dataSource;
        ConstantModel.setCustomTab(this.dataSource.getAllTabs());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageButton imgBtnDelTab;
        public TextView txtTitle;
        public long _id;
        public AQuery aq;
        public ViewHolder(View view) {
            super(view);
            imgBtnDelTab = (ImageButton) view.findViewById(R.id.imgBtnDelTab);
            txtTitle = (TextView) view.findViewById(R.id.txtNameTab);

            aq = new AQuery(view);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tab_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final ConstantModel.TabsModel content = ConstantModel.getCustomTab(position);
        holder.txtTitle.setText(content.getNameTabs());
        holder._id = content.getID();

        holder.imgBtnDelTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSource.deleteTabs(content);
                ConstantModel.removeCustomTab(position);
                notifyDataSetChanged();
            }
        });
       // holder.aq.id(R.id.imgContent).image(content.imageData.small,true,false);
    }

    @Override
    public int getItemCount() {
        return ConstantModel.getSizeCustomTab();
    }

}
