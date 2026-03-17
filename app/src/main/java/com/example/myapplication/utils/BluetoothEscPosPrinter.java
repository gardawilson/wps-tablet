package com.example.myapplication.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.graphics.Bitmap;
import android.graphics.Color;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BluetoothEscPosPrinter {
    private static final UUID SPP_UUID =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final int TARGET_WIDTH_DOTS = 576; // umum untuk thermal 80mm
    private static final int FEED_LINES_BETWEEN_PAGES = 1;
    private static final int FEED_LINES_AFTER_PRINT = 4;

    private BluetoothEscPosPrinter() {
    }

    public static List<BluetoothDevice> getBondedDevices() {
        List<BluetoothDevice> devices = new ArrayList<>();
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) return devices;

        Set<BluetoothDevice> bonded = adapter.getBondedDevices();
        if (bonded != null) {
            devices.addAll(bonded);
        }
        return devices;
    }

    public static boolean printBitmaps(BluetoothDevice device, List<Bitmap> pages) throws IOException {
        if (device == null || pages == null || pages.isEmpty()) return false;

        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) return false;
        if (adapter.isDiscovering()) {
            adapter.cancelDiscovery();
        }

        BluetoothSocket socket = null;
        OutputStream out = null;
        try {
            socket = device.createRfcommSocketToServiceRecord(SPP_UUID);
            socket.connect();
            out = socket.getOutputStream();

            // Initialize printer
            out.write(new byte[]{0x1B, 0x40});

            for (Bitmap page : pages) {
                Bitmap resized = resizeToWidth(page, TARGET_WIDTH_DOTS);
                Bitmap trimmed = trimBottomWhitespace(resized);
                byte[] imageCmd = bitmapToEscPosRaster(trimmed);
                out.write(imageCmd);
                // Jarak tipis antar halaman jika ada multi-page.
                out.write(new byte[]{0x1B, 0x64, (byte) FEED_LINES_BETWEEN_PAGES});
            }

            // Tambahan kertas kosong untuk area potong user.
            out.write(new byte[]{0x1B, 0x64, (byte) FEED_LINES_AFTER_PRINT});

            // Partial cut (abaikan jika printer tidak support)
            out.write(new byte[]{0x1D, 0x56, 0x01});
            out.flush();
            return true;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception ignored) {
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (Exception ignored) {
                }
            }
        }
    }

    private static Bitmap resizeToWidth(Bitmap src, int targetWidth) {
        if (src == null) return null;
        if (src.getWidth() == targetWidth) return src;

        float ratio = (float) targetWidth / (float) src.getWidth();
        int targetHeight = Math.max(1, Math.round(src.getHeight() * ratio));
        return Bitmap.createScaledBitmap(src, targetWidth, targetHeight, true);
    }

    private static byte[] bitmapToEscPosRaster(Bitmap bitmap) throws IOException {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int widthBytes = (width + 7) / 8;

        // Pre-calc luminance to support adaptive dithering for watermark/mid-tone areas.
        int[][] lum = new int[height][width];
        int midToneCount = 0;
        int totalCount = width * height;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = bitmap.getPixel(x, y);
                int a = Color.alpha(pixel);
                int r = Color.red(pixel);
                int g = Color.green(pixel);
                int b = Color.blue(pixel);
                int gray = (r * 30 + g * 59 + b * 11) / 100;
                if (a < 128) gray = 255; // transparent => white
                lum[y][x] = gray;
                if (gray > 40 && gray < 220) {
                    midToneCount++;
                }
            }
        }

        // If many mid-tone pixels exist (typical watermark), use ordered dithering.
        boolean useDithering = totalCount > 0 && ((float) midToneCount / (float) totalCount) > 0.01f;
        final int[][] bayer4 = {
                {0, 8, 2, 10},
                {12, 4, 14, 6},
                {3, 11, 1, 9},
                {15, 7, 13, 5}
        };

        ByteArrayOutputStream imageData = new ByteArrayOutputStream(widthBytes * height);

        for (int y = 0; y < height; y++) {
            for (int xByte = 0; xByte < widthBytes; xByte++) {
                int value = 0;
                for (int bit = 0; bit < 8; bit++) {
                    int x = xByte * 8 + bit;
                    if (x < width) {
                        int gray = lum[y][x];
                        boolean black;
                        if (useDithering) {
                            // Ordered dithering 4x4, threshold in range [0..255].
                            int threshold = bayer4[y & 3][x & 3] * 16;
                            black = gray < threshold;
                        } else {
                            black = gray < 128;
                        }
                        if (black) {
                            value |= (1 << (7 - bit));
                        }
                    }
                }
                imageData.write(value);
            }
        }

        ByteArrayOutputStream command = new ByteArrayOutputStream();
        // GS v 0 m xL xH yL yH d1...dk
        command.write(0x1D);
        command.write(0x76);
        command.write(0x30);
        command.write(0x00);
        command.write(widthBytes & 0xFF);
        command.write((widthBytes >> 8) & 0xFF);
        command.write(height & 0xFF);
        command.write((height >> 8) & 0xFF);
        command.write(imageData.toByteArray());

        return command.toByteArray();
    }

    private static Bitmap trimBottomWhitespace(Bitmap src) {
        if (src == null) return null;
        int width = src.getWidth();
        int height = src.getHeight();
        if (width <= 0 || height <= 0) return src;

        int lastContentRow = -1;

        for (int y = height - 1; y >= 0; y--) {
            boolean hasInk = false;
            for (int x = 0; x < width; x++) {
                int pixel = src.getPixel(x, y);
                int a = Color.alpha(pixel);
                int r = Color.red(pixel);
                int g = Color.green(pixel);
                int b = Color.blue(pixel);
                int gray = (r * 30 + g * 59 + b * 11) / 100;

                // Anggap "ada konten" jika pixel tidak putih/transparent.
                if (a >= 16 && gray < 245) {
                    hasInk = true;
                    break;
                }
            }
            if (hasInk) {
                lastContentRow = y;
                break;
            }
        }

        if (lastContentRow < 0) {
            return Bitmap.createBitmap(width, 1, Bitmap.Config.ARGB_8888);
        }

        int bottomPadding = 2;
        int newHeight = Math.min(height, lastContentRow + 1 + bottomPadding);
        if (newHeight == height) return src;

        return Bitmap.createBitmap(src, 0, 0, width, Math.max(1, newHeight));
    }
}
