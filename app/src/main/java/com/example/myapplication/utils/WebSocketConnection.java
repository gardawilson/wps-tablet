package com.example.myapplication.utils;

import java.net.URI;
import android.util.Log;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class WebSocketConnection {
    private static final String SERVER_URI = "ws://192.168.11.153:5001";  // Ganti dengan alamat WebSocket Anda
    private static WebSocketConnection instance;
    private WebSocketClient webSocketClient;
    private boolean isConnected = false;

    private WebSocketListener listener;

    // Singleton pattern untuk memastikan hanya satu instance
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
            return; // Jangan konek lagi jika sudah terhubung
        }

        URI uri;
        try {
            uri = new URI(SERVER_URI);
            webSocketClient = new WebSocketClient(uri) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    Log.d("WebSocket", "Connected to server");
                    isConnected = true; // Set status connected setelah terhubung
                }

                @Override
                public void onMessage(String message) {
                    Log.d("WebSocket", "Received: " + message);

                    // Menangkap nilai noSO dari pesan
                    String[] parts = message.split(" ");  // Misalnya "Data 123 berhasil diinsert."
                    String noso = parts[1];  // noso berada di index 1

                    if (message.contains("berhasil")) {
                        // Trigger fetch data saat menerima pesan tersebut
                        if (listener != null) {
                            listener.onMessageReceived(noso); // Panggil listener
                        }
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Log.d("WebSocket", "Closed with exit code " + code + " additional info: " + reason);
                    isConnected = false; // Set status disconnected setelah koneksi terputus
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

    // Menambahkan metode untuk menutup koneksi WebSocket
    public void closeConnection() {
        if (webSocketClient != null && webSocketClient.isOpen()) {
            webSocketClient.close();
            Log.d("WebSocket", "Connection closed.");
        }
    }

    // Interface untuk listener
    public interface WebSocketListener {
        void onMessageReceived(String noso); // Metode yang dipanggil saat pesan diterima
    }

    public boolean isConnected() {
        return isConnected;
    }
}
