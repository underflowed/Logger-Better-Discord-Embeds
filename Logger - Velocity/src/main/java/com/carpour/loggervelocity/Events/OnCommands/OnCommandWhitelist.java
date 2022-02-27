package com.carpour.loggervelocity.Events.OnCommands;

import com.carpour.loggervelocity.Database.External.External;
import com.carpour.loggervelocity.Database.External.ExternalData;
import com.carpour.loggervelocity.Database.SQLite.SQLite;
import com.carpour.loggervelocity.Database.SQLite.SQLiteData;
import com.carpour.loggervelocity.Discord.Discord;
import com.carpour.loggervelocity.Main;
import com.carpour.loggervelocity.Utils.FileHandler;
import com.carpour.loggervelocity.Utils.Messages;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.proxy.Player;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import static com.carpour.loggervelocity.Utils.Data.*;

public class OnCommandWhitelist {

    @Subscribe
    public void onWhitelistedCommand(final CommandExecuteEvent event) {

        final Main main = Main.getInstance();
        final Messages messages = new Messages();
        final Player player = (Player) event.getCommandSource();

        if (main.getConfig().getBoolean("Log-Player.Commands") && player.getCurrentServer().isPresent()) {

            final String playerName = player.getUsername();
            final String command = event.getCommand().replace("\\", "\\\\");
            final String server = player.getCurrentServer().get().getServerInfo().getName();
            final List<String> commandParts = Arrays.asList(command.split("\\s+"));

            for (String m : commandsToLog) {

                if (commandParts.contains(m)) {

                    // Log To Files
                    if (isLogToFiles) {

                        if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                            if (!messages.getString("Discord.Player-Commands-Staff").isEmpty()) {

                                Discord.staffChat(player, messages.getString("Discord.Player-Commands-Staff").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%server%", server).replaceAll("%command%", command), false);

                            }

                            try {

                                final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffLogFile(), true));
                                out.write(messages.getString("Files.Player-Commands-Staff").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%server%", server).replaceAll("%player%", playerName).replaceAll("%command%", command) + "\n");
                                out.close();

                            } catch (IOException e) {

                                main.getLogger().error("An error occurred while logging into the appropriate file.");
                                e.printStackTrace();

                            }

                            if (isExternal && External.isConnected()) {

                                ExternalData.playerCommands(serverName, playerName, command, true);

                            }

                            if (isSqlite && SQLite.isConnected()) {

                                SQLiteData.insertPlayerCommands(serverName, playerName, command, true);

                            }
                            return;
                        }

                        try {

                            final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getPlayerCommandLogFile(), true));
                            out.write(messages.getString("Files.Player-Commands").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%server%", server).replaceAll("%player%", playerName).replaceAll("%command%", command) + "\n");
                            out.close();

                        } catch (IOException e) {

                            main.getLogger().error("An error occurred while logging into the appropriate file.");
                            e.printStackTrace();

                        }
                    }

                    // Discord
                    if (!player.hasPermission(loggerExemptDiscord)) {

                        if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                            if (!messages.getString("Discord.Player-Commands-Staff").isEmpty()) {

                                Discord.staffChat(player, messages.getString("Discord.Player-Commands-Staff").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%server%", server).replaceAll("%command%", command), false);

                            }
                        } else {

                            if (!messages.getString("Discord.Player-Commands").isEmpty()) {

                                Discord.playerCommands(player, messages.getString("Discord.Player-Commands").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%server%", server).replaceAll("%command%", command), false);

                            }
                        }
                    }

                    // External
                    if (isExternal && External.isConnected()) {

                        try {

                            ExternalData.playerCommands(serverName, playerName, command, player.hasPermission(loggerStaffLog));

                        } catch (Exception e) { e.printStackTrace(); }
                    }

                    // SQLite
                    if (isSqlite && SQLite.isConnected()) {

                        try {

                            SQLiteData.insertPlayerCommands(serverName, playerName, command, player.hasPermission(loggerStaffLog));

                        } catch (Exception e) { e.printStackTrace(); }
                    }
                }
            }
        }
    }
}
