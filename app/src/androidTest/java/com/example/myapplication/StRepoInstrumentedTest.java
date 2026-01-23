package com.example.myapplication;

import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.myapplication.api.SawnTimberApi;
import com.example.myapplication.model.StData;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class StRepoInstrumentedTest {

    @Test
    public void run_getSawnTimberHeader() {
        String noST = "E.507192"; // ganti-ganti ini aja saat testing

        StData data = SawnTimberApi.getSawnTimberHeader(noST);

        if (data == null) {
            Log.w("ST_TEST", "NULL result for noST=" + noST);
        } else {
            Log.d("ST_TEST", "FOUND: " + data.toString());
            // atau kalau kamu belum punya toString yang jelas:
            Log.d("ST_TEST", "NoST=" + data.getNoST()); // sesuaikan getter kamu
        }
    }
}
