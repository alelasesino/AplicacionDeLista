package com.alejandro.aplicaciondelista.model;

import android.content.Context;
import android.util.Log;

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
                itemProduct.setTags(getItemTags(producto.getJSONArray("tags")));

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

    public interface IItemContent{
        void onDataFinished();
    }

/*
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
                new String[]{"Bebida", "Gluten"});
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Lorem Ipsum es el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas Letraset, las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.");

        return builder.toString();
    }*/

}
