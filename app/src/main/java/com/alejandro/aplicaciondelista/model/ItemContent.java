package com.alejandro.aplicaciondelista.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<ItemProduct> ITEMS = new ArrayList<>();

    private static final int COUNT = 10;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(ItemProduct item) {
        ITEMS.add(item);
    }

    private static ItemProduct createDummyItem(int position) {
        return new ItemProduct(String.valueOf(position),
                "",
                "Item" + position,
                makeDetails(position),
                2.50+position,
                new String[]{"bebida", "gluten"});
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Lorem Ipsum es el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas Letraset, las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.");

        return builder.toString();
    }

}
