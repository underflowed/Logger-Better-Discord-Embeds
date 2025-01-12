package me.prism3.loggerbungeecord.serverside;

import me.prism3.loggerbungeecord.Logger;
import me.prism3.loggerbungeecord.database.external.ExternalData;
import me.prism3.loggerbungeecord.database.sqlite.SQLiteData;
import me.prism3.loggerbungeecord.utils.Data;
import me.prism3.loggerbungeecord.utils.FileHandler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;

public class Start {

    private final Logger main = Logger.getInstance();

    public void run() {

        if (this.main.getConfig().getBoolean("Log-Server.Start")) {

            // Log To Files
            if (Data.isLogToFiles) {

                try {

                    final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getServerStartLogFile(), true));
                    out.write(this.main.getMessages().getString("Files.Server-Side.Start").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())) + "\n");
                    out.close();

                } catch (IOException e) {

                    Logger.getInstance().getLogger().severe("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!this.main.getMessages().getString("Discord.Server-Side.Start").isEmpty()) {

                this.main.getDiscord().serverStart(this.main.getMessages().getString("Discord.Server-Side.Start").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())), false);

            }

            // External
            if (Data.isExternal && this.main.getExternal().isConnected()) {

                try {

                    ExternalData.serverStart(Data.serverName);

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertServerStart(Data.serverName);

                } catch (Exception e) { e.printStackTrace(); }
            }
        }
        if (Data.isWhitelisted && Data.isBlacklisted) {

            this.main.getLogger().warning("Enabling both Whitelist and Blacklist isn't supported. " +
                    "Disable one of them to continue logging Player Commands.");

        }
    }
}
