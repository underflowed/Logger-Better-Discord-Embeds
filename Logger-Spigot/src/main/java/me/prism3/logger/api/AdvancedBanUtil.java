package me.prism3.logger.api;

import me.prism3.logger.Logger;
import org.bukkit.plugin.Plugin;

public class AdvancedBanUtil {

    private AdvancedBanUtil() {}

    public static Plugin getAdvancedBanAPI() {
        return Logger.getInstance().getServer().getPluginManager().getPlugin("AdvancedBan");
    }
}
