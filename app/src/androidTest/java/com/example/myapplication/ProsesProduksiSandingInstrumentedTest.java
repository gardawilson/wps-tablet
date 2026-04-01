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
public class ProsesProduksiSandingInstrumentedTest {

    // ============================================================
    // Ganti nilai ini sesuai data yang ada di database kamu
    // ============================================================
    private static final String NO_PRODUKSI   = "WA.001294";
    private static final String TGL_PRODUKSI  = "2026-04-01";
    private static final String DATE_TIME     = "2026-04-01T08:00:00";
    private static final String ACTOR_ID      = "82";
    private static final String ACTOR_NAME    = "TestUser";
    private static final String NO_SPK_TUJUAN = "";  // kosongkan jika tidak pakai SPK tujuan

    // Ganti dengan nomor yang benar-benar ada di DB
    private static final List<String> NO_MOULDING_LIST = Arrays.asList("T.027728");
    private static final List<String> NO_FJ_LIST       = Arrays.asList("S.015742");
    private static final List<String> NO_CC_LIST       = Arrays.asList("V.014031");
    private static final List<String> NO_SANDING_LIST  = Arrays.asList("W.017544");
    private static final List<String> NO_PACKING_LIST  = Arrays.asList("I.370731");
    // ============================================================

    // -----------------------------------------------------------
    // INSERT tests
    // -----------------------------------------------------------

    @Test
    public void test_saveNoMoulding() {
        String requestId = UUID.randomUUID().toString();
        Log.d("SND_TEST", "=== test_saveNoMoulding START | requestId=" + requestId);
        ProsesProduksiApi.saveNoMoulding(NO_PRODUKSI, TGL_PRODUKSI, NO_MOULDING_LIST, DATE_TIME,
                "SandingProduksiInputMoulding", NO_SPK_TUJUAN, ACTOR_ID, ACTOR_NAME, requestId);
        Log.d("SND_TEST", "=== test_saveNoMoulding DONE");
    }

    @Test
    public void test_saveNoFJ() {
        String requestId = UUID.randomUUID().toString();
        Log.d("SND_TEST", "=== test_saveNoFJ START | requestId=" + requestId);
        ProsesProduksiApi.saveNoFJ(NO_PRODUKSI, TGL_PRODUKSI, NO_FJ_LIST, DATE_TIME,
                "SandingProduksiInputFJ", NO_SPK_TUJUAN, ACTOR_ID, ACTOR_NAME, requestId);
        Log.d("SND_TEST", "=== test_saveNoFJ DONE");
    }

    @Test
    public void test_saveNoCC() {
        String requestId = UUID.randomUUID().toString();
        Log.d("SND_TEST", "=== test_saveNoCC START | requestId=" + requestId);
        ProsesProduksiApi.saveNoCC(NO_PRODUKSI, TGL_PRODUKSI, NO_CC_LIST, DATE_TIME,
                "SandingProduksiInputCCAkhir", NO_SPK_TUJUAN, ACTOR_ID, ACTOR_NAME, requestId);
        Log.d("SND_TEST", "=== test_saveNoCC DONE");
    }

    @Test
    public void test_saveNoSanding() {
        String requestId = UUID.randomUUID().toString();
        Log.d("SND_TEST", "=== test_saveNoSanding START | requestId=" + requestId);
        ProsesProduksiApi.saveNoSanding(NO_PRODUKSI, TGL_PRODUKSI, NO_SANDING_LIST, DATE_TIME,
                "SandingProduksiInputSanding", NO_SPK_TUJUAN, ACTOR_ID, ACTOR_NAME, requestId);
        Log.d("SND_TEST", "=== test_saveNoSanding DONE");
    }

    @Test
    public void test_saveNoPacking() {
        String requestId = UUID.randomUUID().toString();
        Log.d("SND_TEST", "=== test_saveNoPacking START | requestId=" + requestId);
        ProsesProduksiApi.saveNoPacking(NO_PRODUKSI, TGL_PRODUKSI, NO_PACKING_LIST, DATE_TIME,
                "SandingProduksiInputBarangJadi", NO_SPK_TUJUAN, ACTOR_ID, ACTOR_NAME, requestId);
        Log.d("SND_TEST", "=== test_saveNoPacking DONE");
    }

    // -----------------------------------------------------------
    // DELETE tests
    // -----------------------------------------------------------

    @Test
    public void test_deleteNoMoulding() {
        String requestId = UUID.randomUUID().toString();
        Log.d("SND_TEST", "=== test_deleteNoMoulding START | requestId=" + requestId);
        for (String noMoulding : NO_MOULDING_LIST) {
            ProsesProduksiApi.deleteDataByNoLabel(NO_PRODUKSI, noMoulding, ACTOR_ID, ACTOR_NAME, requestId);
            Log.d("SND_TEST", "Deleted: " + noMoulding);
        }
        Log.d("SND_TEST", "=== test_deleteNoMoulding DONE");
    }

    @Test
    public void test_deleteNoFJ() {
        String requestId = UUID.randomUUID().toString();
        Log.d("SND_TEST", "=== test_deleteNoFJ START | requestId=" + requestId);
        for (String noFJ : NO_FJ_LIST) {
            ProsesProduksiApi.deleteDataByNoLabel(NO_PRODUKSI, noFJ, ACTOR_ID, ACTOR_NAME, requestId);
            Log.d("SND_TEST", "Deleted: " + noFJ);
        }
        Log.d("SND_TEST", "=== test_deleteNoFJ DONE");
    }

    @Test
    public void test_deleteNoCC() {
        String requestId = UUID.randomUUID().toString();
        Log.d("SND_TEST", "=== test_deleteNoCC START | requestId=" + requestId);
        for (String noCC : NO_CC_LIST) {
            ProsesProduksiApi.deleteDataByNoLabel(NO_PRODUKSI, noCC, ACTOR_ID, ACTOR_NAME, requestId);
            Log.d("SND_TEST", "Deleted: " + noCC);
        }
        Log.d("SND_TEST", "=== test_deleteNoCC DONE");
    }

    @Test
    public void test_deleteNoSanding() {
        String requestId = UUID.randomUUID().toString();
        Log.d("SND_TEST", "=== test_deleteNoSanding START | requestId=" + requestId);
        for (String noSanding : NO_SANDING_LIST) {
            ProsesProduksiApi.deleteDataByNoLabel(NO_PRODUKSI, noSanding, ACTOR_ID, ACTOR_NAME, requestId);
            Log.d("SND_TEST", "Deleted: " + noSanding);
        }
        Log.d("SND_TEST", "=== test_deleteNoSanding DONE");
    }

    @Test
    public void test_deleteNoPacking() {
        String requestId = UUID.randomUUID().toString();
        Log.d("SND_TEST", "=== test_deleteNoPacking START | requestId=" + requestId);
        for (String noPacking : NO_PACKING_LIST) {
            ProsesProduksiApi.deleteDataByNoLabel(NO_PRODUKSI, noPacking, ACTOR_ID, ACTOR_NAME, requestId);
            Log.d("SND_TEST", "Deleted: " + noPacking);
        }
        Log.d("SND_TEST", "=== test_deleteNoPacking DONE");
    }

    // -----------------------------------------------------------
    // INSERT semua sekaligus
    // -----------------------------------------------------------

    @Test
    public void test_saveAll() {
        String requestId = UUID.randomUUID().toString();
        Log.d("SND_TEST", "=== test_saveAll START | requestId=" + requestId);
        ProsesProduksiApi.saveNoMoulding(NO_PRODUKSI, TGL_PRODUKSI, NO_MOULDING_LIST, DATE_TIME,
                "SandingProduksiInputMoulding", NO_SPK_TUJUAN, ACTOR_ID, ACTOR_NAME, requestId);
        ProsesProduksiApi.saveNoFJ(NO_PRODUKSI, TGL_PRODUKSI, NO_FJ_LIST, DATE_TIME,
                "SandingProduksiInputFJ", NO_SPK_TUJUAN, ACTOR_ID, ACTOR_NAME, requestId);
        ProsesProduksiApi.saveNoCC(NO_PRODUKSI, TGL_PRODUKSI, NO_CC_LIST, DATE_TIME,
                "SandingProduksiInputCCAkhir", NO_SPK_TUJUAN, ACTOR_ID, ACTOR_NAME, requestId);
        ProsesProduksiApi.saveNoSanding(NO_PRODUKSI, TGL_PRODUKSI, NO_SANDING_LIST, DATE_TIME,
                "SandingProduksiInputSanding", NO_SPK_TUJUAN, ACTOR_ID, ACTOR_NAME, requestId);
        ProsesProduksiApi.saveNoPacking(NO_PRODUKSI, TGL_PRODUKSI, NO_PACKING_LIST, DATE_TIME,
                "SandingProduksiInputBarangJadi", NO_SPK_TUJUAN, ACTOR_ID, ACTOR_NAME, requestId);
        Log.d("SND_TEST", "=== test_saveAll DONE");
    }

    // -----------------------------------------------------------
    // DELETE semua sekaligus
    // -----------------------------------------------------------

    @Test
    public void test_deleteAll() {
        String requestId = UUID.randomUUID().toString();
        Log.d("SND_TEST", "=== test_deleteAll START | requestId=" + requestId);
        for (String noMoulding : NO_MOULDING_LIST) {
            ProsesProduksiApi.deleteDataByNoLabel(NO_PRODUKSI, noMoulding, ACTOR_ID, ACTOR_NAME, requestId);
            Log.d("SND_TEST", "Deleted: " + noMoulding);
        }
        for (String noFJ : NO_FJ_LIST) {
            ProsesProduksiApi.deleteDataByNoLabel(NO_PRODUKSI, noFJ, ACTOR_ID, ACTOR_NAME, requestId);
            Log.d("SND_TEST", "Deleted: " + noFJ);
        }
        for (String noCC : NO_CC_LIST) {
            ProsesProduksiApi.deleteDataByNoLabel(NO_PRODUKSI, noCC, ACTOR_ID, ACTOR_NAME, requestId);
            Log.d("SND_TEST", "Deleted: " + noCC);
        }
        for (String noSanding : NO_SANDING_LIST) {
            ProsesProduksiApi.deleteDataByNoLabel(NO_PRODUKSI, noSanding, ACTOR_ID, ACTOR_NAME, requestId);
            Log.d("SND_TEST", "Deleted: " + noSanding);
        }
        for (String noPacking : NO_PACKING_LIST) {
            ProsesProduksiApi.deleteDataByNoLabel(NO_PRODUKSI, noPacking, ACTOR_ID, ACTOR_NAME, requestId);
            Log.d("SND_TEST", "Deleted: " + noPacking);
        }
        Log.d("SND_TEST", "=== test_deleteAll DONE");
    }
}
