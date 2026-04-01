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

        // --- HEADER: tampilkan NoProduksi sebagai wadah ---
        tblHeadToHeadHeader.removeAllViews();
        addTableHeader(tblHeadToHeadHeader, "Field", "Nilai", "");
        TableRow wadahRow = new TableRow(this);
        wadahRow.addView(createCell("No Produksi", false, false, false, "#222222", false));
        wadahRow.addView(createCell(noProduksi, false, false, true, "#1565C0", false));
        wadahRow.addView(createCell("", false, false, false, "#333333", false));
        tblHeadToHeadHeader.addView(wadahRow);

        // --- DETAIL: tampilkan semua label per item ---
        tblHeadToHeadDetail.removeAllViews();
        addTableHeader(tblHeadToHeadDetail, "Field", "Before", "After");

        for (AuditItem item : items) {
            boolean isConsume = "CONSUME".equalsIgnoreCase(item.getAction());
            LinkedHashMap<String, String> pkMap = AuditDisplayFormatter.toFieldMap(item.getPk());

            for (Map.Entry<String, String> entry : pkMap.entrySet()) {
                String fieldKey = entry.getKey();
                if ("NoProduksi".equalsIgnoreCase(fieldKey) || "NoBongkarSusun".equalsIgnoreCase(fieldKey)) {
                    continue;
                }
                String labelValue = entry.getValue();
                String oldVal = isConsume ? "-" : labelValue;
                String newVal = isConsume ? labelValue : "-";
                boolean strikeOld = !isConsume;
                boolean boldNew = isConsume;
                String oldColor = strikeOld ? "#B71C1C" : "#333333";
                String newColor = boldNew ? "#1B5E20" : "#333333";

                TableRow row = new TableRow(this);
                row.addView(createCell(AuditDisplayFormatter.fieldAlias(fieldKey), false, false, false, "#222222", false));
                row.addView(createCell(oldVal, false, strikeOld, false, oldColor, false));
                row.addView(createCell(newVal, false, false, boldNew, newColor, false));
                tblHeadToHeadDetail.addView(row);
            }
        }

        // --- OUTPUT: kosongkan ---
        tblHeadToHeadOutput.removeAllViews();
        addTableHeader(tblHeadToHeadOutput, "Field", "Before", "After");
        addInfoRow(tblHeadToHeadOutput, "Tidak ada data");
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
        Set<String> actions = new LinkedHashSet<>();
        for (AuditItem item : items) {
            actions.add(AuditDisplayFormatter.actionAlias(item.getAction()));
        }
        if (actions.size() == 1) return actions.iterator().next();
        return "MIXED (" + String.join(", ", actions) + ")";
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
            } else {
                lines.add(table + ": " + item.getAction());
            }
        }
        return String.join("\n", lines);
    }

    private void renderHeadToHeadTable(List<AuditItem> items) {
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
        } else if ("CONSUME".equalsIgnoreCase(action) || "UNCONSUME".equalsIgnoreCase(action)) {
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

        boolean isUpdate = "UPDATE".equalsIgnoreCase(action);
        boolean isInsert = "INSERT".equalsIgnoreCase(action) || "CONSUME".equalsIgnoreCase(action);
        boolean isDelete = "DELETE".equalsIgnoreCase(action) || "UNCONSUME".equalsIgnoreCase(action);
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
