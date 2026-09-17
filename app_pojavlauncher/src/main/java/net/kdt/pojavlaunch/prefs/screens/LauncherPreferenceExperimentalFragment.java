package net.kdt.pojavlaunch.prefs.screens;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;

import net.kdt.pojavlaunch.LauncherActivity;
import net.kdt.pojavlaunch.Tools;
import net.kdt.pojavlaunch.extra.ExtraConstants;
import net.kdt.pojavlaunch.extra.ExtraCore;
import net.kdt.pojavlaunch.prefs.CustomSeekBarPreference;
import net.kdt.pojavlaunch.prefs.LauncherPreferences;
import net.kdt.pojavlaunch.utils.GLInfoUtils;
import net.kdt.pojavlaunch.utils.GpuUtils;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import git.artdeell.mojo.R;

public class LauncherPreferenceExperimentalFragment extends LauncherPreferenceFragment {

    private final ActivityResultLauncher<String[]> mBackgroundPickerLauncher = registerForActivityResult(
            new ActivityResultContracts.OpenDocument(), this::onBackgroundPicked
    );

    @Override
    public void onCreatePreferences(Bundle b, String str) {
        addPreferencesFromResource(R.xml.pref_experimental);
        SwitchPreference pref = requirePreference("freedrenoSysmem", SwitchPreference.class);
        boolean hasFreedreno = GpuUtils.getGlInfo().isAdreno();
        pref.setVisible(hasFreedreno);

        CustomSeekBarPreference pageOpacitySeekbar = requirePreference("pageOpacity", CustomSeekBarPreference.class);
        pageOpacitySeekbar.setSuffix("%");
        pageOpacitySeekbar.setValue(LauncherPreferences.PREF_PAGE_OPACITY);
        pageOpacitySeekbar.setOnLiveProgressChangeListener(progress ->
                ExtraCore.setValue(ExtraConstants.PAGE_OPACITY_CHANGED, progress));

        Preference selectBackgroundPreference = requirePreference("selectBackgroundImage");
        selectBackgroundPreference.setOnPreferenceClickListener(preference -> {
            mBackgroundPickerLauncher.launch(new String[]{"image/*"});
            return true;
        });

        Preference deleteBackgroundPreference = requirePreference("deleteBackgroundImage");
        deleteBackgroundPreference.setOnPreferenceClickListener(preference -> {
            deleteBackgroundImage();
            return true;
        });
    }

    private void onBackgroundPicked(Uri uri) {
        if(uri == null) return;
        File bgFile = new File(Tools.DIR_GAME_HOME, "launcher_background");
        try(InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);
            OutputStream outputStream = new FileOutputStream(bgFile)) {
            if(inputStream == null) throw new IOException("Failed to open the selected image");
            byte[] buffer = new byte[8192];
            int read;
            while((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
        } catch (IOException e) {
            Tools.showErrorRemote(e);
            return;
        }
        refreshBackground();
    }

    private void deleteBackgroundImage() {
        File bgFile = new File(Tools.DIR_GAME_HOME, "launcher_background");
        if(bgFile.exists() && bgFile.delete()) {
            Toast.makeText(requireContext(), R.string.background_deleted, Toast.LENGTH_SHORT).show();
            refreshBackground();
        } else if(!bgFile.exists()) {
            Toast.makeText(requireContext(), R.string.no_background_to_delete, Toast.LENGTH_SHORT).show();
        }
    }

    private void refreshBackground() {
        LauncherActivity launcherActivity = getLauncherActivity();
        LauncherActivity.loadBackground(launcherActivity);
        LauncherActivity.refreshPageOpacity(launcherActivity);
    }
}
