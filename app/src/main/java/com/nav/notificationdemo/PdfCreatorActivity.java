package com.nav.notificationdemo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PdfCreatorActivity extends AppCompatActivity {

    public static final int REQUEST_WRITE_PERMISSION = 19882;

    @BindView(R.id.myWebView)
    CustomWebView webView;

    @BindView(R.id.myImageView)
    ImageView myImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            WebView.enableSlowWholeDocumentDraw();
        }
        setContentView(R.layout.activity_pdf_creator);
        ButterKnife.bind(this);

//        webView.loadUrl("file:///android_asset/html/demo.html");

        WebSettings webSettings = webView.getSettings();
        webSettings.setBuiltInZoomControls(true);

        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
//        webSettings.setUseWideViewPort(true);
        webView.loadUrl("file:///android_asset/html/demo2.html");

//        webView.loadDataWithBaseURL("file:///android_asset/html/", "demo.html", "text/html", "UTF-8", null);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick(R.id.print2)
    public void createPdf2() {
        createWebPrintJob2(webView);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick(R.id.print)
    public void createPdf() {
//        createWebPrintJob(webView, this);

        if (!checkForWritePermissions())
            return;
        int w = 1024, h = 1920;

        /*Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
        Bitmap bmp = Bitmap.createBitmap(w, h, conf);

        try {
            PrintDocumentAdapter adapter =  webView.createPrintDocumentAdapter("mydoc");

            AssetFileDescriptor fileDescriptor = getAssets().openFd("demo.html");
            PdfRenderer renderer = new PdfRenderer(fileDescriptor.getParcelFileDescriptor());
            final int pageCount = renderer.getPageCount();
            PdfRenderer.Page mpage = renderer.openPage(0);
            mpage.render(bmp, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            myImageView.setImageBitmap(bmp);

        } catch (IOException e) {
            e.printStackTrace();
        }

        PdfDocument document = new PdfDocument();
        View content = webView;//findViewById(R.id.activity_social_sign_in);
        // crate a page description
        int pageNumber = 1;
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(content.getWidth(),
                content.getHeight() - 20, pageNumber).create();
        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);

        // draw something on the page
        content.draw(page.getCanvas());

        // finish the page
        document.finishPage(page);
*/
        PdfDocument document = createMultiPagePdfDocument(webView.getContentWidth(), webView.getContentHeight());
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyhhmmss");

        try {
            File outputFile = createPdfFile();
            outputFile.createNewFile();
            OutputStream out = new FileOutputStream(outputFile);
            document.writeTo(out);
            document.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

        // Save the job object for later status checking

    }

    public void createWebPrintJob(WebView webView, Context context) {
        PrintManager printManager = (PrintManager) context
                .getSystemService(Context.PRINT_SERVICE);

        PrintDocumentAdapter pda = new PrintDocumentAdapter() {

            @Override
            public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {
                InputStream input = null;
                OutputStream output = null;

                try {

                    AssetFileDescriptor fileDescriptor = getAssets().openFd("html/demo2.html");
                    input = new FileInputStream(fileDescriptor.getFileDescriptor());

                    output = new FileOutputStream(destination.getFileDescriptor());

                    byte[] buf = new byte[1024];
                    int bytesRead;

                    while ((bytesRead = input.read(buf)) > 0) {
                        output.write(buf, 0, bytesRead);
                    }

                    callback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});

                } catch (FileNotFoundException ee) {
                    //Catch exception
                } catch (Exception e) {
                    //Catch exception
                } finally {
                    try {
                        if (input!=null)
                        input.close();
                        if (output!=null)
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {

                if (cancellationSignal.isCanceled()) {
                    callback.onLayoutCancelled();
                    return;
                }


                PrintDocumentInfo pdi = new PrintDocumentInfo.Builder("Name of file").setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT).build();

                callback.onLayoutFinished(pdi, true);
            }
        };

        String jobName = this.getString(R.string.app_name) + " Document";
        printManager.print(jobName, pda, null);

    }


    private File createPdfFile() throws IOException {
        // Create an image file name
//        String pdfName = "pdfdemo"
//                + sdf.format(Calendar.getInstance().getTime()) + ".pdf";

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String pdfFileName = "PDF_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        File pdf = File.createTempFile(
                pdfFileName,  /* prefix */
                ".pdf",         /* suffix */
                storageDir      /* directory */
        );

        return pdf;
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

    private PdfDocument createMultiPagePdfDocument(int webViewWidth, int webViewHeight) {

    /* Find the Letter Size Height depending on the Letter Size Ratio and given Page Width */
        int letterSizeHeight = getLetterSizeHeight(webViewWidth);
        PrintAttributes.Margins margins = new PrintAttributes.Margins(12, 12, 12, 12);
        PrintAttributes pdfPrintAttrs = new PrintAttributes.Builder().
                setColorMode(PrintAttributes.COLOR_MODE_MONOCHROME).
                setMediaSize(PrintAttributes.MediaSize.NA_LETTER.asPortrait()).
                setResolution(new PrintAttributes.Resolution("zooey", PRINT_SERVICE, 300, 300)).
                setMinMargins(margins).
                build();

        PdfDocument document = new PrintedPdfDocument(this, pdfPrintAttrs);

        final int numberOfPages = (webViewHeight/letterSizeHeight) + 1;

        for (int i = 0; i < numberOfPages; i++) {

            int webMarginTop = i*letterSizeHeight;

            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(webViewWidth, letterSizeHeight+20, i+1).create();
            PdfDocument.Page page = document.startPage(pageInfo);

        /* Scale Canvas */
            page.getCanvas().translate(0, -webMarginTop);
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
        return (int)((float)(11*width)/8.5);
    }
}
