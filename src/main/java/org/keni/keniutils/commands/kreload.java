package org.keni.keniutils.commands;

import org.bukkit.Bukkit;
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

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("kreload")) {
            return false;
        }

        if (!sender.hasPermission("keniutils.reload")) {
            sender.sendMessage("§cYou don't have permission!");
            return true;
        }

        sender.sendMessage("§eReloading plugin...");

        String pluginName = plugin.getName();
        Plugin reloadingPlugin = Bukkit.getPluginManager().getPlugin(pluginName);

        if (reloadingPlugin != null) {
            Bukkit.getPluginManager().disablePlugin(reloadingPlugin);
            Bukkit.getPluginManager().enablePlugin(reloadingPlugin);
            sender.sendMessage("§aPlugin successfully reloaded!");
        } else {
            sender.sendMessage("§cPlugin not found!");
        }

        return true;
    }
}
