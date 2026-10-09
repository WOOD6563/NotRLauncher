package me.wood.launch.renderer;

import android.content.Context;

import me.wood.launch.renderer.MgRenderSpec;

import java.io.File;
import java.util.Map;

import git.artdeell.mojo.R;
import git.artdeell.mojo.Tools;
import git.artdeell.mojo.game.renderer.def.Renderers;
import git.artdeell.mojo.game.renderer.impl.GLESRenderSpec;
import git.artdeell.mojo.utils.JREUtils;

public class MgRenderSpec extends GLESRenderSpec {
    @Override
    public boolean compatibleDevice(Context context) {
        return JREUtils.getDetectedVersion() >= 3
                && new File(Tools.NATIVE_LIB_DIR, library()).exists();
    }

    @Override
    public String name() {
        return "MG-ES";
    }

    @Override
    public int displayName() {
        return R.string.mcl_setting_renderer_mobileglues;
    }

    @Override
    public void setupEnvironment(Context context, Map<String, String> envMap) {
        envMap.put("MG_DIR_PATH", Tools.DIR_DATA + "/MobileGlues");
    }

    @Override
    public String tag() {
        return Renderers.MOBILEGLUES_RENDERER;
    }

    @Override
    public String library() {
        return "libmobileglues.so";
    }

    @Override
    protected int glesVersion() {
        return 3;
    }
}
