package com.example.myapplication.model;

public class UserIDSO {
    private String userIDSO;

    public UserIDSO(String userIDSO) {
        this.userIDSO = userIDSO;
    }

    public String getUserIDSO() {
        return userIDSO;
    }

    public void setUserIDSO(String userIDSO) {
        this.userIDSO = userIDSO;
    }

    @Override
    public String toString() {
        return userIDSO; // Menampilkan userIDSO dalam Spinner
    }
}
