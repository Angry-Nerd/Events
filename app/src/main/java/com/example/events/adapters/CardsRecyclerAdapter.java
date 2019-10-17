package com.example.events.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.events.beans.PastCard;

import java.util.List;

public class CardsRecyclerAdapter extends RecyclerView.Adapter<CardsRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private List<PastCard> cards;
    private OnItemClickListener onClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        onClickListener = listener;
    }
    public interface OnItemClickListener {
        void onClick(int position);
    }

    public CardsRecyclerAdapter(Context mContext, List<PastCard> cards) {
        this.mContext = mContext;
        this.cards = cards;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.past_event_card, viewGroup, false), onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        PastCard pastCard = cards.get(i);
        viewHolder.cardTitle.setText(pastCard.getCardTitle());
        viewHolder.cardDescription.setText(pastCard.getCardDescription());
        viewHolder.cardLastDate.setText(pastCard.getDateOfEvent());
        Glide.with(mContext).asBitmap().load(pastCard.getCardImageURL()).into(viewHolder.cardImage);
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView cardTitle, cardDescription, cardLastDate;
        ImageView cardImage;


        ViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            cardTitle = itemView.findViewById(R.id.past_event_card_title);
            cardDescription = itemView.findViewById(R.id.past_event_card_description);
            cardLastDate = itemView.findViewById(R.id.past_event_card_date);
            cardImage = itemView.findViewById(R.id.past_event_card_image);
            itemView.setOnClickListener(view -> {
                if (onItemClickListener != null){
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        onItemClickListener.onClick(pos);
                    }
                }
            });
        }

    }
}
