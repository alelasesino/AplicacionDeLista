package com.alejandro.aplicaciondelista.dummy;

import android.graphics.drawable.Drawable;
import android.media.Image;

import com.alejandro.aplicaciondelista.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static DummyItem createDummyItem(int position) {
        return new DummyItem(String.valueOf(position),
                             null,
                               "Item " + position,
                            "Subtitle text",
                            "Category",
                                    makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {

        public final String id;
        public final Drawable image;
        public final String title;
        public final String subTitle;
        public final String category;
        public final String details;

        public DummyItem(String id, Drawable image, String title, String subTitle, String category, String details) {
            this.id = id;
            this.image = image;
            this.title = title;
            this.subTitle = subTitle;
            this.category = category;
            this.details = details;
        }

    }

}
