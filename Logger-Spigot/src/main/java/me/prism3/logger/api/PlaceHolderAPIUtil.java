package me.prism3.logger.api;

import me.prism3.logger.Logger;
import org.bukkit.plugin.Plugin;

public class PlaceHolderAPIUtil {

    private PlaceHolderAPIUtil() {}

    public static Plugin getPlaceHolderAPI() {

        return Logger.getInstance().getServer().getPluginManager().getPlugin("PlaceholderAPI");
    }
}
