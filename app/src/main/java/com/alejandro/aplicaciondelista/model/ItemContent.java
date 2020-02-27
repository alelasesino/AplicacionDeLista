package com.alejandro.aplicaciondelista.model;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.alejandro.aplicaciondelista.ui.dialog.ResponseDialog;

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
    private static final String URL_PRODUCT = URL_API_REST_BASE + "product";
    private static final String URL_PRODUCTS = URL_API_REST_BASE + "products";
    private static final String URL_PRODUCT_ID = URL_PRODUCT + "/%s";

    /**
     * Metodo que obtiene los datos de la API REST y los almacena en una lista de productos
     * @param listener Callback cuando termine la carga de los datos
     */
    public static void loadItemsService(Context context, IItemContent listener){

        JsonObjectRequest request = new JsonObjectRequest(context, JsonObjectRequest.GET, URL_PRODUCTS, new Response.Listener<JSONObject>() {

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

                if(listener != null)
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

    public static void insertItemService(AppCompatActivity activity, ItemProduct item, HttpResponse listener){

        JsonObjectRequest request = new JsonObjectRequest(activity, JsonObjectRequest.POST, URL_PRODUCT, response -> {

            try{

                Log.d("PRUEBA", "Item inserted: " + response.getJSONObject("data"));
                if(listener != null)
                    listener.onResponseFinished(true);

            } catch (JSONException e){
                e.printStackTrace();
            }

        }, errorResponse -> {

            try{

                new ResponseDialog(activity, "Insert Error", errorResponse.getString("message")).show();
                Log.e("PRUEBA", "Error Message: " + errorResponse.getString("message"));
                if(listener != null)
                    listener.onResponseFinished(false);

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

    public static void deleteItemService(AppCompatActivity activity, ItemProduct item, HttpResponse listener){

        String URL = String.format(URL_PRODUCT_ID, item.getId());

        JsonObjectRequest request = new JsonObjectRequest(activity, JsonObjectRequest.DELETE, URL, response -> {

            try{

                Log.d("PRUEBA", "Item deleted: " + response.getJSONObject("data"));
                if(listener != null)
                    listener.onResponseFinished(true);

            } catch (JSONException e){
                e.printStackTrace();
            }

        }, errorResponse -> {

            try{

                new ResponseDialog(activity, "Delete Error", errorResponse.getString("message")).show();
                Log.e("PRUEBA", "Error Message: " + errorResponse.getString("message"));
                if(listener != null)
                    listener.onResponseFinished(false);

            } catch (JSONException e){
                e.printStackTrace();
            }

        });

        request.execute();

    }

    public static void updateItemService(AppCompatActivity activity, ItemProduct item, HttpResponse listener){

        String URL = String.format(URL_PRODUCT_ID, item.getId());

        JsonObjectRequest request = new JsonObjectRequest(activity, JsonObjectRequest.PUT, URL, response -> {

            try{

                Log.d("PRUEBA", "Item updated: " + response.getJSONObject("data"));
                if(listener != null)
                    listener.onResponseFinished(true);

            } catch (JSONException e){
                e.printStackTrace();
            }

        }, errorResponse -> {

            try{

                new ResponseDialog(activity, "Update Error", errorResponse.getString("message")).show();
                Log.e("PRUEBA", "Error Message: " + errorResponse.getString("message"));
                if(listener != null)
                    listener.onResponseFinished(false);

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

    public interface HttpResponse{
        void onResponseFinished(boolean success);
    }

}

