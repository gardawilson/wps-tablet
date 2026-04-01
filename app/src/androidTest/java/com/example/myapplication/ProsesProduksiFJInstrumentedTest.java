package com.example.myapplication;

import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.myapplication.api.ProsesProduksiApi;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RunWith(AndroidJUnit4.class)
public class ProsesProduksiFJInstrumentedTest {

    // ============================================================
    // Ganti nilai ini sesuai data yang ada di database kamu
    // ============================================================
    private static final String NO_PRODUKSI   = "SA.004056";
    private static final String TGL_PRODUKSI  = "2026-03-31";
    private static final String DATE_TIME     = "2026-03-31T08:00:00";
    private static final String ACTOR_ID      = "82";
    private static final String ACTOR_NAME    = "TestUser";
    private static final String NO_SPK_TUJUAN = "";  // kosongkan jika tidak pakai SPK tujuan

    // Ganti dengan nomor yang benar-benar ada di DB
    private static final List<String> NO_S4S_LIST = Arrays.asList("R.051422", "R.051518");
    private static final List<String> NO_CC_LIST  = Arrays.asList("V.014031");
    // ============================================================

    // -----------------------------------------------------------
    // INSERT tests
    // -----------------------------------------------------------

    @Test
    public void test_saveNoS4S() {
        String requestId = UUID.randomUUID().toString();
        Log.d("FJ_TEST", "=== test_saveNoS4S START | requestId=" + requestId);
        ProsesProduksiApi.saveNoS4S(NO_PRODUKSI, TGL_PRODUKSI, NO_S4S_LIST, DATE_TIME,
                "FJProduksiInputS4S", NO_SPK_TUJUAN, ACTOR_ID, ACTOR_NAME, requestId);
        Log.d("FJ_TEST", "=== test_saveNoS4S DONE");
    }

    @Test
    public void test_saveNoCC() {
        String requestId = UUID.randomUUID().toString();
        Log.d("FJ_TEST", "=== test_saveNoCC START | requestId=" + requestId);
        ProsesProduksiApi.saveNoCC(NO_PRODUKSI, TGL_PRODUKSI, NO_CC_LIST, DATE_TIME,
                "FJProduksiInputCCAkhir", NO_SPK_TUJUAN, ACTOR_ID, ACTOR_NAME, requestId);
        Log.d("FJ_TEST", "=== test_saveNoCC DONE");
    }

    // -----------------------------------------------------------
    // DELETE tests
    // -----------------------------------------------------------

    @Test
    public void test_deleteNoS4S() {
        String requestId = UUID.randomUUID().toString();
        Log.d("FJ_TEST", "=== test_deleteNoS4S START | requestId=" + requestId);
        for (String noS4S : NO_S4S_LIST) {
            ProsesProduksiApi.deleteDataByNoLabel(NO_PRODUKSI, noS4S, ACTOR_ID, ACTOR_NAME, requestId);
            Log.d("FJ_TEST", "Deleted: " + noS4S);
        }
        Log.d("FJ_TEST", "=== test_deleteNoS4S DONE");
    }

    @Test
    public void test_deleteNoCC() {
        String requestId = UUID.randomUUID().toString();
        Log.d("FJ_TEST", "=== test_deleteNoCC START | requestId=" + requestId);
        for (String noCC : NO_CC_LIST) {
            ProsesProduksiApi.deleteDataByNoLabel(NO_PRODUKSI, noCC, ACTOR_ID, ACTOR_NAME, requestId);
            Log.d("FJ_TEST", "Deleted: " + noCC);
        }
        Log.d("FJ_TEST", "=== test_deleteNoCC DONE");
    }

    // -----------------------------------------------------------
    // INSERT semua sekaligus
    // -----------------------------------------------------------

    @Test
    public void test_saveAll() {
        String requestId = UUID.randomUUID().toString();
        Log.d("FJ_TEST", "=== test_saveAll START | requestId=" + requestId);
        ProsesProduksiApi.saveNoS4S(NO_PRODUKSI, TGL_PRODUKSI, NO_S4S_LIST, DATE_TIME,
                "FJProduksiInputS4S", NO_SPK_TUJUAN, ACTOR_ID, ACTOR_NAME, requestId);
        ProsesProduksiApi.saveNoCC(NO_PRODUKSI, TGL_PRODUKSI, NO_CC_LIST, DATE_TIME,
                "FJProduksiInputCCAkhir", NO_SPK_TUJUAN, ACTOR_ID, ACTOR_NAME, requestId);
        Log.d("FJ_TEST", "=== test_saveAll DONE");
    }

    // -----------------------------------------------------------
    // DELETE semua sekaligus
    // -----------------------------------------------------------

    @Test
    public void test_deleteAll() {
        String requestId = UUID.randomUUID().toString();
        Log.d("FJ_TEST", "=== test_deleteAll START | requestId=" + requestId);
        for (String noS4S : NO_S4S_LIST) {
            ProsesProduksiApi.deleteDataByNoLabel(NO_PRODUKSI, noS4S, ACTOR_ID, ACTOR_NAME, requestId);
            Log.d("FJ_TEST", "Deleted: " + noS4S);
        }
        for (String noCC : NO_CC_LIST) {
            ProsesProduksiApi.deleteDataByNoLabel(NO_PRODUKSI, noCC, ACTOR_ID, ACTOR_NAME, requestId);
            Log.d("FJ_TEST", "Deleted: " + noCC);
        }
        Log.d("FJ_TEST", "=== test_deleteAll DONE");
    }
}
