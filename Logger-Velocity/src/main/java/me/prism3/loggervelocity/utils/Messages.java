package me.prism3.loggervelocity.utils;

import com.google.common.io.ByteStreams;
import me.prism3.loggervelocity.Logger;
import me.prism3.loggervelocity.utils.config.Configuration;
import me.prism3.loggervelocity.utils.config.ConfigurationProvider;
import me.prism3.loggervelocity.utils.config.StringUtils;
import me.prism3.loggervelocity.utils.config.YamlConfiguration;

import java.io.*;

public class Messages {

    private File file;
    private Configuration configuration;

    private final File dataFolder = Logger.getInstance().getFolder().toFile();

    public Messages() {

        this.file = new File(dataFolder, "/messages - Velocity.yml");

        try {

            if (!this.file.exists()) {

                if (!dataFolder.exists()) { dataFolder.mkdir(); }

                this.file.createNewFile();

                try (final InputStream is = ConfigManager.class.getResourceAsStream("/messages - Velocity.yml");
                     final OutputStream os = new FileOutputStream(this.file)) {
                    assert is != null;
                    ByteStreams.copy(is, os);
                }
            }
            this.configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.file);
        }
        catch (IOException e) { e.printStackTrace(); }
    }

    public void reload() { load(); }

    private void load() {

        this.file = new File(dataFolder, "/messages - Velocity.yml");

        try {

            this.configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.file);
        }

        catch (IOException e) { e.printStackTrace(); }
    }

    public File getFile() {
        return this.file;
    }

    public String getString(final String path) {

        if (this.configuration.get(path) != null) {

            return StringUtils.translateAlternateColorCodes('&', this.configuration.getString(path));

        } return "String at path: " + path + " not found!";
    }
}
