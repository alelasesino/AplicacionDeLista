package com.alejandro.aplicaciondelista.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alejandro.aplicaciondelista.onItemCardAction;
import com.alejandro.aplicaciondelista.R;
import com.alejandro.aplicaciondelista.model.ItemContent;

import java.util.List;

public class ItemViewAdapter extends RecyclerView.Adapter<ItemViewAdapter.ItemViewHolder> {

    private final List<ItemContent.ItemProduct> itemList;
    private onItemCardAction cardAction;

    public ItemViewAdapter(List<ItemContent.ItemProduct> items, onItemCardAction cardAction) {

        this.cardAction = cardAction;
        itemList = items;

    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layout = LayoutInflater.from(parent.getContext());
        View view = layout.inflate(R.layout.item_list_content, parent, false);

        return new ItemViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {

        holder.bind(itemList.get(position));

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ItemViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        animateItemReveal(holder.itemView);

    }

    private void animateItemReveal(View view){

        AnimationSet animation = new AnimationSet(true);
        animation.addAnimation(new AlphaAnimation(0.0F, 1.0F));
        animation.addAnimation(new ScaleAnimation(0.8f, 1, 0.8f, 1)); // Change args as desired
        animation.setDuration(400);
        view.startAnimation(animation);

    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageCard;
        private final TextView titleCard;
        private final TextView subTitleCard;

        ItemViewHolder(View view) {
            super(view);

            imageCard = view.findViewById(R.id.card_image);
            titleCard = view.findViewById(R.id.title_card);
            subTitleCard = view.findViewById(R.id.subtitle_card);

            view.setOnClickListener(v -> {

                int position = getAdapterPosition();

                if(position != RecyclerView.NO_POSITION)
                    cardAction.onCardItemClick(v, itemList.get(position));

            });

        }

        void bind(ItemContent.ItemProduct item){

            //imageCard.setImageDrawable(item.image);
            titleCard.setText(item.title);
            //subTitleCard.setText(item.subTitle);

        }

    }

}
