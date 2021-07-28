package com.example.travel.Adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.travel.R;
import com.example.travel.items.ExpandableItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class ExpandableAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    public static final int HEADER = 0;
    public static final int CHILD = 1;

    private ArrayList<ExpandableItem> data;

    // constructor
    public ExpandableAdapter(ArrayList<ExpandableItem> data){
        this.data = data;
    }

    public class ListChildrenViewHolder extends RecyclerView.ViewHolder {
        protected TextView tv_place;

        public ListChildrenViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            tv_place = (TextView) itemView.findViewById(R.id.textView_place);
        }
    }

    private static class ListHeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_title, tv_region;
        public ImageView expand_btn;
        public ExpandableItem refferalItem;

        public ListHeaderViewHolder(View itemView) {
            super(itemView);

            tv_title = (TextView) itemView.findViewById(R.id.textView_title);
            tv_region = (TextView) itemView.findViewById(R.id.textView_region);
            expand_btn = (ImageView) itemView.findViewById(R.id.imageview_main);
        }
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view;

        switch (viewType) {
            case HEADER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandable_header, parent, false);
                ListHeaderViewHolder header = new ListHeaderViewHolder(view);
                return header;
            case CHILD:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandable_child, parent, false);
                ListChildrenViewHolder child = new ListChildrenViewHolder(view);
                return child;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {

        final ExpandableItem item = data.get(position);
        switch (item.type) {
            case HEADER:
                final ListHeaderViewHolder itemController = (ListHeaderViewHolder) holder;
                itemController.refferalItem = item;
                itemController.tv_title.setText(item.title);
                if (item.invisibleChildren == null) {
                    itemController.expand_btn.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                } else {
                    itemController.expand_btn.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                }
                itemController.expand_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.invisibleChildren == null) {
                            item.invisibleChildren = new ArrayList<ExpandableItem>();
                            int count = 0;
                            int pos = data.indexOf(itemController.refferalItem);
                            while (data.size() > pos + 1 && data.get(pos + 1).type == CHILD) {
                                item.invisibleChildren.add(data.remove(pos + 1));
                                count++;
                            }
                            notifyItemRangeRemoved(pos + 1, count);
                            itemController.expand_btn.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                        } else {
                            int pos = data.indexOf(itemController.refferalItem);
                            int index = pos + 1;
                            for (ExpandableItem i : item.invisibleChildren) {
                                data.add(index, i);
                                index++;
                            }
                            notifyItemRangeInserted(pos + 1, index - pos - 1);
                            itemController.expand_btn.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                            item.invisibleChildren = null;
                        }
                    }
                });
                break;

            case CHILD:
                ListChildrenViewHolder listchildController = (ListChildrenViewHolder) holder;
                listchildController.tv_place.setText(data.get(position).place);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public int getItemViewType(int position) {
        return data.get(position).type;
    }
}