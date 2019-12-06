package com.alejandro.aplicaciondelista.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alejandro.aplicaciondelista.R;

import java.util.ArrayList;
import java.util.Arrays;

public class TagViewAdapter extends RecyclerView.Adapter<TagViewAdapter.TagViewHolder> {

    private final ArrayList<String> tagList;
    private boolean allowEdit;

    public TagViewAdapter(String[] tags, boolean allowEdit) {
        this.allowEdit = allowEdit;

        if(tags != null)
            tagList = new ArrayList<>(Arrays.asList(tags));
        else
            tagList = new ArrayList<>();
    }

    @NonNull
    @Override
    public TagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layout = LayoutInflater.from(parent.getContext());
        View view = layout.inflate(R.layout.item_tag_content, parent, false);

        return new TagViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final TagViewHolder holder, int position) {

        holder.bind(tagList.get(position));

    }

    @Override
    public int getItemCount() {
        return tagList.size();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull TagViewHolder holder) {
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

    private void animateItemDelete(View view, int position){

        AnimationSet animation = new AnimationSet(true);
        animation.addAnimation(new AlphaAnimation(1.0F, 0.0F));
        animation.addAnimation(new ScaleAnimation(1, 0.8f, 1, 0.8f)); // Change args as desired
        animation.setDuration(400);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tagList.remove(position);
                notifyItemRemoved(position);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        view.startAnimation(animation);

    }

    public String[] getTags(){
        return tagList.toArray(new String[0]);
    }

    private void removeItem(View view, int position){
        animateItemDelete(view, position);
    }

    public void addItem(String tag){
        tagList.add(tag);
        notifyItemInserted(tagList.size()-1);
    }

    class TagViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTag;
        private final Button removeTag;

        TagViewHolder(View view) {
            super(view);

            tvTag = view.findViewById(R.id.tv_tag);
            removeTag = view.findViewById(R.id.remove_tag);

            removeTag.setOnClickListener(v -> {

                int position = getAdapterPosition();

                if(position != RecyclerView.NO_POSITION)
                    removeItem(view, position);

            });

        }

        void bind(String tag){

            removeTag.setVisibility(allowEdit ? View.VISIBLE : View.GONE);
            tvTag.setText(tag);

        }

    }

}
