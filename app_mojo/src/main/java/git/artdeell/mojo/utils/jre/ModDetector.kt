package net.kdt.pojavlaunch.utils.jre

import java.io.File

object ModDetector {

    @JvmStatic
    fun getMods(gameDir: File): Array<File>? {
        val modsDir = File(gameDir, "mods")
        return modsDir.listFiles { file ->
            file.isFile && file.name.endsWith(".jar")
        }
    }

    @JvmStatic
    fun hasSodium(gameDir: File): Boolean {
        return getMods(gameDir)?.any { file ->
            val name = file.name.lowercase()
            name.contains("sodium") ||
                name.contains("embeddium") ||
                name.contains("rubidium")
        } ?: false
    }

    @JvmStatic
    fun hasAngelica(gameDir: File): Boolean {
        return getMods(gameDir)?.any { file ->
            file.name.lowercase().contains("angelica")
        } ?: false
    }
}
