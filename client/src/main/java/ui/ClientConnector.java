package ui;

import com.google.gson.Gson;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ClientConnector {

    public String post(String urlString,Object request,String authToken) throws IOException {
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod("POST");


        connection.addRequestProperty("Accept", "application/json");
        connection.addRequestProperty("Content-Type", "application/json");

        if (authToken != null) {
            connection.addRequestProperty("Authorization", authToken);
        }
        connection.setDoOutput(true);



        try(OutputStream requestBody = connection.getOutputStream()) {
            Gson gson = new Gson();
            OutputStreamWriter writer = new OutputStreamWriter(requestBody);

            writer.write(gson.toJson(request));
            writer.flush();

        }
        connection.connect();
        return processResult(connection);
    }

    public String get(String urlString,Object request,String authToken) throws IOException {
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod("GET");

        connection.addRequestProperty("Accept", "text/html");
        if (authToken != null) {
            connection.addRequestProperty("Authorization", authToken);
        }

        connection.connect();
        return processResult(connection);
    }

    public void delete(String urlString,Object request,String authToken) throws IOException {
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod("DELETE");
        if (authToken != null) {
            connection.addRequestProperty("Authorization", authToken);
        }

        connection.connect();
        processResult(connection);
    }



    private String processResult(HttpURLConnection connection) throws IOException {
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream responseBody = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(responseBody);
            BufferedReader bufferedReader = new BufferedReader(reader);
            return bufferedReader.readLine();

        } else {
            InputStream responseBody = connection.getErrorStream();
            throw new IOException(connection.getResponseMessage() + ": " + connection.getResponseCode());

        }
    }
}
