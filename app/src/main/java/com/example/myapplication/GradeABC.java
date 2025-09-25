package com.example.myapplication;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.myapplication.api.GradeApi;
import com.example.myapplication.api.MasterApi;
import com.example.myapplication.model.GradeABCData;
import com.example.myapplication.model.GradeABCDetailData;
import com.example.myapplication.model.MstGradeABCData;
import com.example.myapplication.utils.DateTimeUtils;
import com.example.myapplication.utils.LoadingDialogHelper;
import com.example.myapplication.utils.PermissionUtils;
import com.example.myapplication.utils.SharedPrefUtils;
import com.example.myapplication.utils.TableUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class GradeABC extends AppCompatActivity {

    private TableLayout mainTable;
    private TableLayout detailTable;
    private TableRow selectedRow;
    private TableRow selectedDetailRow;
    private GradeABCData selectedGradeABCData;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private String noGradeABC;
    private List<GradeABCData> dataList; // Data asli yang tidak difilter
    private List<GradeABCDetailData> dataDetailList; // Data asli yang tidak difilter
    private ProgressBar mainLoadingIndicator;
    private ProgressBar detailLoadingIndicator;
    private final LoadingDialogHelper loadingDialogHelper = new LoadingDialogHelper();
    private Button btnCreate;
    private Button btnEdit;
    private Button btnDelete;
    private FloatingActionButton fabAddDetail;
    private float touchXDetail, touchYDetail;
    private List<String> userPermissions;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_abc);

        mainTable = findViewById(R.id.mainTable);
        detailTable = findViewById(R.id.detailTable);
        mainLoadingIndicator = findViewById(R.id.mainLoadingIndicator);
        detailLoadingIndicator = findViewById(R.id.detailLoadingIndicator);
        btnCreate = findViewById(R.id.btnCreate);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        fabAddDetail = findViewById(R.id.fabAddDetail);

        //PERMISSION CHECK
        userPermissions = SharedPrefUtils.getPermissions(this);
        PermissionUtils.permissionCheck(this, btnCreate, "grade_abc:create");
        PermissionUtils.permissionCheck(this, fabAddDetail, "grade_abc:create");
        PermissionUtils.permissionCheck(this, btnEdit, "grade_abc:update");
        PermissionUtils.permissionCheck(this, btnDelete, "grade_abc:delete");

        loadDataAndDisplayTable();

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateGradeABCHeaderDialog();
            }
        });


        btnEdit.setOnClickListener(v -> {
            if (selectedGradeABCData != null) {
                showEditGradeABCHeaderDialog(selectedGradeABCData);
            } else {
                Toast.makeText(GradeABC.this, "Pilih data terlebih dahulu!", Toast.LENGTH_SHORT).show();
            }
        });

        // Listener Delete
        btnDelete.setOnClickListener(v -> {
            if (selectedGradeABCData != null) {
                showConfirmDeleteGradeABCHeaderDialog(selectedGradeABCData);
            } else {
                Toast.makeText(GradeABC.this, "Pilih data terlebih dahulu!", Toast.LENGTH_SHORT).show();
            }
        });

        fabAddDetail.setOnClickListener(v -> {
            if (selectedGradeABCData != null) {
                showAddGradeABCDetailDialog(selectedGradeABCData);
            } else {
                Toast.makeText(GradeABC.this, "Pilih data terlebih dahulu!", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void loadGradeABCSpinner(Spinner spinGrade, Integer selectedIdGradeABC, @Nullable Runnable onDone) {
        executorService.execute(() -> {
            List<MstGradeABCData> gradeList = MasterApi.getGradeABCList();

            // Tambahkan item default "PILIH"
            gradeList.add(0, new MstGradeABCData(0, "PILIH"));

            runOnUiThread(() -> {
                ArrayAdapter<MstGradeABCData> adapter = new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        gradeList
                );
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinGrade.setAdapter(adapter);

                // Set default selection
                if (selectedIdGradeABC == null || selectedIdGradeABC == 0) {
                    spinGrade.setSelection(0);
                } else {
                    for (int i = 0; i < gradeList.size(); i++) {
                        if (gradeList.get(i).getIdGradeABC() == selectedIdGradeABC) {
                            spinGrade.setSelection(i);
                            break;
                        }
                    }
                }

                // 🔑 Jalankan callback setelah spinner selesai diisi
                if (onDone != null) onDone.run();
            });
        });
    }

    // Overload tanpa callback
    private void loadGradeABCSpinner(Spinner spinGrade, Integer selectedIdGradeABC) {
        loadGradeABCSpinner(spinGrade, selectedIdGradeABC, null);
    }


    private void showConfirmDeleteGradeABCHeaderDialog(GradeABCData item) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Hapus Data")
                .setMessage("Hapus NoGradeABC: " + item.getNoGradeABC() + " ?\n" +
                        "Semua detail di dalamnya juga akan dihapus.")
                .setPositiveButton("Hapus", (d, w) -> performDeleteHeaderCascade(item.getNoGradeABC()))
                .setNegativeButton("Batal", null)
                .show();
    }

    private void performDeleteHeaderCascade(String noGradeABC) {
        loadingDialogHelper.show(this);

        executorService.execute(() -> {
            boolean ok = GradeApi.deleteGradeABCHeaderCascade(noGradeABC);
            if (ok) {
                dataList = GradeApi.getGradeABCData(); // refresh list
            }

            runOnUiThread(() -> {
                loadingDialogHelper.hide();
                if (ok) {
                    Toast.makeText(GradeABC.this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
                    populateTable(dataList);
                    // reset selection & tombol
                    selectedGradeABCData = null;
                    selectedRow = null;

                    // kosongkan panel detail
                    detailTable.removeAllViews();
                } else {
                    Toast.makeText(GradeABC.this, "Gagal menghapus data", Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    private void showEditGradeABCHeaderDialog(GradeABCData item) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_field_grade_abc_header, null);

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setView(dialogView);
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        // Referensi UI
        TextView titleDialog = dialogView.findViewById(R.id.titleDialog);
        TextView tvNoPenjualan = dialogView.findViewById(R.id.tvNoPenjualan);
        EditText editTanggal = dialogView.findViewById(R.id.editTanggal);
        EditText editKeterangan = dialogView.findViewById(R.id.editKeterangan);
        ImageButton btnClose = dialogView.findViewById(R.id.btnCloseDialogInput);
        Button btnSave = dialogView.findViewById(R.id.btnSave);

        titleDialog.setText("Edit Grade ABC");
        tvNoPenjualan.setText(item.getNoGradeABC());

        // Tanggal & Keterangan existing
        editTanggal.setText(item.getTanggal()); // pastikan item.tanggal format "yyyy-MM-dd"
        editTanggal.setInputType(InputType.TYPE_NULL);
        editTanggal.setFocusable(false);
        editTanggal.setOnClickListener(v -> DateTimeUtils.showDatePicker(this, editTanggal));

        editKeterangan.setText(item.getKeterangan());

        btnClose.setOnClickListener(v -> dialog.dismiss());

        btnSave.setOnClickListener(v -> {
            loadingDialogHelper.show(GradeABC.this);

            String tanggal = editTanggal.getText().toString().trim();
            String keterangan = editKeterangan.getText().toString().trim();

            executorService.execute(() -> {
                boolean ok = GradeApi.updateGradeABCHeader(
                        item.getNoGradeABC(),
                        tanggal,
                        keterangan.isEmpty() ? null : keterangan
                );

                // Refresh data bila sukses
                if (ok) {
                    dataList = GradeApi.getGradeABCData();
                }

                runOnUiThread(() -> {
                    loadingDialogHelper.hide();
                    if (ok) {
                        Toast.makeText(GradeABC.this, "Header berhasil diubah", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        populateTable(dataList);
                        selectedGradeABCData = null;
                        detailTable.removeAllViews(); // opsional: kosongkan detail
                    } else {
                        Toast.makeText(GradeABC.this, "Gagal mengubah header", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });

        dialog.show();
    }


    private void showCreateGradeABCHeaderDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_field_grade_abc_header, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        // Referensi UI
        TextView titleDialog = dialogView.findViewById(R.id.titleDialog);
        TextView tvNoPenjualan = dialogView.findViewById(R.id.tvNoPenjualan);
        EditText editTanggal = dialogView.findViewById(R.id.editTanggal);
        EditText editKeterangan = dialogView.findViewById(R.id.editKeterangan);
        ImageButton btnClose = dialogView.findViewById(R.id.btnCloseDialogInput);
        Button btnSave = dialogView.findViewById(R.id.btnSave);

        titleDialog.setText("Grade ABC (Data Baru)");

        // Untuk create, NoBongkarSusun belum di-generate → kasih placeholder
        tvNoPenjualan.setText("GG.XXXXXX");


        editTanggal.setText(DateTimeUtils.getCurrentDate());
        editTanggal.setInputType(InputType.TYPE_NULL);
        editTanggal.setFocusable(false);
        editTanggal.setOnClickListener(v -> {
            DateTimeUtils.showDatePicker(this, editTanggal);
        });

        btnClose.setOnClickListener(v -> dialog.dismiss());

        btnSave.setOnClickListener(v -> {
            // tampilkan loader kalau punya helper
             loadingDialogHelper.show(this);

            String tanggal = editTanggal.getText().toString().trim(); // "yyyy-MM-dd"
            String keterangan = editKeterangan.getText().toString().trim();

            executorService.execute(() -> {
                String newNo = GradeApi.insertGradeABCHeaderWithGeneratedNo(
                        tanggal,
                        keterangan.isEmpty() ? null : keterangan
                );
                boolean success = (newNo != null);
                if (success) {
                    dataList = GradeApi.getGradeABCData();
                }

                runOnUiThread(() -> {
                    loadingDialogHelper.hide();
                    if (success) {
                        tvNoPenjualan.setText(newNo); // tampilkan NoGradeABC yang baru
                        dialog.dismiss();
                        Toast.makeText(GradeABC.this, "Header berhasil dibuat", Toast.LENGTH_SHORT).show();

                        // refresh list header kalau ada:
                         populateTable(dataList);
                        // kosongkan panel detail
                        detailTable.removeAllViews();

                    } else {
                        Toast.makeText(GradeABC.this, "Gagal membuat header", Toast.LENGTH_SHORT).show();

                    }
                });
            });
        });

        dialog.show();
    }


    private void loadDataAndDisplayTable() {
        mainLoadingIndicator.setVisibility(View.VISIBLE);

        // Menjalankan operasi di background thread
        executorService.execute(() -> {
            dataList = GradeApi.getGradeABCData();

            // Menampilkan data di UI thread setelah selesai mengambil data
            runOnUiThread(() -> {
                populateTable(dataList);  // Memperbarui tampilan tabel dengan data terbaru
                // Sembunyikan loading indicator jika ada
                mainLoadingIndicator.setVisibility(View.GONE);
            });
        });
    }


    private void populateTable(List<GradeABCData> dataList) {

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

        for (GradeABCData data : dataList) {
            TableRow row = new TableRow(this);

            // Simpan indeks baris di tag
            row.setTag(rowIndex);

            // Tambahkan TextView untuk setiap kolom
            TextView col1 = TableUtils.createTextView(this, data.getNoGradeABC(), 1.0f);
            TextView col2 = TableUtils.createTextView(this, data.getTanggal(), 1.0f);
            TextView col3 = TableUtils.createTextView(this, data.getKeterangan(), 1.0f);

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
                selectedGradeABCData = data;

                // Tangani aksi tambahan
                onRowClick(data);
            });


            mainTable.addView(row);
            rowIndex++; // Tingkatkan indeks
        }
    }

    private void populateDetailTable(List<GradeABCDetailData> dataDetailList) {

        detailTable.removeAllViews();

        if (dataDetailList == null || dataDetailList.isEmpty()) {
            TextView noDataView = new TextView(this);
            noDataView.setText("Data tidak ditemukan");
            noDataView.setGravity(Gravity.CENTER);
            noDataView.setPadding(16, 16, 16, 16);
            detailTable.addView(noDataView);
            return;
        }

        int rowIndex = 0;

        for (GradeABCDetailData data : dataDetailList) {
            TableRow row = new TableRow(this);

            // simpan data & index di tag kalau perlu
            row.setTag(data);

            TextView col1 = TableUtils.createTextView(this, String.valueOf(data.getNamaGrade()), 0.7f);
            TextView col2 = TableUtils.createTextView(this, String.valueOf(data.getJmlhBatang()), 0.3f);

            row.addView(col1);
            row.addView(TableUtils.createDivider(this));
            row.addView(col2);
            row.addView(TableUtils.createDivider(this));

            // zebra background
            if (rowIndex % 2 == 0) {
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.background_cream));
            } else {
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            }

            // opsional: click biasa untuk select
            row.setOnClickListener(v -> {
                if (selectedDetailRow != null && selectedDetailRow != row) {
                    int prevIndex = detailTable.indexOfChild(selectedDetailRow);
                    int prevColor = (prevIndex % 2 == 0)
                            ? ContextCompat.getColor(this, R.color.background_cream)
                            : ContextCompat.getColor(this, R.color.white);
                    selectedDetailRow.setBackgroundColor(prevColor);
                    TableUtils.resetTextColor(this, selectedDetailRow);
                }
                row.setBackgroundResource(R.drawable.row_selector);
                TableUtils.setTextColor(this, row, R.color.white);
                selectedDetailRow = row;

                // TODO: kalau mau ada onRowClickDetail(data) taruh sini
            });

            // tangkap koordinat sentuhan (buat posisi popup)
            row.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    touchXDetail = event.getRawX();
                    touchYDetail = event.getRawY();
                }
                return false; // biar longClick tetap jalan
            });

            // long-press => tampilkan popup
            // 1) Panggil ini di onLongClick
            row.setOnLongClickListener(v -> {
                // highlight row (opsional)
                if (selectedDetailRow != null && selectedDetailRow != row) {
                    int prevIndex = detailTable.indexOfChild(selectedDetailRow);
                    int prevColor = (prevIndex % 2 == 0)
                            ? ContextCompat.getColor(this, R.color.background_cream)
                            : ContextCompat.getColor(this, R.color.white);
                    selectedDetailRow.setBackgroundColor(prevColor);
                    TableUtils.resetTextColor(this, selectedDetailRow);
                }
                row.setBackgroundResource(R.drawable.row_selector);
                TableUtils.setTextColor(this, row, R.color.white);
                selectedDetailRow = row;

                // tampilkan tooltip tepat di lokasi long-press
                showDetailRowPopup(row, (GradeABCDetailData) row.getTag(), touchXDetail, touchYDetail);
                row.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                return true;
            });


            detailTable.addView(row);
            rowIndex++;
        }
    }


    // 2) Helper untuk menampilkan tooltip di posisi sentuhan
    private void showDetailRowPopup(View anchorView,
                                    GradeABCDetailData data,
                                    float rawX, float rawY) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View popupView = inflater.inflate(R.layout.popup_menu_edit_delete, null);

        // Ukur dulu agar bisa clamp ke dalam layar
        popupView.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        );
        int pw = popupView.getMeasuredWidth();
        int ph = popupView.getMeasuredHeight();

        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        // (opsional) jika mau animasi muncul
        // popupWindow.setAnimationStyle(R.style.PopupZoomFade);

        // Ambil ukuran layar untuk clamp posisi
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screenW = dm.widthPixels;
        int screenH = dm.heightPixels;

        // Posisi awal: sedikit ke kiri & di atas titik sentuhan biar “tooltip feel”
        int desiredX = (int) rawX - (int) (pw * 0.85f);
        int desiredY = (int) rawY - ph - Math.round(8 * getResources().getDisplayMetrics().density);
        // Clamp supaya tidak keluar layar
        desiredX = Math.max(0, Math.min(desiredX, screenW - pw));
        desiredY = Math.max(0, Math.min(desiredY, screenH - ph));

        // Tampilkan relatif ke dekor view window di koordinat layar (TOP|START)
        View root = getWindow().getDecorView();
        popupWindow.showAtLocation(root, Gravity.TOP | Gravity.START, desiredX, desiredY);


        // ---- wiring tombol ----
        Button btnEditDetail = popupView.findViewById(R.id.btnEdit);
        PermissionUtils.permissionCheck(this, btnEditDetail, "grade_abc:update");

        btnEditDetail.setOnClickListener(v -> {
            popupWindow.dismiss();
            showEditGradeABCDetailDialog(data);
        });


        Button btnDeleteDetail = popupView.findViewById(R.id.btnDelete);
        PermissionUtils.permissionCheck(this, btnDeleteDetail, "grade_abc:delete");

        btnDeleteDetail.setOnClickListener(v -> {
            popupWindow.dismiss();

            new AlertDialog.Builder(this)
                    .setTitle("Hapus Detail")
                    .setMessage("Yakin hapus grade \"" + data.getNamaGrade() + "\" (" + data.getJmlhBatang() + " pcs)?")
                    .setPositiveButton("Hapus", (d, which) -> {
                        loadingDialogHelper.show(this);

                        executorService.execute(() -> {
                            boolean ok = GradeApi.deleteGradeABCDetail(
                                    data.getNoGradeABC(),
                                    data.getIdGradeABC()
                            );

                            List<GradeABCDetailData> refreshed = null;
                            if (ok) {
                                refreshed = GradeApi.getGradeABCDetailData(data.getNoGradeABC());
                            }

                            List<GradeABCDetailData> finalRefreshed = refreshed;
                            boolean finalOk = ok;

                            runOnUiThread(() -> {
                                loadingDialogHelper.hide();
                                if (finalOk) {
                                    Toast.makeText(this, "Detail berhasil dihapus.", Toast.LENGTH_SHORT).show();
                                    if (finalRefreshed != null) {
                                        populateDetailTable(finalRefreshed);
                                    }
                                    // reset highlight seleksi row (opsional)
                                    selectedDetailRow = null;
                                } else {
                                    Toast.makeText(this, "Gagal menghapus detail.", Toast.LENGTH_LONG).show();
                                }
                            });
                        });
                    })
                    .setNegativeButton("Batal", null)
                    .show();
        });


        // Saat popup ditutup, balikan highlight row (opsional)
        popupWindow.setOnDismissListener(() -> {
            if (selectedDetailRow != null) {
                int idx = detailTable.indexOfChild(selectedDetailRow);
                int base = (idx % 2 == 0)
                        ? ContextCompat.getColor(this, R.color.background_cream)
                        : ContextCompat.getColor(this, R.color.white);
                selectedDetailRow.setBackgroundColor(base);
                TableUtils.resetTextColor(this, selectedDetailRow);
                selectedDetailRow = null;
            }
        });
    }


    private void showAddGradeABCDetailDialog(GradeABCData header) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_field_grade_abc_detail, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        // UI refs
        TextView titleDialog = dialogView.findViewById(R.id.titleDialog);
        Spinner spinGradeABC   = dialogView.findViewById(R.id.spinGradeABC);
        EditText editPcs       = dialogView.findViewById(R.id.editPcs);
        Button btnClose        = dialogView.findViewById(R.id.btnCloseDialogInput);
        Button btnSave         = dialogView.findViewById(R.id.btnSave);

        if (titleDialog != null) titleDialog.setText("Grade ABC (Tambah Detail)");

        // Load spinner (default pilih "PILIH"/id=0)
        loadGradeABCSpinner(spinGradeABC, 0);

        btnClose.setOnClickListener(v -> dialog.dismiss());

        btnSave.setOnClickListener(v -> {

            MstGradeABCData selected = (MstGradeABCData) spinGradeABC.getSelectedItem();
            int idGradeABC = selected.getIdGradeABC();

            if (idGradeABC == 0) {
                Toast.makeText(this, "Pilih Grade yang valid.", Toast.LENGTH_SHORT).show();
                return;
            }

            String pcsStr = editPcs.getText().toString().trim();
            int pcs;
            try {
                pcs = Integer.parseInt(pcsStr);
                if (pcs <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                Toast.makeText(this, "Pcs harus angka > 0.", Toast.LENGTH_SHORT).show();
                return;
            }

            loadingDialogHelper.show(this);

            executorService.execute(() -> {
                // 🔎 Cek duplikat
                boolean isDuplicate = GradeApi.existsGradeABCDetail(header.getNoGradeABC(), idGradeABC);

                if (isDuplicate) {
                    runOnUiThread(() -> {
                        loadingDialogHelper.hide();
                        Toast.makeText(this, "Duplikat! Grade ini telah di input", Toast.LENGTH_LONG).show();
                    });
                    return; // stop di sini
                }

                // kalau tidak duplikat baru insert
                boolean ok = GradeApi.insertGradeABCDetail(
                        header.getNoGradeABC(),
                        idGradeABC,
                        pcs
                );

                List<GradeABCDetailData> newDetail = null;
                if (ok) {
                    newDetail = GradeApi.getGradeABCDetailData(header.getNoGradeABC());
                }

                List<GradeABCDetailData> finalNewDetail = newDetail;
                boolean finalOk = ok;

                runOnUiThread(() -> {
                    loadingDialogHelper.hide();
                    if (finalOk) {
                        Toast.makeText(this, "Detail berhasil ditambahkan.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        if (finalNewDetail != null) {
                            populateDetailTable(finalNewDetail);
                        }
                    } else {
                        Toast.makeText(this, "Gagal menambahkan detail.", Toast.LENGTH_LONG).show();
                    }
                });
            });

        });

        dialog.show();

    }



    private void showEditGradeABCDetailDialog(GradeABCDetailData existing) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_field_grade_abc_detail, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        // UI refs
        TextView titleDialog     = dialogView.findViewById(R.id.titleDialog);
        Spinner  spinGradeABC    = dialogView.findViewById(R.id.spinGradeABC);
        EditText editPcs         = dialogView.findViewById(R.id.editPcs);
        Button   btnClose        = dialogView.findViewById(R.id.btnCloseDialogInput);
        Button   btnSave         = dialogView.findViewById(R.id.btnSave);

        if (titleDialog != null) titleDialog.setText("Grade ABC (Ubah Detail)");

        // Preselect spinner dgn Id grade lama
        int oldIdGradeABC = existing.getIdGradeABC();
        loadGradeABCSpinner(spinGradeABC, oldIdGradeABC);

        // Isi pcs lama
        editPcs.setText(String.valueOf(existing.getJmlhBatang()));

        btnClose.setOnClickListener(v -> dialog.dismiss());

        btnSave.setOnClickListener(v -> {
            // ambil pilihan baru
            MstGradeABCData selected = (MstGradeABCData) spinGradeABC.getSelectedItem();
            int newIdGradeABC = selected.getIdGradeABC();

            if (newIdGradeABC == 0) {
                Toast.makeText(this, "Pilih Grade yang valid.", Toast.LENGTH_SHORT).show();
                return;
            }

            String pcsStr = editPcs.getText().toString().trim();
            int pcs;
            try {
                pcs = Integer.parseInt(pcsStr);
                if (pcs <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                Toast.makeText(this, "Pcs harus angka > 0.", Toast.LENGTH_SHORT).show();
                return;
            }

            loadingDialogHelper.show(this);

            executorService.execute(() -> {
                // Jika ganti grade, pastikan tidak duplikat (NoGradeABC + IdGradeABC)
                if (newIdGradeABC != oldIdGradeABC) {
                    boolean dup = GradeApi.existsGradeABCDetail(existing.getNoGradeABC(), newIdGradeABC);
                    if (dup) {
                        runOnUiThread(() -> {
                            loadingDialogHelper.hide();
                            Toast.makeText(this, "Duplikat! Grade ini sudah ada.", Toast.LENGTH_LONG).show();
                        });
                        return;
                    }
                }

                boolean ok = GradeApi.updateGradeABCDetail(
                        existing.getNoGradeABC(),
                        oldIdGradeABC,
                        newIdGradeABC,
                        pcs
                );

                List<GradeABCDetailData> refreshed = null;
                if (ok) {
                    refreshed = GradeApi.getGradeABCDetailData(existing.getNoGradeABC());
                }

                List<GradeABCDetailData> finalRefreshed = refreshed;
                boolean finalOk = ok;

                runOnUiThread(() -> {
                    loadingDialogHelper.hide();
                    if (finalOk) {
                        Toast.makeText(this, "Detail berhasil diubah.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        if (finalRefreshed != null) {
                            populateDetailTable(finalRefreshed);
                        }
                    } else {
                        Toast.makeText(this, "Gagal mengubah detail.", Toast.LENGTH_LONG).show();
                    }
                });
            });
        });

        dialog.show();
    }

    private void onRowClick(GradeABCData data) {
        executorService.execute(() -> {
            noGradeABC = data.getNoGradeABC();

            // TAMPILKAN LOADER + SEMBUNYIKAN TABEL (MAIN THREAD)
            runOnUiThread(() -> {
                detailLoadingIndicator.setVisibility(View.VISIBLE);
                detailLoadingIndicator.bringToFront();    // jaga-jaga kalau z-order
                detailTable.setVisibility(View.INVISIBLE);
            });

            // AMBIL DATA (BACKGROUND)
            List<GradeABCDetailData> detailList = GradeApi.getGradeABCDetailData(noGradeABC);

            // RENDER HASIL (MAIN THREAD)
            runOnUiThread(() -> {
                try {
                    populateDetailTable(detailList);      // ini sudah removeAllViews() di awal
                } finally {
                    detailLoadingIndicator.setVisibility(View.GONE);
                    detailTable.setVisibility(View.VISIBLE);
                }
            });
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}