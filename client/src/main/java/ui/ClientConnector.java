package ui;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ClientConnector {

    public String post(String urlString,Object request,String authToken) throws IOException {
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod("POST");


        connection.addRequestProperty("Accept", "text/html");
        if (authToken != null) {
            connection.addRequestProperty("Authorization", authToken);
        }
        connection.setDoOutput(true);

        connection.connect();

        try(OutputStream requestBody = connection.getOutputStream()) {
            Gson gson = new Gson();
            requestBody.write(gson.toJson(request).getBytes());

        }

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream responseBody = connection.getInputStream();


        } else {
            InputStream responseBody = connection.getErrorStream();
            throw new IOException(connection.getResponseMessage());

        }
    }
}
