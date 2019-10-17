package com.example.events.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.events.beans.EventDetailsBean;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class EventsRecyclerAdapter extends RecyclerView.Adapter<EventsRecyclerAdapter.ViewHolder> {

    private ArrayList<EventDetailsBean> events;
    private Context mContext;
    private OnItemClickListener onClickListener;

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onClickListener = listener;
    }

    EventsRecyclerAdapter(Context mContext, ArrayList<EventDetailsBean> events) {
        this.events = events;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_item, parent, false);
        return new ViewHolder(view, onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        EventDetailsBean event = events.get(position);
        Glide.with(mContext).asBitmap().load(event.getEventImageURL()).into(holder.imageView);
        holder.imageName.setText(event.getEventName());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imageView;
        TextView imageName;
        RelativeLayout layout;

        ViewHolder(View itemView, OnItemClickListener onClickListener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            imageName = itemView.findViewById(R.id.image_name);
            itemView.setOnClickListener(view -> {
                if (onClickListener != null){
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        onClickListener.onClick(pos);
                    }
                }
            });
        }
    }


}
