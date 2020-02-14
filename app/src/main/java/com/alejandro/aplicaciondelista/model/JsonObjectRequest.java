package com.alejandro.aplicaciondelista.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.Map;

public class JsonObjectRequest extends Request<JSONObject> {

    public JsonObjectRequest(String method,
                             String url,
                             Response.Listener<JSONObject> onResponse,
                             Response.ErrorListener<JSONObject> onErrorResponse){
        super(url, method, onResponse, onErrorResponse);
    }

    @Override
    public JSONObject sendRequest(String params) {
        return null;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {

        try {

            if (!jsonObject.getBoolean("error")) {
                onResponse.onResponse(jsonObject);
                return;
            }

        } catch (JSONException ignored){}

        onErrorResponse.onErrorResponse(jsonObject);

    }

    private JSONObject sendGetRequest() throws IOException {

        http.setRequestMethod("GET");
        //http.setRequestProperty("User-Agent", "");

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

    @Override
    public Map<String, String> getParams() {
        return null;
    }

    private class PostRequestJSON extends JsonObjectRequest {

        @Override
        public JSONObject sendRequest(String params) {

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
        }

            if(item != null) {

                http.setDoOutput(true);
                http.setRequestMethod("POST");
                http.setRequestProperty("User-Agent", "");

                OutputStreamWriter output = new OutputStreamWriter(http.getOutputStream());
                output.write(item.getURLEncode());
                output.flush();
                output.close();

            }*/

            return null;
        }

    }

}
