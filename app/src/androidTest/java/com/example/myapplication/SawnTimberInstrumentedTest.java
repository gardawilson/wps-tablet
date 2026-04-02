package com.example.myapplication;

import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.myapplication.api.SawnTimberApi;
import com.example.myapplication.model.LabelDetailData;
import com.example.myapplication.model.GradeDetailData;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RunWith(AndroidJUnit4.class)
public class SawnTimberInstrumentedTest {

    // ============================================================
    // CONFIG
    // ============================================================

    private static final String NO_KAYU_BULAT = "A.000001";
    private static final String JENIS_KAYU    = "23";
    private static final String NO_SPK        = "2025-72";
    private static final String TELLY         = "32";
    private static final String STICK_BY      = "183";

    private static final String DATE_CREATE   = "2026-04-01";
    private static final String VACUUM_DATE   = null;
    private static final String REMARK        = "";

    private static final int IS_SLP           = 0;
    private static final int IS_STICKED       = 0;
    private static final int IS_KERING        = 0;
    private static final int IS_BAGUS_KULIT   = 1;
    private static final int IS_UPAH          = 0;

    private static final int ID_UOM_LEBAR     = 1;
    private static final int ID_UOM_PANJANG   = 4;

    private static final String NO_PEN_ST     = null;
    private static final int LABEL_VERSION    = -1;

    private static final String NO_BONGKAR    = null;
    private static final boolean IS_BONGKAR   = false;

    private static final String ACTOR_ID   = "82";
    private static final String ACTOR_NAME = "TestUser";

    // 🔥 shared variable antar test
    private static String GENERATED_NO_ST = null;

    // ============================================================
    // MOCK DATA
    // ============================================================

    private List<LabelDetailData> getDetailList() {
        List<LabelDetailData> list = new ArrayList<>();
        list.add(new LabelDetailData("2", "10", "4", "50"));
        list.add(new LabelDetailData("2", "10", "3", "30"));
        return list;
    }

    private List<GradeDetailData> getGradeList() {
        List<GradeDetailData> list = new ArrayList<>();
        list.add(new GradeDetailData(1, "100", "21"));
        list.add(new GradeDetailData(2, "50", "21"));
        return list;
    }

    // ============================================================
    // ✅ TEST CREATE
    // ============================================================

    @Test
    public void test_create() {

        String requestId = UUID.randomUUID().toString();
        Log.d("ST_TEST", "=== CREATE START | " + requestId);

        GENERATED_NO_ST = SawnTimberApi.saveSawnTimberTransaction(
                NO_KAYU_BULAT,
                JENIS_KAYU,
                NO_SPK,
                TELLY,
                STICK_BY,
                DATE_CREATE,
                VACUUM_DATE,
                REMARK,
                IS_SLP,
                IS_STICKED,
                IS_KERING,
                IS_BAGUS_KULIT,
                IS_UPAH,
                ID_UOM_LEBAR,
                ID_UOM_PANJANG,
                getDetailList(),
                getGradeList(),
                NO_PEN_ST,
                LABEL_VERSION,
                NO_BONGKAR,
                IS_BONGKAR,
                ACTOR_ID,
                ACTOR_NAME,
                requestId
        );

        Log.d("ST_TEST", "Generated NoST: " + GENERATED_NO_ST);
    }

    // ============================================================
    // ✅ TEST UPDATE
    // ============================================================

    @Test
    public void test_update() {

        String requestId = UUID.randomUUID().toString();
        Log.d("ST_TEST", "=== UPDATE START | " + requestId);

        String noST = (GENERATED_NO_ST != null) ? GENERATED_NO_ST : "E.509267";

        boolean result = SawnTimberApi.updateSawnTimberTransaction(
                noST,
                NO_KAYU_BULAT,
                JENIS_KAYU,
                NO_SPK,
                TELLY,
                STICK_BY,
                DATE_CREATE,
                VACUUM_DATE,
                "UPDATED TEST",
                IS_SLP,
                IS_STICKED,
                IS_KERING,
                IS_BAGUS_KULIT,
                IS_UPAH,
                ID_UOM_LEBAR,
                ID_UOM_PANJANG,
                getDetailList(),
                getGradeList(),
                NO_PEN_ST,
                LABEL_VERSION,
                NO_BONGKAR,
                IS_BONGKAR,
                ACTOR_ID,
                ACTOR_NAME,
                requestId
        );

        Log.d("ST_TEST", "Update result: " + result);
    }

    // ============================================================
    // ✅ TEST DELETE
    // ============================================================

    @Test
    public void test_delete() {

        String requestId = UUID.randomUUID().toString();
        Log.d("ST_TEST", "=== DELETE START | " + requestId);

        String noST = (GENERATED_NO_ST != null) ? GENERATED_NO_ST : "E.509267";

        boolean result = SawnTimberApi.deleteSawnTimberTransaction(
                noST,
                ACTOR_ID,
                ACTOR_NAME,
                requestId
        );

        Log.d("ST_TEST", "Delete result: " + result);
    }
}