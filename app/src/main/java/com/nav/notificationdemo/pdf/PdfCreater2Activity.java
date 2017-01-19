package com.nav.notificationdemo.pdf;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.nav.notificationdemo.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PdfCreater2Activity extends AppCompatActivity {

    /*
    IMPORTANT
    Files Required demo3.html, template_1.css, CustomWebView.java

    1. In CSS there is a new class '.all' and a div '<div class='all'>' needs to be added
    as root for printing requirement, please check the demo3.html file
    2. Use the custom Webview class as it returns the correct content size of the webview
    3. Only call print after the webview has been loaded
     */

    public static final int REQUEST_WRITE_PERMISSION = 19882;

    @BindView(R.id.myWebView)
    CustomWebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        IMPORTANT
        WebView.enableSlowWholeDocumentDraw() needs to be called before a Webview is created
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            WebView.enableSlowWholeDocumentDraw();
        }
        setContentView(R.layout.activity_pdf_creator);
        ButterKnife.bind(this);

        WebSettings webSettings = webView.getSettings();
        webSettings.setBuiltInZoomControls(true);

        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
        webView.loadUrl("file:///android_asset/html/demo3.html");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick(R.id.print2)
    public void createPdf2() {
        createWebPrintJob2(webView);
    }


    private void createWebPrintJob2(WebView webView) {

        // Get a PrintManager instance
        PrintManager printManager = (PrintManager)
                getSystemService(Context.PRINT_SERVICE);

        // Get a print adapter instance
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter();

        // Create a print job with name and adapter instance
        String jobName = getString(R.string.app_name) + " Document";
        printManager.print(jobName, printAdapter,
                new PrintAttributes.Builder().build());

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick(R.id.print)
    public void createPdf() {
        if (!checkForWritePermissions())
            return;

        PdfDocument document = createMultiPagePdfDocument(webView.getContentWidth(), webView.getContentHeight());

        try {
            File outputFile = createPdfFile();
            outputFile.createNewFile();
            OutputStream out = new FileOutputStream(outputFile);
            document.writeTo(out);
            document.close();
            out.close();
            Intent myIntent = new Intent(Intent.ACTION_VIEW);
            myIntent.setData(Uri.fromFile(outputFile));
            startActivity(myIntent);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private boolean checkForWritePermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_PERMISSION);
            return false;
        } else {
            return true;
        }
    }

    private File createPdfFile() throws IOException {
        /*
          Save file to wherever you want
        */
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String pdfFileName = "PDF_1";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        File pdf = File.createTempFile(
                pdfFileName,  /* prefix */
                ".pdf",         /* suffix */
                storageDir      /* directory */
        );

        return pdf;
    }

    private PdfDocument createMultiPagePdfDocument(int webViewWidth, int webViewHeight) {

    /* Find the Letter Size Height depending on the Letter Size Ratio and given Page Width */
        int letterSizeHeight = getLetterSizeHeight(webViewWidth);
        PrintAttributes.Margins margins = new PrintAttributes.Margins(0, 0, 0, 0);
        PrintAttributes pdfPrintAttrs = new PrintAttributes.Builder().
                setColorMode(PrintAttributes.COLOR_MODE_MONOCHROME).
                setMediaSize(PrintAttributes.MediaSize.NA_LETTER.asPortrait()).
                setResolution(new PrintAttributes.Resolution("zooey", PRINT_SERVICE, 72, 72)).
                setMinMargins(margins).
                build();

        PdfDocument document = new PrintedPdfDocument(this, pdfPrintAttrs);

        final int numberOfPages = (webViewHeight/letterSizeHeight) + 1;

        for (int i = 0; i < numberOfPages; i++) {

            int webMarginTop = i*letterSizeHeight;

            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(webViewWidth, letterSizeHeight, i+1).create();
            PdfDocument.Page page = document.startPage(pageInfo);

        /* Scale Canvas */
            page.getCanvas().translate(0, -webMarginTop);
            /*
            Although we should have used webView.draw(page.getCanvas());
            but there is a bug in Android so we need to use capturePicture
             */
            webView.capturePicture().draw(page.getCanvas());

            document.finishPage(page);
        }

        return document;
    }


    /**
     * Calculates the Letter Size Paper's Height depending on the LetterSize Dimensions and Given width.
     *
     * @param width
     * @return
     */
    private int getLetterSizeHeight(int width) {
        return (int)((float)(11.69*width)/8.27);
    }
}
