package com.alejandro.aplicaciondelista.model.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.alejandro.aplicaciondelista.model.ItemProduct;
import com.alejandro.aplicaciondelista.model.Tag;

public class ProductDao {

    public static void insert(SQLiteDatabase db, ItemProduct product) {

        int id_product = insertProduct(db, product);

        Tag[] tags = product.getTags();
        int[] id_tags = new int[tags.length];

        int  i = 0;
        for(Tag tag: tags) {

            int id_tag = insertTag(db, tag);

            if(id_tag != -1)
                id_tags[i++] = id_tag;
        }

        insertProductTag(db, id_product, id_tags);

    }

    private static int insertProduct(SQLiteDatabase db, ItemProduct product){

        ContentValues cv = new ContentValues();
        cv.put("name", product.getName());
        cv.put("details", product.getDetails());
        cv.put("price", product.getPrice());
        cv.put("favorite", product.isFavorite());
        cv.put("imageUrl", product.getImageUrl());

        db.insert("Product", null, cv);

        return getLastIdInserted(db, "Product");

    }

    private static int insertTag(SQLiteDatabase db, Tag tag){

        if(!existTag(db, tag.getTag())){

            ContentValues cv = new ContentValues();
            cv.put("id", tag.getId());
            cv.put("tag", tag.getTag());
            db.insert("Tag", null, cv);

            return getLastIdInserted(db, "Tag");

        }

        return -1;

    }

    private static boolean existTag(SQLiteDatabase db, String tag) {

        try(Cursor c = db.query("Tag", new String[]{"tag"}, "tag = ?", new String[]{tag}, null, null, null)){
            return c.getCount() == 0;
        }

    }

    private static void insertProductTag(SQLiteDatabase db, int id_product, int[] id_tags){

        for(int tag : id_tags){

            ContentValues cv = new ContentValues();
            cv.put("id_product", id_product);
            cv.put("id_tag", tag);

            db.insert("ProductTag", null, cv);

        }

    }

    private static int getLastIdInserted(SQLiteDatabase db, String table){

        try(Cursor c = db.rawQuery("SELECT last_insert_rowid() FROM " + table, null)){
            c.moveToFirst();
            return c.getInt(0);
        }

    }

    public static void delete(SQLiteDatabase db, String id) {

        db.delete("Product", "id = ?", new String[]{id});

    }

    public static void update(SQLiteDatabase db, ItemProduct product) {



    }

}
