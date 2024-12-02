package me.prism3.logger.discord;

import org.bukkit.configuration.file.FileConfiguration;

public class DiscordEmbedImages {


    private final FileConfiguration discordFile;

    public DiscordEmbedImages(FileConfiguration discordFile) {
        this.discordFile = discordFile;
    }

    public String getUrl(String key) {
       String url = discordFile.getString("Discord."+key+".EmbedImage");
       System.out.println(url);
       return url;
    }
}