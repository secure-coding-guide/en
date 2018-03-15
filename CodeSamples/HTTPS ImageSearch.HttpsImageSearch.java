package org.jssec.android.https.imagesearch;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class HttpsImageSearch extends AsyncTask<String, Void, Object> {

    @Override
    protected Object doInBackground(String... params) {
        byte[] responseArray;
        // --------------------------------------------------------
        // Communication 1st time : Execute image search
        // --------------------------------------------------------
            
        // *** POINT 1 *** URI starts with https://.
        // *** POINT 2 *** Sensitive information may be contained in send data.
        StringBuilder s = new StringBuilder();
        for (String param : params){
            s.append(param);
            s.append('+');
        }
        s.deleteCharAt(s.length() - 1);

        String search_url = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" +
                s.toString();

        responseArray = getByteArray(search_url);
        if (responseArray == null) {
            return null;
        }

        // *** POINT 3 *** Handle the received data carefully and securely,
        // even though the data was sent from the server connected by HTTPS.
        // Omitted, since this is a sample. Please refer to "3.2 Handling Input Data Carefully and Securely."
        String image_url;
        try {
            String json = new String(responseArray);
            image_url = new JSONObject(json).getJSONObject("responseData")
                    .getJSONArray("results").getJSONObject(0).getString("url");
        } catch(JSONException e) {
            return e;
        }
            
        // --------------------------------------------------------
        // Communication 2nd time : Get image
        // --------------------------------------------------------
            
        // *** POINT 1 *** URI starts with https://.
        // *** POINT 2 *** Sensitive information may be contained in send data.
        if (image_url != null ) {
            responseArray = getByteArray(image_url);
            if (responseArray == null) {
                return null;
            }
        }

        return responseArray;
    }

    private byte[] getByteArray(String strUrl) {
        byte[] buff = new byte[1024];
        byte[] result = null;
        HttpURLConnection response;
        BufferedInputStream inputStream = null;
        ByteArrayOutputStream responseArray = null;
        int length;

        try {
            URL url = new URL(strUrl);
            response = (HttpURLConnection) url.openConnection();
            response.setRequestMethod("GET");
            response.connect();
            checkResponse(response);

            inputStream = new BufferedInputStream(response.getInputStream());
            responseArray = new ByteArrayOutputStream();

            while ((length = inputStream.read(buff)) != -1) {
                if (length > 0) {
                    responseArray.write(buff, 0, length);
                }
            }
            result = responseArray.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // This is sample, so omit the exception process
                }
            }
            if (responseArray != null) {
                try {
                    responseArray.close();
                } catch (IOException e) {
                    // This is sample, so omit the exception process
                }
            }
        }
        return result;
    }

    private void checkResponse(HttpURLConnection response) throws IOException {
        int statusCode = response.getResponseCode();
        if (HttpURLConnection.HTTP_OK != statusCode) {
            throw new IOException("HttpStatus: " + statusCode);
        }
    }
}
