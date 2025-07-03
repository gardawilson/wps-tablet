package com.example.myapplication;

import static com.example.myapplication.api.LabelApi.insertDataRejectPembelian;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.myapplication.api.LabelApi;
import com.example.myapplication.api.ProductionApi;
import com.example.myapplication.api.SawmillApi;
import com.example.myapplication.model.JenisKayuData;
import com.example.myapplication.model.QcSawmillData;
import com.example.myapplication.model.QcSawmillDetailData;
import com.example.myapplication.model.STPembelianData;
import com.example.myapplication.model.STPembelianDataReject;
import com.example.myapplication.model.SawmillDetailData;
import com.example.myapplication.model.SupplierData;
import com.example.myapplication.model.TooltipData;
import com.example.myapplication.utils.DateTimeUtils;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class QcSawmill extends AppCompatActivity {

    private TableLayout mainTable;
    private TableLayout detailQcSawmillTable;
    private TableRow selectedRow;
    private QcSawmillData selectedQcSawmillData;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private String noQcSawmill;
    private List<QcSawmillData> dataList; // Data asli yang tidak difilter
    private ImageButton btnAddDetail;
    private Button btnDataBaru;
    private Button btnEditData;
    private Button btnDeleteData;
    private List<QcSawmillDetailData> qcSawmillDetailDataList;
    private TableRow selectedRowDetail;
    private float touchX, touchY;     // Variabel untuk menyimpan koordinat klik
    final private DecimalFormat df = new DecimalFormat("0.####"); // Format angka



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qc_sawmill);

        mainTable = findViewById(R.id.mainTable);
        detailQcSawmillTable = findViewById(R.id.detailQcSawmillTable);
        btnAddDetail = findViewById(R.id.btnAddDetail);
        btnDataBaru = findViewById(R.id.btnDataBaru);
        btnEditData = findViewById(R.id.btnEditData);
        btnDeleteData = findViewById(R.id.btnDeleteData);


        loadDataAndDisplayTable();

        // Menambahkan OnClickListener
        btnDataBaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewDataDialog();

            }
        });

        btnEditData.setOnClickListener(v -> {
            if (selectedQcSawmillData != null) {
                showEditDataDialog(selectedQcSawmillData);
            } else {
                Toast.makeText(QcSawmill.this, "Pilih data yang ingin diedit terlebih dahulu", Toast.LENGTH_SHORT).show();
            }
        });

        btnDeleteData.setOnClickListener(v -> {
            if (selectedQcSawmillData != null) {
                showDeleteDataDialog(selectedQcSawmillData);
            } else {
                Toast.makeText(QcSawmill.this, "Pilih data yang ingin dihapus terlebih dahulu", Toast.LENGTH_SHORT).show();
            }
        });


        btnAddDetail.setOnClickListener(v -> {
            if (noQcSawmill == null || noQcSawmill.isEmpty()) {
                Toast.makeText(QcSawmill.this, "Pilih No. QC terlebih dahulu!", Toast.LENGTH_SHORT).show();
            } else {
                showAddDetailDialog(noQcSawmill);
            }
        });
    }





    private void showAddDetailDialog(String noQc) {
        AlertDialog.Builder builder = new AlertDialog.Builder(QcSawmill.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_insert_detail_qc_sawmill, null);
        builder.setView(view);

        final EditText editCuttingTebal = view.findViewById(R.id.editCuttingTebal);
        final EditText editCuttingLebar = view.findViewById(R.id.editCuttingLebar);
        final EditText editActualTebal = view.findViewById(R.id.editActualTebal);
        final EditText editActualLebar = view.findViewById(R.id.editActualLebar);
        final EditText editSusutTebal = view.findViewById(R.id.editSusutTebal);
        final EditText editSusutLebar = view.findViewById(R.id.editSusutLebar);

        Button btnSimpan = view.findViewById(R.id.btnSimpan);
        ImageButton btnClose = view.findViewById(R.id.btnCloseDialogInputDetail);

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        btnClose.setOnClickListener(v -> dialog.dismiss());

        btnSimpan.setOnClickListener(v -> {
            String strCuttingTebal = editCuttingTebal.getText().toString().trim();
            String strCuttingLebar = editCuttingLebar.getText().toString().trim();
            String strActualTebal = editActualTebal.getText().toString().trim();
            String strActualLebar = editActualLebar.getText().toString().trim();
            String strSusutTebal = editSusutTebal.getText().toString().trim();
            String strSusutLebar = editSusutLebar.getText().toString().trim();

            boolean valid = true;

            if (strCuttingTebal.isEmpty()) {
                editCuttingTebal.setError("Wajib diisi");
                valid = false;
            }
            if (strCuttingLebar.isEmpty()) {
                editCuttingLebar.setError("Wajib diisi");
                valid = false;
            }
            if (strActualTebal.isEmpty()) {
                editActualTebal.setError("Wajib diisi");
                valid = false;
            }
            if (strActualLebar.isEmpty()) {
                editActualLebar.setError("Wajib diisi");
                valid = false;
            }
            if (strSusutTebal.isEmpty()) {
                editSusutTebal.setError("Wajib diisi");
                valid = false;
            }
            if (strSusutLebar.isEmpty()) {
                editSusutLebar.setError("Wajib diisi");
                valid = false;
            }

            if (!valid) return;

            // Jika semua input aman, baru parse float
            float cuttingTebal = Float.parseFloat(strCuttingTebal);
            float cuttingLebar = Float.parseFloat(strCuttingLebar);
            float actualTebal = Float.parseFloat(strActualTebal);
            float actualLebar = Float.parseFloat(strActualLebar);
            float susutTebal = Float.parseFloat(strSusutTebal);
            float susutLebar = Float.parseFloat(strSusutLebar);

            executorService.execute(() -> {
                try {
                    SawmillApi.insertQcSawmillDetailAutoNoUrut(
                            noQc,
                            cuttingTebal, cuttingLebar,
                            actualTebal, actualLebar,
                            susutTebal, susutLebar
                    );

                    qcSawmillDetailDataList = SawmillApi.fetchQcSawmillDetailByNoQc(noQcSawmill);

                    runOnUiThread(() -> {
                        Toast.makeText(QcSawmill.this, "Berhasil disimpan", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        populateDetailQcSawmill(qcSawmillDetailDataList);
                    });
                } catch (SQLException e) {
                    runOnUiThread(() -> {
                        Toast.makeText(QcSawmill.this, "Gagal menyimpan: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            });
        });


        dialog.show();
    }


    private void loadDataAndDisplayTable() {
        // Menjalankan operasi di background thread
        executorService.execute(() -> {
            dataList = SawmillApi.getQcSawmillData();

            // Menampilkan data di UI thread setelah selesai mengambil data
            runOnUiThread(() -> {
                populateTable(dataList);
                detailQcSawmillTable.removeAllViews();
                // Memperbarui tampilan tabel dengan data terbaru
                // Sembunyikan loading indicator jika ada
                // loadingIndicator.setVisibility(View.GONE);
            });
        });
    }


    // Di dalam Activity atau Fragment Anda
    private void showNewDataDialog() {
        // Membuat Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(QcSawmill.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_insert_header_qc_sawmill, null);
        builder.setView(view);

        // Menyiapkan EditText dan Spinner
        final EditText editTgl = view.findViewById(R.id.editTgl);
        final Spinner spinJenisKayu = view.findViewById(R.id.spinJenisKayu);
        final EditText editMeja = view.findViewById(R.id.editMeja);
        final ProgressBar progressJenisKayu = view.findViewById(R.id.progressJenisKayu);



        // Mengambil tanggal hari ini
        String date = DateTimeUtils.getCurrentDate();


        // Menetapkan tanggal hari ini ke tglLaporan dan tglMasuk
        editTgl.setText(date);

        // Deklarasikan objek dialog di luar listener
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        loadJenisKayuDataToSpinner(spinJenisKayu, -1, progressJenisKayu);

        // Set OnClickListener untuk tglLaporan (datepicker)
        editTgl.setOnClickListener(v -> showDatePickerDialog(editTgl));

        // Menambahkan onClickListener untuk tombol tutup dialog
        ImageButton btnCloseDialogInputHeader = view.findViewById(R.id.btnCloseDialogInputHeader);
        btnCloseDialogInputHeader.setOnClickListener(v -> {
            dialog.dismiss();  // Menutup DialogFragment
        });

        // Tombol Simpan
        Button btnSave = view.findViewById(R.id.btnSimpan);
        btnSave.setOnClickListener(v -> {

            String tglVal = DateTimeUtils.formatToDatabaseDate(editTgl.getText().toString());
            String mejaVal = editMeja.getText().toString();

            JenisKayuData selectedJenisKayu = (JenisKayuData) spinJenisKayu.getSelectedItem();
            int idJenisKayu = (selectedJenisKayu != null) ? selectedJenisKayu.getIdJenisKayu() : -1;

            boolean valid = true;

            if (tglVal.isEmpty()) {
                editTgl.setError("Tanggal harus diisi");
                valid = false;
            }
            if (mejaVal.isEmpty()) {
                editMeja.setError("Meja harus diisi");
                valid = false;
            }
            if (idJenisKayu == -1) {
                spinJenisKayu.setBackgroundResource(R.drawable.spinner_error);
                valid = false;
            }

            if (valid) {
                executorService.execute(() -> {
                    String newNoQc = SawmillApi.getNextNoQc();
                    if (newNoQc != null) {
                        try {
                            SawmillApi.insertQcSawmillHeader(newNoQc, tglVal, idJenisKayu, mejaVal);
                            runOnUiThread(() -> {
                                Toast.makeText(QcSawmill.this, "Data QC berhasil disimpan dengan NoQc: " + newNoQc, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                loadDataAndDisplayTable(); // Refresh tabel
                            });
                        } catch (SQLException e) {
                            runOnUiThread(() -> Toast.makeText(QcSawmill.this, "Gagal menyimpan data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                        }
                    } else {
                        runOnUiThread(() -> Toast.makeText(QcSawmill.this, "Gagal membuat NoQc baru", Toast.LENGTH_SHORT).show());
                    }
                });
            } else {
                Toast.makeText(QcSawmill.this, "Mohon lengkapi data!", Toast.LENGTH_SHORT).show();
            }
        });


        // Menampilkan Dialog
        dialog.show();
    }


    private void showEditDataDialog(QcSawmillData data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(QcSawmill.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_insert_header_qc_sawmill, null);
        builder.setView(view);

        final EditText editTgl = view.findViewById(R.id.editTgl);
        final Spinner spinJenisKayu = view.findViewById(R.id.spinJenisKayu);
        final EditText editMeja = view.findViewById(R.id.editMeja);
        final ProgressBar progressJenisKayu = view.findViewById(R.id.progressJenisKayu);


        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        // Set tanggal awal dari data
        editTgl.setText(data.getTgl());

        // Set meja awal dari data
        editMeja.setText(data.getMeja());

        // Muat data jenis kayu ke spinner, lalu set selection ke id dari data
        loadJenisKayuDataToSpinner(spinJenisKayu, data.getIdJenisKayu(), progressJenisKayu);

        // Tampilkan DatePicker saat klik tanggal
        editTgl.setOnClickListener(v -> showDatePickerDialog(editTgl));

        // Tombol tutup
        ImageButton btnCloseDialogInputHeader = view.findViewById(R.id.btnCloseDialogInputHeader);
        btnCloseDialogInputHeader.setOnClickListener(v -> dialog.dismiss());

        // Tombol simpan edit
        Button btnSave = view.findViewById(R.id.btnSimpan);
        btnSave.setOnClickListener(v -> {
            String tglVal = editTgl.getText().toString();
            String mejaVal = editMeja.getText().toString();

            JenisKayuData selectedJenisKayu = (JenisKayuData) spinJenisKayu.getSelectedItem();
            int idJenisKayu = (selectedJenisKayu != null) ? selectedJenisKayu.getIdJenisKayu() : -1;

            boolean valid = true;

            if (tglVal.isEmpty()) {
                editTgl.setError("Tanggal harus diisi");
                valid = false;
            }
            if (mejaVal.isEmpty()) {
                editMeja.setError("Meja harus diisi");
                valid = false;
            }
            if (idJenisKayu == -1) {
                spinJenisKayu.setBackgroundResource(R.drawable.spinner_error);
                valid = false;
            }

            if (valid) {
                executorService.execute(() -> {
                    try {
                        SawmillApi.updateQcSawmillHeader(data.getNoQc(), tglVal, idJenisKayu, mejaVal);

                        runOnUiThread(() -> {
                            Toast.makeText(QcSawmill.this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            loadDataAndDisplayTable(); // Refresh table
                        });
                    } catch (SQLException e) {
                        runOnUiThread(() -> Toast.makeText(QcSawmill.this, "Gagal update: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    }
                });
            } else {
                Toast.makeText(QcSawmill.this, "Mohon lengkapi data!", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void showDeleteDataDialog(QcSawmillData data) {
        new AlertDialog.Builder(QcSawmill.this)
                .setTitle("Konfirmasi Hapus")
                .setMessage("Yakin ingin menghapus data dengan NoQc: " + data.getNoQc() + "?")
                .setPositiveButton("Ya", (dialog, which) -> {
                    executorService.execute(() -> {
                        try {
                            boolean success = SawmillApi.deleteQcSawmillHeader(data.getNoQc());

                            runOnUiThread(() -> {
                                if (success) {
                                    Toast.makeText(QcSawmill.this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
                                    loadDataAndDisplayTable(); // Refresh tabel setelah delete
                                    selectedQcSawmillData = null;
                                    selectedRow = null;
                                } else {
                                    Toast.makeText(QcSawmill.this, "Gagal menghapus data", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (SQLException e) {
                            runOnUiThread(() -> Toast.makeText(QcSawmill.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                        }
                    });
                })
                .setNegativeButton("Batal", null)
                .show();
    }



    private void showDatePickerDialog(final EditText dateEditText) {
        // Mendapatkan tanggal hari ini
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Membuat DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(QcSawmill.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Buat Calendar dari tanggal yang dipilih
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(selectedYear, selectedMonth, selectedDay);

                    // Format ke dd-MMM-yyyy, contoh: 03-Jul-2025
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                    String formattedDate = sdf.format(selectedDate.getTime());

                    // Tampilkan ke EditText
                    dateEditText.setText(formattedDate);
                }, year, month, day);

        // Tampilkan dialog
        datePickerDialog.show();
    }



    private void loadJenisKayuDataToSpinner(final Spinner spinJenisKayu, final int selectedIdJenisKayu, final ProgressBar progressBar) {
        final List<JenisKayuData> jenisKayuList = new ArrayList<>();
        jenisKayuList.add(new JenisKayuData(-1, "PILIH"));

        progressBar.setVisibility(View.VISIBLE);
        spinJenisKayu.setEnabled(false);

        executorService.execute(() -> {
            List<JenisKayuData> fromDb = SawmillApi.getJenisKayuList();
            jenisKayuList.addAll(fromDb);

            runOnUiThread(() -> {
                ArrayAdapter<JenisKayuData> adapter = new ArrayAdapter<>(QcSawmill.this, android.R.layout.simple_spinner_item, jenisKayuList);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinJenisKayu.setAdapter(adapter);

                // Pilih otomatis item jika ada selected ID
                if (selectedIdJenisKayu != -1) {
                    for (int i = 0; i < jenisKayuList.size(); i++) {
                        if (jenisKayuList.get(i).getIdJenisKayu() == selectedIdJenisKayu) {
                            spinJenisKayu.setSelection(i);
                            break;
                        }
                    }
                } else {
                    spinJenisKayu.setSelection(0); // default
                }

                progressBar.setVisibility(View.GONE);
                spinJenisKayu.setEnabled(true);
            });
        });

        spinJenisKayu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                JenisKayuData selectedJenisKayu = (JenisKayuData) spinJenisKayu.getSelectedItem();

                if (selectedJenisKayu != null && selectedJenisKayu.getIdJenisKayu() != -1) {
                    spinJenisKayu.setBackgroundResource(R.drawable.border_input);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Kosong
            }
        });
    }


    private void populateTable(List<QcSawmillData> dataList) {

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

        for (QcSawmillData data : dataList) {
            TableRow row = new TableRow(this);

            // Simpan indeks baris di tag
            row.setTag(rowIndex);

            // Tambahkan TextView untuk setiap kolom
            TextView col1 = createTextView(data.getNoQc(), 1.0f);
            TextView col2 = createTextView(DateTimeUtils.formatDate(data.getTgl()), 1.0f);
            TextView col3 = createTextView(data.getNamaJenisKayu(), 1.0f);
            TextView col4 = createTextView(data.getMeja(), 1.0f);


            row.addView(col1);
            row.addView(createDivider());

            row.addView(col2);
            row.addView(createDivider());

            row.addView(col3);
            row.addView(createDivider());

            row.addView(col4);

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
                    resetTextColor(selectedRow); // Kembalikan warna teks ke hitam
                }

                // Tandai baris yang baru dipilih
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.primary)); // Warna penandaan
                setTextColor(row, R.color.white); // Ubah warna teks menjadi putih
                selectedRow = row;

                // Simpan data yang dipilih
                selectedQcSawmillData = data;

                // Tangani aksi tambahan
                onRowClick(selectedQcSawmillData);
            });


            mainTable.addView(row);
            rowIndex++; // Tingkatkan indeks
        }

    }


    private void populateDetailQcSawmill(List<QcSawmillDetailData> detailQcSawmillDataList) {
        detailQcSawmillTable.removeAllViews(); // Menghapus tampilan sebelumnya

        int rowIndex = 0;

        if (detailQcSawmillDataList == null || detailQcSawmillDataList.isEmpty()) {
            TextView noDataView = new TextView(this);
            noDataView.setText("Tidak ada Data");
            noDataView.setGravity(Gravity.CENTER);
            noDataView.setPadding(16, 16, 16, 16);
            detailQcSawmillTable.addView(noDataView);
            return;
        }

        for (QcSawmillDetailData data : detailQcSawmillDataList) {
            TableRow row = new TableRow(this);
            row.setTag(data); // Tag diset ke objek QcSawmillDetailData, bukan Integer

            row.addView(createTextView(String.valueOf(data.getNoUrut()), 0.5f));
            row.addView(createDivider());

            row.addView(createTextView(df.format(data.getCuttingTebal()), 1.0f));
            row.addView(createDivider());

            row.addView(createTextView(df.format(data.getCuttingLebar()), 1.0f));
            row.addView(createDivider());

            row.addView(createTextView(df.format(data.getActualTebal()), 1.0f));
            row.addView(createDivider());

            row.addView(createTextView(df.format(data.getActualLebar()), 1.0f));
            row.addView(createDivider());

            row.addView(createTextView(df.format(data.getSusutTebal()), 1.0f));
            row.addView(createDivider());

            row.addView(createTextView(df.format(data.getSusutLebar()), 1.0f));
            row.addView(createDivider());

            // Warna latar belakang bergantian
            int bgColorRes = (rowIndex % 2 == 0) ? R.color.background_cream : R.color.white;
            row.setBackgroundColor(ContextCompat.getColor(this, bgColorRes));

            // Simpan koordinat sentuhan
            row.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    touchX = event.getRawX();
                    touchY = event.getRawY();
                }
                return false;
            });

            // Long click untuk seleksi & popup
            row.setOnLongClickListener(v -> {
                if (selectedRowDetail != null && selectedRowDetail != row) {
                    Object tag = selectedRowDetail.getTag();
                    if (tag instanceof QcSawmillDetailData) {
                        QcSawmillDetailData prevData = (QcSawmillDetailData) tag;
                        int prevIndex = detailQcSawmillDataList.indexOf(prevData); // pakai list yang sedang digunakan
                        int prevColorRes = (prevIndex % 2 == 0) ? R.color.background_cream : R.color.white;
                        selectedRowDetail.setBackgroundColor(ContextCompat.getColor(this, prevColorRes));
                        resetTextColor(selectedRowDetail);
                    }
                }

                row.setBackgroundResource(R.drawable.row_selector); // highlight baris
                setTextColor(row, R.color.white);
                selectedRowDetail = row;

                showRowPopupDetailAtTouch(v, data, noQcSawmill, touchX, touchY);
                return true;
            });

            detailQcSawmillTable.addView(row);
            rowIndex++;
        }
    }


    // Method untuk menampilkan popup di koordinat touch
    private void showRowPopupDetailAtTouch(View anchorView, QcSawmillDetailData data, String noQcSawmill, float touchX, float touchY) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_menu_row_sawmill_detail, null);

        PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);

        Button btnDelete = popupView.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(v -> {
            popupWindow.dismiss();
            confirmDeleteDetailQcSawmill(data, noQcSawmill);
        });



        Button btnEdit = popupView.findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(v -> {
            popupWindow.dismiss();
            showEditDetailDialog(data);
        });


        // Ukur ukuran popup
        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        int x = (int) touchX - (popupView.getMeasuredWidth());
        int y = (int) touchY - popupView.getMeasuredHeight() - 75;

        // Pastikan popup tidak keluar layar
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        if (x < 0) x = 10;
        if (x + popupView.getMeasuredWidth() > displayMetrics.widthPixels) {
            x = displayMetrics.widthPixels - popupView.getMeasuredWidth() - 10;
        }
        if (y < 0) {
            y = (int) touchY + 50;
        }

        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, x, y);

        popupWindow.setOnDismissListener(() -> {
            if (selectedRowDetail != null) {
                Object tag = selectedRowDetail.getTag();
                if (tag instanceof QcSawmillDetailData) {
                    QcSawmillDetailData previousData = (QcSawmillDetailData) tag;
                    int previousIndex = qcSawmillDetailDataList.indexOf(previousData);

                    int color = (previousIndex % 2 == 0) ? R.color.background_cream : R.color.white;
                    selectedRowDetail.setBackgroundColor(ContextCompat.getColor(this, color));
                    resetTextColor(selectedRowDetail);
                }
                selectedRowDetail = null;
            }
        });
    }

    private void confirmDeleteDetailQcSawmill(QcSawmillDetailData data, String noQcSawmill) {
        new AlertDialog.Builder(QcSawmill.this)
                .setTitle("Konfirmasi Hapus")
                .setMessage("Yakin ingin menghapus data detail ini?")
                .setPositiveButton("Ya", (dialogConfirm, which) -> {
                    executorService.execute(() -> {
                        try {
                            // Hapus data
                            SawmillApi.deleteQcSawmillDetail(noQcSawmill, data.getNoUrut());

                            // Ambil ulang data dan update NoUrut
                            List<QcSawmillDetailData> updatedList = SawmillApi.fetchQcSawmillDetailByNoQc(noQcSawmill);

                            // Refresh UI
                            runOnUiThread(() -> {
                                Toast.makeText(QcSawmill.this, "Detail berhasil dihapus", Toast.LENGTH_SHORT).show();
                                populateDetailQcSawmill(updatedList);
                            });
                        } catch (SQLException e) {
                            runOnUiThread(() -> Toast.makeText(QcSawmill.this, "Gagal menghapus: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                        }
                    });
                })
                .setNegativeButton("Batal", null)
                .show();
    }

    private void showEditDetailDialog(QcSawmillDetailData data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(QcSawmill.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_insert_detail_qc_sawmill, null);
        builder.setView(view);

        final EditText editCuttingTebal = view.findViewById(R.id.editCuttingTebal);
        final EditText editCuttingLebar = view.findViewById(R.id.editCuttingLebar);
        final EditText editActualTebal = view.findViewById(R.id.editActualTebal);
        final EditText editActualLebar = view.findViewById(R.id.editActualLebar);
        final EditText editSusutTebal = view.findViewById(R.id.editSusutTebal);
        final EditText editSusutLebar = view.findViewById(R.id.editSusutLebar);

        Button btnSimpan = view.findViewById(R.id.btnSimpan);
        ImageButton btnClose = view.findViewById(R.id.btnCloseDialogInputDetail);

        // Set nilai dari data yang akan diedit
        editCuttingTebal.setText(df.format(data.getCuttingTebal()));
        editCuttingLebar.setText(df.format(data.getCuttingLebar()));
        editActualTebal.setText(df.format(data.getActualTebal()));
        editActualLebar.setText(df.format(data.getActualLebar()));
        editSusutTebal.setText(df.format(data.getSusutTebal()));
        editSusutLebar.setText(df.format(data.getSusutLebar()));

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        btnClose.setOnClickListener(v -> dialog.dismiss());

        btnSimpan.setOnClickListener(v -> {
            String strCuttingTebal = editCuttingTebal.getText().toString().trim();
            String strCuttingLebar = editCuttingLebar.getText().toString().trim();
            String strActualTebal = editActualTebal.getText().toString().trim();
            String strActualLebar = editActualLebar.getText().toString().trim();
            String strSusutTebal = editSusutTebal.getText().toString().trim();
            String strSusutLebar = editSusutLebar.getText().toString().trim();

            boolean valid = true;

            if (strCuttingTebal.isEmpty()) {
                editCuttingTebal.setError("Wajib diisi");
                valid = false;
            }
            if (strCuttingLebar.isEmpty()) {
                editCuttingLebar.setError("Wajib diisi");
                valid = false;
            }
            if (strActualTebal.isEmpty()) {
                editActualTebal.setError("Wajib diisi");
                valid = false;
            }
            if (strActualLebar.isEmpty()) {
                editActualLebar.setError("Wajib diisi");
                valid = false;
            }
            if (strSusutTebal.isEmpty()) {
                editSusutTebal.setError("Wajib diisi");
                valid = false;
            }
            if (strSusutLebar.isEmpty()) {
                editSusutLebar.setError("Wajib diisi");
                valid = false;
            }

            if (!valid) return;

            float cuttingTebal = Float.parseFloat(strCuttingTebal);
            float cuttingLebar = Float.parseFloat(strCuttingLebar);
            float actualTebal = Float.parseFloat(strActualTebal);
            float actualLebar = Float.parseFloat(strActualLebar);
            float susutTebal = Float.parseFloat(strSusutTebal);
            float susutLebar = Float.parseFloat(strSusutLebar);

            executorService.execute(() -> {
                try {
                    // Method update yang perlu kamu implementasikan di SawmillApi
                    SawmillApi.updateQcSawmillDetail(
                            data.getNoQc(),
                            data.getNoUrut(),
                            cuttingTebal, cuttingLebar,
                            actualTebal, actualLebar,
                            susutTebal, susutLebar
                    );

                    qcSawmillDetailDataList = SawmillApi.fetchQcSawmillDetailByNoQc(data.getNoQc());

                    runOnUiThread(() -> {
                        Toast.makeText(QcSawmill.this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        populateDetailQcSawmill(qcSawmillDetailDataList);
                    });
                } catch (SQLException e) {
                    runOnUiThread(() -> Toast.makeText(QcSawmill.this, "Gagal update: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            });
        });

        dialog.show();
    }


    private void onRowClick(QcSawmillData data) {

        executorService.execute(() -> {
            // Ambil data latar belakang
            noQcSawmill = data.getNoQc();

            qcSawmillDetailDataList = SawmillApi.fetchQcSawmillDetailByNoQc(noQcSawmill);

            // Perbarui UI di thread utama
            runOnUiThread(() -> {
                populateDetailQcSawmill(qcSawmillDetailDataList);
            });
        });
    }

    // Tambahkan metode untuk membuat garis pembatas
    private View createDivider() {
        View divider = new View(this);
        divider.setBackgroundColor(Color.GRAY); // Warna garis pemisah

        // Set parameter untuk garis tipis (0.5dp)
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                1,
                TableRow.LayoutParams.MATCH_PARENT // Tinggi penuh
        );
        divider.setLayoutParams(params);

        return divider;
    }

    private TextView createTextView(String text, float weight) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(8, 15, 8, 15); // Padding untuk jarak
        textView.setGravity(Gravity.CENTER); // Pusatkan teks di tengah

        // Atur LayoutParams untuk mengatur lebar kolom berdasarkan weight
        textView.setLayoutParams(new TableRow.LayoutParams(
                0, // Lebar proporsional (diatur oleh weight)
                TableRow.LayoutParams.MATCH_PARENT, // Tinggi mengikuti konten
                weight // Berat untuk membagi lebar
        ));

        return textView;
    }

    private void setTextColor(TableRow row, int colorResId) {
        for (int i = 0; i < row.getChildCount(); i++) {
            View child = row.getChildAt(i);
            if (child instanceof TextView) {
                ((TextView) child).setTextColor(ContextCompat.getColor(this, colorResId));
            }
        }
    }

    private void resetTextColor(TableRow row) {
        for (int i = 0; i < row.getChildCount(); i++) {
            View child = row.getChildAt(i);
            if (child instanceof TextView) {
                ((TextView) child).setTextColor(ContextCompat.getColor(this, R.color.black)); // Kembalikan ke hitam
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }

}