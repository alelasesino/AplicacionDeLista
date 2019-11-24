package com.alejandro.aplicaciondelista;

import android.view.View;

import com.alejandro.aplicaciondelista.dummy.ItemContent;

public interface onItemCardAction {

    void onCardItemClick(View card, ItemContent.DummyItem item);
    //void onCardItemLongClick(View v);

}
