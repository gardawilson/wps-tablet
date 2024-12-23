package com.example.myapplication.utils;

import android.util.Log;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import java.nio.ByteBuffer;

public class QRCodeUtils {

    public static String decodeQRCode(byte[] data, int width, int height) {
        try {
            // Buat sumber luminance dari data mentah
            LuminanceSource source = new PlanarYUVLuminanceSource(
                    data, width, height, 0, 0, width, height, false);

            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Reader reader = new QRCodeReader();
            Result result = reader.decode(bitmap);

            return result.getText(); // Hasil decoding QR Code
        } catch (Exception e) {
            Log.e("QRCodeUtils", "QR Code decoding failed: " + e.getMessage());
            return null; // Tidak ada QR Code yang terdeteksi
        }
    }
}
