package com.alejandro.aplicaciondelista.model;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.alejandro.aplicaciondelista.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ConcurrentModificationException;
import java.util.Map;

import static java.util.stream.Collectors.joining;

public class JsonObjectRequest extends AsyncTask<Void, Integer, JSONObject> {

    private final String TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyIjoxNTgxODc5ODM5MTQzfQ.K7HWKqzjmKb52QSV4D6WI4TiiKR_PMTCy43xFTq2XaY";
    static final String GET = "GET";
    static final String POST = "POST";
    static final String PUT = "PUT";
    static final String DELETE = "DELETE";

    private final String boundary;
    private static final String LINE_FEED = "\r\n";
    private String charset;

    private Context context;
    private HttpURLConnection http;
    private OutputStream outputStream;
    private PrintWriter writer;

    private final String requestUrl;
    private String method;

    private ProgressDialog dialog;
    private Response.Listener<JSONObject> onResponse;
    private Response.ErrorListener<JSONObject> onErrorResponse;

    public JsonObjectRequest(Context context, String method, String requestUrl,
                             Response.Listener<JSONObject> onResponse,
                             Response.ErrorListener<JSONObject> onErrorResponse){
        this.method = method;
        this.requestUrl  = requestUrl;
        this.onResponse = onResponse;
        this.onErrorResponse = onErrorResponse;
        this.context = context;

        boundary = "===" + System.currentTimeMillis() + "===";
        charset = "UTF-8";

        setProgressDialog();

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
    protected void onPreExecute() {

        dialog.show();

    }

    @Override
    protected JSONObject doInBackground(Void... voids) {

        try {

            URL url = new URL(requestUrl);
            http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setUseCaches(false);
            http.setRequestProperty("Authorization", "Bearer " + TOKEN);

            Map<String, String> params = getParams();

            if(params != null)
                sendRequest(params);

            return readResponse();

        } catch (IOException e){
            e.printStackTrace();
        }

        return null;

    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {

        dialog.dismiss();


        try {

            if(jsonObject != null)
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

    private void sendRequest(Map<String, String> params) throws IOException{

        http.setDoOutput(true);
        http.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        //http.setRequestProperty("User-Agent", "CodeJava Agent");

        outputStream = http.getOutputStream();
        writer = new PrintWriter(new OutputStreamWriter(outputStream, charset), true);

        for(Map.Entry<String, String> param : params.entrySet()){

            String key = param.getKey();
            String value = param.getValue();
            if(key.equalsIgnoreCase("imagen")){

                if(value != null) {
                    Uri uri = Uri.parse(value);
                    Bitmap bitmap = Utils.getImage(context, uri);

                    if(bitmap != null) {
                        addFilePart(key, bitmap);
                        Log.d("PRUEBA", "HAY IMAGEN");
                    }

                }

            } else {
                addFormField(key, value);
            }

        }

        writer.append(LINE_FEED).flush();
        writer.append("--" + boundary + "--").append(LINE_FEED);
        writer.close();

    }

    private void setProgressDialog() {

        String title = "";
        dialog = new ProgressDialog(context);
        Log.d("PRUEBA", method);
        switch (method){
            case GET:
                title = "Cargando productos";
                break;
            case POST:
                title = "Insertando producto";
                break;
            case DELETE:
                title = "Borrando producto";
                break;
            case PUT:
                title = "Actualizando producto";
                break;
        }

        dialog.setTitle(title);
        dialog.setMessage("Please wait ...");

    }

    public void addFormField(String name, String value) {
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"" + name + "\"").append(LINE_FEED);
        //writer.append("Content-Type: text/plain; charset=" + charset).append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.append(value).append(LINE_FEED);
        writer.flush();
    }

    public void addFilePart(String fieldName, Bitmap uploadFile) throws IOException {

        byte[] bitmapdata = Utils.getBytes(uploadFile);

        InputStream inputStream = new ByteArrayInputStream(bitmapdata);
        String fileName = "image.png";

        writer.append("--" + boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + fileName + "\"").append(LINE_FEED);
        writer.append("Content-Type: image/png").append(LINE_FEED);
        writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.flush();

        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        outputStream.flush();
        inputStream.close();

        writer.append(LINE_FEED);
        writer.flush();

    }

}
