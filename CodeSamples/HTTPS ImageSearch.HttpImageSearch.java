package org.jssec.android.https.imagesearch;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class HttpImageSearch extends AsyncTask<String, Void, Object> {

    @Override
    protected Object doInBackground(String... params) {
        byte[] responseArray;
        // --------------------------------------------------------
        // Communication 1st time: Execute image search
        // --------------------------------------------------------

        // *** POINT 1 *** Sensitive information must not be contained in send data.
        // Send image search character string
        StringBuilder s = new StringBuilder();
        for (String param : params){
            s.append(param);
            s.append('+');
        }
        s.deleteCharAt(s.length() - 1);

        String search_url = "http://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" +
                s.toString();

        responseArray = getByteArray(search_url);
        if (responseArray == null) {
            return null;
        }

        // *** POINT 2 *** Suppose that received data may be sent from attackers.
        // This is sample, so omit the process in case of the searching result is the data from an attacker.
        // This is sample, so omit the exception process in case of JSON purse.
        String image_url;
        try {
            String json = new String(responseArray);
            image_url = new JSONObject(json).getJSONObject("responseData")
                    .getJSONArray("results").getJSONObject(0).getString("url");
        } catch(JSONException e) {
            return e;
        }

        // --------------------------------------------------------
        // Communication 2nd time: Get images
        // --------------------------------------------------------
        // *** POINT 1 *** Sensitive information must not be contained in send data.
        if (image_url != null ) {
            responseArray = getByteArray(image_url);
            if (responseArray == null) {
                return null;
            }
        }

        // *** POINT 2 *** Suppose that received data may be sent from attackers.
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
