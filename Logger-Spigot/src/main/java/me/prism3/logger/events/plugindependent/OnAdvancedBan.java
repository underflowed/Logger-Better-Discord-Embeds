package me.prism3.logger.events.plugindependent;

import me.leoko.advancedban.bukkit.event.PunishmentEvent;
import me.prism3.logger.Logger;
import me.prism3.logger.database.external.ExternalData;
import me.prism3.logger.database.sqlite.global.SQLiteData;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.Log;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Objects;

public class OnAdvancedBan implements Listener {

    private final Logger main = Logger.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPunishment(final PunishmentEvent event) {

        if (this.main.getConfig().getBoolean("Log-Extras.AdvancedBan")) {

            final String type = event.getPunishment().getType().toString();
            final String executor = event.getPunishment().getOperator();

            final Player player = Bukkit.getPlayer(executor);
            assert player != null;

            final String executed_on = event.getPunishment().getName();
            final String reason = event.getPunishment().getReason();
            final long expirationDate = event.getPunishment().getEnd();

            // Log To Files
            if (Data.isLogToFiles) {

                if (Data.isStaffEnabled) {
                    if (player.hasPermission(Data.loggerStaffLog)) {

                        if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Extras.AdvancedBan")).isEmpty()) {

                            this.main.getDiscord().staffChat(player, Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Extras.AdvancedBan")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%executor%", executor).replace("%executed_on%", executed_on).replace("%reason%", reason).replace("%expiration%", String.valueOf(expirationDate)).replace("%type%", type), false);

                        }

                        try {

                            BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true));
                            out.write(Objects.requireNonNull(this.main.getMessages().get().getString("Files.Extras.AdvancedBan")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%executor%", executor).replace("%executed_on%", executed_on).replace("%reason%", reason).replace("%expiration%", String.valueOf(expirationDate)).replace("%type%", type) + "\n");
                            out.close();

                        } catch (IOException e) {

                            Log.warning("An error occurred while logging into the appropriate file.");
                            e.printStackTrace();

                        }

                        if (Data.isExternal && this.main.getExternal().isConnected()) {

                            ExternalData.advancedBanData(Data.serverName, type, executor, executed_on, reason, expirationDate);

                        }

                        if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                            SQLiteData.insertAdvancedBan(Data.serverName, type, executor, executed_on, reason, expirationDate);

                        }

                        return;

                    }
                }

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getAdvancedBanFile(), true));
                    out.write(Objects.requireNonNull(this.main.getMessages().get().getString("Files.Extras.AdvancedBan")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%executor%", executor).replace("%executed_on%", executed_on).replace("%reason%", reason).replace("%expiration%", String.valueOf(expirationDate)).replace("%type%", type) + "\n");
                    out.close();

                } catch (IOException e) {

                    Log.warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord Integration
            if (player == null) { // This is essential when performed by the console

                if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Extras.AdvancedBan")).isEmpty()) {

                    this.main.getDiscord().advancedBan(Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Extras.AdvancedBan")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%executor%", executor).replace("%executed_on%", executed_on).replace("%reason%", reason).replace("%expiration%", String.valueOf(expirationDate)).replace("%type%", type), false);
                }
            } else if (!player.hasPermission(Data.loggerExemptDiscord)) {

                if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                    if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Extras.AdvancedBan")).isEmpty()) {

                        this.main.getDiscord().staffChat(player, Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Extras.AdvancedBan")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%executor%", executor).replace("%executed_on%", executed_on).replace("%reason%", reason).replace("%expiration%", String.valueOf(expirationDate)).replace("%type%", type), false);

                    }
                } else {
                    if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Extras.AdvancedBan")).isEmpty()) {

                        this.main.getDiscord().advancedBan(Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Extras.AdvancedBan")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%executor%", executor).replace("%executed_on%", executed_on).replace("%reason%", reason).replace("%expiration%", String.valueOf(expirationDate)).replace("%type%", type), false);
                    }
                }
            }

            // External
            if (Data.isExternal && this.main.getExternal().isConnected()) {

                try {

                    ExternalData.advancedBanData(Data.serverName, type, executor, executed_on, reason, expirationDate);

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertAdvancedBan(Data.serverName, type, executor, executed_on, reason, expirationDate);

                } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }
}
