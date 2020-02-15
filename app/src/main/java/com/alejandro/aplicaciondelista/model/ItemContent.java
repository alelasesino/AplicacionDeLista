package com.alejandro.aplicaciondelista.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemContent {

    public static final List<ItemProduct> ITEMS = new ArrayList<>();

    public static final String URL_IMAGES_BASE = "https://android-rest.000webhostapp.com/images/";

    private static final String URL_API_REST_BASE = "https://api-rest-android-mysql.herokuapp.com/";
    static final String URL_PRODUCT = URL_API_REST_BASE + "product";
    static final String URL_PRODUCTS = URL_API_REST_BASE + "products";
    static final String URL_PRODUCT_ID = URL_PRODUCT + "/%s";

    /**
     * Metodo que obtiene los datos de la API REST y los almacena en una lista de productos
     * @param listener Callback cuando termine la carga de los datos
     */
    public static void loadItemsApiRest(IItemContent listener){

        JsonObjectRequest request = new JsonObjectRequest(JsonObjectRequest.GET, URL_PRODUCTS, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                    if(response != null) {

                        JSONArray datos = response.getJSONArray("data");

                        for (int i = 0; i < datos.length(); i++)
                            addItem(datos.getJSONObject(i));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                listener.onDataFinished();

            }

            private void addItem(JSONObject producto) throws JSONException {

                ItemProduct itemProduct = new ItemProduct();
                itemProduct.setId(producto.getString("_id"));
                itemProduct.setDetails(producto.getString("descripcion"));
                itemProduct.setImageUrl(producto.getString("imagen"));
                itemProduct.setName(producto.getString("nombre"));
                itemProduct.setPrice(producto.getDouble("precio"));

                if(producto.has("tags"))
                    itemProduct.setTags(getItemTags(producto.getJSONArray("tags")));

                ITEMS.add(itemProduct);

            }

            private String[] getItemTags(JSONArray tags) throws JSONException {

                String[] itemTags = new String[tags.length()];
                for (int i = 0; i < itemTags.length; i++)
                    itemTags[i] = tags.getString(i);

                return itemTags;

            }

        }, response -> {

            try{

                Log.e("PRUEBA", response.getString("message"));

            } catch (JSONException e){
                e.printStackTrace();
            }

        });

        ITEMS.clear();
        request.execute();

    }

    public static void insertItemApiRest(ItemProduct item){

        JsonObjectRequest request = new JsonObjectRequest(JsonObjectRequest.POST, URL_PRODUCT, response -> {

            try{

                Log.d("PRUEBA", "Item inserted: " + response.getJSONObject("data"));

            } catch (JSONException e){
                e.printStackTrace();
            }

        }, errorResponse -> {

            try{

                Log.e("PRUEBA", "Error Message: " + errorResponse.getString("message"));

            } catch (JSONException e){
                e.printStackTrace();
            }

        }){

            @Override
            protected Map<String, String> getParams() {
                return item.getMapParams();
            }
        };

        request.execute();

    }

    public static void deleteItemApiRest(ItemProduct item){

        String URL = String.format(URL_PRODUCT_ID, item.getId());

        JsonObjectRequest request = new JsonObjectRequest(JsonObjectRequest.DELETE, URL, response -> {

            try{

                Log.d("PRUEBA", "Item deleted: " + response.getJSONObject("data"));

            } catch (JSONException e){
                e.printStackTrace();
            }

        }, errorResponse -> {

            try{

                Log.e("PRUEBA", "Error Message: " + errorResponse.getString("message"));

            } catch (JSONException e){
                e.printStackTrace();
            }

        });

        request.execute();

    }

    public static void updateItemApiRest(ItemProduct item){

        String URL = String.format(URL_PRODUCT_ID, item.getId());

        JsonObjectRequest request = new JsonObjectRequest(JsonObjectRequest.PUT, URL, response -> {

            try{

                Log.d("PRUEBA", "Item updated: " + response.getJSONObject("data"));

            } catch (JSONException e){
                e.printStackTrace();
            }

        }, errorResponse -> {

            try{

                Log.e("PRUEBA", "Error Message: " + errorResponse.getString("message"));

            } catch (JSONException e){
                e.printStackTrace();
            }

        }){

            @Override
            protected Map<String, String> getParams() {
                return item.getMapParams();
            }
        };

        request.execute();

    }

    public interface IItemContent{
        void onDataFinished();
    }

}

