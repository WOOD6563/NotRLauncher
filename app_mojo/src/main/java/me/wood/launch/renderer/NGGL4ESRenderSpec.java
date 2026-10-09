package me.wood.launch.renderer;

import android.content.Context;

import java.io.File;
import java.util.Map;

import git.artdeell.mojo.R;
import git.artdeell.mojo.Tools;
import git.artdeell.mojo.game.renderer.def.Renderers;
import git.artdeell.mojo.game.renderer.impl.GLESRenderSpec;
import git.artdeell.mojo.utils.JREUtils;

public class NGGL4ESRenderSpec extends GLESRenderSpec {
    @Override
    public boolean compatibleDevice(Context context) {
        return JREUtils.getDetectedVersion() >= 3
                && new File(Tools.NATIVE_LIB_DIR, library()).exists();
    }

    @Override
    public String name() {
        return "NG-GL4ES";
    }

    @Override
    public int displayName() {
        return R.string.mcl_setting_renderer_nggl4es;
    }

    @Override
    public void setupEnvironment(Context context, Map<String, String> envMap) {
        envMap.put("LIBGL_USE_MC_COLOR", "1");
        envMap.put("LIBGL_GL", "31");
        envMap.put("LIBGL_ES", "3");
        envMap.put("LIBGL_NORMALIZE", "1");
        envMap.put("LIBGL_NOERROR", "1");
    }

    @Override
    public String tag() {
        return Renderers.NGGL4ES_RENDERER;
    }

    @Override
    public String library() {
        return "libng_gl4es.so";
    }

    @Override
    protected int glesVersion() {
        return 3;
    }
}
