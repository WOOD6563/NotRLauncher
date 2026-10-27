package me.wood.launch.game;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import git.artdeell.dnbootstrap.glfw.GLFW;
import git.artdeell.mojo.JVersionList;
import git.artdeell.mojo.utils.DateUtils;
import git.mojo.sdl.SDLActivity;

public final class FpsProvider {
    private static JVersionList.Version currentVersion;
    public static TextView currentFpsView;
    public static boolean getCurrentFps = false;
    private static final Handler fpsHandler = new Handler(Looper.getMainLooper());


    public static void setVersion(JVersionList.Version version) {
        currentVersion = version;
    }

    private static boolean isSdl(JVersionList.Version version) throws Exception {
        return !DateUtils.dateBefore(DateUtils.getOriginalReleaseDate(version), 2026, 7, 16);
    }

    public static int getFps() {
        if (currentVersion == null) return 0;
        try {
            return isSdl(currentVersion)
                    ? SDLActivity.initFps()
                    : GLFW.initFps();
        } catch (Exception e) {
            return 0;
        }
    }

    private static final Runnable fpsRunnable = new Runnable() {
        @Override
        public void run() {
            if (!getCurrentFps) return;
            int fps = getFps();
            currentFpsView.setText("FPS: " + fps);
            fpsHandler.postDelayed(this, 1000);
        }
    };

    public static void initCurrentFps() {
        if (!getCurrentFps) {
            getCurrentFps = true;
            currentFpsView.setVisibility(View.VISIBLE);
            fpsHandler.post(fpsRunnable);
        } else {
          getCurrentFps = false;
          fpsHandler.removeCallbacks(fpsRunnable);
          currentFpsView.setVisibility(View.GONE);
        }
    }
}
