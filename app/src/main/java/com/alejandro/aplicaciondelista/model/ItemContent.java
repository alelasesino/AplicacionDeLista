package com.alejandro.aplicaciondelista.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.alejandro.aplicaciondelista.model.db.ProductSQLiteHelper;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ItemContent {

    public static final List<ItemProduct> ITEMS = new ArrayList<>();

    public static final String URL_IMAGES_BASE = "https://android-rest.000webhostapp.com/images/";
    private static final String URL_API_REST_BASE = "https://api-rest-android.herokuapp.com/";
    private static final String URL_PRODUCTS = "products";

    private static final int ID_TAG = 0;
    private static final int TAG = 1;
    private static final int ID_PRODUCT = 0;
    private static final int IMAGE_URL_PRODUCT = 1;
    private static final int NAME_PRODUCT = 2;
    private static final int DETAILS_PRODUCT = 3;
    private static final int PRICE_PRODUCT = 4;
    private static final int FAVORITE_PRODUCT = 5;

    /**
     * Metodo que obtiene los datos de la API REST y los almacena en una lista de productos
     * @param context Contexto
     * @param listener Callback cuando termine la carga de los datos
     */
    public static void loadItemsApiRest(Context context, IItemContent listener){

        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_API_REST_BASE + URL_PRODUCTS, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    JSONArray datos = response.getJSONArray("datos");

                    for(int i = 0; i<datos.length(); i++)
                        addItem(datos.getJSONObject(i));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                listener.onDataFinished();

            }

            private void addItem(JSONObject producto) throws JSONException{

                ItemProduct itemProduct = new ItemProduct();
                itemProduct.setId(producto.getString("_id"));
                itemProduct.setDetails(producto.getString("descripcion"));
                itemProduct.setImageUrl(producto.getString("imagen"));
                itemProduct.setName(producto.getString("nombre"));
                itemProduct.setPrice(producto.getDouble("precio"));
                //itemProduct.setTags(getItemTags(producto.getJSONArray("tags")));

                ITEMS.add(itemProduct);

            }

            private String[] getItemTags(JSONArray tags) throws JSONException{

                String[] itemTags = new String[tags.length()];
                for(int i = 0; i<itemTags.length; i++)
                    itemTags[i] = tags.getString(i);

                return itemTags;

            }

        }, error -> Log.e("PRUEBA", "Error al cargar los datos: " + error.getMessage()));

        ITEMS.clear();

        queue.add(request);

    }

    public static void loadItemsSQLite(ProductSQLiteHelper helper, IItemContent listener) {

        SQLiteDatabase db = helper.getReadableDatabase();

        String[] product_fields = new String[] {"id", "imageUrl", "name", "details", "price", "favorite"};

        Cursor product_cursor = db.query("Product", product_fields, null, null, null, null, null);

        if (product_cursor.moveToFirst()) {

            do {

                ItemProduct itemProduct = new ItemProduct();
                itemProduct.setId(product_cursor.getString(ID_PRODUCT));
                itemProduct.setDetails(product_cursor.getString(DETAILS_PRODUCT));
                itemProduct.setImageUrl(product_cursor.getString(IMAGE_URL_PRODUCT));
                itemProduct.setName(product_cursor.getString(NAME_PRODUCT));
                itemProduct.setPrice(product_cursor.getDouble(PRICE_PRODUCT));
                itemProduct.setFavorite(product_cursor.getDouble(FAVORITE_PRODUCT) == 1);
                itemProduct.setTags(getItemTags(db, Integer.parseInt(itemProduct.getId())));

                ITEMS.add(itemProduct);

            } while(product_cursor.moveToNext());
        }

        product_cursor.close();

        listener.onDataFinished();

    }

    private static Tag[] getItemTags(SQLiteDatabase db, int id_product) {

        String[] args = new String[] {String.valueOf(id_product)};

        Cursor tag_cursor = db.rawQuery("SELECT id, tag FROM Tag WHERE id IN (SELECT id_tag FROM ProductTag WHERE id_product = ?)", args);

        ArrayList<Tag> tags = new ArrayList<>();


        if (tag_cursor.moveToFirst()) {

            do {
                tags.add(new Tag(tag_cursor.getInt(ID_TAG), tag_cursor.getString(TAG)));
            } while (tag_cursor.moveToNext());

        }

        tag_cursor.close();

        return tags.toArray(new Tag[0]);

    }

    public interface IItemContent{
        void onDataFinished();
    }

}
