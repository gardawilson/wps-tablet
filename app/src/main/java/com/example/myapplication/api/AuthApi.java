package com.example.myapplication.api;

import static com.example.myapplication.config.ApiEndpoints.BASE_URL_API;

import android.util.Log;

import com.example.myapplication.config.ApiEndpoints;
import com.example.myapplication.model.LoginResponse;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AuthApi {

    public static LoginResponse login(String username, String password) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(BASE_URL_API + "/api/auth/login2");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);

            JSONObject requestBody = new JSONObject();
            requestBody.put("username", username);
            requestBody.put("password", password);

            OutputStream os = connection.getOutputStream();
            os.write(requestBody.toString().getBytes("UTF-8"));
            os.close();

            int responseCode = connection.getResponseCode();

            InputStream inputStream;
            if (responseCode >= 200 && responseCode < 300) {
                inputStream = connection.getInputStream();
            } else {
                inputStream = connection.getErrorStream();
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            return LoginResponse.fromJSON(response.toString());

        } catch (Exception e) {
            Log.e("AuthApi", "Error login: " + e.getMessage());
            return new LoginResponse(false, "Error: " + e.getMessage(), "", null);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}