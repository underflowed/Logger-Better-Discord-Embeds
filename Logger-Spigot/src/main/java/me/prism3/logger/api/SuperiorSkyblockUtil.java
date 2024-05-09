package me.prism3.logger.api;

import me.prism3.logger.Logger;
import org.bukkit.plugin.Plugin;

public class SuperiorSkyblockUtil {

    private SuperiorSkyblockUtil() {}

    public static Plugin getSuperiorSkyblockAPI() {

        return Logger.getInstance().getServer().getPluginManager().getPlugin("SuperiorSkyblock");
    }
}
