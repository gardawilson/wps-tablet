package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myapplication.api.AuditApi;
import com.example.myapplication.model.AuditItem;
import com.example.myapplication.model.AuditRequestGroup;
import com.example.myapplication.utils.AuditDisplayFormatter;
import com.example.myapplication.utils.TokenManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.text.SimpleDateFormat;

public class AuditActivity extends AppCompatActivity {
    public static final String EXTRA_SEARCH_PK = "extra_search_pk";

    private static final int PAGE_SIZE = 20;

    private EditText etSearchPk;
    private Button btnSearch;
    private Button btnReset;
    private TextView tvStatus;
    private TextView tvDetailPlaceholder;
    private LinearLayout layoutDetailContent;
    private LinearLayout layoutHeaderSection;
    private LinearLayout layoutOutputSection;
    private TableLayout tblHeadToHeadHeader;
    private TableLayout tblHeadToHeadDetail;
    private TableLayout tblHeadToHeadOutput;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView recyclerAudit;
    private LinearLayoutManager auditLayoutManager;

    private AuditAdapter adapter;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final List<AuditItem> allAuditItems = new ArrayList<>();

    private int currentPage = 1;
    private int totalPages = 1;
    private boolean isLastPage = false;
    private String selectedRequestId = null;
    private String currentPkFilter = "";
    private boolean isLoading = false;
    private String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit);

        token = TokenManager.getToken(this);
        if (token == null || token.trim().isEmpty()) {
            Toast.makeText(this, "Token login tidak ditemukan", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        bindViews();
        applyInitialFilterFromIntent();
        setupList();
        setupActions();
        resetAndLoadAudit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdownNow();
    }

    private void bindViews() {
        etSearchPk = findViewById(R.id.etSearchPk);
        btnSearch = findViewById(R.id.btnSearchPk);
        btnReset = findViewById(R.id.btnResetSearchPk);
        tvStatus = findViewById(R.id.tvStatus);
        progressBar = findViewById(R.id.progressAudit);
        swipeRefresh = findViewById(R.id.swipeRefreshAudit);
        tvDetailPlaceholder = findViewById(R.id.tvDetailPlaceholder);
        layoutDetailContent = findViewById(R.id.layoutDetailContent);
        layoutHeaderSection = findViewById(R.id.layoutHeaderSection);
        layoutOutputSection = findViewById(R.id.layoutOutputSection);
        tblHeadToHeadHeader = findViewById(R.id.tblHeadToHeadHeader);
        tblHeadToHeadDetail = findViewById(R.id.tblHeadToHeadDetail);
        tblHeadToHeadOutput = findViewById(R.id.tblHeadToHeadOutput);

        clearDetailPanel();
    }

    private void applyInitialFilterFromIntent() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }

        String pkFromIntent = intent.getStringExtra(EXTRA_SEARCH_PK);
        if (pkFromIntent == null) {
            return;
        }

        currentPkFilter = pkFromIntent.trim();
        etSearchPk.setText(currentPkFilter);
        etSearchPk.setSelection(etSearchPk.getText().length());
    }

    private void setupList() {
        recyclerAudit = findViewById(R.id.recyclerAudit);
        auditLayoutManager = new LinearLayoutManager(this);
        recyclerAudit.setLayoutManager(auditLayoutManager);
        adapter = new AuditAdapter(group -> {
            selectedRequestId = group.getRequestId();
            renderGroupDetail(group);
        });
        recyclerAudit.setAdapter(adapter);
        recyclerAudit.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy <= 0 || isLoading || isLastPage) return;
                int visible = auditLayoutManager.getChildCount();
                int total = auditLayoutManager.getItemCount();
                int firstVisible = auditLayoutManager.findFirstVisibleItemPosition();
                if ((visible + firstVisible) >= (total - 4)) {
                    loadAuditData(true);
                }
            }
        });
    }

    private void setupActions() {
        btnSearch.setOnClickListener(v -> {
            currentPkFilter = etSearchPk.getText().toString().trim();
            resetAndLoadAudit();
        });

        btnReset.setOnClickListener(v -> {
            etSearchPk.setText("");
            currentPkFilter = "";
            resetAndLoadAudit();
        });

        swipeRefresh.setOnRefreshListener(this::resetAndLoadAudit);
    }

    private void resetAndLoadAudit() {
        currentPage = 1;
        totalPages = 1;
        isLastPage = false;
        selectedRequestId = null;
        allAuditItems.clear();
        adapter.submitList(new ArrayList<>());
        clearDetailPanel();
        loadAuditData(false);
    }

    private void loadAuditData(boolean append) {
        if (isLoading) return;
        if (append && isLastPage) return;
        isLoading = true;
        if (!swipeRefresh.isRefreshing() && !append) {
            progressBar.setVisibility(View.VISIBLE);
        }
        tvStatus.setVisibility(View.GONE);

        final int pageToLoad = append ? (currentPage + 1) : 1;
        final String pkFilter = currentPkFilter;

        executorService.execute(() -> {
            AuditApi.AuditPageResponse response;
            if (pkFilter == null || pkFilter.isEmpty()) {
                response = AuditApi.getAuditList(
                        token,
                        pageToLoad,
                        PAGE_SIZE,
                        "",
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                );
            } else {
                response = AuditApi.getAuditByPk(
                        token,
                        pkFilter,
                        pageToLoad,
                        PAGE_SIZE,
                        null,
                        null
                );
            }

            runOnUiThread(() -> {
                isLoading = false;
                progressBar.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);

                if (!response.isSuccess()) {
                    if (!append) {
                        adapter.submitList(null);
                        totalPages = 1;
                    }
                    tvStatus.setVisibility(View.VISIBLE);
                    tvStatus.setText(response.getMessage());
                    return;
                }

                currentPage = response.getPage();
                totalPages = Math.max(response.getTotalPages(), 1);
                isLastPage = currentPage >= totalPages;

                if (!append) {
                    allAuditItems.clear();
                }
                allAuditItems.addAll(response.getData());

                List<AuditRequestGroup> groups = groupByRequestId(allAuditItems);
                adapter.submitList(groups);
                adapter.setSelectedRequestId(selectedRequestId);

                if (groups.isEmpty()) {
                    tvStatus.setVisibility(View.VISIBLE);
                    tvStatus.setText("Data audit tidak ditemukan");
                    clearDetailPanel();
                } else {
                    tvStatus.setVisibility(View.GONE);
                    AuditRequestGroup selectedGroup = findGroupByRequestId(groups, selectedRequestId);
                    if (selectedGroup != null) {
                        renderGroupDetail(selectedGroup);
                    } else {
                        clearDetailPanel();
                    }
                }
            });
        });
    }

    private void renderGroupDetail(AuditRequestGroup group) {
        List<AuditItem> items = group.getItems();

        tvDetailPlaceholder.setVisibility(View.GONE);
        layoutDetailContent.setVisibility(View.VISIBLE);

        if (isConsumeOrUnconsumeGroup(items)) {
            renderConsumeUnconsumeDetail(items);
        } else if (isProduceOrUnproduceGroup(items)) {
            renderProduceUnproduceDetail(items);
        } else {
            renderHeadToHeadTable(items);
        }
    }

    private boolean isConsumeOrUnconsumeGroup(List<AuditItem> items) {
        if (items == null || items.isEmpty()) return false;
        for (AuditItem item : items) {
            String action = item.getAction();
            if (!"CONSUME".equalsIgnoreCase(action) && !"UNCONSUME".equalsIgnoreCase(action)) {
                return false;
            }
        }
        return true;
    }

    private boolean isProduceOrUnproduceGroup(List<AuditItem> items) {
        if (items == null || items.isEmpty()) return false;
        for (AuditItem item : items) {
            String action = item.getAction();
            if (!"PRODUCE".equalsIgnoreCase(action) && !"UNPRODUCE".equalsIgnoreCase(action)) {
                return false;
            }
        }
        return true;
    }

    private void renderConsumeUnconsumeDetail(List<AuditItem> items) {
        // Ambil NoProduksi dari item pertama sebagai "wadah"
        String noProduksi = "-";
        for (AuditItem item : items) {
            LinkedHashMap<String, String> pkMap = AuditDisplayFormatter.toFieldMap(item.getPk());
            if (pkMap.containsKey("NoProduksi")) {
                noProduksi = pkMap.get("NoProduksi");
                break;
            }
        }

        // Kumpulkan semua input material values + action
        List<String[]> inputMaterials = new ArrayList<>(); // [value, action]
        for (AuditItem item : items) {
            LinkedHashMap<String, String> pkMap = AuditDisplayFormatter.toFieldMap(item.getPk());
            for (Map.Entry<String, String> entry : pkMap.entrySet()) {
                String fieldKey = entry.getKey();
                if ("NoProduksi".equalsIgnoreCase(fieldKey) || "NoBongkarSusun".equalsIgnoreCase(fieldKey)) {
                    continue;
                }
                String labelValue = entry.getValue();
                if (labelValue == null || labelValue.trim().isEmpty() || "-".equals(labelValue.trim())) {
                    continue;
                }
                inputMaterials.add(new String[]{labelValue, item.getAction()});
            }
        }

        // Hide HEADER dan OUTPUT section, hanya tampilkan DETAIL
        layoutHeaderSection.setVisibility(View.GONE);
        layoutOutputSection.setVisibility(View.GONE);
        tblHeadToHeadHeader.removeAllViews();
        tblHeadToHeadOutput.removeAllViews();

        tblHeadToHeadDetail.removeAllViews();
        addTableHeader(tblHeadToHeadDetail, "Proses", "Input");

        int total = inputMaterials.size();
        if (total == 0) {
            TableRow row = new TableRow(this);
            row.addView(createCell(noProduksi, false, false, true, "#1565C0", false));
            row.addView(createCell("-", false, false, false, "#333333", false));
            tblHeadToHeadDetail.addView(row);
        } else {
            for (int i = 0; i < total; i++) {
                String value = inputMaterials.get(i)[0];
                String action = inputMaterials.get(i)[1];
                boolean isConsume = "CONSUME".equalsIgnoreCase(action);
                String detailColor = isConsume ? "#1B5E20" : "#B71C1C";

                TableRow row = new TableRow(this);
                if (i == 0) {
                    row.addView(createCell(noProduksi, false, false, true, "#1565C0", false));
                } else {
                    TextView emptyCell = new TextView(this);
                    emptyCell.setText("");
                    row.addView(emptyCell);
                }
                row.addView(createCell(value, false, false, false, detailColor, false));
                tblHeadToHeadDetail.addView(row);
            }
        }
    }

    private void renderProduceUnproduceDetail(List<AuditItem> items) {
        // Ambil NoProduksi dari item pertama
        String noProduksi = "-";
        for (AuditItem item : items) {
            LinkedHashMap<String, String> pkMap = AuditDisplayFormatter.toFieldMap(item.getPk());
            if (pkMap.containsKey("NoProduksi")) {
                noProduksi = pkMap.get("NoProduksi");
                break;
            }
        }

        // Kumpulkan output values + action (semua key selain NoProduksi)
        List<String[]> outputItems = new ArrayList<>(); // [value, action]
        for (AuditItem item : items) {
            // Untuk PRODUCE: data ada di newData; untuk UNPRODUCE: data ada di oldData
            String rawData = "PRODUCE".equalsIgnoreCase(item.getAction())
                    ? item.getNewData()
                    : item.getOldData();
            LinkedHashMap<String, String> dataMap = AuditDisplayFormatter.toFieldMap(rawData);
            // Fallback ke PK jika data kosong
            if (dataMap.isEmpty()) {
                dataMap = AuditDisplayFormatter.toFieldMap(item.getPk());
            }
            for (Map.Entry<String, String> entry : dataMap.entrySet()) {
                String fieldKey = entry.getKey();
                if ("NoProduksi".equalsIgnoreCase(fieldKey)) continue;
                String labelValue = entry.getValue();
                if (labelValue == null || labelValue.trim().isEmpty() || "-".equals(labelValue.trim())) continue;
                outputItems.add(new String[]{labelValue, item.getAction()});
            }
        }

        // Sembunyikan HEADER dan OUTPUT section, hanya tampilkan DETAIL
        layoutHeaderSection.setVisibility(View.GONE);
        layoutOutputSection.setVisibility(View.GONE);
        tblHeadToHeadHeader.removeAllViews();
        tblHeadToHeadOutput.removeAllViews();

        tblHeadToHeadDetail.removeAllViews();
        addTableHeader(tblHeadToHeadDetail, "Produksi", "Output");

        int total = outputItems.size();
        if (total == 0) {
            TableRow row = new TableRow(this);
            row.addView(createCell(noProduksi, false, false, true, "#1565C0", false));
            row.addView(createCell("-", false, false, false, "#333333", false));
            tblHeadToHeadDetail.addView(row);
        } else {
            for (int i = 0; i < total; i++) {
                String value = outputItems.get(i)[0];
                String action = outputItems.get(i)[1];
                boolean isProduce = "PRODUCE".equalsIgnoreCase(action);
                String outputColor = isProduce ? "#1B5E20" : "#B71C1C";
                boolean strike = !isProduce;

                TableRow row = new TableRow(this);
                if (i == 0) {
                    row.addView(createCell(noProduksi, false, false, true, "#1565C0", false));
                } else {
                    TextView emptyCell = new TextView(this);
                    emptyCell.setText("");
                    row.addView(emptyCell);
                }
                row.addView(createCell(value, false, strike, isProduce, outputColor, false));
                tblHeadToHeadDetail.addView(row);
            }
        }
    }

    private void clearDetailPanel() {
        tvDetailPlaceholder.setVisibility(View.VISIBLE);
        tvDetailPlaceholder.setText("Pilih Request ID untuk melihat detail perubahan.");
        layoutDetailContent.setVisibility(View.GONE);
        renderHeadToHeadTable(new ArrayList<>());
    }

    private List<AuditRequestGroup> groupByRequestId(List<AuditItem> rawItems) {
        Map<String, List<AuditItem>> grouped = new LinkedHashMap<>();
        for (AuditItem item : rawItems) {
            String key = item.getRequestId();
            if (key == null || key.trim().isEmpty() || "-".equals(key.trim())) {
                key = "NO-REQUEST-" + item.getAuditId();
            }
            if (!grouped.containsKey(key)) {
                grouped.put(key, new ArrayList<>());
            }
            grouped.get(key).add(item);
        }

        List<AuditRequestGroup> result = new ArrayList<>();
        for (Map.Entry<String, List<AuditItem>> entry : grouped.entrySet()) {
            List<AuditItem> items = entry.getValue();
            AuditItem first = items.get(0);

            String actionSummary = buildActionSummary(items);
            String tableSummary = buildTableSummary(items);
            String timeSummary = buildTimeSummary(items);
            String actorSummary = first.getActorUsername() + " (" + first.getActor() + ")";
            String changeSummary = buildChangeSummary(items);

            result.add(new AuditRequestGroup(
                    entry.getKey(),
                    actionSummary,
                    tableSummary,
                    timeSummary,
                    actorSummary,
                    changeSummary,
                    items
            ));
        }
        return result;
    }

    private AuditRequestGroup findGroupByRequestId(List<AuditRequestGroup> groups, String requestId) {
        if (requestId == null || requestId.trim().isEmpty()) return null;
        for (AuditRequestGroup group : groups) {
            if (requestId.equals(group.getRequestId())) {
                return group;
            }
        }
        return null;
    }

    private String buildActionSummary(List<AuditItem> items) {
        Set<String> rawActions = new LinkedHashSet<>();
        for (AuditItem item : items) {
            rawActions.add(item.getAction() == null ? "" : item.getAction().toUpperCase());
        }

        // Satu aksi tunggal — tampilkan langsung
        if (rawActions.size() == 1) {
            return AuditDisplayFormatter.actionAlias(rawActions.iterator().next());
        }

        // Resolusi aksi dominan berdasarkan prioritas bisnis:
        // DELETE/UNPRODUCE mengalahkan segalanya (destruktif)
        // INSERT/CREATE mengalahkan PRODUCE (INSERT adalah aksi utama, PRODUCE efek sampingnya)
        // UPDATE/EDIT mengalahkan PRINT
        boolean hasDelete    = rawActions.contains("DELETE");
        boolean hasUnproduce = rawActions.contains("UNPRODUCE");
        boolean hasInsert    = rawActions.contains("INSERT");
        boolean hasProduce   = rawActions.contains("PRODUCE");
        boolean hasUpdate    = rawActions.contains("UPDATE");
        boolean hasMapping   = rawActions.contains("MAPPING");
        boolean hasPrint     = rawActions.contains("PRINT");
        boolean hasConsume   = rawActions.contains("CONSUME");
        boolean hasUnconsume = rawActions.contains("UNCONSUME");

        if (hasDelete && hasUnproduce) return "DELETE";
        if (hasDelete)    return "DELETE";
        if (hasUnproduce) return "UNPRODUCE";
        if (hasInsert && hasProduce) return "CREATE";
        if (hasInsert)    return "CREATE";
        if (hasProduce)   return "PRODUCE";
        if (hasUpdate && hasPrint)  return "EDIT";
        if (hasUpdate)    return "EDIT";
        if (hasMapping)   return "MAPPING";
        if (hasPrint)     return "PRINT";
        if (hasConsume && hasUnconsume) return "UNCONSUME";
        if (hasConsume)   return "CONSUME";
        if (hasUnconsume) return "UNCONSUME";

        // Fallback: semua aksi unik
        Set<String> aliases = new LinkedHashSet<>();
        for (String a : rawActions) {
            aliases.add(AuditDisplayFormatter.actionAlias(a));
        }
        return String.join("+", aliases);
    }

    private String buildTableSummary(List<AuditItem> items) {
        Set<String> tables = new LinkedHashSet<>();
        for (AuditItem item : items) {
            tables.add(AuditDisplayFormatter.tableAlias(item.getTableName()));
        }
        return String.join(", ", tables);
    }

    private String buildTimeSummary(List<AuditItem> items) {
        if (items.isEmpty()) return "-";
        return formatEventTimeDisplay(items.get(0).getEventTime());
    }

    private String buildChangeSummary(List<AuditItem> items) {
        List<String> lines = new ArrayList<>();
        for (AuditItem item : items) {
            String table = AuditDisplayFormatter.tableAlias(item.getTableName());
            if ("UPDATE".equalsIgnoreCase(item.getAction()) || "PRINT".equalsIgnoreCase(item.getAction())) {
                String diff = AuditDisplayFormatter.formatActionDiff(
                        item.getTableName(),
                        item.getAction(),
                        item.getOldData(),
                        item.getNewData()
                );
                lines.add(table + ": " + firstLine(diff));
            } else if ("INSERT".equalsIgnoreCase(item.getAction())) {
                lines.add(table + ": data baru ditambahkan");
            } else if ("DELETE".equalsIgnoreCase(item.getAction())) {
                lines.add(table + ": data dihapus");
            } else if ("CONSUME".equalsIgnoreCase(item.getAction())) {
                lines.add(table + ": data dikonsumsi");
            } else if ("UNCONSUME".equalsIgnoreCase(item.getAction())) {
                lines.add(table + ": konsumsi data dibatalkan");
            } else if ("PRODUCE".equalsIgnoreCase(item.getAction())) {
                lines.add(table + ": data diproduksi");
            } else if ("UNPRODUCE".equalsIgnoreCase(item.getAction())) {
                lines.add(table + ": produksi data dibatalkan");
            } else if ("MAPPING".equalsIgnoreCase(item.getAction())) {
                String diff = AuditDisplayFormatter.formatActionDiff(
                        item.getTableName(), item.getAction(),
                        item.getOldData(), item.getNewData());
                lines.add(table + ": pindah lokasi — " + firstLine(diff));
            } else {
                lines.add(table + ": " + item.getAction());
            }
        }
        return String.join("\n", lines);
    }

    private void renderHeadToHeadTable(List<AuditItem> items) {
        layoutHeaderSection.setVisibility(View.VISIBLE);
        layoutOutputSection.setVisibility(View.VISIBLE);
        List<AuditItem> headerItems = new ArrayList<>();
        List<AuditItem> detailItems = new ArrayList<>();
        List<AuditItem> outputItems = new ArrayList<>();

        if (items == null || items.isEmpty()) {
            renderSectionTable(tblHeadToHeadHeader, new ArrayList<>());
            renderSectionTable(tblHeadToHeadDetail, new ArrayList<>());
            renderSectionTable(tblHeadToHeadOutput, new ArrayList<>());
            return;
        }

        for (AuditItem item : items) {
            String section = resolveHeadToHeadSectionTitle(item.getTableName());
            if ("HEADER".equals(section)) {
                headerItems.add(item);
            } else if ("OUTPUT".equals(section)) {
                outputItems.add(item);
            } else {
                detailItems.add(item);
            }
        }

        renderSectionTable(tblHeadToHeadHeader, headerItems);
        renderSectionTable(tblHeadToHeadDetail, detailItems);
        renderSectionTable(tblHeadToHeadOutput, outputItems);
    }

    private void renderSectionTable(TableLayout target, List<AuditItem> sectionItems) {
        target.removeAllViews();
        addTableHeader(target, "Field", "Before", "After");
        if (sectionItems == null || sectionItems.isEmpty()) {
            addInfoRow(target, "Tidak ada data");
            return;
        }
        for (AuditItem item : sectionItems) {
            LinkedHashMap<String, String> pkMap = AuditDisplayFormatter.toFieldMap(item.getPk());
            LinkedHashMap<String, String> oldMap = AuditDisplayFormatter.toFieldMap(item.getOldData());
            LinkedHashMap<String, String> newMap = AuditDisplayFormatter.toFieldMap(item.getNewData());
            addAllRowsForAction(target, item.getTableName(), item.getAction(), pkMap.keySet(), oldMap, newMap);
        }
    }

    private void addTableHeader(TableLayout target, String c1) {
        TableRow row = new TableRow(this);
        row.setBackgroundColor(Color.parseColor("#E9EEF6"));
        row.addView(createCell(c1, true, false, false, "#111111", true));
        target.addView(row);
    }

    private void addTableHeader(TableLayout target, String c1, String c2) {
        TableRow row = new TableRow(this);
        row.setBackgroundColor(Color.parseColor("#E9EEF6"));
        row.addView(createCell(c1, true, false, false, "#111111", true));
        row.addView(createCell(c2, true, false, false, "#111111", true));
        target.addView(row);
    }

    private void addTableHeader(TableLayout target, String c1, String c2, String c3) {
        TableRow row = new TableRow(this);
        row.setBackgroundColor(Color.parseColor("#E9EEF6"));
        row.addView(createCell(c1, true, false, false, "#111111", true));
        row.addView(createCell(c2, true, false, false, "#111111", true));
        row.addView(createCell(c3, true, false, false, "#111111", true));
        target.addView(row);
    }

    private void addInfoRow(TableLayout target, String text) {
        TableRow row = new TableRow(this);
        TextView tv = createCell(text, false, false, false, "#666666", false);
        tv.setLayoutParams(spanLayoutParams(3));
        row.addView(tv);
        target.addView(row);
    }

    private void addAllRowsForAction(TableLayout target, String tableName, String action, Set<String> pkKeys, LinkedHashMap<String, String> oldMap, LinkedHashMap<String, String> newMap) {
        Set<String> keys = new LinkedHashSet<>();
        keys.addAll(oldMap.keySet());
        keys.addAll(newMap.keySet());

        if (isOutputTable(tableName)) {
            Set<String> outputKeys = new LinkedHashSet<>();
            if (keys.contains("NoProduksi")) {
                outputKeys.add("NoProduksi");
            } else if (keys.contains("NoBongkarSusun")) {
                outputKeys.add("NoBongkarSusun");
            }
            keys = outputKeys;
        } else if ("CONSUME".equalsIgnoreCase(action) || "UNCONSUME".equalsIgnoreCase(action)
                || "PRODUCE".equalsIgnoreCase(action) || "UNPRODUCE".equalsIgnoreCase(action)) {
            // Hanya buang NoProduksi/NoBongkarSusun, tampilkan nilai label (NoS4S, NoST, dll)
            keys.remove("NoProduksi");
            keys.remove("NoBongkarSusun");
            keys.remove("DateTimeSaved");
        } else {
            keys.removeAll(pkKeys);
        }

        if (keys.isEmpty()) {
            addInfoRow(target, "Tidak ada detail data");
            return;
        }

        boolean isUpdate = "UPDATE".equalsIgnoreCase(action) || "MAPPING".equalsIgnoreCase(action);
        boolean isInsert = "INSERT".equalsIgnoreCase(action) || "CONSUME".equalsIgnoreCase(action) || "PRODUCE".equalsIgnoreCase(action);
        boolean isDelete = "DELETE".equalsIgnoreCase(action) || "UNCONSUME".equalsIgnoreCase(action) || "UNPRODUCE".equalsIgnoreCase(action);
        boolean isPrint = "PRINT".equalsIgnoreCase(action);
        boolean rowsAdded = false;

        for (String key : keys) {
            if (isPrint && !AuditDisplayFormatter.isPrintField(key)) {
                continue;
            }

            String oldVal = oldMap.containsKey(key) ? oldMap.get(key) : "-";
            String newVal = newMap.containsKey(key) ? newMap.get(key) : "-";

            boolean changed = !safeEquals(oldVal, newVal);
            if (isPrint && !changed) {
                continue;
            }

            boolean strikeOld = isDelete || ((isUpdate || isPrint) && changed);
            boolean boldNew = isInsert || ((isUpdate || isPrint) && changed);
            String oldColor = strikeOld ? "#B71C1C" : "#333333";
            String newColor = boldNew ? "#1B5E20" : "#333333";

            TableRow row = new TableRow(this);
            row.addView(createCell(AuditDisplayFormatter.fieldAlias(key), false, false, false, "#222222", false));
            row.addView(createCell(oldVal, false, strikeOld, false, oldColor, false));
            row.addView(createCell(newVal, false, false, boldNew, newColor, false));
            target.addView(row);
            rowsAdded = true;
        }

        if (!rowsAdded) {
            addInfoRow(target, "Tidak ada detail data");
        }
    }

    private boolean isOutputTable(String tableName) {
        if (tableName == null) return false;
        return tableName.toLowerCase().contains("output");
    }

    private String resolveHeadToHeadSectionTitle(String tableName) {
        if (tableName == null || tableName.trim().isEmpty()) {
            return "DETAIL";
        }
        String lower = tableName.toLowerCase();
        if (lower.contains("output")) {
            return "OUTPUT";
        }
        if (lower.endsWith("_h")) {
            return "HEADER";
        }
        if (lower.endsWith("_d")) {
            return "DETAIL";
        }
        return "DETAIL";
    }

    private TextView createCell(String text, boolean bold, boolean strike, boolean green, String colorHex, boolean isHeader) {
        TextView tv = new TextView(this);
        tv.setText(text == null || text.trim().isEmpty() ? "-" : text);
        tv.setTextSize(isHeader ? 12 : 11);
        tv.setTextColor(Color.parseColor(colorHex));
        tv.setPadding(10, 8, 10, 8);
        tv.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_table_cell_border));
        tv.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        if (bold || green) {
            tv.setTypeface(Typeface.DEFAULT_BOLD);
        }
        if (strike) {
            tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        return tv;
    }

    private TableRow.LayoutParams spanLayoutParams(int span) {
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.span = span;
        params.width = TableRow.LayoutParams.MATCH_PARENT;
        return params;
    }

    private boolean safeEquals(String a, String b) {
        return (a == null && b == null) || (a != null && a.equals(b));
    }

    private String firstLine(String text) {
        if (text == null || text.trim().isEmpty()) return "-";
        String[] parts = text.split("\\r?\\n");
        return parts.length == 0 ? text : parts[0];
    }

    private String formatEventTimeDisplay(String raw) {
        if (raw == null || raw.trim().isEmpty()) return "-";
        String value = raw.trim();
        Date parsed = parseDate(value);
        if (parsed == null) return value;

        Locale localeId = new Locale("id", "ID");
        String tanggal = new SimpleDateFormat("dd MMM yyyy", localeId).format(parsed);
        String jam = new SimpleDateFormat("HH:mm:ss", localeId).format(parsed);
        return tanggal + " | " + jam;
    }

    private Date parseDate(String value) {
        String[] patterns = new String[]{
                "yyyy-MM-dd HH:mm:ss.SSS",
                "yyyy-MM-dd HH:mm:ss",
                "yyyy-MM-dd'T'HH:mm:ss.SSS",
                "yyyy-MM-dd'T'HH:mm:ss"
        };
        for (String pattern : patterns) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.US);
                sdf.setLenient(false);
                return sdf.parse(value);
            } catch (Exception ignored) {
            }
        }
        return null;
    }

}
