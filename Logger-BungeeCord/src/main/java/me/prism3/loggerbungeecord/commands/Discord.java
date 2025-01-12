package me.prism3.loggerbungeecord.commands;

import me.prism3.loggerbungeecord.Logger;
import me.prism3.loggerbungeecord.utils.Data;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import static me.prism3.loggerbungeecord.utils.Data.discordSupportServer;
import static me.prism3.loggerbungeecord.utils.Data.pluginVersion;

public class Discord extends Command {

    public Discord() { super("loggerb"); }

    @Override
    public void execute(CommandSender sender, String[] args) {

        final Logger main = Logger.getInstance();

        if (sender.hasPermission(Data.loggerStaff) || sender.hasPermission(Data.loggerReload)) {

            if (args.length == 0) {

                sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "Running Logger &b&l" + Data.pluginVersion)));

            } else if (args.length == 1 && args[0].equalsIgnoreCase("discord")) {

                sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', pluginVersion + discordSupportServer)));

            } else {

                sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("General.Invalid-Syntax").replace("%prefix%", pluginVersion))));

            }
        } else {

            sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("General.No-Permission").replace("%prefix%", pluginVersion))));

        }
    }
}
