package me.wood.launch.game;

import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
    private static float downX, downY, startX, startY;


    public static void setVersion(JVersionList.Version version) {
        currentVersion = version;
    }

    public static void enableDragging() {
        currentFpsView.setOnTouchListener((view, event) -> {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                downX = event.getRawX();
                downY = event.getRawY();
                startX = view.getX();
                startY = view.getY();
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                view.setX(Math.max(0, Math.min(parent.getWidth() - view.getWidth(),
                        startX + event.getRawX() - downX)));
                view.setY(Math.max(0, Math.min(parent.getHeight() - view.getHeight(),
                        startY + event.getRawY() - downY)));
            }
            return true;
        });
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
