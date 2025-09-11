package com.example.myapplication;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.myapplication.api.GradeApi;
import com.example.myapplication.api.PlanningMesinApi;
import com.example.myapplication.api.ProsesProduksiApi;
import com.example.myapplication.api.SawmillApi;
import com.example.myapplication.model.GradeABCDetailData;
import com.example.myapplication.model.MesinProsesProduksiData;
import com.example.myapplication.model.MstMejaData;
import com.example.myapplication.model.PlanningMesinData;
import com.example.myapplication.utils.DateTimeUtils;
import com.example.myapplication.utils.LoadingDialogHelper;
import com.example.myapplication.utils.TableUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class PlanningMesin extends AppCompatActivity {

    private TableLayout mainTable;
    private TableRow selectedRow;
    private PlanningMesinData selectedPlanningMesinData;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private List<PlanningMesinData> dataList; // Data asli yang tidak difilter
    private ProgressBar mainLoadingIndicator;
    private final LoadingDialogHelper loadingDialogHelper = new LoadingDialogHelper();
    private Button btnCreate;
    private Button btnEdit;
    private Button btnDelete;
    // Tambahan guard agar hasil fetch lama tidak menimpa hasil terbaru
    private final java.util.concurrent.atomic.AtomicInteger requestSeq = new java.util.concurrent.atomic.AtomicInteger(0);
    private String currentJenis = "S4S"; // default tab


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planning_mesin);

        mainTable = findViewById(R.id.mainTable);
        mainLoadingIndicator = findViewById(R.id.mainLoadingIndicator);
        btnCreate = findViewById(R.id.btnCreate);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);

        // Setup tabs
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("S4S"));
        tabs.addTab(tabs.newTab().setText("FJ"));
        tabs.addTab(tabs.newTab().setText("MLD"));
        tabs.addTab(tabs.newTab().setText("LMT"));
        tabs.addTab(tabs.newTab().setText("CCA"));
        tabs.addTab(tabs.newTab().setText("SND"));
        tabs.addTab(tabs.newTab().setText("BJ"));
        tabs.addTab(tabs.newTab().setText("SLP"));

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 1: currentJenis = "FJ";  break;
                    case 2: currentJenis = "MLD"; break;
                    case 3: currentJenis = "LMT"; break;
                    case 4: currentJenis = "CCA"; break;
                    case 5: currentJenis = "SND"; break;
                    case 6: currentJenis = "BJ"; break;
                    case 7: currentJenis = "SLP"; break;
                    default: currentJenis = "S4S";
                }
                fetchAndPopulate(currentJenis);
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {
                // klik ulang tab yang sama → refresh
                fetchAndPopulate(currentJenis);
            }
        });

        // Default: select S4S dan langsung load
        TabLayout.Tab first = tabs.getTabAt(0);
        if (first != null) first.select(); // akan trigger fetchAndPopulate("S4S")

        btnCreate.setOnClickListener(v -> {
            int jenisId = getJenisId(currentJenis);   // S4S=1, FJ=2, ...
            showCreatePlanningDialog(jenisId);
            showCreatePlanningDialog(jenisId);
        });

        btnEdit.setOnClickListener(v -> {
            int jenisId = getJenisId(currentJenis);   // S4S=1, FJ=2, ...

            if (selectedPlanningMesinData != null) {
                showEditPlanningDialog(jenisId, selectedPlanningMesinData);
            } else {
                Toast.makeText(this, "Pilih data terlebih dahulu!", Toast.LENGTH_SHORT).show();
            }
        });

        btnDelete.setOnClickListener(v -> {
            int jenisId = getJenisId(currentJenis);   // S4S=1, FJ=2, ...

            if (selectedPlanningMesinData != null) {
                showDeletePlanningDialog(jenisId, selectedPlanningMesinData);
            } else {
                Toast.makeText(this, "Pilih data terlebih dahulu!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showCreatePlanningDialog(int jenisId) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_field_planning_mesin, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        TextView titleDialog = dialogView.findViewById(R.id.titleDialog);
        EditText editTanggal = dialogView.findViewById(R.id.editTanggal);
        Spinner  spinMesin   = dialogView.findViewById(R.id.spinMesin);
        EditText editPlanning = dialogView.findViewById(R.id.editPlanning);

        ImageButton btnClose = dialogView.findViewById(R.id.btnCloseDialogInput);
        Button btnSave       = dialogView.findViewById(R.id.btnSave);

        titleDialog.setText("Planning Mesin (Data Baru)");

        // ⬅️ di sini pakai jenisId dari tab aktif
        if (jenisId == 8) {
            loadMejaSawmillSpinner(spinMesin, "");
        } else {
            loadMesinSpinner(spinMesin, 0, jenisId);
        }

        editTanggal.setText(DateTimeUtils.getCurrentDate());
        editTanggal.setInputType(InputType.TYPE_NULL);
        editTanggal.setFocusable(false);
        editTanggal.setOnClickListener(v -> DateTimeUtils.showDatePicker(this, editTanggal));

        btnClose.setOnClickListener(v -> dialog.dismiss());

        btnSave.setOnClickListener(v -> {
            loadingDialogHelper.show(this);

            executorService.execute(() -> {
                boolean success = false;

                String tanggal = editTanggal.getText().toString().trim();
                String txtPlanning = editPlanning.getText().toString().trim();
                int planningJamKerja;

                try {
                    // default 0 kalau kosong
                    planningJamKerja = txtPlanning.isEmpty() ? 0 : Integer.parseInt(txtPlanning);
                } catch (NumberFormatException nfe) {
                    runOnUiThread(() -> {
                        loadingDialogHelper.hide();
                        editPlanning.setError("Jam kerja harus angka");
                        Toast.makeText(this, "Input jam kerja tidak valid.", Toast.LENGTH_SHORT).show();
                    });
                    return; // stop eksekusi
                }

                String idMesin = "";
                if (jenisId == 8) {
                    MstMejaData meja = (MstMejaData) spinMesin.getSelectedItem();
                    if (meja != null) idMesin = meja.getNoMeja();            // SLP pakai NoMeja (String)
                } else {
                    MesinProsesProduksiData mesin = (MesinProsesProduksiData) spinMesin.getSelectedItem();
                    if (mesin != null) idMesin = String.valueOf(mesin.getIdMesin()); // lainnya pakai IdMesin
                }

                // TODO: mapping jenisId -> insert ke tabel yang benar
                if (jenisId == 1) { // contoh: 1 = S4S
                    success = PlanningMesinApi.insertPlanningMesinS4S(idMesin, tanggal, planningJamKerja);
                } else if (jenisId == 2) { // 2 = FJ (contoh)
                    success = PlanningMesinApi.insertPlanningMesinFJ(idMesin, tanggal, planningJamKerja);
                } else if (jenisId == 3) {
                    success = PlanningMesinApi.insertPlanningMesinMoulding(idMesin, tanggal, planningJamKerja);
                } else if (jenisId == 4) {
                    success = PlanningMesinApi.insertPlanningMesinLaminating(idMesin, tanggal, planningJamKerja);
                } else if (jenisId == 5) {
                    success = PlanningMesinApi.insertPlanningMesinCCA(idMesin, tanggal, planningJamKerja);
                } else if (jenisId == 6) {
                    success = PlanningMesinApi.insertPlanningMesinSanding(idMesin, tanggal, planningJamKerja);
                } else if (jenisId == 7) {
                    success = PlanningMesinApi.insertPlanningMesinPacking(idMesin, tanggal, planningJamKerja);
                } else if (jenisId == 8) { // SLP
                    success = PlanningMesinApi.insertPlanningMesinSLP(idMesin, tanggal, planningJamKerja);
                } else {
                    success = false; // unknown jenisId
                }

                boolean finalSuccess = success;
                runOnUiThread(() -> {
                    loadingDialogHelper.hide();
                    if (finalSuccess) {
                        Toast.makeText(this, "Planning berhasil disimpan.", Toast.LENGTH_SHORT).show();
                        // TODO: refresh list
                        fetchAndPopulate(currentJenis);
                        dialog.dismiss();
                    } else {
                        Toast.makeText(this, "Gagal simpan planning.", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });

        dialog.show();
    }


    private void showEditPlanningDialog(int jenisId, PlanningMesinData data) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_field_planning_mesin, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        TextView titleDialog = dialogView.findViewById(R.id.titleDialog);
        EditText editTanggal = dialogView.findViewById(R.id.editTanggal);
        Spinner  spinMesin   = dialogView.findViewById(R.id.spinMesin);
        EditText editPlanning = dialogView.findViewById(R.id.editPlanning);

        ImageButton btnClose = dialogView.findViewById(R.id.btnCloseDialogInput);
        Button btnSave       = dialogView.findViewById(R.id.btnSave);

        titleDialog.setText("Planning Mesin (Edit)");

        // === simpan nilai lama (kunci WHERE) ===
        final String oldIdMesin = String.valueOf(data.getIdMesin());
        final String oldTanggal = data.getTanggal();

        // isi field dengan data lama
        editTanggal.setText(data.getTanggal());
        editTanggal.setInputType(InputType.TYPE_NULL);
        editTanggal.setFocusable(false);
        editTanggal.setOnClickListener(v -> DateTimeUtils.showDatePicker(this, editTanggal));

        editPlanning.setText(String.valueOf(data.getPlanningJamKerja()));

        if (jenisId == 8) {
            loadMejaSawmillSpinner(spinMesin, String.valueOf(data.getIdMesin()));
        } else {
            loadMesinSpinner(spinMesin, data.getIdMesin(), jenisId);
        }

        btnClose.setOnClickListener(v -> dialog.dismiss());

        btnSave.setOnClickListener(v -> {
            loadingDialogHelper.show(this);

            executorService.execute(() -> {
                boolean success = false;

                String newTanggal = editTanggal.getText().toString().trim();
                String txtPlanning = editPlanning.getText().toString().trim();
                int newPlanningJamKerja;
                try {
                    newPlanningJamKerja = txtPlanning.isEmpty() ? 0 : Integer.parseInt(txtPlanning);
                } catch (NumberFormatException nfe) {
                    runOnUiThread(() -> {
                        loadingDialogHelper.hide();
                        editPlanning.setError("Jam kerja harus angka");
                        Toast.makeText(this, "Input jam kerja tidak valid.", Toast.LENGTH_SHORT).show();
                    });
                    return;
                }

                String newIdMesin = "";
                if (jenisId == 8) {
                    MstMejaData meja = (MstMejaData) spinMesin.getSelectedItem();
                    if (meja != null) newIdMesin = meja.getNoMeja();
                } else {
                    MesinProsesProduksiData mesin = (MesinProsesProduksiData) spinMesin.getSelectedItem();
                    if (mesin != null) newIdMesin = String.valueOf(mesin.getIdMesin());
                }

                // 🔄 Update pakai oldIdMesin + oldTanggal
                if (jenisId == 1) {
                    success = PlanningMesinApi.updatePlanningMesinS4S(oldIdMesin, oldTanggal,
                            newIdMesin, newTanggal, newPlanningJamKerja);
                } else if (jenisId == 2) {
                    success = PlanningMesinApi.updatePlanningMesinFJ(oldIdMesin, oldTanggal,
                            newIdMesin, newTanggal, newPlanningJamKerja);
                } else if (jenisId == 3) {
                    success = PlanningMesinApi.updatePlanningMesinMoulding(oldIdMesin, oldTanggal,
                            newIdMesin, newTanggal, newPlanningJamKerja);
                } else if (jenisId == 4) {
                    success = PlanningMesinApi.updatePlanningMesinLaminating(oldIdMesin, oldTanggal,
                            newIdMesin, newTanggal, newPlanningJamKerja);
                } else if (jenisId == 5) {
                    success = PlanningMesinApi.updatePlanningMesinCCA(oldIdMesin, oldTanggal,
                            newIdMesin, newTanggal, newPlanningJamKerja);
                } else if (jenisId == 6) {
                    success = PlanningMesinApi.updatePlanningMesinSanding(oldIdMesin, oldTanggal,
                            newIdMesin, newTanggal, newPlanningJamKerja);
                } else if (jenisId == 7) {
                    success = PlanningMesinApi.updatePlanningMesinPacking(oldIdMesin, oldTanggal,
                            newIdMesin, newTanggal, newPlanningJamKerja);
                } else if (jenisId == 8) {
                    success = PlanningMesinApi.updatePlanningMesinSLP(oldIdMesin, oldTanggal,
                            newIdMesin, newTanggal, newPlanningJamKerja);
                }

                boolean finalSuccess = success;
                runOnUiThread(() -> {
                    loadingDialogHelper.hide();
                    if (finalSuccess) {
                        Toast.makeText(this, "Planning berhasil diupdate.", Toast.LENGTH_SHORT).show();
                        fetchAndPopulate(currentJenis); // refresh list
                        dialog.dismiss();
                    } else {
                        Toast.makeText(this, "Gagal update planning.", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });

        dialog.show();
    }


    private void showDeletePlanningDialog(int jenisId, PlanningMesinData data) {
        new AlertDialog.Builder(this)
                .setTitle("Hapus Data")
                .setMessage("Yakin hapus planning mesin \"" + data.getNamaMesin() + "\" tanggal " + data.getTanggal() + "?")
                .setPositiveButton("Hapus", (d, which) -> {
                    loadingDialogHelper.show(this);

                    executorService.execute(() -> {
                        boolean success = false;

                        String idMesin = String.valueOf(data.getIdMesin());
                        String tanggal = data.getTanggal();

                        // 🔄 Panggil API delete sesuai jenisId
                        if (jenisId == 1) {
                            success = PlanningMesinApi.deletePlanningMesinS4S(idMesin, tanggal);
                        } else if (jenisId == 2) {
                            success = PlanningMesinApi.deletePlanningMesinFJ(idMesin, tanggal);
                        } else if (jenisId == 3) {
                            success = PlanningMesinApi.deletePlanningMesinMoulding(idMesin, tanggal);
                        } else if (jenisId == 4) {
                            success = PlanningMesinApi.deletePlanningMesinLaminating(idMesin, tanggal);
                        } else if (jenisId == 5) {
                            success = PlanningMesinApi.deletePlanningMesinCCA(idMesin, tanggal);
                        } else if (jenisId == 6) {
                            success = PlanningMesinApi.deletePlanningMesinSanding(idMesin, tanggal);
                        } else if (jenisId == 7) {
                            success = PlanningMesinApi.deletePlanningMesinPacking(idMesin, tanggal);
                        } else if (jenisId == 8) {
                            success = PlanningMesinApi.deletePlanningMesinSLP(idMesin, tanggal);
                        }

                        boolean finalSuccess = success;
                        runOnUiThread(() -> {
                            loadingDialogHelper.hide();
                            if (finalSuccess) {
                                Toast.makeText(this, "Planning berhasil dihapus.", Toast.LENGTH_SHORT).show();
                                fetchAndPopulate(currentJenis); // refresh tabel
                            } else {
                                Toast.makeText(this, "Gagal hapus planning.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    });
                })
                .setNegativeButton("Batal", null)
                .show();
    }


    private int getJenisId(String jenis) {
        switch (jenis) {
            case "FJ":  return 2;
            case "MLD": return 3;
            case "LMT": return 4;
            case "CCA": return 5;
            case "SND": return 6;
            case "BJ":  return 7;
            case "SLP": return 8;
            case "S4S":
            default:    return 1;
        }
    }


    private void loadMesinSpinner(Spinner spinner, int selectedIdMesin, int jenisId) {
        // Lebih baik pakai executorService yang sudah ada:
        executorService.execute(() -> {
            List<MesinProsesProduksiData> mesinList = ProsesProduksiApi.getAllMesinData(jenisId);

            runOnUiThread(() -> {
                ArrayAdapter<MesinProsesProduksiData> adapter =
                        new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mesinList);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinner.setAdapter(adapter);

                // Set item terpilih jika ada
                for (int i = 0; i < mesinList.size(); i++) {
                    if (mesinList.get(i).getIdMesin() == selectedIdMesin) {
                        spinner.setSelection(i);
                        break;
                    }
                }
            });
        });
    }


    private void loadMejaSawmillSpinner(Spinner spinner, String selectedNoMeja) {
        executorService.execute(() -> {
            List<MstMejaData> mejaList = SawmillApi.getAllMeja();

            runOnUiThread(() -> {
                ArrayAdapter<MstMejaData> adapter =
                        new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mejaList);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinner.setAdapter(adapter);

                // Set item terpilih jika ada
                for (int i = 0; i < mejaList.size(); i++) {
                    if (mejaList.get(i).getNoMeja().equals(selectedNoMeja)) {
                        spinner.setSelection(i);
                        break;
                    }
                }
            });
        });
    }


    private void fetchAndPopulate(String jenis) {
        mainLoadingIndicator.setVisibility(View.VISIBLE);
        // clear tabel dan seleksi
        mainTable.removeAllViews();
        selectedRow = null;
        selectedPlanningMesinData = null;

        // token untuk request ini
        final int token = requestSeq.incrementAndGet();

        executorService.execute(() -> {
            try {
                List<PlanningMesinData> result;
                switch (jenis) {
                    case "FJ":
                        // TODO: ganti dengan API asli, contoh:
                        // result = PlanningMesinApi.getPlanningMesinFJData();
                        result = PlanningMesinApi.getPlanningMesinFJData(); // placeholder
                        break;
                    case "MLD":
                        result = PlanningMesinApi.getPlanningMesinMLDData(); // placeholder
                        break;
                    case "LMT":
                        result = PlanningMesinApi.getPlanningMesinLMTData(); // placeholder
                        break;
                    case "CCA":
                        result = PlanningMesinApi.getPlanningMesinCCAData(); // placeholder
                        break;
                    case "SND":
                        result = PlanningMesinApi.getPlanningMesinSNDData(); // placeholder
                        break;
                    case "BJ":
                        result = PlanningMesinApi.getPlanningMesinBJData(); // placeholder
                        break;
                    case "SLP":
                        result = PlanningMesinApi.getPlanningMesinSLPData(); // placeholder
                        break;
                    default:
                        result = PlanningMesinApi.getPlanningMesinS4SData();
                }

                List<PlanningMesinData> finalResult = result;
                runOnUiThread(() -> {
                    // Abaikan jika ada request yang lebih baru
                    if (token != requestSeq.get()) return;

                    // Render sesuai tab (kalau kolom sama, cukup satu method seperti punyamu)
                    if ("FJ".equals(jenis)) {
                        populateTable(finalResult); // atau populateTableFJ(finalResult);
                    } else if ("MLD".equals(jenis)) {
                        populateTable(finalResult); // atau populateTableMLD(finalResult);
                    } else {
                        populateTable(finalResult);
                    }

                    mainLoadingIndicator.setVisibility(View.GONE);
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    if (token != requestSeq.get()) return;
                    mainLoadingIndicator.setVisibility(View.GONE);
                    // tampilkan pesan error singkat
                    TextView tv = new TextView(this);
                    tv.setText("Gagal memuat data " + jenis);
                    tv.setGravity(Gravity.CENTER);
                    tv.setPadding(16,16,16,16);
                    mainTable.addView(tv);
                });
            }
        });
    }


    private void populateTable(List<PlanningMesinData> dataList) {

        mainTable.removeAllViews();

        if (dataList == null || dataList.isEmpty()) {
            TextView noDataView = new TextView(this);
            noDataView.setText("Data tidak ditemukan");
            noDataView.setGravity(Gravity.CENTER);
            noDataView.setPadding(16, 16, 16, 16);
            mainTable.addView(noDataView);
            return;
        }

        int rowIndex = 0; // Untuk melacak indeks baris

        for (PlanningMesinData data : dataList) {
            TableRow row = new TableRow(this);

            // Simpan indeks baris di tag
            row.setTag(rowIndex);

            // Tambahkan TextView untuk setiap kolom
            TextView col1 = TableUtils.createTextView(this, data.getTanggal(), 1.0f);
            TextView col2 = TableUtils.createTextView(this, data.getNamaMesin(), 1.0f);
            TextView col3 = TableUtils.createTextView(this, String.valueOf(data.getPlanningJamKerja()), 1.0f);

            row.addView(col1);
            row.addView(TableUtils.createDivider(this));

            row.addView(col2);
            row.addView(TableUtils.createDivider(this));

            row.addView(col3);


            // Tetapkan warna latar belakang berdasarkan indeks baris
            if (rowIndex % 2 == 0) {
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.background_cream)); // Warna untuk baris genap
            } else {
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.white)); // Warna untuk baris ganjil
            }

            row.setOnClickListener(v -> {
                // Reset warna baris sebelumnya (jika ada)
                if (selectedRow != null) {
                    int previousRowIndex = (int) selectedRow.getTag();
                    if (previousRowIndex % 2 == 0) {
                        selectedRow.setBackgroundColor(ContextCompat.getColor(this, R.color.background_cream));
                    } else {
                        selectedRow.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
                    }
                    TableUtils.resetTextColor(this, selectedRow); // Kembalikan warna teks ke hitam
                }

                // Tandai baris yang baru dipilih
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.primary)); // Warna penandaan
                TableUtils.setTextColor(this, row, R.color.white); // Ubah warna teks menjadi putih
                selectedRow = row;

                // Simpan data yang dipilih
                selectedPlanningMesinData = data;

            });

            mainTable.addView(row);
            rowIndex++; // Tingkatkan indeks
        }
    }


}