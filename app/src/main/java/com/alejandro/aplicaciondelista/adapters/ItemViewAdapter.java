package com.alejandro.aplicaciondelista.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alejandro.aplicaciondelista.Utils;
import com.alejandro.aplicaciondelista.model.ItemProduct;
import com.alejandro.aplicaciondelista.onItemCardAction;
import com.alejandro.aplicaciondelista.R;

import java.util.List;

public class ItemViewAdapter extends RecyclerView.Adapter<ItemViewAdapter.ItemViewHolder> {

    private final List<ItemProduct> itemList;
    private onItemCardAction cardAction;
    private boolean editMode;

    public ItemViewAdapter(List<ItemProduct> items, onItemCardAction cardAction) {

        this.cardAction = cardAction;
        itemList = items;

    }

    @NonNull
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

        if(!editMode)
            animateItemReveal(holder.itemView);

    }

    private void animateItemReveal(View view){

        AnimationSet animation = new AnimationSet(true);
        animation.addAnimation(new AlphaAnimation(0.0F, 1.0F));
        animation.addAnimation(new ScaleAnimation(0.8f, 1, 0.8f, 1)); // Change args as desired
        animation.setDuration(400);
        view.startAnimation(animation);

    }

    private void animateItemDelete(View view, int position){

        AnimationSet animation = new AnimationSet(true);
        animation.addAnimation(new AlphaAnimation(1.0F, 0.0F));
        animation.addAnimation(new ScaleAnimation(1, 0.8f, 1, 0.8f)); // Change args as desired
        animation.setDuration(400);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.INVISIBLE);
                itemList.remove(position);
                notifyItemRemoved(position);
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });

        view.startAnimation(animation);

    }

    public void setEditMode(boolean editMode){
        this.editMode = editMode;
        notifyDataSetChanged();
    }

    public boolean getEditMode(){
        return editMode;
    }

    private void removeItem(View view, int position){
        animateItemDelete(view, position);
    }

    private void addItem(ItemProduct item){
        itemList.add(item);
        notifyItemInserted(itemList.size()-1);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private final Button removeCard;
        private final ImageView imageCard;
        private final TextView nameCard;
        private final TextView detailsCard;
        private final TextView priceCard;

        ItemViewHolder(View view) {
            super(view);

            removeCard = view.findViewById(R.id.remove_card);
            imageCard = view.findViewById(R.id.image_card);
            nameCard = view.findViewById(R.id.name_card);
            detailsCard = view.findViewById(R.id.details_card);
            priceCard = view.findViewById(R.id.price_card);

            view.setOnClickListener(v -> {

                int position = getAdapterPosition();

                if(position != RecyclerView.NO_POSITION)
                    cardAction.onCardItemClick(v, itemList.get(position), editMode);

            });

            removeCard.setOnClickListener(v -> {

                int position = getAdapterPosition();

                if(position != RecyclerView.NO_POSITION)
                    removeItem(view, position);

            });

        }

        void bind(ItemProduct item){
            
            removeCard.setVisibility(editMode ? View.VISIBLE : View.INVISIBLE);
            //imageCard.setImageDrawable(item.image);
            nameCard.setText(item.getName());
            detailsCard.setText(item.getDetails());
            priceCard.setText(Utils.toPrice(item.getPrice()));

        }

    }

}
