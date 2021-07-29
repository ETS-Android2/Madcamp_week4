package com.example.travel.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.travel.R;
import com.example.travel.items.Placeinfo;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class JoinAdapter extends RecyclerView.Adapter<JoinAdapter.ViewHolder> {
    static Context context;
    private ArrayList<Bitmap> mList=null;
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

        View view = inflater.inflate(R.layout.join_item, parent, false);
        ViewHolder vh = new ViewHolder(view, mListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull JoinAdapter.ViewHolder holder, int position) {

        Glide.with(context).load(mList.get(position)).into(holder.pic);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView pic;

        public ViewHolder(View itemView, OnItemClickListener listener){
            super(itemView);

            pic = itemView.findViewById(R.id.joinImg);

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

    public JoinAdapter(Context context, ArrayList<Bitmap> mlist){
        this.context = context;
        this.mList = mlist;
    }
}
