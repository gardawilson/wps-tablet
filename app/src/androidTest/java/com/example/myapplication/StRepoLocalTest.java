package com.example.myapplication;

import com.example.myapplication.api.SawnTimberApi;
import com.example.myapplication.model.StData;

import org.junit.Test;

public class StRepoLocalTest {
    @Test
    public void run_getSawnTimberHeader_local() {
        String noST = "E.507192";
        StData data = SawnTimberApi.getSawnTimberHeader(noST);
        System.out.println(data == null ? "NULL" : data.toString());
    }
}
