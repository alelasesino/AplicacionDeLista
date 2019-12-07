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

}
