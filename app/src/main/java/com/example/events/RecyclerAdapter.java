package com.example.events;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>
{

    private ArrayList<String> mImageName;
    private ArrayList<String> images;
    private Context mContext;

    RecyclerAdapter(Context mContext, ArrayList<String> mImageName, ArrayList<String> images) {
        this.mImageName = mImageName;
        this.images = images;
        this.mContext = mContext;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Glide.with(mContext).asBitmap().load(images.get(position)).into(holder.imageView);
        holder.imageName.setText(mImageName.get(position));
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView imageView;
        TextView imageName;
        RelativeLayout layout;
        ViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image);
            imageName=itemView.findViewById(R.id.image_name);
            layout=itemView.findViewById(R.id.parent_layout);

        }
    }


}
