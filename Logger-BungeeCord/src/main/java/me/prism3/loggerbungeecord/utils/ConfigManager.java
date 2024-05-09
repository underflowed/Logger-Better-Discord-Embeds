package me.prism3.loggerbungeecord.utils;

import com.google.common.io.ByteStreams;
import me.prism3.loggerbungeecord.Logger;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {

    private Configuration config = null;

    public void init() {

        this.saveDefaultConfig();

        try {

            this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(getFile());

        } catch (IOException e) {

            e.printStackTrace();

        }
    }

    public String getString(String key) {
        String str = config.getString(key);
        str = ChatColor.translateAlternateColorCodes('&', str);
        return str;
    }

    public int getInt(String key) { return this.config.getInt(key); }

    public boolean getBoolean(String key) {
        return this.config.getBoolean(key);
    }

    public List<String> getStringList(String key) {
        final List<String> list = this.config.getStringList(key);
        final List<String> avail = new ArrayList<>();
        for (String str : list)
            avail.add(ChatColor.translateAlternateColorCodes('&', str));
        return avail;
    }

    public File getFile() {
        return new File(Logger.getInstance().getDataFolder(), "config - Bungee.yml");
    }

    private void saveDefaultConfig() {

        if (!Logger.getInstance().getDataFolder().exists()) Logger.getInstance().getDataFolder().mkdir();

        final File file = getFile();

        if (!file.exists()) {

            try {

                file.createNewFile();

                try (InputStream is = Logger.getInstance().getResourceAsStream("config - Bungee.yml")) {

                    OutputStream os = new FileOutputStream(file);
                    ByteStreams.copy(is, os);
                    os.close();

                }
            } catch (IOException e) {

                e.printStackTrace();

            }
        }
    }
}
