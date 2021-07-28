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
import com.gjiazhe.panoramaimageview.GyroscopeObserver;
import com.gjiazhe.panoramaimageview.PanoramaImageView;

import java.util.ArrayList;

public class PathAdapter extends RecyclerView.Adapter<PathAdapter.ViewHolder> {
    static Context context;
    private ArrayList<PathItem> mList=null;
    private OnItemClickListener mListener;
    private GyroscopeObserver gyroscopeObserver;


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

        gyroscopeObserver.register(context);


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title, region, count;
        PanoramaImageView panoramaImageView;

        public ViewHolder(View itemView, OnItemClickListener listener){
            super(itemView);

            title = itemView.findViewById(R.id.pathTitle);
            region = itemView.findViewById(R.id.pathPlace);
            count = itemView.findViewById(R.id.pathCount);

            gyroscopeObserver = new GyroscopeObserver();
//        // Set the maximum radian the device should rotate to show image's bounds.
//        // It should be set between 0 and π/2.
//        // The default value is π/9.
            gyroscopeObserver.setMaxRotateRadian(Math.PI/9);

            panoramaImageView = (PanoramaImageView) itemView.findViewById(R.id.pathImg);
//        // Set GyroscopeObserver for PanoramaImageView.
            panoramaImageView.setGyroscopeObserver(gyroscopeObserver);

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
