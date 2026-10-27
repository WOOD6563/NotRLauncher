package git.artdeell.mojo.prefs.screens;

import android.os.Bundle;

import git.artdeell.mojo.R;
import git.artdeell.mojo.Tools;
import git.artdeell.mojo.LauncherActivity;
import git.artdeell.mojo.prefs.LauncherPreferences;

public class LauncherPreferenceLauncherFragment extends LauncherPreferenceFragment {

    @Override
    public void onCreatePreferences(Bundle b, String str) {
        addPreferencesFromResource(R.xml.pref_launcher_settings);
        requirePreference("launcherIgnoreNotch").setOnPreferenceChangeListener((preference, newValue) -> {
            LauncherPreferences.PREF_LAUNCHER_IGNORE_NOTCH = (Boolean) newValue;
            LauncherActivity activity = getLauncherActivity();
            Tools.setInsetsMode(activity, activity.setFullscreen(), (Boolean) newValue);
            return true;
        });
    }
}
