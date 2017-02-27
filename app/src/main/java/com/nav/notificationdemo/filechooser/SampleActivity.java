package com.nav.notificationdemo.filechooser;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.nav.notificationdemo.R;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SampleActivity extends AppCompatActivity {

    /*
    Setup Notes
    1. Check the gradle file
    1. Check the Style file for FilePickerTheme
    2. Check the res/xml folder and file in that (replace your package name)
    3. Check the Manifest file for Activities
    4. Just start the FileChooserActivity and get the result in onActivityResult

    Additional
    1. Filters are available in FilteredFilePickerFragment
    */

    final int FileRequestCode = 1001;

    @BindView(R.id.resultTextView)
    TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.select_file_button)
    public void selectFile() {

        startActivityForResult(new Intent(this, FileChooserActivity.class), FileRequestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FileRequestCode && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            File file = com.nononsenseapps.filepicker.Utils.getFileForUri(uri);
            resultTextView.setText(file.getAbsolutePath());
            Toast.makeText(this, file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } else {
            resultTextView.setText("Didn't get the file");
            Toast.makeText(this, "Didn't get the file", Toast.LENGTH_SHORT).show();
        }
    }
}
