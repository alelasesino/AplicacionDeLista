package com.alejandro.aplicaciondelista.model;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ItemContent {

    public static final List<ItemProduct> ITEMS = new ArrayList<>();

    public static final String URL_IMAGES_BASE = "https://android-rest.000webhostapp.com/images/";
    public static final String URL_API_REST_BASE = "https://api-rest-android-mysql.herokuapp.com/";
    public static final String URL_PRODUCTS = "products";

    /**
     * Metodo que obtiene los datos de la API REST y los almacena en una lista de productos
     * @param listener Callback cuando termine la carga de los datos
     */
    public static void loadItemsApiRest(IItemContent listener){

        String URL = ItemContent.URL_API_REST_BASE + ItemContent.URL_PRODUCTS;

        JsonObjectRequest request = new JsonObjectRequest(Request.GET, URL, null, new JsonObjectRequest.ResponseListener() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                    JSONArray datos = response.getJSONArray("data");

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

        });

        ITEMS.clear();
        request.execute();

    }

    public interface IItemContent{
        void onDataFinished();
    }

}

