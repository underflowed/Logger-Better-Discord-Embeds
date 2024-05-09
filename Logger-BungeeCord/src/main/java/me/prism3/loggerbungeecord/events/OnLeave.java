package me.prism3.loggerbungeecord.events;

import me.prism3.loggerbungeecord.Logger;
import me.prism3.loggerbungeecord.database.external.ExternalData;
import me.prism3.loggerbungeecord.database.sqlite.SQLiteData;
import me.prism3.loggerbungeecord.utils.Data;
import me.prism3.loggerbungeecord.utils.FileHandler;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Objects;

public class OnLeave implements Listener {

    private final Logger main = Logger.getInstance();

    @EventHandler
    public void onQuit(final PlayerDisconnectEvent event) {

        if (this.main.getConfig().getBoolean("Log-Player.Leave")) {

            final ProxiedPlayer player = event.getPlayer();

            if (player.hasPermission(Data.loggerExempt)) return;

            String playerName = player.getName();

            // This resolves an error showing up if the targeted server is offline whist connecting
            if (player.getServer() == null) return;

            final String playerServerName = player.getServer().getInfo().getName();

            if (player.getServer() == null || playerServerName == null) return;

            // Log To Files
            if (Data.isLogToFiles) {

                if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                    if (!this.main.getMessages().getString("Discord.Player-Leave-Staff").isEmpty()) {

                        this.main.getDiscord().staffChat(player, Objects.requireNonNull(this.main.getMessages().getString("Discord.Player-Leave-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", playerServerName), false);

                    }

                    try {

                        final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffLogFile(), true));
                        out.write(this.main.getMessages().getString("Files.Player-Leave-Staff").replace("%server%", playerServerName).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%player%", playerName) + "\n");
                        out.close();

                    } catch (IOException e) {

                        Logger.getInstance().getLogger().severe("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (Data.isExternal && this.main.getExternal().isConnected()) {

                        ExternalData.playerLeave(Data.serverName, playerName, true);

                    }

                    if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                        SQLiteData.insertPlayerLeave(Data.serverName, playerName, true);

                    }

                    return;

                }

                try {

                    final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getLeaveLogFile(), true));
                    out.write(this.main.getMessages().getString("Files.Player-Leave").replace("%server%", playerServerName).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%player%", playerName) + "\n");
                    out.close();

                } catch (IOException e) {

                    Logger.getInstance().getLogger().severe("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord Integration
            if (!player.hasPermission(Data.loggerExemptDiscord)) {

                if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                    if (!this.main.getMessages().getString("Discord.Player-Leave-Staff").isEmpty()) {

                        this.main.getDiscord().staffChat(player, Objects.requireNonNull(this.main.getMessages().getString("Discord.Player-Leave-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", playerServerName), false);

                    }
                } else {

                    if (!this.main.getMessages().getString("Discord.Player-Leave").isEmpty()) {

                        this.main.getDiscord().playerLeave(player, Objects.requireNonNull(this.main.getMessages().getString("Discord.Player-Leave")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", playerServerName), false);

                    }
                }
            }

            // External
            if (Data.isExternal && this.main.getExternal().isConnected()) {

                try {

                    ExternalData.playerLeave(Data.serverName, playerName, player.hasPermission(Data.loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertPlayerLeave(Data.serverName, playerName, player.hasPermission(Data.loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }
}
