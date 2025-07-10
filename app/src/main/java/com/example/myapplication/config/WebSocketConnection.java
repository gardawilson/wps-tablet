package com.example.myapplication.config;

import android.util.Log;

import com.example.myapplication.BuildConfig;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class WebSocketConnection {
    private static final String SERVER_URI =
            "ws://" + BuildConfig.DB_IP + ":5001";  // ⬅️ Ambil dari local.properties

    private static WebSocketConnection instance;
    private WebSocketClient webSocketClient;
    private boolean isConnected = false;

    private WebSocketListener listener;

    private WebSocketConnection() {}

    public static WebSocketConnection getInstance() {
        if (instance == null) {
            instance = new WebSocketConnection();
        }
        return instance;
    }

    public void setListener(WebSocketListener listener) {
        this.listener = listener;
    }

    public void connect() {
        if (isConnected) {
            Log.d("WebSocket", "Already connected to server.");
            return;
        }

        try {
            URI uri = new URI(SERVER_URI);
            webSocketClient = new WebSocketClient(uri) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    Log.d("WebSocket", "Connected to server");
                    isConnected = true;
                }

                @Override
                public void onMessage(String message) {
                    Log.d("WebSocket", "Received: " + message);
                    String[] parts = message.split(" ");
                    String noso = parts.length > 1 ? parts[1] : "";
                    if (message.contains("berhasil") && listener != null) {
                        listener.onMessageReceived(noso);
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Log.d("WebSocket", "Closed with exit code " + code + ", reason: " + reason);
                    isConnected = false;
                }

                @Override
                public void onError(Exception ex) {
                    Log.d("WebSocket", "Error: " + ex.getMessage());
                }
            };
            webSocketClient.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        if (webSocketClient != null && webSocketClient.isOpen()) {
            webSocketClient.close();
            Log.d("WebSocket", "Connection closed.");
        }
    }

    public interface WebSocketListener {
        void onMessageReceived(String noso);
    }

    public boolean isConnected() {
        return isConnected;
    }
}
