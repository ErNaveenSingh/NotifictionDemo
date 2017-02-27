package com.nav.notificationdemo.filechooser;

import android.os.Environment;
import android.support.annotation.Nullable;

import com.nononsenseapps.filepicker.AbstractFilePickerFragment;
import com.nononsenseapps.filepicker.FilePickerActivity;

import java.io.File;

public class MyFilePickerActivity extends FilePickerActivity {
    FilteredFilePickerFragment currentFragment;

    @Override
    protected AbstractFilePickerFragment<File> getFragment(
            @Nullable final String startPath, final int mode, final boolean allowMultiple,
            final boolean allowCreateDir, final boolean allowExistingFile,
            final boolean singleClick) {
        currentFragment = new FilteredFilePickerFragment();
        // startPath is allowed to be null. In that case, default folder should be SD-card and not "/"
        currentFragment.setArgs(startPath != null ? startPath : Environment.getExternalStorageDirectory().getPath(),
                mode, allowMultiple, allowCreateDir, allowExistingFile, singleClick);
        return currentFragment;
    }
    @Override
    public void onBackPressed() {
        // If at top most level, normal behaviour
        if (currentFragment==null || currentFragment.isBackTop()) {
            super.onBackPressed();
        } else {
            // Else go up
            currentFragment.goUp();
        }
    }
}


