package com.example.myapplication.model;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class LoginResponse {
    private boolean success;
    private String message;
    private String token;
    private UserData user;

    public static class UserData {
        private int idUsername;
        private String username;
        private String fullName;
        private List<String> permissions;

        public UserData(int idUsername, String username, String fullName, List<String> permissions) {
            this.idUsername = idUsername;
            this.username = username;
            this.fullName = fullName;
            this.permissions = permissions;
        }

        public int getIdUsername() {
            return idUsername;
        }

        public String getUsername() {
            return username;
        }

        public String getFullName() {
            return fullName;
        }

        public List<String> getPermissions() {
            return permissions;
        }
    }

    public LoginResponse(boolean success, String message, String token, UserData user) {
        this.success = success;
        this.message = message;
        this.token = token;
        this.user = user;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

    public UserData getUser() {
        return user;
    }

    public static LoginResponse fromJSON(String jsonString) {
        try {
            JSONObject json = new JSONObject(jsonString);
            boolean success = json.getBoolean("success");
            String message = json.getString("message");
            String token = json.optString("token", "");

            UserData userData = null;
            if (json.has("user")) {
                JSONObject userObj = json.getJSONObject("user");
                int idUsername = userObj.getInt("idUsername");
                String username = userObj.getString("username");
                String fullName = userObj.getString("fullName");

                List<String> permissions = new ArrayList<>();
                if (userObj.has("permissions")) {
                    JSONArray permsArray = userObj.getJSONArray("permissions");
                    for (int i = 0; i < permsArray.length(); i++) {
                        permissions.add(permsArray.getString(i));
                    }
                }

                userData = new UserData(idUsername, username, fullName, permissions);
            }

            return new LoginResponse(success, message, token, userData);
        } catch (Exception e) {
            return new LoginResponse(false, "Error parsing response: " + e.getMessage(), "", null);
        }
    }
}