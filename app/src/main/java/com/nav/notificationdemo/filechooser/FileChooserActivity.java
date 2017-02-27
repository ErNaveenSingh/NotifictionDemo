package com.nav.notificationdemo.filechooser;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nav.notificationdemo.R;
import com.nononsenseapps.filepicker.FilePickerActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;

public class FileChooserActivity extends AppCompatActivity {

    public static final String PARAM_FILE_PATH = "FilePath";
    private static final int MY_PERMISSIONS_STORAGE = 1998;

    public static final int SLIDES[] = {R.drawable.im1, R.drawable.im2, R.drawable.im3};
    public static final int TITLE[] = {R.string.slider_title_1, R.string.slider_title_2,
            R.string.slider_title_3};
    private static final int FILE_CODE = 2001;


    @BindView(R.id.file_chooser_viewPager)
    ViewPager viewPager;
    @BindView(R.id.indicator)
    CircleIndicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_chooser);
        ButterKnife.bind(this);
        viewPager.setAdapter(new ScreenSlidePagerAdapter(getSupportFragmentManager()));
        indicator.setViewPager(viewPager);
    }

    @OnClick(R.id.select_file_textView)
    public void selectFilePressed() {
        if (checkPermission())
            startPicker();
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_STORAGE);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    startPicker();
                } else {
                    Toast.makeText(this, "Permission required to show file picker", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == FILE_CODE && resultCode == Activity.RESULT_OK) {

            // The URI will now be something like content://PACKAGE-NAME/root/path/to/file
//            Uri uri = intent.getData();
            // A utility method is provided to transform the URI to a File object
//            File file = com.nononsenseapps.filepicker.Utils.getFileForUri(uri);
            Intent data = new Intent();
            data.setData(intent.getData());
            setResult(RESULT_OK, data);
            finish();
        }
    }

    void startPicker() {
        Intent i = new Intent(this, MyFilePickerActivity.class);

        i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
        i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);

        i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());

        startActivityForResult(i, FILE_CODE);
    }


    private static class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return SliderFragment.newInstance(SLIDES[position], position + 1, TITLE[position]);
        }

        @Override
        public int getCount() {
            return SLIDES.length;
        }
    }

    public static class SliderFragment extends Fragment {

        public static final String PARAM_IMAGE_ID = "ImageId";
        public static final String PARAM_NUMBER = "Number";
        public static final String PARAM_DESCRIPTION_ID = "DescriptionId";

        @BindView(R.id.page_back_imageView)
        ImageView imageView;
        @BindView(R.id.page_number_textView)
        TextView numberTextView;
        @BindView(R.id.page_title_textView)
        TextView descriptionTextView;

        public static SliderFragment newInstance(int imageId, int number, int descriptionId) {
            SliderFragment fragment = new SliderFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(PARAM_IMAGE_ID, imageId);
            bundle.putInt(PARAM_NUMBER, number);
            bundle.putInt(PARAM_DESCRIPTION_ID, descriptionId);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.view_file_chooser_pager, container, false);
            ButterKnife.bind(this, view);

            imageView.setImageResource(getArguments().getInt(PARAM_IMAGE_ID));
            numberTextView.setText(getArguments().getInt(PARAM_NUMBER) + "");
            descriptionTextView.setText(getArguments().getInt(PARAM_DESCRIPTION_ID));

            return view;
        }
    }
}
