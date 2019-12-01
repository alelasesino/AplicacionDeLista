package com.alejandro.aplicaciondelista;

import android.view.View;

import com.alejandro.aplicaciondelista.model.ItemContent;
import com.alejandro.aplicaciondelista.model.ItemProduct;

public interface ItemCardActionListener {

    void onCardItemClick(View card, ItemProduct item, boolean editMode);
    void onCardItemRemoved(View card, ItemProduct item);
    void onChangeFavoriteState(boolean isFavorite);
}
