package com.in.den.android.text2speechapp.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>

 */
public class RecepieContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Recepie> ITEMS = new ArrayList<Recepie>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Recepie> ITEM_MAP = new HashMap<String, Recepie>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(Recepie item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static Recepie createDummyItem(int position) {
        return new Recepie(String.valueOf(position), "Item " + position, makeDetails(position));
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
    public static class Recepie {
        public final String id;
        public final String content;
        public final String details;

        public Recepie(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
