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
    private static final String URL_API_REST_BASE = "https://api-rest-android.herokuapp.com/";
    private static final String URL_PRODUCTS = "products";

    /**
     * Metodo que obtiene los datos de la API REST y los almacena en una lista de productos
     * @param context Contexto
     * @param listener Callback cuando termine la carga de los datos
     */
    public static void loadItemsApiRest(Context context, IItemContent listener){

        /*JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_API_REST_BASE + URL_PRODUCTS, null, new Response.Listener<JSONObject>() {
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

        queue.add(request);*/

    }

    public interface IItemContent{
        void onDataFinished();
    }

}

class JsonObjectRequest extends AsyncTask<Void, Integer, JSONObject> {

    private final String URL;
    private final String METHOD;
    private HttpURLConnection http;
    private ItemProduct item;

    public JsonObjectRequest(String method, String url, ItemProduct item){
        this.URL  = url;
        this.item = item;
        this.METHOD = method;
    }

    @Override
    protected JSONObject doInBackground(Void... voids) {

        try{

            URL url = new URL(URL);
            http = (HttpURLConnection)url.openConnection();

        } catch (IOException ignored){}

        return null;
    }

    private JSONObject sendGetRequest() throws IOException {

        http.setRequestMethod("GET");
        http.setRequestProperty("User-Agent", "");

        int responseCode = http.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream()));

            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }

            reader.close();

            try{

                return new JSONObject(response.toString());

            }catch (JSONException ignored){}

        }

        return null;

    }

    private void sendPostRequest() throws IOException {

        /* For POST only - START
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(POST_PARAMS.getBytes());
        os.flush();
        os.close();
         For POST only - END

        int responseCode = con.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());
        } else {
            System.out.println("POST request not worked");
        }*/


        if(item != null) {

            http.setDoOutput(true);
            http.setRequestMethod("POST");
            http.setRequestProperty("User-Agent", "");

            OutputStreamWriter output = new OutputStreamWriter(http.getOutputStream());
            output.write(item.getURLEncode());
            output.flush();
            output.close();

        }

    }

}
