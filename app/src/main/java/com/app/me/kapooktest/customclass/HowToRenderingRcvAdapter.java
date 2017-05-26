package com.app.me.kapooktest.customclass;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;

import android.content.DialogInterface;

import android.os.Build;
import android.support.v7.app.AlertDialog;

import android.support.v7.widget.RecyclerView;

import android.text.Selection;
import android.util.Log;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;

import com.app.me.kapooktest.R;
import com.google.gson.Gson;

import static com.app.me.kapooktest.modelclass.HowToModel.*;

import java.util.ArrayList;


/**
 * Created by SuRiV on 7/4/2560.
 */

public class HowToRenderingRcvAdapter extends RecyclerView.Adapter<HowToRenderingRcvAdapter.ViewHolder> {
    private ArrayList<RenderingModel> contents;
    private Context context;
    private AlertDialog.Builder adb;
    private static int currentDragPosition = 0;
    private ViewHolder currentViewHolder;
    private RecyclerView rcvRender;
    private static boolean isEntered = false;
    private static int lastEnter = 0;

    public HowToRenderingRcvAdapter(ArrayList<RenderingModel> contents) {
        this.contents = contents;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private EditText edtRendering ;
        private ImageButton buttonDelete;
        private ImageButton btnRenderDragItem;

        private LinearLayout llRendering;
        private LinearLayout llRenderingView;

        private TextView txtRendering;
        private TextView txtNumberView;
        private TextView txtRenderingView;

        private ImageView imageView2;


        public AQuery aq;
        public ViewHolder(View view) {
            super(view);
            context = view.getContext();
            adb = new AlertDialog.Builder(context);

            edtRendering = (EditText) view.findViewById(R.id.edtRendering);
            buttonDelete = (ImageButton) view.findViewById(R.id.buttonDelete);
            btnRenderDragItem   = (ImageButton) view.findViewById(R.id.btnRenderDragItem);

            txtRendering = (TextView) view.findViewById(R.id.txtRendering);
            txtNumberView = (TextView) view.findViewById(R.id.txtNumberView);
            txtRenderingView  = (TextView) view.findViewById(R.id.txtRenderingView);

            llRenderingView = (LinearLayout) view.findViewById(R.id.llRenderingView);
            llRendering = (LinearLayout) view.findViewById(R.id.llRendering);
            llRendering.setTag("");

            imageView2 = (ImageView) view.findViewById(R.id.imageView2);
            //watchIsTyping(view);

            aq = new AQuery(view);

        }

        private void switchView(boolean isEdit){
            if(isEdit){
                llRendering.setVisibility(View.VISIBLE);
                llRenderingView.setVisibility(View.GONE);
                imageView2.setVisibility(View.GONE);
            }else{
                llRendering.setVisibility(View.GONE);
                llRenderingView.setVisibility(View.VISIBLE);
                imageView2.setVisibility(View.VISIBLE);
                //edtRendering.clearFocus();
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rendering, parent, false);
        rcvRender = (RecyclerView) parent;
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final RenderingModel content = contents.get(position);
        Gson gson = new Gson();

        holder.txtRendering.setText((position+1)+".");
        holder.txtNumberView.setText((position+1)+".");

        layoutStart(holder,content);

        setActionOnKeyListener(holder,content);
        setActionOnFocusListener(holder,content);
        setActionOnClickListener(holder,content);
        setActionOnDragEvent(holder);

    }

    @Override
    public int getItemCount() {
        return contents.size();
    }


    private void layoutStart(final ViewHolder holder, final RenderingModel content){

        holder.edtRendering.setText(content.getTxtContent());
        holder.txtRenderingView.setText(content.getTxtContent());

        if(getItemCount() > 1){
            if(holder.edtRendering.getText().toString().equals("")){
                currentViewHolder = holder;
                holder.switchView(true);
                holder.edtRendering.setFocusable(true);
                holder.edtRendering.requestFocus();
            }

            holder.btnRenderDragItem.setVisibility(View.VISIBLE);
            holder.buttonDelete.setVisibility(View.VISIBLE);
        }else{

            holder.btnRenderDragItem.setVisibility(View.GONE);
            holder.buttonDelete.setVisibility(View.GONE);
        }

    }

    private void setActionOnKeyListener(final ViewHolder holder,final RenderingModel content){
        holder.edtRendering.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button

                if ((event.getAction() == KeyEvent.ACTION_UP) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    if(holder.getLayoutPosition() == 0){
                        holder.btnRenderDragItem.setVisibility(View.VISIBLE);
                    }

                    holder.switchView(false);
                    notifyItemChanged(holder,content);
                    return true;
                }

                return false;
            }
        });
    }

    private void setActionOnFocusListener(final ViewHolder holder,final RenderingModel content){
        holder.edtRendering.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d("Render", "onFocusChange: "+holder.getLayoutPosition()+" "+hasFocus);
                if(!hasFocus){
                    holder.switchView(false);
                }
            }
        });

        /*holder.llRendering.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    refreshContent(holder,content);
                    holder.switchView(false);
                }
            }
        });*/

    }

    private void setActionOnDragEvent(final ViewHolder holder){
        holder.btnRenderDragItem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_CANCEL){

                    currentDragPosition = holder.getLayoutPosition();
                }

                return false;
            }
        });

        holder.btnRenderDragItem.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                ClipData.Item item = new ClipData.Item(holder.llRendering.getTag().toString());
                ClipData dragData = new ClipData(holder.llRendering.getTag().toString(),mimeTypes,item);
                View.DragShadowBuilder myShadow = new View.DragShadowBuilder(holder.itemView);
                myShadow.getView().setAlpha(1);
                holder.switchView(false);

                currentViewHolder = holder;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    holder.llRendering.startDragAndDrop(dragData, myShadow  , null, 0);
                } else {
                    holder.llRendering.startDrag(dragData, myShadow, null, 0);
                }

                return false;
            }

        });

        holder.llRenderingView.setOnDragListener(new View.OnDragListener() {

            @Override
            public boolean onDrag(View v, DragEvent event) {
                if(currentViewHolder == null){
                    return false;
                }
                int fromPosition = currentViewHolder.getLayoutPosition();//
                int toPosition = holder.getLayoutPosition();

                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:

                        return true;

                    case DragEvent.ACTION_DRAG_ENTERED:
                        isEntered = true;
                        lastEnter = toPosition;
                        if(fromPosition != toPosition){

                            notifyItemMoved(fromPosition,toPosition);
                            holder.txtRendering.setText(String.valueOf(fromPosition+1)+"." );
                            currentViewHolder.txtRendering.setText(String.valueOf(toPosition+1)+"." );

                            holder.txtNumberView.setText(String.valueOf(fromPosition+1)+"." );
                            currentViewHolder.txtNumberView.setText(String.valueOf(toPosition+1)+".");
                        }
                        return true;

                    case DragEvent.ACTION_DROP:
                        isEntered = false;
                        swapContent(contents,currentDragPosition,toPosition);

                        return true;
                    case DragEvent.ACTION_DRAG_ENDED:
                        // the drag has ended
                        if(isEntered && lastEnter == toPosition){
                            swapContent(contents,currentDragPosition,toPosition);
                            isEntered = false;
                        }
                        return false;


                }
                return false;
            }
        });
    }

    private void setActionOnClickListener(final ViewHolder holder, final RenderingModel content){
        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Delete Item
                adb.setTitle("ยืนยันลบขั้นตอนนี้?");
                adb.setMessage("");
                adb.setNegativeButton("NO", null);
                adb.setPositiveButton("YES", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        // TODO Auto-generated method stub
                        removeItem(holder);
                    }
                });
                adb.show();
            }
        });

        holder.llRenderingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentViewHolder != null){
                    currentViewHolder.switchView(false);
                }
                currentViewHolder = holder;
                holder.switchView(true);

            }
        });

    }

    private void addItem(){
        contents.add(new RenderingModel());
        notifyItemInserted(contents.size()-1);
        rcvRender.scrollToPosition(getItemCount()-1);

        //notifyDataSetChanged();

    }
    private void removeItem(ViewHolder holder){
        Gson gson = new Gson();
        Log.d("RenderView", "removeItem: "+gson.toJson(contents));
        contents.remove(holder.getLayoutPosition());

        Log.d("RenderView", "removeItem Done: "+gson.toJson(contents));
        notifyDataSetChanged();
    }

    private void refreshContent(ViewHolder holder,RenderingModel content){
        content.setNumberContent(holder.getLayoutPosition()+1);
        content.setTxtContent(holder.edtRendering.getText().toString());
        holder.txtRenderingView.setText(holder.edtRendering.getText());

    }

    private void notifyItemChanged(ViewHolder holder,RenderingModel content){
        refreshContent(holder,content);
        if(getItemCount() > 1 && holder.edtRendering.getText().toString().equals("")){
            removeItem(holder);

        } else if(!contents.get(getItemCount()-1).getTxtContent().equals("")){
            holder.buttonDelete.setVisibility(View.VISIBLE);
            addItem();
        }

    }


}
