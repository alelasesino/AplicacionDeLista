package com.alejandro.aplicaciondelista.model;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<ProductItem> ITEMS = new ArrayList<>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, ProductItem> ITEM_MAP = new HashMap<String, ProductItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(ProductItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static ProductItem createDummyItem(int position) {
        return new ProductItem(String.valueOf(position),
                             null,
                               "Item " + position,
                            "Subtitle text",
                            "Category",
                                    makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Delicius Cheese Burguer");

        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class ProductItem {

        public final String id;
        public final Drawable image;
        public final String title;
        public final String subTitle;
        public final String category;
        public final String details;

        ProductItem(String id, Drawable image, String title, String subTitle, String category, String details) {
            this.id = id;
            this.image = image;
            this.title = title;
            this.subTitle = subTitle;
            this.category = category;
            this.details = details;
        }

    }

}
