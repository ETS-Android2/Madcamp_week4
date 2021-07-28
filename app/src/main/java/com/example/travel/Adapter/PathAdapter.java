package com.example.travel.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel.items.PathItem;
import com.example.travel.items.Pathinfo;
import com.example.travel.R;

import java.util.ArrayList;

public class PathAdapter extends RecyclerView.Adapter<PathAdapter.ViewHolder> {
    static Context context;
    private ArrayList<PathItem> mList = null;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.userpath_item, parent, false);
        ViewHolder vh = new ViewHolder(view, mListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull PathAdapter.ViewHolder holder, int position) {
        holder.title.setText(mList.get(position).getPathtitle());
        holder.region.setText(mList.get(position).getPathregion());
        holder.count.setText(mList.get(position).getPathcount()+"곳 방문");

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title, region, count;
        ImageView pic;

        public ViewHolder(View itemView, OnItemClickListener listener){
            super(itemView);

            title = itemView.findViewById(R.id.pathTitle);
            region = itemView.findViewById(R.id.pathPlace);
            pic = itemView.findViewById(R.id.pathImg);
            count = itemView.findViewById(R.id.pathCount);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public PathAdapter(Context context, ArrayList<PathItem> mlist){
        this.context = context;
        this.mList = mlist;
    }
}
