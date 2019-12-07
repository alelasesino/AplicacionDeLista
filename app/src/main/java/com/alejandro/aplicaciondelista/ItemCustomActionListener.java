package com.alejandro.aplicaciondelista;

import com.alejandro.aplicaciondelista.model.ItemProduct;

public interface ItemCustomActionListener {

    /**
     * Evento para cuando se guarda un producto personalizado
     * @param item Producto para guardar
     */
    void onSaveItemCustom(ItemProduct item);

}
