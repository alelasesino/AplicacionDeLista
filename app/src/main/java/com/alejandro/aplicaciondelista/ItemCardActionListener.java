package com.alejandro.aplicaciondelista;

import android.view.View;

import com.alejandro.aplicaciondelista.model.ItemProduct;

public interface ItemCardActionListener {

    /**
     * Evento clic para las tarjetas de los productos
     * @param card Tarjeta que recibio el clic
     * @param item Productos asociado a la tarjeta
     * @param editMode Modo de edicion
     */
    void onCardItemClick(View card, ItemProduct item, boolean editMode);

    /**
     * Evento al borrar una tarjeta de producto
     * @param card Tarjeta para borrar
     * @param item Producto para borrar
     */
    void onCardItemRemoved(View card, ItemProduct item);

    /**
     * Cambia el estado de favorito de un producto
     * @param isFavorite Es un producto favorito
     */
    void onChangeFavoriteState(boolean isFavorite);

}
