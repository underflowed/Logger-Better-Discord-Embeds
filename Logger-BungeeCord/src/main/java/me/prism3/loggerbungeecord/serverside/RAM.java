package me.prism3.loggerbungeecord.serverside;

import me.prism3.loggerbungeecord.Logger;
import me.prism3.loggerbungeecord.database.external.ExternalData;
import me.prism3.loggerbungeecord.database.sqlite.SQLiteData;
import me.prism3.loggerbungeecord.utils.FileHandler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Objects;

import static me.prism3.loggerbungeecord.utils.Data.*;

public class RAM implements Runnable {

    private final Logger main = Logger.getInstance();

    public void run() {

        if (this.main.getConfig().getBoolean("Log-Server.RAM")) {

            if (ramPercent <= 0 || ramPercent >= 100) return;

            long maxMemory = Runtime.getRuntime().maxMemory() / 1048576L;
            long freeMemory = Runtime.getRuntime().freeMemory() / 1048576L;
            long usedMemory = maxMemory - freeMemory;
            double percentUsed = usedMemory * 100.0D / maxMemory;

            if (ramPercent <= percentUsed) {

                // Log To Files Handling
                if (isLogToFiles) {

                    try {

                        final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getRamLogFile(), true));
                        out.write(Objects.requireNonNull(this.main.getMessages().getString("Files.Server-Side.RAM")).replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%max%", String.valueOf(maxMemory)).replace("%used%", String.valueOf(usedMemory)).replace("%free%", String.valueOf(freeMemory)) + "\n");
                        out.close();

                    } catch (IOException e) {

                        Logger.getInstance().getLogger().severe("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }
                }

                // Discord
                if (!this.main.getMessages().getString("Discord.Server-Side.RAM").isEmpty()) {

                    this.main.getDiscord().ram(Objects.requireNonNull(this.main.getMessages().getString("Discord.Server-Side.RAM")).replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%max%", String.valueOf(maxMemory)).replace("%used%", String.valueOf(usedMemory)).replace("%free%", String.valueOf(freeMemory)), false);

                }

                // External
                if (isExternal && this.main.getExternal().isConnected()) {

                    try {

                        ExternalData.ram(serverName, maxMemory, usedMemory, freeMemory);

                    } catch (Exception e) { e.printStackTrace(); }
                }

                // SQLite
                if (isSqlite && this.main.getSqLite().isConnected()) {

                    try {

                        SQLiteData.insertRAM(serverName, maxMemory, usedMemory, freeMemory);

                    } catch (Exception e) { e.printStackTrace(); }
                }
            }
        }
    }
}
