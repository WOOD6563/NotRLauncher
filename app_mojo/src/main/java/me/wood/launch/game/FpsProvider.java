package me.wood.launch.game;

import git.artdeell.dnbootstrap.glfw.GLFW;
import git.artdeell.mojo.JVersionList;
import git.artdeell.mojo.utils.DateUtils;
import git.mojo.sdl.SDLActivity;

public final class FpsProvider {
    private static JVersionList.Version currentVersion;

    public static void setVersion(JVersionList.Version version) {
        currentVersion = version;
    }

    private static boolean isSdl(JVersionList.Version version) throws Exception {
        return !DateUtils.dateBefore(DateUtils.getOriginalReleaseDate(version), 2026, 7, 16);
    }

    public static int getCurrentFps() {
        if (currentVersion == null) return 0;
        try {
            return isSdl(currentVersion)
                    ? SDLActivity.initFps()
                    : GLFW.initFps();
        } catch (Exception e) {
            return 0;
        }
    }
}
