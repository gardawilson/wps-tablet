package com.example.myapplication.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BluetoothEscPosPrinter {
    private static final String TAG = "BluetoothEscPosPrinter";
    private static final UUID SPP_UUID =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final int TARGET_WIDTH_DOTS = 576;
    private static final int FEED_LINES_BETWEEN_PAGES = 1;
    private static final int FEED_LINES_AFTER_PRINT = 4;
    private static final int CONSERVATIVE_THRESHOLD = 145;
    private static final int CONSERVATIVE_CHUNK_DELAY_MS = 12;
    private static final int CONSERVATIVE_CHUNK_SIZE = 256;
    private static final int CONSERVATIVE_POST_INIT_DELAY_MS = 120;

    private BluetoothEscPosPrinter() {
    }

    private static class PrintOptions {
        private final int targetWidthDots;
        private final int threshold;
        private final boolean useDithering;
        private final boolean sendCut;
        private final int chunkDelayMs;
        private final int chunkSize;
        private final int postInitDelayMs;
        private final String profileName;

        private PrintOptions(int targetWidthDots, int threshold, boolean useDithering, boolean sendCut,
                             int chunkDelayMs, int chunkSize, int postInitDelayMs, String profileName) {
            this.targetWidthDots = targetWidthDots;
            this.threshold = threshold;
            this.useDithering = useDithering;
            this.sendCut = sendCut;
            this.chunkDelayMs = chunkDelayMs;
            this.chunkSize = chunkSize;
            this.postInitDelayMs = postInitDelayMs;
            this.profileName = profileName;
        }
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
        Log.d(TAG, "printBitmaps:start device=" + safeName(device) + " addr=" + device.getAddress() + " pages=" + pages.size());
        PrintOptions options = resolvePrintOptions();
        Log.d(TAG, "printBitmaps:profile=" + options.profileName
                + " width=" + options.targetWidthDots
                + " threshold=" + options.threshold
                + " dithering=" + options.useDithering
                + " cut=" + options.sendCut
                + " chunkDelayMs=" + options.chunkDelayMs
                + " chunkSize=" + options.chunkSize
                + " postInitDelayMs=" + options.postInitDelayMs);

        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) return false;
        if (adapter.isDiscovering()) {
            adapter.cancelDiscovery();
        }

        BluetoothSocket socket = null;
        OutputStream out = null;
        try {
            socket = connectWithFallback(device, adapter);
            out = socket.getOutputStream();
            Log.d(TAG, "printBitmaps:connected");

            writeChunked(out, new byte[]{0x1B, 0x40}, options.chunkDelayMs, options.chunkSize);
            sleepQuietly(options.postInitDelayMs);
            Log.d(TAG, "printBitmaps:init sent");

            for (int i = 0; i < pages.size(); i++) {
                Bitmap page = pages.get(i);
                Bitmap resized = resizeToWidth(page, options.targetWidthDots);
                Bitmap trimmed = trimBottomWhitespace(resized);
                byte[] imageCmd = bitmapToEscPosRaster(trimmed, options.threshold, options.useDithering);
                Log.d(TAG, "printBitmaps:page=" + i
                        + " original=" + page.getWidth() + "x" + page.getHeight()
                        + " resized=" + resized.getWidth() + "x" + resized.getHeight()
                        + " trimmed=" + trimmed.getWidth() + "x" + trimmed.getHeight()
                        + " rasterBytes=" + imageCmd.length);
                writeChunked(out, imageCmd, options.chunkDelayMs, options.chunkSize);
                writeChunked(out, new byte[]{0x1B, 0x64, (byte) FEED_LINES_BETWEEN_PAGES}, options.chunkDelayMs, options.chunkSize);
            }

            writeChunked(out, new byte[]{0x1B, 0x64, (byte) FEED_LINES_AFTER_PRINT}, options.chunkDelayMs, options.chunkSize);

            if (options.sendCut) {
                writeChunked(out, new byte[]{0x1D, 0x56, 0x01}, options.chunkDelayMs, options.chunkSize);
            }
            out.flush();
            Log.d(TAG, "printBitmaps:done");
            return true;
        } catch (IOException e) {
            Log.e(TAG, "printBitmaps:io failure", e);
            throw e;
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

    private static BluetoothSocket connectWithFallback(BluetoothDevice device, BluetoothAdapter adapter) throws IOException {
        IOException lastError = null;

        // Attempt 1: secure SPP RFCOMM.
        try {
            if (adapter.isDiscovering()) {
                adapter.cancelDiscovery();
            }
            BluetoothSocket socket = device.createRfcommSocketToServiceRecord(SPP_UUID);
            Log.d(TAG, "connectWithFallback: try secure uuid=" + SPP_UUID);
            socket.connect();
            Log.d(TAG, "connectWithFallback: secure success");
            return socket;
        } catch (IOException e) {
            lastError = e;
            Log.w(TAG, "connectWithFallback: secure failed", e);
        }

        // Attempt 2: insecure SPP RFCOMM (often needed on some thermal printers/ROMs).
        try {
            if (adapter.isDiscovering()) {
                adapter.cancelDiscovery();
            }
            BluetoothSocket socket = device.createInsecureRfcommSocketToServiceRecord(SPP_UUID);
            Log.d(TAG, "connectWithFallback: try insecure uuid=" + SPP_UUID);
            socket.connect();
            Log.d(TAG, "connectWithFallback: insecure success");
            return socket;
        } catch (IOException e) {
            lastError = e;
            Log.w(TAG, "connectWithFallback: insecure failed", e);
        }

        // Attempt 3: reflection fallback to RFCOMM channel 1.
        try {
            if (adapter.isDiscovering()) {
                adapter.cancelDiscovery();
            }
            Method method = device.getClass().getMethod("createRfcommSocket", int.class);
            BluetoothSocket socket = (BluetoothSocket) method.invoke(device, 1);
            Log.d(TAG, "connectWithFallback: try reflection channel=1");
            socket.connect();
            Log.d(TAG, "connectWithFallback: reflection success");
            return socket;
        } catch (Exception e) {
            Log.w(TAG, "connectWithFallback: reflection failed", e);
            if (e instanceof IOException) {
                lastError = (IOException) e;
            } else if (lastError == null) {
                lastError = new IOException("Bluetooth connect fallback failed", e);
            }
        }

        throw lastError != null ? lastError : new IOException("Bluetooth connect failed");
    }

    private static PrintOptions resolvePrintOptions() {
        return new PrintOptions(
                TARGET_WIDTH_DOTS,
                CONSERVATIVE_THRESHOLD,
                true,
                false,
                CONSERVATIVE_CHUNK_DELAY_MS,
                CONSERVATIVE_CHUNK_SIZE,
                CONSERVATIVE_POST_INIT_DELAY_MS,
                "UNIFIED_CONSERVATIVE_GSV0_576"
        );
    }

    private static Bitmap resizeToWidth(Bitmap src, int targetWidth) {
        if (src == null) return null;
        if (src.getWidth() == targetWidth) return src;

        float ratio = (float) targetWidth / (float) src.getWidth();
        int targetHeight = Math.max(1, Math.round(src.getHeight() * ratio));
        return Bitmap.createScaledBitmap(src, targetWidth, targetHeight, true);
    }

    private static byte[] bitmapToEscPosRaster(Bitmap bitmap, int blackThreshold, boolean allowDithering) throws IOException {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int widthBytes = (width + 7) / 8;

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
                if (a < 128) gray = 255;
                lum[y][x] = gray;
                if (gray > 40 && gray < 220) {
                    midToneCount++;
                }
            }
        }

        boolean useDithering = allowDithering && totalCount > 0 && ((float) midToneCount / (float) totalCount) > 0.01f;
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
                            int threshold = bayer4[y & 3][x & 3] * 16;
                            black = gray < threshold;
                        } else {
                            black = gray < blackThreshold;
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

    private static void writeChunked(OutputStream out, byte[] data, int chunkDelayMs, int chunkSize) throws IOException {
        if (data == null || data.length == 0) return;
        int offset = 0;
        int chunkCount = 0;
        while (offset < data.length) {
            int len = Math.min(chunkSize, data.length - offset);
            out.write(data, offset, len);
            offset += len;
            chunkCount++;
            if (chunkDelayMs > 0) {
                sleepQuietly(chunkDelayMs);
            }
        }
        Log.d(TAG, "writeChunked: bytes=" + data.length + " chunks=" + chunkCount + " chunkSize=" + chunkSize + " delayMs=" + chunkDelayMs);
    }

    private static void sleepQuietly(int delayMs) {
        if (delayMs <= 0) return;
        try {
            Thread.sleep(delayMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static String safeName(BluetoothDevice device) {
        if (device == null) return "null";
        String name = device.getName();
        return name != null && !name.trim().isEmpty() ? name : "Unknown";
    }
}
