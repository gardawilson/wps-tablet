package com.example.myapplication;

import androidx.multidex.MultiDexApplication;

/**
 * Custom Application class.
 * Extends MultiDexApplication untuk mempertahankan multidex support.
 * WorkManager dan Room auto-initialize lewat ContentProvider mereka masing-masing.
 */
public class WpsApplication extends MultiDexApplication {
}
