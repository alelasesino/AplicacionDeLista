package com.alejandro.aplicaciondelista.adapters;

import android.content.Context;
import android.database.Cursor;
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
import com.alejandro.aplicaciondelista.ItemCardActionListener;
import com.alejandro.aplicaciondelista.R;
import com.alejandro.aplicaciondelista.model.db.ProductProvider;

/**
 * Clase adaptador del recycler view que muestra la lista de los productos
 */
public class ItemViewAdapter extends CursorRecyclerViewAdapter<ItemViewAdapter.ItemViewHolder> {

    private ItemCardActionListener cardAction;

    /*Modo de edicion que permite modificar los productos*/
    private boolean editMode;

    private Context context;

    public ItemViewAdapter(Context context, ItemCardActionListener cardAction) {
        super(context, null);

        this.context = context;
        this.cardAction = cardAction;

    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layout = LayoutInflater.from(parent.getContext());
        View view = layout.inflate(R.layout.item_list_content, parent, false);

        return new ItemViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ItemViewHolder viewHolder, Cursor cursor, int position) {

        ItemProduct item = getItemFromCursor(position);

        if(item != null)
            viewHolder.bind(item);

    }

    private ItemProduct getItemFromCursor(int position){

        Cursor cursor = getCursor();

        if(!cursor.moveToPosition(position)){
            return null;
        }

        ItemProduct item = new ItemProduct();
        item.setId(cursor.getString(cursor.getColumnIndex(ProductProvider.ItemProduct.COLUMN_ID)));
        item.setImageUrl(cursor.getString(cursor.getColumnIndex(ProductProvider.ItemProduct.COLUMN_IMAGE_URL)));
        item.setName(cursor.getString(cursor.getColumnIndex(ProductProvider.ItemProduct.COLUMN_NAME)));
        item.setDetails(cursor.getString(cursor.getColumnIndex(ProductProvider.ItemProduct.COLUMN_DETAILS)));
        item.setPrice(cursor.getDouble(cursor.getColumnIndex(ProductProvider.ItemProduct.COLUMN_PRICE)));
        item.setFavorite(cursor.getInt(cursor.getColumnIndex(ProductProvider.ItemProduct.COLUMN_FAVORITE)) == 1);

        return item;

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
        animation.addAnimation(new ScaleAnimation(0.8f, 1, 0.8f, 1));
        animation.setDuration(400);
        view.startAnimation(animation);

    }

    private void animateItemDelete(View view, int position){

        AnimationSet animation = new AnimationSet(true);
        animation.addAnimation(new AlphaAnimation(1.0F, 0.0F));
        animation.addAnimation(new ScaleAnimation(1, 0.8f, 1, 0.8f));
        animation.setDuration(400);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardAction.onCardItemRemoved(view, getItemFromCursor(position));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
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

    /*public void addItem(ItemProduct item){

        itemList.add(item);
        itemListFull.add(item);
        ((ItemListActivity)context).doSmoothScroll(getItemCount());
        notifyItemInserted(getItemCount());
        ProductDao.insert(productHelper.getWritableDatabase(), item);

    }*/

    /*public void updateItem(ItemProduct item){

        int position = getItemById(item.getId());

        if(position != -1){

            ItemProduct itemProduct = getItemFromCursor(position);
            itemProduct.setName(item.getName());
            itemProduct.setTags(item.getTags());
            itemProduct.setPrice(item.getPrice());
            itemProduct.setDetails(item.getDetails());
            itemProduct.setImageUrl(item.getImageUrl());

            ItemProduct itemProductFull = getItemFromCursor(position);
            itemProductFull.setName(item.getName());
            itemProductFull.setTags(item.getTags());
            itemProductFull.setPrice(item.getPrice());
            itemProductFull.setDetails(item.getDetails());
            itemProductFull.setImageUrl(item.getImageUrl());

            notifyItemChanged(position);

        }

    }*/

    /*public void updateFavoriteState(ItemProduct item){

        int position = getItemById(item.getId());

        if(position != -1){

            ItemProduct itemProduct = getItemFromCursor(position);
            itemProduct.setFavorite(item.isFavorite());

        }

    }

    /**
     * Metodo que filtra los productos por favoritos
     * @param filter Aplicar filtro
     */
    /*public void setFavoriteFilter(boolean filter){

        IFilterAdapter favoriteFilter = (item, tags) -> item.isFavorite();

        setFilter(favoriteFilter, filter, null);

    }

    /**
     * Metodo que filtra los productos de la lista mediante
     * los tags recibidos por parametro
     * @param filter Aplicar el filtro
     * @param tagsToFilter Tags que se desean filtrar
     */
    /*public void setTagsFilter(boolean filter, Tag[] tagsToFilter){

        IFilterAdapter tagsFilter = (item, tags) -> {

            for(Tag tag: tags) {

                Tag[] itemTags = item.getTags();

                if(itemTags != null && tag != null) {

                    for(Tag t : itemTags){

                        if(t.getTag().equals(tag.getTag())) {
                            return true;
                        }

                    }

                    return false;

                }

            }

            return false;

        };

        setFilter(tagsFilter, filter, tagsToFilter);

    }

    /**
     * @param iFilter Forma de aplicar el filtro
     * @param filter Aplicar filtro
     * @param tags Tags para filtrar
     */
    /*private void setFilter(IFilterAdapter iFilter, boolean filter, Tag[] tags){

        itemList.clear();

        if(filter){

            List<ItemProduct> filteredList = new ArrayList<>();

            for(ItemProduct item: itemListFull){

                if(iFilter.filterItem(item, tags))
                    filteredList.add(item);

            }

            itemList.addAll(filteredList);

        } else {

            itemList.addAll(itemListFull);

        }

        notifyDataSetChanged();

    }

    /**
     * Ordena la lista de productos por precio
     * @param ascendente Ordenar ascendentemente
     */
    /*public void orderByPrice(boolean ascendente){

        itemList.sort((itemProduct, itemProduct2) -> {

            if(itemProduct2.getPrice() == itemProduct.getPrice())
                return 0;

            if(ascendente){

                if(itemProduct2.getPrice() < itemProduct.getPrice())
                    return 1;

            } else {

                if(itemProduct2.getPrice() > itemProduct.getPrice())
                    return 1;

            }

            return -1;

        });
        notifyDataSetChanged();

    }

    /**
     * Interfaz que implementa la forma de filtrar un producto
     */
    /*private interface IFilterAdapter{
        boolean filterItem(ItemProduct item, Tag[] tags);
    }*/

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
                    cardAction.onCardItemClick(view, getItemFromCursor(position), editMode);

            });

            removeCard.setOnClickListener(v -> {

                int position = getAdapterPosition();

                if(position != RecyclerView.NO_POSITION)
                    animateItemDelete(view, position);

            });

        }

        void bind(ItemProduct item){

            removeCard.setVisibility(editMode ? View.VISIBLE : View.INVISIBLE);
            Utils.loadPicassoImage(context, imageCard, item.getImageUrl());
            nameCard.setText(item.getName());
            detailsCard.setText(item.getDetails());
            priceCard.setText(Utils.toPrice(item.getPrice()));

        }

    }

}
