package com.example.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.helper.EpsonPrinterHelper;
import com.example.myapplication.model.LabelDetailData;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class TextPreviewActivity extends AppCompatActivity {

    private TextView tvPreview, tvQRText;
    private ImageView ivQRCode;
    private Button btnPrint, btnCancel;

    // Data dari Intent
    private String noST, jenisKayu, tglStickBundle, tellyBy, noSPK, stickBy, platTruk;
    private String noKayuBulat, namaSupplier, noTruk, jumlahPcs, m3, ton;
    private String remark, customer, noPenST, idUOMTblLebar, idUOMPanjang;
    private int printCount, isSLP, labelVersion;
    private List<LabelDetailData> detailData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_preview);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Preview Label");
        }

        // Init views
        tvPreview = findViewById(R.id.tvPreview);
        ivQRCode = findViewById(R.id.ivQRCode);
        tvQRText = findViewById(R.id.tvQRText);
        btnPrint = findViewById(R.id.btnPrint);
        btnCancel = findViewById(R.id.btnCancel);

        // Get data from intent
        receiveDataFromIntent();

        // Generate preview text
        String previewText = generatePreviewText();
        tvPreview.setText(previewText);

        // Generate QR Code
        generateAndDisplayQRCode();

        // Button listeners
        btnPrint.setOnClickListener(v -> showPrinterIPDialog());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void receiveDataFromIntent() {
        Intent intent = getIntent();
        noST = intent.getStringExtra("noST");
        jenisKayu = intent.getStringExtra("jenisKayu");
        tglStickBundle = intent.getStringExtra("tglStickBundle");
        tellyBy = intent.getStringExtra("tellyBy");
        noSPK = intent.getStringExtra("noSPK");
        stickBy = intent.getStringExtra("stickBy");
        platTruk = intent.getStringExtra("platTruk");
        noKayuBulat = intent.getStringExtra("noKayuBulat");
        namaSupplier = intent.getStringExtra("namaSupplier");
        noTruk = intent.getStringExtra("noTruk");
        jumlahPcs = intent.getStringExtra("jumlahPcs");
        m3 = intent.getStringExtra("m3");
        ton = intent.getStringExtra("ton");
        remark = intent.getStringExtra("remark");
        customer = intent.getStringExtra("customer");
        noPenST = intent.getStringExtra("noPenST");
        idUOMTblLebar = intent.getStringExtra("idUOMTblLebar");
        idUOMPanjang = intent.getStringExtra("idUOMPanjang");
        printCount = intent.getIntExtra("printCount", 0);
        isSLP = intent.getIntExtra("isSLP", 0);
        labelVersion = intent.getIntExtra("labelVersion", 0);

        // Receive ArrayList<LabelDetailData>
        detailData = (List<LabelDetailData>) intent.getSerializableExtra("detailData");

        // Safety check
        if (detailData == null) {
            detailData = new ArrayList<>();
            Toast.makeText(this, "⚠️ Data detail kosong", Toast.LENGTH_SHORT).show();
        }
    }

    private String generatePreviewText() {
        StringBuilder sb = new StringBuilder();

        // === HEADER ===
        String headerLabel = "";
        if (labelVersion == 1 || (noPenST != null && noPenST.startsWith("BA"))) {
            headerLabel = "LABEL ST (Pbl)";
        } else if (labelVersion == 2 || (noPenST != null && noPenST.startsWith("O"))) {
            headerLabel = "LABEL ST (Upah)";
        } else {
            headerLabel = "LABEL ST";
        }

        sb.append(center(headerLabel, 42)).append("\n");
        sb.append(center("==================", 42)).append("\n\n");

        // === NO ST ===
        sb.append("NO: ").append(noST).append("\n\n");

        // === WATERMARK COPY ===
        if (printCount > 0) {
            sb.append(center("*** COPY ***", 42)).append("\n\n");
        }

        // === INFO SECTION (2 KOLOM) ===
        sb.append(formatTwoColumns("Jenis", jenisKayu, "Tgl", tglStickBundle));
        sb.append(formatTwoColumns("Plat", platTruk, "Telly", tellyBy));
        sb.append(formatTwoColumns("SPK", noSPK, "Stick", stickBy));
        sb.append("\n");

        // === INFO TAMBAHAN ===
        if (labelVersion == 1 || (noPenST != null && noPenST.startsWith("BA"))) {
            sb.append("No. Pbl : ").append(noPenST).append("\n");
            sb.append("Supplier: ").append(truncate(namaSupplier, 32)).append("\n");
            sb.append("No. Truk: ").append(noTruk).append("\n");
        } else if (labelVersion == 2 || (noPenST != null && noPenST.startsWith("O"))) {
            sb.append("No. Upah: ").append(noPenST).append("\n");
            sb.append("Customer: ").append(truncate(customer, 32)).append("\n");
            sb.append("No. Truk: ").append(noTruk).append("\n");
        } else {
            sb.append("No. KB  : ").append(noKayuBulat).append("\n");
            sb.append("Supplier: ").append(truncate(namaSupplier, 32)).append("\n");
            sb.append("No. Truk: ").append(noTruk).append("\n");
        }
        sb.append("\n");

        // === SEPARATOR ===
        sb.append("==========================================\n");

        // === TABLE HEADER ===
        sb.append(String.format("%-9s %-9s %-10s %6s\n", "Tebal", "Lebar", "Panjang", "Pcs"));
        sb.append("------------------------------------------\n");

        // === TABLE DATA ===
        DecimalFormat df = new DecimalFormat("#,###.##");
        if (detailData != null && !detailData.isEmpty()) {
            for (LabelDetailData row : detailData) {
                try {
                    String tebal = row.getTebal() != null ? df.format(Float.parseFloat(row.getTebal())) : "-";
                    String lebar = row.getLebar() != null ? df.format(Float.parseFloat(row.getLebar())) : "-";
                    String panjang = row.getPanjang() != null ? df.format(Float.parseFloat(row.getPanjang())) : "-";
                    String pcs = row.getPcs() != null ? df.format(Integer.parseInt(row.getPcs())) : "-";

                    String tebalStr = tebal + " " + idUOMTblLebar;
                    String lebarStr = lebar + " " + idUOMTblLebar;
                    String panjangStr = panjang + " " + idUOMPanjang;

                    sb.append(String.format("%-9s %-9s %-10s %6s\n",
                            truncate(tebalStr, 9),
                            truncate(lebarStr, 9),
                            truncate(panjangStr, 10),
                            pcs));
                } catch (NumberFormatException e) {
                    sb.append(center("(Data error)", 42)).append("\n");
                }
            }
        } else {
            sb.append(center("(Tidak ada data)", 42)).append("\n");
        }

        // === SEPARATOR ===
        sb.append("==========================================\n");

        // === SUMMARY ===
        sb.append(String.format("%-20s: %s\n", "Jumlah", jumlahPcs));
        sb.append(String.format("%-20s: %s\n", "Ton", ton));
        sb.append(String.format("%-20s: %s\n", "m³", m3));
        sb.append("\n");

        // === REMARK ===
        if (remark != null && !remark.isEmpty() && !remark.equals("-")) {
            sb.append("Remark: ").append(wrapText(remark, 34)).append("\n\n");
        }

        // === LABEL SLP ===
        if (isSLP == 1) {
            sb.append(String.format("%42s\n", "SLP"));
            sb.append("\n");
        }

        // === SEPARATOR AKHIR ===
        sb.append(center("- - - - - - - - - - - - - - - -", 42)).append("\n");

        return sb.toString();
    }

    // === HELPER METHODS ===
    private String formatTwoColumns(String label1, String value1, String label2, String value2) {
        String col1 = String.format("%-6s: %-12s", label1, truncate(value1, 12));
        String col2 = String.format("%-5s: %s", label2, truncate(value2, 10));
        return col1 + " " + col2 + "\n";
    }

    private String truncate(String text, int maxLength) {
        if (text == null) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength - 3) + "...";
    }

    private String wrapText(String text, int width) {
        if (text == null || text.length() <= width) return text != null ? text : "";

        StringBuilder result = new StringBuilder();
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();

        for (String word : words) {
            if (line.length() + word.length() + 1 > width) {
                result.append(line.toString().trim()).append("\n         ");
                line = new StringBuilder();
            }
            line.append(word).append(" ");
        }
        result.append(line.toString().trim());

        return result.toString();
    }

    private String center(String text, int width) {
        if (text == null) return "";
        if (text.length() >= width) return text;
        int padding = (width - text.length()) / 2;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < padding; i++) sb.append(" ");
        sb.append(text);
        return sb.toString();
    }

    private void generateAndDisplayQRCode() {
        if (noST == null || noST.isEmpty()) {
            tvQRText.setText("No ST tidak tersedia");
            return;
        }

        try {
            Bitmap qrBitmap = generateQRCode(noST, 180); // 180px untuk paper simulation
            ivQRCode.setImageBitmap(qrBitmap);
            tvQRText.setText(noST);
        } catch (WriterException e) {
            e.printStackTrace();
            tvQRText.setText("Gagal generate QR Code");
            Toast.makeText(this, "Error generate QR Code: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private Bitmap generateQRCode(String text, int size) throws WriterException {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, size, size);

        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bitmap;
    }

    private void showPrinterIPDialog() {
        AlertDialog.Builder ipDialog = new AlertDialog.Builder(this);
        ipDialog.setTitle("Input IP Printer");

        final EditText inputIP = new EditText(this);
        inputIP.setHint("Contoh: 192.168.1.100");
        inputIP.setText("192.168.1.100");
        inputIP.setInputType(InputType.TYPE_CLASS_TEXT);
        ipDialog.setView(inputIP);

        ipDialog.setPositiveButton("Print", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String printerIP = inputIP.getText().toString().trim();

                if (printerIP.isEmpty()) {
                    Toast.makeText(TextPreviewActivity.this, "IP Printer tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    return;
                }

                String printerTarget = "TCP:" + printerIP;

                ProgressDialog progressDialog = new ProgressDialog(TextPreviewActivity.this);
                progressDialog.setTitle("Printing");
                progressDialog.setMessage("Menghubungkan ke printer...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                EpsonPrinterHelper printerHelper = new EpsonPrinterHelper(TextPreviewActivity.this);
                printerHelper.printDirectText(
                        noST, jenisKayu, tglStickBundle, tellyBy, noSPK, stickBy, platTruk,
                        detailData, noKayuBulat, namaSupplier, noTruk, jumlahPcs, m3, ton,
                        printCount, remark, isSLP, idUOMTblLebar, idUOMPanjang, noPenST,
                        labelVersion, customer, printerTarget,
                        new EpsonPrinterHelper.PrintCallback() {
                            @Override
                            public void onPrintSuccess() {
                                progressDialog.dismiss();
                                Toast.makeText(TextPreviewActivity.this, "✅ Print berhasil!", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void onPrintError(String error) {
                                progressDialog.dismiss();
                                Toast.makeText(TextPreviewActivity.this, "❌ Print gagal: " + error, Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onPrintProgress(String message) {
                                progressDialog.setMessage(message);
                            }
                        }
                );
            }
        });

        ipDialog.setNegativeButton("Batal", null);
        ipDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}