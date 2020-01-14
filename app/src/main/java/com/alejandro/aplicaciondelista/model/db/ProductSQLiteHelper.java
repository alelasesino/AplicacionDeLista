package com.alejandro.aplicaciondelista.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ProductSQLiteHelper extends SQLiteOpenHelper {

    private String productTable = "CREATE TABLE Product (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "imageUrl TEXT, " +
            "name TEXT, " +
            "details TEXT, " +
            "price NUMERIC, " +
            "favorite INTEGER(1)" +
            ")";
    /*
    private String tagTable = "CREATE TABLE Tag (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "tag TEXT" +
            ")";

    private String productTagTable = "CREATE TABLE ProductTag (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "id_product TEXT, " +
            "id_tag TEXT, " +
            "FOREIGN KEY (id_product) REFERENCES Product(id) ON DELETE CASCADE, " +
            "FOREIGN KEY (id_tag) REFERENCES Tag(id)" +
            ")";*/

    public ProductSQLiteHelper(Context contexto, String nombre, CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(productTable);
        //db.execSQL(tagTable);
        //db.execSQL(productTagTable);

        insertExampleData(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS Product");
        //db.execSQL("DROP TABLE IF EXISTS Tag");
        //db.execSQL("DROP TABLE IF EXISTS ProductTag");

        onCreate(db);

    }

    private void insertExampleData(SQLiteDatabase db){

        ContentValues cv = new ContentValues();
        /*cv.put("tag", "Burger Tag");

        db.insert("Tag", null, cv);*/

        cv = new ContentValues();
        cv.put("imageUrl", "burger");
        cv.put("name", "Burger 1");
        cv.put("details", "Cupidatat reprehenderit elit commodo ut eu laboris laborum ut consectetur eu enim aliquip cillum aute. Et pariatur veniam aute ea sit proident id nulla magna esse ea. Mollit dolor dolore nulla excepteur anim qui minim et non amet fugiat. Non et enim sunt aliquip. Ex exercitation occaecat incididunt pariatur laboris excepteur officia. Irure ut ea amet sint sit nulla proident dolor mollit.\\\\nMinim dolore qui veniam amet tempor est. Laboris duis ad esse voluptate irure exercitation tempor cillum. Aliqua in deserunt dolor excepteur deserunt.\\\\nEa aute ad anim non quis. Aliqua laborum non consectetur ullamco esse ullamco. Exercitation exercitation irure veniam sunt incididunt Lorem irure ad veniam ex ut. Duis quis incididunt do nisi. Aliquip sunt consequat nisi est Lorem ad do. Ad aliquip mollit ut magna est fugiat eu consequat. Dolore aute amet est ex eiusmod in pariatur mollit aliquip duis mollit ipsum.\\\\nQuis minim consectetur enim adipisicing officia ipsum ex veniam laborum incididunt consequat ipsum amet mollit. Tempor ullamco aliqua enim adipisicing culpa eiusmod reprehenderit veniam enim mollit qui nulla velit. Eu excepteur laboris pariatur quis. Duis labore nisi qui consequat est excepteur.\\\\nAliquip dolor est enim pariatur ea laborum. Velit officia deserunt esse est exercitation sunt. Enim amet deserunt anim consectetur consectetur ullamco aute nisi ullamco aliquip cillum deserunt in commodo. Tempor consequat reprehenderit labore culpa aliquip ipsum. Exercitation nisi enim ullamco pariatur eiusmod anim magna nostrud qui veniam excepteur cupidatat excepteur.\\\\nExcepteur ut minim laborum sit aute dolore proident aliquip eu sint duis ex magna. Lorem culpa in cupidatat exercitation ullamco commodo sit aute. Fugiat Lorem aliquip officia cillum. Laboris anim enim adipisicing irure culpa nostrud excepteur aute aliquip cupidatat nulla. Proident aliqua amet eiusmod reprehenderit mollit ipsum irure consectetur labore.");
        cv.put("price", 2.0);
        cv.put("favorite", 1);

        db.insert("Product", null, cv);

        cv.put("imageUrl", "burger");
        cv.put("name", "Burger 2");
        cv.put("details", "Cupidatat reprehenderit elit commodo ut eu laboris laborum ut consectetur eu enim aliquip cillum aute. Et pariatur veniam aute ea sit proident id nulla magna esse ea. Mollit dolor dolore nulla excepteur anim qui minim et non amet fugiat. Non et enim sunt aliquip. Ex exercitation occaecat incididunt pariatur laboris excepteur officia. Irure ut ea amet sint sit nulla proident dolor mollit.\\\\nMinim dolore qui veniam amet tempor est. Laboris duis ad esse voluptate irure exercitation tempor cillum. Aliqua in deserunt dolor excepteur deserunt.\\\\nEa aute ad anim non quis. Aliqua laborum non consectetur ullamco esse ullamco. Exercitation exercitation irure veniam sunt incididunt Lorem irure ad veniam ex ut. Duis quis incididunt do nisi. Aliquip sunt consequat nisi est Lorem ad do. Ad aliquip mollit ut magna est fugiat eu consequat. Dolore aute amet est ex eiusmod in pariatur mollit aliquip duis mollit ipsum.\\\\nQuis minim consectetur enim adipisicing officia ipsum ex veniam laborum incididunt consequat ipsum amet mollit. Tempor ullamco aliqua enim adipisicing culpa eiusmod reprehenderit veniam enim mollit qui nulla velit. Eu excepteur laboris pariatur quis. Duis labore nisi qui consequat est excepteur.\\\\nAliquip dolor est enim pariatur ea laborum. Velit officia deserunt esse est exercitation sunt. Enim amet deserunt anim consectetur consectetur ullamco aute nisi ullamco aliquip cillum deserunt in commodo. Tempor consequat reprehenderit labore culpa aliquip ipsum. Exercitation nisi enim ullamco pariatur eiusmod anim magna nostrud qui veniam excepteur cupidatat excepteur.\\\\nExcepteur ut minim laborum sit aute dolore proident aliquip eu sint duis ex magna. Lorem culpa in cupidatat exercitation ullamco commodo sit aute. Fugiat Lorem aliquip officia cillum. Laboris anim enim adipisicing irure culpa nostrud excepteur aute aliquip cupidatat nulla. Proident aliqua amet eiusmod reprehenderit mollit ipsum irure consectetur labore.");
        cv.put("price", 2.0);
        cv.put("favorite", 1);

        db.insert("Product", null, cv);

        cv.put("imageUrl", "burger");
        cv.put("name", "Burger 3");
        cv.put("details", "Cupidatat reprehenderit elit commodo ut eu laboris laborum ut consectetur eu enim aliquip cillum aute. Et pariatur veniam aute ea sit proident id nulla magna esse ea. Mollit dolor dolore nulla excepteur anim qui minim et non amet fugiat. Non et enim sunt aliquip. Ex exercitation occaecat incididunt pariatur laboris excepteur officia. Irure ut ea amet sint sit nulla proident dolor mollit.\\\\nMinim dolore qui veniam amet tempor est. Laboris duis ad esse voluptate irure exercitation tempor cillum. Aliqua in deserunt dolor excepteur deserunt.\\\\nEa aute ad anim non quis. Aliqua laborum non consectetur ullamco esse ullamco. Exercitation exercitation irure veniam sunt incididunt Lorem irure ad veniam ex ut. Duis quis incididunt do nisi. Aliquip sunt consequat nisi est Lorem ad do. Ad aliquip mollit ut magna est fugiat eu consequat. Dolore aute amet est ex eiusmod in pariatur mollit aliquip duis mollit ipsum.\\\\nQuis minim consectetur enim adipisicing officia ipsum ex veniam laborum incididunt consequat ipsum amet mollit. Tempor ullamco aliqua enim adipisicing culpa eiusmod reprehenderit veniam enim mollit qui nulla velit. Eu excepteur laboris pariatur quis. Duis labore nisi qui consequat est excepteur.\\\\nAliquip dolor est enim pariatur ea laborum. Velit officia deserunt esse est exercitation sunt. Enim amet deserunt anim consectetur consectetur ullamco aute nisi ullamco aliquip cillum deserunt in commodo. Tempor consequat reprehenderit labore culpa aliquip ipsum. Exercitation nisi enim ullamco pariatur eiusmod anim magna nostrud qui veniam excepteur cupidatat excepteur.\\\\nExcepteur ut minim laborum sit aute dolore proident aliquip eu sint duis ex magna. Lorem culpa in cupidatat exercitation ullamco commodo sit aute. Fugiat Lorem aliquip officia cillum. Laboris anim enim adipisicing irure culpa nostrud excepteur aute aliquip cupidatat nulla. Proident aliqua amet eiusmod reprehenderit mollit ipsum irure consectetur labore.");
        cv.put("price", 2.0);
        cv.put("favorite", 1);

        db.insert("Product", null, cv);

        /*cv = new ContentValues();
        cv.put("id_product", 1);
        cv.put("id_tag", 1);

        db.insert("ProductTag", null, cv);*/

    }

}
