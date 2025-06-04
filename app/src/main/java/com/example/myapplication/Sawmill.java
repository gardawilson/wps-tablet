package com.example.myapplication;

import static androidx.core.content.ContentProviderCompat.requireContext;
import static com.example.myapplication.api.LabelApi.insertDataRejectPembelian;
import static com.example.myapplication.api.SawmillApi.isHourRangeOverlapping;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.example.myapplication.api.SawmillApi;
import com.example.myapplication.model.OperatorData;
import com.example.myapplication.model.SawmillData;
import com.example.myapplication.model.SawmillDetailData;
import com.example.myapplication.model.SawmillDetailInputData;
import com.example.myapplication.model.SpecialConditionData;
import com.example.myapplication.model.KayuBulatData;
import com.example.myapplication.utils.DateTimeUtils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.app.AlertDialog;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.myapplication.api.LabelApi;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.util.Calendar;

import android.widget.AutoCompleteTextView;
import android.widget.ArrayAdapter;
import android.text.TextWatcher;
import android.text.Editable;


public class Sawmill extends AppCompatActivity {

    private TableLayout mainTable;
    private TableRow selectedRow;
    private List<SawmillData> dataList; // Data asli yang tidak difilter
    private SawmillData selectedSawmillData;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Button btnDataBaru;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sawmill);

        mainTable = findViewById(R.id.mainTable);
        btnDataBaru = findViewById(R.id.btnDataBaru);

        loadDataAndDisplayTable();

        btnDataBaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewDataDialog();
            }
        });

    }

    private void showDatePickerDialog(final EditText dateEditText) {
        // Mendapatkan tanggal hari ini
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Membuat DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(Sawmill.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Membuat Calendar baru untuk tanggal yang dipilih
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, monthOfYear, dayOfMonth);

                        // Format tanggal ke dd-MMM-yyyy (contoh: 11-Apr-2024)
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                        String formattedDate = sdf.format(selectedDate.getTime());

                        // Tampilkan tanggal ke EditText
                        dateEditText.setText(formattedDate);
                    }
                }, year, month, day);

        // Tampilkan DatePickerDialog
        datePickerDialog.show();
    }

    private void setTodayDateToEditText(final EditText dateEditText) {
        // Mendapatkan tanggal hari ini
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Membuat Calendar untuk tanggal hari ini
        calendar.set(year, month, day);

        // Format tanggal ke dd-MMM-yyyy (contoh: 11-Apr-2024)
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = sdf.format(calendar.getTime());

        // Tampilkan tanggal ke EditText
        dateEditText.setText(formattedDate);
    }


    private void loadSpecialConditionToSpinner(final Spinner spinSpecialCondition) {
        // List untuk menyimpan data SpecialCondition
        final List<SpecialConditionData> conditionList = new ArrayList<>();

        // Menambahkan pilihan default ("PILIH") dengan id null
        conditionList.add(new SpecialConditionData(null, "PILIH"));  // Tambahkan pilihan default

        // Mengambil data special condition dari database menggunakan ExecutorService
        executorService.execute(() -> {
            // Ambil data dari database via SawmillApi
            List<SpecialConditionData> conditions = SawmillApi.getSpecialConditionList();
            conditionList.addAll(conditions);  // Menambahkan data dari database

            // Perbarui Spinner di UI thread
            runOnUiThread(() -> {
                // Membuat ArrayAdapter untuk Spinner
                ArrayAdapter<SpecialConditionData> adapter = new ArrayAdapter<>(Sawmill.this, android.R.layout.simple_spinner_item, conditionList);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinSpecialCondition.setAdapter(adapter);
                spinSpecialCondition.setSelection(0);  // Tampilkan pilihan default pertama ("PILIH")
            });
        });

        spinSpecialCondition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                SpecialConditionData selectedCondition = (SpecialConditionData) spinSpecialCondition.getSelectedItem();

                int selectedId = -1;
                if (selectedCondition != null && selectedCondition.getIdSawmillSpecialCondition() != null) {
                    selectedId = selectedCondition.getIdSawmillSpecialCondition();
                }

                // Jika item yang dipilih valid, ubah background ke normal
                if (selectedId != -1) {
                    spinSpecialCondition.setBackgroundResource(R.drawable.border_input); // Ganti dengan background normal
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Opsional: tindakan saat tidak ada yang dipilih
            }
        });
    }


    private void loadOperatorToSpinner(final Spinner spinOperator) {
        final List<OperatorData> operatorList = new ArrayList<>();

        // Tambahkan pilihan default
        operatorList.add(new OperatorData(null, "PILIH"));

        executorService.execute(() -> {
            List<OperatorData> data = SawmillApi.getOperatorList();
            operatorList.addAll(data);

            runOnUiThread(() -> {
                ArrayAdapter<OperatorData> adapter = new ArrayAdapter<>(Sawmill.this, android.R.layout.simple_spinner_item, operatorList);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinOperator.setAdapter(adapter);
                spinOperator.setSelection(0);
            });
        });

        spinOperator.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                OperatorData selectedOperator = (OperatorData) spinOperator.getSelectedItem();
                int selectedId = -1;

                if (selectedOperator != null && selectedOperator.getIdOperator() != null) {
                    selectedId = selectedOperator.getIdOperator();
                }

                // Validasi atau efek UI jika perlu
                if (selectedId != -1) {
                    spinOperator.setBackgroundResource(R.drawable.border_input);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }


    private void loadShiftToSpinner(final Spinner spinShift) {
        final List<String> shiftList = new ArrayList<>();

        // Tambahkan pilihan default
        shiftList.add("PILIH");
        shiftList.add("1");
        shiftList.add("2");
        shiftList.add("3");

        runOnUiThread(() -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    Sawmill.this,
                    android.R.layout.simple_spinner_item,
                    shiftList
            );
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            spinShift.setAdapter(adapter);
            spinShift.setSelection(0);
        });

        spinShift.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedShift = (String) spinShift.getSelectedItem();

                // Validasi atau efek UI jika perlu
                if (!"PILIH".equals(selectedShift)) {
                    spinShift.setBackgroundResource(R.drawable.border_input);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }


    private void loadNoKayuBulatSuggestions(String input, ArrayAdapter<String> adapter, AutoCompleteTextView noKB) {
        executorService.execute(() -> {
            List<String> suggestions = SawmillApi.getNoKayuBulatSuggestions(input);
            runOnUiThread(() -> {
                adapter.clear();
                adapter.addAll(suggestions);
                adapter.notifyDataSetChanged();
                noKB.showDropDown();
            });
        });
    }

    private void hitungTotalJamKerja(EditText jamMulai, EditText jamBerhenti, TextView totalJamKerja) {
        String start = jamMulai.getText().toString();
        String end = jamBerhenti.getText().toString();

        if (start.isEmpty() || end.isEmpty()) {
            totalJamKerja.setText("");
            return;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date dateStart = sdf.parse(start);
            Date dateEnd = sdf.parse(end);

            long diff = dateEnd.getTime() - dateStart.getTime();

            if (diff < 0) {
                // Tambahkan 1 hari jika end lebih kecil dari start
                diff += 24 * 60 * 60 * 1000;
            }

            long diffHours = diff / (60 * 60 * 1000);
            long diffMinutes = (diff / (60 * 1000)) % 60;

            String result = String.format(Locale.getDefault(), "%d.%02d", diffHours, diffMinutes);
            totalJamKerja.setText(result);
        } catch (ParseException e) {
            e.printStackTrace();
            totalJamKerja.setText("-");
        }
    }


    private void setupTimePicker(EditText targetEditText, EditText jamMulai, EditText jamBerhenti, TextView totalJamKerja, EditText tanggal, EditText noMeja) {
        targetEditText.setInputType(InputType.TYPE_NULL);
        targetEditText.setFocusable(false);
        targetEditText.setClickable(true);

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        targetEditText.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    targetEditText.getContext(),
                    (view, hourOfDay, minute1) -> {
                        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute1);
                        targetEditText.setText(timeFormatted);

                        String tgl = tanggal.getText().toString().trim();
                        String meja = noMeja.getText().toString().trim();
                        String start = jamMulai.getText().toString().trim();
                        String end = jamBerhenti.getText().toString().trim();

                        if (!tgl.isEmpty() && !start.isEmpty() && !end.isEmpty()) {
                            executorService.execute(() -> {
                                boolean isOverlap = SawmillApi.isHourRangeOverlapping(tgl, meja, start, end);

                                runOnUiThread(() -> {
                                    if (isOverlap) {
                                        Toast.makeText(targetEditText.getContext(), "Rentang Jam tersebut telah digunakan!", Toast.LENGTH_SHORT).show();
                                        targetEditText.setText("");
                                        totalJamKerja.setText("");
                                    } else {
                                        hitungTotalJamKerja(jamMulai, jamBerhenti, totalJamKerja);
                                    }
                                });
                            });
                        } else {
                            hitungTotalJamKerja(jamMulai, jamBerhenti, totalJamKerja);
                        }
                    },
                    hour, minute, true
            );

            timePickerDialog.show();
        });
    }



    // Di dalam Activity atau Fragment
    private void showNewDataDialog() {
        // Membuat Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(Sawmill.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_insert_header_sawmill, null);
        builder.setView(view);

        // Menyiapkan EditText dan Spinner
        final AutoCompleteTextView noKB = view.findViewById(R.id.editNoKB);
        final EditText tanggal = view.findViewById(R.id.editTgl);
        final EditText jenisKayu = view.findViewById(R.id.editJenisKayu);
        final EditText noMeja = view.findViewById(R.id.editNoMeja);
        final Spinner spinShift = view.findViewById(R.id.spinShift);
        final EditText jamMulai = view.findViewById(R.id.editJamMulai);
        final EditText jamBerhenti = view.findViewById(R.id.editJamBerhenti);
        final TextView totalJamKerja = view.findViewById(R.id.tvTotalJamKerja);
        final EditText hourMeter = view.findViewById(R.id.editHourMeter);
        final EditText jlhBalokTerpakai = view.findViewById(R.id.editJlhBalokTerpakai);
        final EditText beratBalokTim = view.findViewById(R.id.editBeratBalokTim);
        final EditText remark = view.findViewById(R.id.editRemark);
        final EditText noTruk = view.findViewById(R.id.editNoTruk);
        final EditText noPlat = view.findViewById(R.id.editNoPlat);
        final EditText noSuket = view.findViewById(R.id.editSuket);
        final EditText supplier = view.findViewById(R.id.editSupplier);
        final EditText jlhBatang = view.findViewById(R.id.editJlhBatang);
        final Spinner spinSpecialCondition = view.findViewById(R.id.spinSpecialCondition);
        final Spinner spinOperator1 = view.findViewById(R.id.spinOperator1);
        final Spinner spinOperator2 = view.findViewById(R.id.spinOperator2);

        setupTimePicker(jamMulai, jamMulai, jamBerhenti, totalJamKerja, tanggal, noMeja);
        setupTimePicker(jamBerhenti, jamMulai, jamBerhenti, totalJamKerja, tanggal, noMeja);


        // Siapkan adapter suggestion Kayu Bulat
        ArrayAdapter<String> kbAdapter = new ArrayAdapter<>(Sawmill.this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<>());
        noKB.setAdapter(kbAdapter);

        // Set default prefix "A."
        noKB.setText("A.");
        noKB.setSelection(noKB.getText().length());

        noKB.addTextChangedListener(new TextWatcher() {
            boolean isEditing;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (isEditing) return;
                isEditing = true;

                String text = s.toString();

                // Pastikan prefix "A." selalu ada
                if (!text.startsWith("A.")) {
                    noKB.setText("A.");
                    noKB.setSelection(noKB.getText().length());
                    isEditing = false;
                    return;
                }

                // Ambil substring angka setelah prefix "A."
                String angka = "";
                if (text.length() > 2) {
                    angka = text.substring(2);
                }

                // Batasi input angka maksimal 6 digit (karena sisa 6 digit setelah "A.")
                if (angka.length() > 6) {
                    angka = angka.substring(0, 6);
                    noKB.setText("A." + angka);
                    noKB.setSelection(noKB.getText().length());
                }

                // Tampilkan suggestion jika minimal 4 digit angka sudah diinput
                if (angka.length() >= 4 && angka.length() < 6) {
                    // Kirim prefix + angka ke query suggestion
                    loadNoKayuBulatSuggestions("A." + angka, kbAdapter, noKB);
                    jenisKayu.setText("-");
                    supplier.setText("-");
                    noPlat.setText("-");
                    noTruk.setText("-");
                    noSuket.setText("-");

                    noKB.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    noKB.setBackgroundResource(R.drawable.border_input);

                }
                else if (angka.length() == 6) {
                    executorService.execute(() -> {
                        KayuBulatData data = SawmillApi.getKayuBulatDetail(text);
                        if (data != null) {
                            runOnUiThread(() -> {
                                // Isi ke field sesuai variabel
                                jenisKayu.setText(data.getJenis());
                                supplier.setText(data.getSupplier());
                                noPlat.setText(data.getNoPlat());
                                noTruk.setText(data.getNoTruk());
                                noSuket.setText(data.getSuket());

                                // Tampilkan icon ceklis di kanan AutoCompleteTextView
                                Drawable checkIcon = ContextCompat.getDrawable(view.getContext(), R.drawable.ic_done);
                                if (checkIcon != null) {
                                    checkIcon.setTint(Color.parseColor("#4CAF50"));
                                }
                                noKB.setCompoundDrawablesWithIntrinsicBounds(null, null, checkIcon, null);
                                noKB.setBackgroundResource(R.drawable.border_accepted);

                            });
                        }
                    });
                }
                else {
                    kbAdapter.clear();
                    kbAdapter.notifyDataSetChanged();
                }

                // Cursor tetap di akhir
                noKB.setSelection(noKB.getText().length());
                isEditing = false;
            }
        });

        // Mengambil tanggal hari ini
        setTodayDateToEditText(tanggal);

        // Deklarasikan objek dialog di luar listener
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        // Memanggil metode untuk memuat data ke Spinner
        loadSpecialConditionToSpinner(spinSpecialCondition);
        loadOperatorToSpinner(spinOperator1);
        loadOperatorToSpinner(spinOperator2);
        loadShiftToSpinner(spinShift);


        // Set OnClickListener untuk tglMasuk (datepicker)
        tanggal.setOnClickListener(v -> showDatePickerDialog(tanggal));

        // Menambahkan onClickListener untuk tombol tutup dialog
        ImageButton btnCloseDialogInputNoPST = view.findViewById(R.id.btnCloseDialogInputNoPST);
        btnCloseDialogInputNoPST.setOnClickListener(v -> {
            dialog.dismiss();  // Menutup DialogFragment
        });

        // Tombol Simpan
        Button btnSave = view.findViewById(R.id.btnSimpan);
        btnSave.setOnClickListener(v -> {
            // Ambil data dari EditText dan Spinner
            SpecialConditionData selectedSpecialCondition = (SpecialConditionData) spinSpecialCondition.getSelectedItem();
            OperatorData selectedOperator1 = (OperatorData) spinOperator1.getSelectedItem();
            OperatorData selectedOperator2 = (OperatorData) spinOperator2.getSelectedItem();
            String selectedShiftStr = (String) spinShift.getSelectedItem();

            final int idSpecialConditionVal = (selectedSpecialCondition != null && selectedSpecialCondition.getIdSawmillSpecialCondition() != null) ? selectedSpecialCondition.getIdSawmillSpecialCondition() : -1;
            final int idOperator1Val = (selectedOperator1 != null && selectedOperator1.getIdOperator() != null) ? selectedOperator1.getIdOperator() : -1;
            final int idOperator2Val = (selectedOperator2 != null && selectedOperator2.getIdOperator() != null) ? selectedOperator2.getIdOperator() : -1;
            final int shiftVal = (!selectedShiftStr.equals("PILIH")) ? Integer.parseInt(selectedShiftStr) : -1;

            String noKBVal = noKB.getText().toString();
            String tanggalVal = tanggal.getText().toString();
            String remarkVal = remark.getText().toString();
            String inputJlhBalokTerpakai = jlhBalokTerpakai.getText().toString().trim();
            int jlhBalokTerpakaiVal = inputJlhBalokTerpakai.isEmpty() ? 0 : Integer.parseInt(inputJlhBalokTerpakai);
            String noMejaVal = noMeja.getText().toString();
            String hourMeterVal = hourMeter.getText().toString();
            String jlhBatangVal = jlhBatang.getText().toString();

            String beratBalokTimVal = beratBalokTim.getText().toString();

            String jenisKayuVal = jenisKayu.getText().toString();

            String jamMulaiVal = jamMulai.getText().toString();
            String jamBerhentiVal = jamBerhenti.getText().toString();
            String totalJamKerjaVal = totalJamKerja.getText().toString();

            // Validasi: Cek apakah ada field yang kosong
            boolean valid = true;

            // Validasi Spinner untuk memastikan item yang dipilih bukan "PILIH"
            if (idSpecialConditionVal == -1) {  // Jika supplier yang dipilih adalah "PILIH"
                // Menambahkan error pada spinner dan memberi pesan kesalahan
                spinSpecialCondition.setBackgroundResource(R.drawable.spinner_error);  // Background merah untuk error
                valid = false;
            }

            if (idOperator1Val == -1) {
                // Menambahkan error pada spinner dan memberi pesan kesalahan
                spinOperator1.setBackgroundResource(R.drawable.spinner_error);  // Background merah untuk error
                valid = false;
            }

            if (shiftVal == -1) {
                // Menambahkan error pada spinner dan memberi pesan kesalahan
                spinShift.setBackgroundResource(R.drawable.spinner_error);  // Background merah untuk error
                valid = false;
            }

            if (jlhBalokTerpakai.getText().toString().trim().isEmpty()) {
                jlhBalokTerpakai.setError("Jumlah Balok Terpakai Harus Diisi!");
                valid = false;
            }

            if (noMejaVal.isEmpty()) {
                noMeja.setError("Ton SJ harus diisi");
                valid = false;
            }

            if (beratBalokTimVal.isEmpty()) {
                beratBalokTim.setError("Ton SJ harus diisi");
                valid = false;
            }

            // Jika semua field valid, lanjutkan proses
            if (valid) {
                // Menjalankan ExecutorService untuk mengambil nomor penerimaan baru dan menyimpan data
                executorService.execute(() -> {
                    // Ambil NoTellySawmill baru dari database ketika tombol simpan diklik
                    String newNoTellySawmill = SawmillApi.getNextNoTellySawmill();

                    // Periksa apakah NoTellySawmill berhasil diambil
                    if (newNoTellySawmill != null && !newNoTellySawmill.isEmpty()) {
                        try {
                            SawmillApi.insertSawmillData(
                                    newNoTellySawmill,
                                    shiftVal,
                                    tanggalVal,
                                    noKBVal,
                                    noMejaVal,
                                    idSpecialConditionVal,
                                    jlhBalokTerpakaiVal,
                                    jlhBatangVal,
                                    hourMeterVal,
                                    idOperator1Val,
                                    idOperator2Val,
                                    remarkVal,
                                    beratBalokTimVal,
                                    jenisKayuVal,
                                    jamMulaiVal,
                                    jamBerhentiVal,
                                    totalJamKerjaVal
                            );

                            // Setelah operasi selesai, update UI di main thread
                            runOnUiThread(() -> {
                                AlertDialog.Builder builderAlert = new AlertDialog.Builder(this);
                                View dialogView = getLayoutInflater().inflate(R.layout.alert_success, null);

                                TextView tvMessage = dialogView.findViewById(R.id.tvMessage);
                                Button btnOk = dialogView.findViewById(R.id.btnOk);

                                tvMessage.setText("Berhasil disimpan dengan No.Telly " + newNoTellySawmill);

                                builderAlert.setView(dialogView);
                                AlertDialog dialogSuccess = builderAlert.create();
                                dialogSuccess.setCancelable(false);

                                dialogSuccess.show(); // Tampilkan dulu agar window tersedia

                                // Buat sudut dialog kelihatan
                                dialogSuccess.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                // Atur ukuran dialog
                                dialogSuccess.getWindow().setLayout(
                                        1000,
                                        ViewGroup.LayoutParams.WRAP_CONTENT
                                );

                                btnOk.setOnClickListener(a -> {
                                    loadDataAndDisplayTable();
                                    dialogSuccess.dismiss();
                                    dialog.dismiss();  // tutup dialog lama kalau ada
                                });
                            });



                        } catch (Exception e) {
                            // Jika terjadi error, beri tahu pengguna di main thread
                            runOnUiThread(() -> {
                                Toast.makeText(Sawmill.this, "Tidak dapat menyimpan, " + e.getMessage(), Toast.LENGTH_LONG).show();
                            });
                        }

                    } else {
                        // Tangani kasus jika NoPenerimaanST gagal didapatkan
                        runOnUiThread(() -> {
                            Toast.makeText(Sawmill.this, "Gagal mengambil NoPenerimaanST", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();  // Menutup dialog jika terjadi error
                        });
                    }
                });
            } else {
                Toast.makeText(Sawmill.this, "Data belum lengkap!", Toast.LENGTH_SHORT).show();
            }
        });

        // Menampilkan Dialog
        dialog.show();
    }

    private void loadDataAndDisplayTable() {
        // Menjalankan operasi di background thread
        executorService.execute(() -> {
            dataList = SawmillApi.getSawmillData();

            // Menampilkan data di UI thread setelah selesai mengambil data
            runOnUiThread(() -> {
                populateTable(dataList);  // Memperbarui tampilan tabel dengan data terbaru
                // Sembunyikan loading indicator jika ada
                // loadingIndicator.setVisibility(View.GONE);
            });
        });
    }

    private void populateTable(List<SawmillData> dataList) {
        mainTable.removeAllViews();

        if (dataList == null || dataList.isEmpty()) {
            TextView noDataView = new TextView(this);
            noDataView.setText("Data tidak ditemukan");
            noDataView.setGravity(Gravity.CENTER);
            noDataView.setPadding(16, 16, 16, 16);
            mainTable.addView(noDataView);
            return;
        }

        int rowIndex = 0;

        for (SawmillData data : dataList) {
            TableRow row = new TableRow(this);
            row.setTag(rowIndex);

            TextView col1 = createTextView(data.getNoSTSawmill(), 1.0f);
            TextView col2 = createTextView(data.getTglSawmill(), 1.0f);
            TextView col3 = createTextView(data.getNoKayuBulat(), 1.0f);
            TextView col4 = createTextView(data.getNoMeja(), 1.0f);
            TextView col5 = createTextView(data.getOperator(), 1.0f);
            TextView col6 = createTextView(data.getBalokTerpakai(), 1.0f);
            TextView col7 = createTextView(String.valueOf(data.getJamKerja()), 1.0f);


            setDateToView(data.getTglSawmill(), col2);

            row.addView(col1);
            row.addView(createDivider());
            row.addView(col2);
            row.addView(createDivider());
            row.addView(col3);
            row.addView(createDivider());
            row.addView(col4);
            row.addView(createDivider());
            row.addView(col5);
            row.addView(createDivider());
            row.addView(col6);
            row.addView(createDivider());
            row.addView(col7);
            row.addView(createDivider());

            if (rowIndex % 2 == 0) {
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.background_cream));
            } else {
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            }

            row.setOnClickListener(v -> {
                if (selectedRow != null) {
                    int previousRowIndex = (int) selectedRow.getTag();
                    if (previousRowIndex % 2 == 0) {
                        selectedRow.setBackgroundColor(ContextCompat.getColor(this, R.color.background_cream));
                    } else {
                        selectedRow.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
                    }
                    resetTextColor(selectedRow);
                }

                row.setBackgroundColor(ContextCompat.getColor(this, R.color.primary));
                setTextColor(row, R.color.white);
                selectedRow = row;

                // Simpan data yang dipilih
                selectedSawmillData = data;

                // Tangani aksi tambahan
                onRowClick(data);
            });

            mainTable.addView(row);
            rowIndex++;
        }
    }

    private final DecimalFormat decimalFormat = new DecimalFormat("#.##");

    private void populateDetailTable(TableLayout tableLayout, List<SawmillDetailData> dataList) {
        tableLayout.removeAllViews();

        if (dataList == null || dataList.isEmpty()) {
            TextView noDataView = new TextView(this);
            noDataView.setText("Tidak ada data");
            noDataView.setGravity(Gravity.CENTER);
            noDataView.setPadding(16, 16, 16, 16);
            tableLayout.addView(noDataView);
            return;
        }

        int rowIndex = 0;

        for (SawmillDetailData data : dataList) {
            TableRow row = new TableRow(this);
            row.setTag(rowIndex);

            TextView col1 = createTextView(String.valueOf(data.getNoUrut()), 0.3f);
            TextView col2 = createTextView(decimalFormat.format(data.getTebal()), 1.0f);
            TextView col3 = createTextView(decimalFormat.format(data.getLebar()), 1.0f);
            TextView col4 = createTextView(decimalFormat.format(data.getPanjang()), 1.0f);
            TextView col5 = createTextView(String.valueOf(data.getPcs()), 1.0f);
            TextView col6 = createTextView(data.isLocal() ? "LOKAL / KAYU LAT" : "-", 1.0f);
//            TextView col7 = createTextView(String.valueOf(data.getIdUOMTblLebar()), 1.0f);
//            TextView col8 = createTextView(String.valueOf(data.getIdUOMPanjang()), 1.0f);
            TextView col9 = createTextView(data.getIsBagusKulitLabel(), 1.0f);

            row.addView(col1); row.addView(createDivider());
            row.addView(col2); row.addView(createDivider());
            row.addView(col3); row.addView(createDivider());
            row.addView(col4); row.addView(createDivider());
            row.addView(col5); row.addView(createDivider());
            row.addView(col6); row.addView(createDivider());
//            row.addView(col7); row.addView(createDivider());
//            row.addView(col8); row.addView(createDivider());
            row.addView(col9);

            if (rowIndex % 2 == 0) {
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.background_cream));
            } else {
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            }

            tableLayout.addView(row);
            rowIndex++;
        }
    }



    private void showDetailDialog(String noSTSawmill) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Sawmill.this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_detail_sawmill, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        EditText inputTebal = dialogView.findViewById(R.id.inputTebal);
        EditText inputLebar = dialogView.findViewById(R.id.inputLebar);
        Button addRowButton = dialogView.findViewById(R.id.addRowButton);
        Button btnSubmit = dialogView.findViewById(R.id.BtnInputDetail);
        RadioButton radioMillimeter = dialogView.findViewById(R.id.radioMillimeter);
        TableLayout detailSawmillTableLayout = dialogView.findViewById(R.id.detailSawmillTableLayout);
        TextView textJumlah = dialogView.findViewById(R.id.textJumlah);
        TextView textTon = dialogView.findViewById(R.id.textTon);
        TableLayout tablePjgPcs = dialogView.findViewById(R.id.TabelInputPjgPcs);

        List<SawmillDetailInputData> inputList = new ArrayList<>();

        addRowButton.setOnClickListener(v -> {
            TableRow newRow = new TableRow(dialogView.getContext());

            // Buat objek model untuk menyimpan input user
            SawmillDetailInputData inputData = new SawmillDetailInputData();

            // Kolom Panjang (Pjg)
            EditText panjangEditText = new EditText(this);
            panjangEditText.setHint("Pjg");
            panjangEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            TableRow.LayoutParams panjangParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
            panjangEditText.setLayoutParams(panjangParams);

            // Kolom Pcs
            EditText pcsEditText = new EditText(this);
            pcsEditText.setHint("Pcs");
            pcsEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
            pcsEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
            TableRow.LayoutParams pcsParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
            pcsEditText.setLayoutParams(pcsParams);

            // Kolom Delete
            ImageButton deleteButton = new ImageButton(this);
            deleteButton.setImageResource(R.drawable.ic_close);
            deleteButton.setContentDescription("Delete Button");
            deleteButton.setBackgroundColor(Color.TRANSPARENT);
            TableRow.LayoutParams deleteButtonParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.5f);
            deleteButton.setLayoutParams(deleteButtonParams);

            // ➕ Tambahkan TextWatcher ke EditText untuk simpan ke model
            panjangEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    inputData.setPanjang(s.toString());
                }
                @Override
                public void afterTextChanged(Editable s) {}
            });

            pcsEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    inputData.setPcs(s.toString());
                }
                @Override
                public void afterTextChanged(Editable s) {}
            });

            // ➖ Delete row dan hapus data dari list
            deleteButton.setOnClickListener(view -> {
                tablePjgPcs.removeView(newRow);
                inputList.remove(inputData);
            });

            // Tambahkan ke list model
            inputList.add(inputData);

            // Tambahkan ke TableRow dan TableLayout
            newRow.addView(panjangEditText);
            newRow.addView(pcsEditText);
            newRow.addView(deleteButton);
            tablePjgPcs.addView(newRow);
        });


        btnSubmit.setOnClickListener(v -> {
            if (inputList.isEmpty()) {
                Log.d("SubmitData", "Tidak ada data yang diinput");
            } else {
                for (int i = 0; i < inputList.size(); i++) {
                    SawmillDetailInputData data = inputList.get(i);
                    Log.d("SubmitData", "Baris ke-" + (i + 1) + ": Panjang = " + data.getPanjang() + ", Pcs = " + data.getPcs());
                }
            }
        });


        executorService.execute(() -> {
            List<SawmillDetailData> detailDataList = SawmillApi.fetchSawmillDetailData(noSTSawmill);
            float totalTon = SawmillApi.fetchTotalTon(noSTSawmill);
            int count = detailDataList.size(); // Hitung jumlah data

                    runOnUiThread(() -> {
                populateDetailTable(detailSawmillTableLayout, detailDataList);
                textJumlah.setText("Jumlah Batang : " + count);
                textTon.setText("Total Ton : " + String.valueOf(totalTon));

                if (count == 0) {
                    textJumlah.setVisibility(View.GONE);
                    textTon.setVisibility(View.GONE);
                }

            });
        });

        dialog.show();

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(
                    (int) (getResources().getDisplayMetrics().widthPixels * 0.8),
                    (int) (getResources().getDisplayMetrics().heightPixels * 0.9)            )
            ;
        }
    }

    private void onRowClick(SawmillData data) {

        executorService.execute(() -> {
            // Ambil data latar belakang
            String noSTSawmill = data.getNoSTSawmill();

            runOnUiThread(() -> {
                showDetailDialog(noSTSawmill);
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
                TableRow.LayoutParams.WRAP_CONTENT, // Tinggi mengikuti konten
                weight // Berat untuk membagi lebar
        ));

        return textView;
    }

    public void setDateToView(String tanggal, TextView tanggalView) {
        // Gunakan metode dari DateTimeUtils untuk memformat tanggal
        String formattedDate = DateTimeUtils.formatDate(tanggal);

        // Set tanggal terformat ke TextView
        tanggalView.setText(formattedDate);
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
}