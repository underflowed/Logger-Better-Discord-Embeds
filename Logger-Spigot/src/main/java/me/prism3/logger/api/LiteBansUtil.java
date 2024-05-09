package me.prism3.logger.api;

import me.prism3.logger.Logger;
import org.bukkit.plugin.Plugin;

public class LiteBansUtil {

    private LiteBansUtil() {}

    public static Plugin getLiteBansAPI() {

        return Logger.getInstance().getServer().getPluginManager().getPlugin("LiteBans");
    }
}
