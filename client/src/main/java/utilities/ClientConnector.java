package utilities;

import com.google.gson.Gson;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ClientConnector {

    public String post(String urlString,Object request,String authToken) throws Exception {
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(5000);
        connection.setRequestMethod("POST");


        connection.addRequestProperty("Accept", "application/json");
        connection.addRequestProperty("Content-Type", "application/json");

        setOutput(request, authToken, connection);
        return processResult(connection);
    }
    public String get(String urlString, String authToken) throws Exception {
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
    public void delete(String urlString, String authToken) throws Exception {
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
    public void put(String urlString, Object request, String authToken) throws Exception {
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod("PUT");
        setOutput(request, authToken, connection);
        processResult(connection);
    }

    private void setOutput(Object request, String authToken, HttpURLConnection connection) throws IOException {
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
    }
    private String processResult(HttpURLConnection connection) throws Exception {
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream responseBody = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(responseBody);
            BufferedReader bufferedReader = new BufferedReader(reader);
            return bufferedReader.readLine();


        } else {
            throw new HTMLException(connection.getResponseMessage(),connection.getResponseCode());

        }
    }
    }
