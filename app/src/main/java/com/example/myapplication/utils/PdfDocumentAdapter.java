package com.example.myapplication.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PdfDocumentAdapter extends PrintDocumentAdapter {

    private Context context;
    private Uri pdfUri;

    public PdfDocumentAdapter(Context context, Uri pdfUri) {
        this.context = context;
        this.pdfUri = pdfUri;
    }

    @Override
    public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes,
                         CancellationSignal cancellationSignal,
                         LayoutResultCallback callback, Bundle extras) {

        if (cancellationSignal.isCanceled()) {
            callback.onLayoutCancelled();
            return;
        }

        PrintDocumentInfo.Builder builder = new PrintDocumentInfo.Builder("document.pdf");
        builder.setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                .setPageCount(PrintDocumentInfo.PAGE_COUNT_UNKNOWN)
                .build();

        callback.onLayoutFinished(builder.build(), !newAttributes.equals(oldAttributes));
    }

    @Override
    public void onWrite(PageRange[] pages, ParcelFileDescriptor destination,
                        CancellationSignal cancellationSignal,
                        WriteResultCallback callback) {

        InputStream input = null;
        OutputStream output = null;

        try {
            input = context.getContentResolver().openInputStream(pdfUri);
            output = new FileOutputStream(destination.getFileDescriptor());

            byte[] buf = new byte[8192];
            int bytesRead;

            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }

            callback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});

        } catch (Exception e) {
            callback.onWriteFailed(e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (input != null) input.close();
                if (output != null) output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}