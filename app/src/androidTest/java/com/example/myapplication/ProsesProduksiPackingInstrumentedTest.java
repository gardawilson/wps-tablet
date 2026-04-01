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
public class ProsesProduksiPackingInstrumentedTest {

    // ============================================================
    // CONFIG
    // ============================================================
    private static final String NO_PRODUKSI   = "X.001290";
    private static final String TGL_PRODUKSI  = "2026-03-31";
    private static final String DATE_TIME     = "2026-03-31T08:00:00";
    private static final String ACTOR_ID      = "82";
    private static final String ACTOR_NAME    = "TestUser";
    private static final String NO_SPK_TUJUAN = "";

    // ============================================================
    // DATA TEST (PASTIKAN ADA DI DB)
    // ============================================================
    private static final List<String> NO_MOULDING_LIST = Arrays.asList("T.027728");
    private static final List<String> NO_CC_LIST       = Arrays.asList("V.014031");
    private static final List<String> NO_SANDING_LIST  = Arrays.asList("W.017544");
    private static final List<String> NO_PACKING_LIST  = Arrays.asList("I.370731");

    // ============================================================
    // INSERT TEST
    // ============================================================

    @Test
    public void test_saveNoMoulding() {
        String requestId = UUID.randomUUID().toString();
        Log.d("PK_TEST", "=== test_saveNoMoulding START | " + requestId);

        ProsesProduksiApi.saveNoMoulding(
                NO_PRODUKSI, TGL_PRODUKSI, NO_MOULDING_LIST, DATE_TIME,
                "PackingProduksiInputMoulding",
                NO_SPK_TUJUAN, ACTOR_ID, ACTOR_NAME, requestId
        );

        Log.d("PK_TEST", "=== DONE");
    }

    @Test
    public void test_saveNoCC() {
        String requestId = UUID.randomUUID().toString();
        Log.d("PK_TEST", "=== test_saveNoCC START | " + requestId);

        ProsesProduksiApi.saveNoCC(
                NO_PRODUKSI, TGL_PRODUKSI, NO_CC_LIST, DATE_TIME,
                "PackingProduksiInputCCAkhir",
                NO_SPK_TUJUAN, ACTOR_ID, ACTOR_NAME, requestId
        );

        Log.d("PK_TEST", "=== DONE");
    }

    @Test
    public void test_saveNoSanding() {
        String requestId = UUID.randomUUID().toString();
        Log.d("PK_TEST", "=== test_saveNoSanding START | " + requestId);

        ProsesProduksiApi.saveNoSanding(
                NO_PRODUKSI, TGL_PRODUKSI, NO_SANDING_LIST, DATE_TIME,
                "PackingProduksiInputSanding",
                NO_SPK_TUJUAN, ACTOR_ID, ACTOR_NAME, requestId
        );

        Log.d("PK_TEST", "=== DONE");
    }

    @Test
    public void test_saveNoPacking() {
        String requestId = UUID.randomUUID().toString();
        Log.d("PK_TEST", "=== test_saveNoPacking START | " + requestId);

        ProsesProduksiApi.saveNoPacking(
                NO_PRODUKSI, TGL_PRODUKSI, NO_PACKING_LIST, DATE_TIME,
                "PackingProduksiInputBarangJadi",
                NO_SPK_TUJUAN, ACTOR_ID, ACTOR_NAME, requestId
        );

        Log.d("PK_TEST", "=== DONE");
    }

    // ============================================================
    // DELETE TEST
    // ============================================================

    @Test
    public void test_deleteAll() {
        String requestId = UUID.randomUUID().toString();
        Log.d("PK_TEST", "=== test_deleteAll START | " + requestId);

        for (String noMoulding : NO_MOULDING_LIST) {
            ProsesProduksiApi.deleteDataByNoLabel(
                    NO_PRODUKSI, noMoulding, ACTOR_ID, ACTOR_NAME, requestId);
        }

        for (String noCC : NO_CC_LIST) {
            ProsesProduksiApi.deleteDataByNoLabel(
                    NO_PRODUKSI, noCC, ACTOR_ID, ACTOR_NAME, requestId);
        }

        for (String noSanding : NO_SANDING_LIST) {
            ProsesProduksiApi.deleteDataByNoLabel(
                    NO_PRODUKSI, noSanding, ACTOR_ID, ACTOR_NAME, requestId);
        }

        for (String noPacking : NO_PACKING_LIST) {
            ProsesProduksiApi.deleteDataByNoLabel(
                    NO_PRODUKSI, noPacking, ACTOR_ID, ACTOR_NAME, requestId);
        }

        Log.d("PK_TEST", "=== DONE");
    }

    // ============================================================
    // INSERT ALL
    // ============================================================

    @Test
    public void test_saveAll() {
        String requestId = UUID.randomUUID().toString();
        Log.d("PK_TEST", "=== test_saveAll START | " + requestId);

        ProsesProduksiApi.saveNoMoulding(
                NO_PRODUKSI, TGL_PRODUKSI, NO_MOULDING_LIST, DATE_TIME,
                "PackingProduksiInputMoulding",
                NO_SPK_TUJUAN, ACTOR_ID, ACTOR_NAME, requestId
        );

        ProsesProduksiApi.saveNoCC(
                NO_PRODUKSI, TGL_PRODUKSI, NO_CC_LIST, DATE_TIME,
                "PackingProduksiInputCCAkhir",
                NO_SPK_TUJUAN, ACTOR_ID, ACTOR_NAME, requestId
        );

        ProsesProduksiApi.saveNoSanding(
                NO_PRODUKSI, TGL_PRODUKSI, NO_SANDING_LIST, DATE_TIME,
                "PackingProduksiInputSanding",
                NO_SPK_TUJUAN, ACTOR_ID, ACTOR_NAME, requestId
        );

        ProsesProduksiApi.saveNoPacking(
                NO_PRODUKSI, TGL_PRODUKSI, NO_PACKING_LIST, DATE_TIME,
                "PackingProduksiInputBarangJadi",
                NO_SPK_TUJUAN, ACTOR_ID, ACTOR_NAME, requestId
        );

        Log.d("PK_TEST", "=== DONE");
    }
}