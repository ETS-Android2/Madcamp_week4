package com.example.travel.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel.R;
import com.example.travel.items.PathItem;
import com.example.travel.items.Placeinfo;

import java.util.ArrayList;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> {
    static Context context;
    private ArrayList<Placeinfo> mList=null;
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

        View view = inflater.inflate(R.layout.userplaces_item, parent, false);
        ViewHolder vh = new ViewHolder(view, mListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceAdapter.ViewHolder holder, int position) {
        holder.name.setText(mList.get(position).getAddress());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, more;
        ImageView pic;

        public ViewHolder(View itemView, OnItemClickListener listener){
            super(itemView);

            name = itemView.findViewById(R.id.placename);
            pic = itemView.findViewById(R.id.placeImg);
            more = itemView.findViewById(R.id.more);

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

    public PlaceAdapter(Context context, ArrayList<Placeinfo> mlist){
        this.context = context;
        this.mList = mlist;
    }
}
