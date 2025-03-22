package org.keni.keniutils.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class kreload implements CommandExecutor {
    private final JavaPlugin plugin;

    public kreload(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("kreload")) {
            this.plugin.getServer().getPluginManager().disablePlugin((Plugin)this.plugin);
            this.plugin.getServer().getPluginManager().enablePlugin((Plugin)this.plugin);
            sender.sendMessage("yep");
            return true;
        }
        return false;
    }
}
