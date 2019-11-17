package com.alejandro.aplicaciondelista.adapters;

import android.animation.Animator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alejandro.aplicaciondelista.onItemCardAction;
import com.alejandro.aplicaciondelista.R;
import com.alejandro.aplicaciondelista.dummy.ItemContent;

import java.util.List;

public class AnimalItemViewAdapter extends RecyclerView.Adapter<AnimalItemViewAdapter.ItemViewHolder> {

    private final List<ItemContent.DummyItem> itemList;
    private onItemCardAction cardAction;

    public AnimalItemViewAdapter(List<ItemContent.DummyItem> items, onItemCardAction cardAction) {

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

        animateCircleReveal(holder.itemView);

    }

    private void animateCircleReveal(View view){

        int centerX = view.getWidth()/2;
        int centerY = view.getHeight()/2;
        int startRadius = 0;
        int endRadius = Math.max(view.getWidth(), view.getHeight());

        Animator animator = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, startRadius, endRadius);
        view.setVisibility(View.VISIBLE);
        animator.start();

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
                    cardAction.onCardItemClick(itemList.get(position));

            });

        }

        void bind(ItemContent.DummyItem item){

            //imageCard.setImageDrawable(item.image);
            titleCard.setText(item.title);
            subTitleCard.setText(item.subTitle);

        }

    }

}
