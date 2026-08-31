package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.myapplication.utils.SharedPrefUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DebugUiLauncherActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "DebugUiWorkflow";
    private static final String KEY_LAST_ACTIVITY = "last_activity";
    private static final String KEY_AUTO_OPEN = "auto_open";
    private static final Set<String> EXCLUDED_ACTIVITIES = new HashSet<>(Arrays.asList(
            DebugUiLauncherActivity.class.getName(),
            SplashActivity.class.getName(),
            MainActivity.class.getName(),
            PdfPreviewActivity.class.getName()
    ));

    private final List<ActivityEntry> activityEntries = new ArrayList<>();
    private final Map<String, ActivityEntry> entriesByLabel = new HashMap<>();
    private final Map<String, ActivityEntry> entriesByClassName = new HashMap<>();

    private SharedPreferences preferences;
    private ArrayAdapter<String> activityAdapter;
    private TextView lastScreenText;
    private Button openLastButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_ui_launcher);

        preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        seedDebugIdentity();
        loadActivities();
        bindViews();

        if (savedInstanceState == null && preferences.getBoolean(KEY_AUTO_OPEN, false)) {
            String lastActivity = preferences.getString(KEY_LAST_ACTIVITY, "");
            if (entriesByClassName.containsKey(lastActivity)) {
                findViewById(android.R.id.content).post(() -> openActivity(lastActivity));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (preferences != null && lastScreenText != null) {
            updateLastScreen();
        }
    }

    private void bindViews() {
        EditText searchInput = findViewById(R.id.debugSearchInput);
        ListView activityList = findViewById(R.id.debugActivityList);
        SwitchCompat autoOpenSwitch = findViewById(R.id.debugAutoOpenSwitch);
        Button normalLoginButton = findViewById(R.id.debugNormalLoginButton);
        openLastButton = findViewById(R.id.debugOpenLastButton);
        lastScreenText = findViewById(R.id.debugLastScreenText);

        List<String> labels = new ArrayList<>();
        for (ActivityEntry entry : activityEntries) {
            labels.add(entry.label);
        }

        activityAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                labels
        );
        activityList.setAdapter(activityAdapter);
        activityList.setOnItemClickListener((parent, view, position, id) -> {
            String selectedLabel = activityAdapter.getItem(position);
            ActivityEntry selectedEntry = entriesByLabel.get(selectedLabel);
            if (selectedEntry != null) {
                openActivity(selectedEntry.className);
            }
        });

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                activityAdapter.getFilter().filter(text);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        autoOpenSwitch.setChecked(preferences.getBoolean(KEY_AUTO_OPEN, false));
        autoOpenSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean(KEY_AUTO_OPEN, isChecked).apply();
            if (isChecked && !hasLastActivity()) {
                Toast.makeText(
                        this,
                        "Pilih satu layar terlebih dahulu",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        openLastButton.setOnClickListener(view -> {
            String lastActivity = preferences.getString(KEY_LAST_ACTIVITY, "");
            if (entriesByClassName.containsKey(lastActivity)) {
                openActivity(lastActivity);
            }
        });

        normalLoginButton.setOnClickListener(
                view -> startActivity(new Intent(this, MainActivity.class))
        );

        updateLastScreen();
    }

    private void loadActivities() {
        try {
            PackageInfo packageInfo;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageInfo = getPackageManager().getPackageInfo(
                        getPackageName(),
                        PackageManager.PackageInfoFlags.of(PackageManager.GET_ACTIVITIES)
                );
            } else {
                packageInfo = getPackageManager().getPackageInfo(
                        getPackageName(),
                        PackageManager.GET_ACTIVITIES
                );
            }

            if (packageInfo.activities == null) {
                return;
            }

            String appPackage = DebugUiLauncherActivity.class.getPackage().getName() + ".";
            for (ActivityInfo activityInfo : packageInfo.activities) {
                if (!activityInfo.name.startsWith(appPackage)
                        || EXCLUDED_ACTIVITIES.contains(activityInfo.name)) {
                    continue;
                }

                ActivityEntry entry = new ActivityEntry(
                        createLabel(activityInfo.name),
                        activityInfo.name
                );
                activityEntries.add(entry);
            }

            Collections.sort(activityEntries, (first, second) ->
                    first.label.compareToIgnoreCase(second.label));
            for (ActivityEntry entry : activityEntries) {
                entriesByLabel.put(entry.label, entry);
                entriesByClassName.put(entry.className, entry);
            }
        } catch (PackageManager.NameNotFoundException exception) {
            Toast.makeText(this, "Daftar Activity tidak dapat dibaca", Toast.LENGTH_LONG).show();
        }
    }

    private void openActivity(String className) {
        ActivityEntry entry = entriesByClassName.get(className);
        if (entry == null) {
            Toast.makeText(this, "Activity tidak tersedia", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Class<?> targetClass = Class.forName(className);
            preferences.edit().putString(KEY_LAST_ACTIVITY, className).apply();
            updateLastScreen();
            startActivity(new Intent(this, targetClass));
        } catch (ClassNotFoundException exception) {
            Toast.makeText(this, "Activity gagal dibuka", Toast.LENGTH_LONG).show();
        }
    }

    private void seedDebugIdentity() {
        if (SharedPrefUtils.getUsername(this).isEmpty()) {
            SharedPrefUtils.saveUsername(this, "debug-ui");
        }
        if (SharedPrefUtils.getIdUsername(this).isEmpty()) {
            SharedPrefUtils.saveIdUsername(this, "0");
        }
        if (SharedPrefUtils.getRoles(this).isEmpty()) {
            SharedPrefUtils.saveRoles(this, Collections.singletonList("DEBUG"));
        }
    }

    private void updateLastScreen() {
        String lastActivity = preferences.getString(KEY_LAST_ACTIVITY, "");
        ActivityEntry entry = entriesByClassName.get(lastActivity);
        boolean hasLastActivity = entry != null;
        lastScreenText.setText(hasLastActivity
                ? "Layar terakhir: " + entry.label
                : "Layar terakhir: belum dipilih");
        openLastButton.setEnabled(hasLastActivity);
    }

    private boolean hasLastActivity() {
        String lastActivity = preferences.getString(KEY_LAST_ACTIVITY, "");
        return entriesByClassName.containsKey(lastActivity);
    }

    private String createLabel(String className) {
        String simpleName = className.substring(className.lastIndexOf('.') + 1);
        return simpleName.replaceAll("(?<=[a-z0-9])(?=[A-Z])", " ");
    }

    private static class ActivityEntry {
        private final String label;
        private final String className;

        private ActivityEntry(String label, String className) {
            this.label = label;
            this.className = className;
        }
    }
}
