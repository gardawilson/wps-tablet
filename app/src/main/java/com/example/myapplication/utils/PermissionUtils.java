package com.example.myapplication.utils;

import android.content.Context;
import android.view.View;

import java.util.List;

public class PermissionUtils {
    /**
     * Memeriksa permission dan menonaktifkan/mengubah tampilan View jika tidak ada permission.
     * @param context Konteks aplikasi.
     * @param view View yang akan diatur (bisa CardView, Button, dll.).
     * @param permissionKey Kunci permission yang dibutuhkan, contoh: "stock_opname:read".
     */
    public static void permissionCheck(Context context, View view, String permissionKey) {
        List<String> userPermissions = SharedPrefUtils.getPermissions(context);
        if (userPermissions == null || !userPermissions.contains(permissionKey)) {
            view.setAlpha(0.5f);
            view.setEnabled(false);
        }
    }
}