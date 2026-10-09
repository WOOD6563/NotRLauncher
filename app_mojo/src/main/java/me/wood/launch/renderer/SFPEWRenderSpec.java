package me.wood.launch.renderer;

import android.content.Context;

import java.io.File;
import java.util.Map;

import git.artdeell.mojo.R;
import git.artdeell.mojo.Tools;
import git.artdeell.mojo.game.renderer.def.Renderers;
import git.artdeell.mojo.game.renderer.impl.GLESRenderSpec;
import git.artdeell.mojo.instances.Instance;
import git.artdeell.mojo.instances.Instances;
import git.artdeell.mojo.utils.jre.GameRunner;

public class SFPEWRenderSpec extends GLESRenderSpec {
    @Override
    public boolean compatibleDevice(Context context) {
        return new File(Tools.NATIVE_LIB_DIR, library()).exists();
    }

    @Override
    public String name() {
        return "SFPEW";
    }

    @Override
    public int displayName() {
        return R.string.mcl_setting_renderer_sfpew;
    }

    @Override
    public void setupEnvironment(Context context, Map<String, String> envMap) {
         Instance instance = Instances.loadSelectedInstance();
         File gamedir = instance.getGameDirectory();
         boolean hasAngelica = GameRunner.hasAngelica(gamedir);

        if (!hasAngelica) {
            envMap.put("SFPEW_EGL", "libmobileglues.so");
            envMap.put("MG_DIR_PATH", Tools.DIR_DATA + "/MobileGlues");
        }
        // If Angelica is present, don't set SFPEW_EGL (Angelica provides its own FPE)
    }

    @Override
    public String tag() {
        return Renderers.SFPEW_RENDERER;
    }

    @Override
    public String library() {
        return "libSimpleFPEWrapper.so";
    }

    @Override
    protected int glesVersion() {
        return 3;
    }
}
