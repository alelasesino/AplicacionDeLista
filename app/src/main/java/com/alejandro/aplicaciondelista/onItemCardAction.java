package com.alejandro.aplicaciondelista;

import android.view.View;

import com.alejandro.aplicaciondelista.model.ItemContent;
import com.alejandro.aplicaciondelista.model.ItemProduct;

public interface onItemCardAction {

    void onCardItemClick(View card, ItemProduct item, boolean editMode);
    //void onCardItemLongClick(View v);

}
