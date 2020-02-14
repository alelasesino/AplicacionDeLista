package com.alejandro.aplicaciondelista.model;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import static java.util.stream.Collectors.joining;

public abstract class Request<T> extends AsyncTask<Void, Integer, T> {

    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";

    private HttpURLConnection http;
    private final String URL;
    private String method;
    public Response.Listener<T> onResponse;
    public Response.ErrorListener<T> onErrorResponse;

    public Request(String method,
                   String url,
                   Response.Listener<T> onResponse,
                   Response.ErrorListener<T> onErrorResponse){
        this.URL  = url;
        this.method = method;
        this.onResponse = onResponse;
        this.onErrorResponse = onErrorResponse;
    }

    @Override
    protected T doInBackground(Void... voids) {

        try {

            URL url = new URL(URL);
            http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod(method);

            String params = joinParams();

            if(params != null)
                sendRequest(params);

        } catch (IOException ignored){}

        return (T) readResponse();

    }

    public String readResponse(){

        try {

            int responseCode = http.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {

                BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream()));

                String inputLine;
                StringBuilder response = new StringBuilder();

                try{

                    while ((inputLine = reader.readLine()) != null) {
                        response.append(inputLine);
                    }

                    reader.close();

                    return response.toString();

                }catch (Exception ignored){}

            }

        } catch (IOException ignored){}

        return null;

    }

    public abstract  T sendRequest(String params);

    public Map<String, String> getParams() {
        return null;
    }

    private String joinParams(){

        return getParams().entrySet()
                .stream()
                .map(e -> e.getKey()+"="+e.getValue())
                .collect(joining("&"));

    }

}
