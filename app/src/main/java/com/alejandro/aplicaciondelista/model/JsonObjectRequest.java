package com.alejandro.aplicaciondelista.model;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import static java.util.stream.Collectors.joining;

public class JsonObjectRequest extends AsyncTask<Void, Integer, JSONObject> {

    static final String GET = "GET";
    static final String POST = "POST";
    static final String PUT = "PUT";
    static final String DELETE = "DELETE";

    private HttpURLConnection http;

    private final String URL;
    private String method;

    private Response.Listener<JSONObject> onResponse;
    private Response.ErrorListener<JSONObject> onErrorResponse;

    public JsonObjectRequest(String method,
                             String url,
                             Response.Listener<JSONObject> onResponse,
                             Response.ErrorListener<JSONObject> onErrorResponse){
        this.URL  = url;
        this.method = method;
        this.onResponse = onResponse;
        this.onErrorResponse = onErrorResponse;
    }

    private JSONObject readResponse(){

        try {

            int responseCode = http.getResponseCode();

            InputStream inputStream;

            if (responseCode < HttpURLConnection.HTTP_BAD_REQUEST)
                inputStream = http.getInputStream();
            else
                inputStream = http.getErrorStream();

            return new JSONObject(readHttpStreamResponse(inputStream));

        }catch (IOException | JSONException e){
            e.printStackTrace();
        }

        return null;

    }

    private String readHttpStreamResponse(InputStream inputStream) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String inputLine;
        StringBuilder response = new StringBuilder();

        try{

            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }

            reader.close();

        } catch (IOException e){
            e.printStackTrace();
        }

        return response.toString();

    }

    @Override
    protected JSONObject doInBackground(Void... voids) {

        try {

            URL url = new URL(URL);
            http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod(method);

            String params = joinParams();

            if(params != null)
                return sendRequest(params);

            return readResponse();

        } catch (IOException e){
            e.printStackTrace();
        }

        return null;

    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {

        try {

            if (jsonObject.getInt("error") == 0) {
                if(onResponse != null)
                    onResponse.onResponse(jsonObject);
                return;
            }

        } catch (JSONException ignored){}

        if(onErrorResponse != null)
            onErrorResponse.onErrorResponse(jsonObject);

    }

    protected Map<String, String> getParams() {
        return null;
    }

    private String joinParams(){

        Map<String, String> params = getParams();

        if(params != null) {

            return params.entrySet()
                    .stream()
                    .map(e -> e.getKey()+"="+e.getValue())
                    .collect(joining("&"));

        } else {
            return null;
        }

    }

    private JSONObject sendRequest(String params) throws IOException{

        http.setDoOutput(true);
        http.setRequestProperty("User-Agent", "");

        OutputStreamWriter output = new OutputStreamWriter(http.getOutputStream());
        output.write(params);
        output.flush();
        output.close();

        return readResponse();

    }

}
