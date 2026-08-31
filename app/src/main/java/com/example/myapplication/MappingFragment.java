package com.example.myapplication;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.api.MappingApi;
import com.example.myapplication.model.LokasiSummary;
import com.example.myapplication.model.MappingLabel;
import com.example.myapplication.utils.TokenManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Menu Mapping: daftar blok lokasi gudang + jumlah label
 * ({@code GET /api/mapping/lokasi-summary}). Klik sebuah lokasi membuka dialog
 * berisi daftar label pada lokasi tersebut
 * ({@code GET /api/label-list/?idlokasi=&lt;IdLokasi&gt;}).
 */
public class MappingFragment extends Fragment {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private static final int SORT_ID_ASC = 0;
    private static final int SORT_ID_DESC = 1;
    private static final int SORT_LABEL_DESC = 2;
    private static final int SORT_LABEL_ASC = 3;
    private static final String[] SORT_LABELS = {
            "Lokasi A–Z", "Lokasi Z–A", "Label terbanyak", "Label tersedikit"
    };

    private RecyclerView recycler;
    private ProgressBar progress;
    private TextView status;
    private EditText search;
    private Spinner sortSpinner;
    private LokasiAdapter adapter;
    private int sortMode = SORT_ID_ASC;

    private final List<LokasiSummary> allLokasi = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mapping, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recycler = view.findViewById(R.id.mappingRecycler);
        progress = view.findViewById(R.id.mappingProgress);
        status = view.findViewById(R.id.mappingStatus);
        search = view.findViewById(R.id.mappingSearch);
        sortSpinner = view.findViewById(R.id.mappingSort);

        int span = Math.max(2, getResources().getDisplayMetrics().widthPixels
                / (int) (240 * getResources().getDisplayMetrics().density));
        recycler.setLayoutManager(new GridLayoutManager(requireContext(), span));
        adapter = new LokasiAdapter(this::openLabelDialog);
        recycler.setAdapter(adapter);

        search.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) { }
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) { }
            @Override public void afterTextChanged(Editable s) {
                applyFilter(s.toString());
            }
        });

        ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, SORT_LABELS);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(sortAdapter);
        sortSpinner.setSelection(sortMode);
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
                sortMode = pos;
                applyFilter(search.getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        loadLokasi();
    }

    private void loadLokasi() {
        setLoading(true);
        String token = TokenManager.getToken(requireContext());
        executor.execute(() -> {
            final MappingApi.Result result = MappingApi.getLokasiSummary(token);
            if (!isAdded()) {
                return;
            }
            requireActivity().runOnUiThread(() -> {
                setLoading(false);
                allLokasi.clear();
                allLokasi.addAll(result.data);
                applyFilter(search.getText().toString());
                if (allLokasi.isEmpty()) {
                    showStatus(result.ok ? "Belum ada data lokasi." : result.message);
                }
            });
        });
    }

    private void applyFilter(String query) {
        String q = query == null ? "" : query.trim().toLowerCase(Locale.getDefault());
        List<LokasiSummary> filtered = new ArrayList<>();
        for (LokasiSummary item : allLokasi) {
            String hay = (item.getTitle() + " " + item.getIdLokasi() + " " + item.getBlok()
                    + " " + item.getDescription()).toLowerCase(Locale.getDefault());
            if (q.isEmpty() || hay.contains(q)) {
                filtered.add(item);
            }
        }
        sortLokasi(filtered);
        adapter.submit(filtered);
        if (!allLokasi.isEmpty()) {
            if (filtered.isEmpty()) {
                showStatus("Tidak ada lokasi cocok dengan \"" + query + "\".");
            } else {
                hideStatus();
            }
        }
    }

    private void sortLokasi(List<LokasiSummary> list) {
        java.util.Comparator<LokasiSummary> byId = (a, b) ->
                safe(a.getIdLokasi()).compareToIgnoreCase(safe(b.getIdLokasi()));
        java.util.Comparator<LokasiSummary> byCount = (a, b) ->
                Integer.compare(a.getJumlahLabel(), b.getJumlahLabel());
        switch (sortMode) {
            case SORT_ID_DESC:
                java.util.Collections.sort(list, byId.reversed());
                break;
            case SORT_LABEL_DESC:
                java.util.Collections.sort(list, byCount.reversed().thenComparing(byId));
                break;
            case SORT_LABEL_ASC:
                java.util.Collections.sort(list, byCount.thenComparing(byId));
                break;
            case SORT_ID_ASC:
            default:
                java.util.Collections.sort(list, byId);
                break;
        }
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }

    // --- dialog daftar label -------------------------------------------------

    private void openLabelDialog(LokasiSummary loc) {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_mapping_labels);

        Window window = dialog.getWindow();
        if (window != null) {
            int w = (int) (getResources().getDisplayMetrics().widthPixels * 0.62f);
            int h = (int) (getResources().getDisplayMetrics().heightPixels * 0.82f);
            window.setLayout(w, h);
        }

        TextView title = dialog.findViewById(R.id.dlgTitle);
        TextView summary = dialog.findViewById(R.id.dlgSummary);
        RecyclerView rv = dialog.findViewById(R.id.dlgRecycler);
        ProgressBar dlgProgress = dialog.findViewById(R.id.dlgProgress);
        TextView dlgStatus = dialog.findViewById(R.id.dlgStatus);
        View close = dialog.findViewById(R.id.dlgClose);

        title.setText("Lokasi " + loc.getTitle());
        summary.setText(loc.getDescription());
        LabelAdapter labelAdapter = new LabelAdapter();
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv.setAdapter(labelAdapter);
        dlgProgress.setVisibility(View.VISIBLE);
        close.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

        String token = TokenManager.getToken(requireContext());
        executor.execute(() -> {
            final MappingApi.LabelResult res = MappingApi.getLabelsByLokasi(token, loc.getIdLokasi());
            if (!isAdded() || !dialog.isShowing()) {
                return;
            }
            requireActivity().runOnUiThread(() -> {
                dlgProgress.setVisibility(View.GONE);
                if (!res.ok) {
                    dlgStatus.setText(res.message);
                    dlgStatus.setVisibility(View.VISIBLE);
                    return;
                }
                labelAdapter.submit(res.labels);
                if (res.labels.isEmpty()) {
                    dlgStatus.setText("Tidak ada label di lokasi ini.");
                    dlgStatus.setVisibility(View.VISIBLE);
                }
            });
        });
    }

    // --- state helpers -----------------------------------------------------

    private void setLoading(boolean loading) {
        progress.setVisibility(loading ? View.VISIBLE : View.GONE);
        if (loading) {
            hideStatus();
        }
    }

    private void showStatus(String text) {
        status.setText(text);
        status.setVisibility(View.VISIBLE);
    }

    private void hideStatus() {
        status.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }

    // --- adapter lokasi --------------------------------------------------------

    interface OnLokasiClick {
        void onClick(LokasiSummary item);
    }

    private static class LokasiAdapter extends RecyclerView.Adapter<LokasiAdapter.VH> {
        private final List<LokasiSummary> items = new ArrayList<>();
        private final OnLokasiClick listener;

        LokasiAdapter(OnLokasiClick listener) {
            this.listener = listener;
        }

        void submit(List<LokasiSummary> data) {
            items.clear();
            items.addAll(data);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_mapping_lokasi, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            LokasiSummary item = items.get(position);
            holder.title.setText(item.getTitle());
            holder.subtitle.setText(item.getDescription());
            holder.count.setText(item.getJumlahLabel() + " label");
            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onClick(item);
                }
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class VH extends RecyclerView.ViewHolder {
            final TextView title;
            final TextView subtitle;
            final TextView count;

            VH(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.lokasiBlok);
                subtitle = itemView.findViewById(R.id.lokasiId);
                count = itemView.findViewById(R.id.lokasiCount);
            }
        }
    }

    // --- adapter label (isi dialog) -----------------------------------------

    private static class LabelAdapter extends RecyclerView.Adapter<LabelAdapter.VH> {
        private final List<MappingLabel> items = new ArrayList<>();

        void submit(List<MappingLabel> data) {
            items.clear();
            items.addAll(data);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_mapping_label, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            MappingLabel item = items.get(position);
            holder.type.setText(String.valueOf(position + 1));
            holder.jenis.setText(item.getJenis());
            holder.no.setText(item.getLabelNo());
            holder.dateRight.setText(item.getDateCreate());

            android.widget.LinearLayout box = holder.detailContainer;
            box.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(box.getContext());
            List<MappingLabel.Detail> details = item.getDetails();

            if (details.isEmpty()) {
                TextView tv = new TextView(box.getContext());
                tv.setText("Tidak ada detail ukuran");
                tv.setTextSize(12f);
                tv.setTextColor(androidx.core.content.ContextCompat.getColor(box.getContext(), R.color.muted_text));
                box.addView(tv);
                return;
            }

            inflater.inflate(R.layout.item_mapping_label_detail_header, box, true);
            for (MappingLabel.Detail d : details) {
                View row = inflater.inflate(R.layout.item_mapping_label_detail, box, false);
                ((TextView) row.findViewById(R.id.dTebal)).setText(d.getTebalStr());
                ((TextView) row.findViewById(R.id.dLebar)).setText(d.getLebarStr());
                ((TextView) row.findViewById(R.id.dPanjang)).setText(d.getPanjangStr());
                ((TextView) row.findViewById(R.id.dBatang)).setText(String.valueOf(d.getJmlhBatang()));
                box.addView(row);
            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class VH extends RecyclerView.ViewHolder {
            final TextView type;
            final TextView jenis;
            final TextView no;
            final TextView dateRight;
            final android.widget.LinearLayout detailContainer;

            VH(@NonNull View itemView) {
                super(itemView);
                type = itemView.findViewById(R.id.lblType);
                jenis = itemView.findViewById(R.id.lblJenis);
                no = itemView.findViewById(R.id.lblNo);
                dateRight = itemView.findViewById(R.id.lblQty);
                detailContainer = itemView.findViewById(R.id.lblDetailContainer);
            }
        }
    }
}
