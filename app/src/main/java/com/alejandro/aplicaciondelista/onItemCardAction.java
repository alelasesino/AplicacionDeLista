package com.alejandro.aplicaciondelista;

import android.view.View;

import com.alejandro.aplicaciondelista.model.ItemContent;

public interface onItemCardAction {

    void onCardItemClick(View card, ItemContent.ProductItem item);
    //void onCardItemLongClick(View v);

}
