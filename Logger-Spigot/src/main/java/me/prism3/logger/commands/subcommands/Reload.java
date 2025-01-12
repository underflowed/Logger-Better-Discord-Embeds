package me.prism3.logger.commands.subcommands;

import me.prism3.logger.Logger;
import me.prism3.logger.commands.SubCommand;
import me.prism3.logger.utils.Data;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

import static me.prism3.logger.utils.Data.pluginPrefix;

public class Reload implements SubCommand {

    private final Logger main = Logger.getInstance();

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Reloads the plugin files";
    }

    @Override
    public String getSyntax() {
        return "/logger reload";
    }

    @Override
    public void perform(CommandSender commandSender, String[] args) {

        this.main.reloadConfig();
        this.main.getMessages().reload();
        this.main.getDiscordFile().reload();
        Data.initializer();
        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.main.getMessages().get().getString("General.Reload").replace("%prefix%", pluginPrefix)));
    }

    @Override
    public List<String> getSubCommandsArgs(CommandSender commandSender, String[] args) { return Collections.emptyList(); }
}
