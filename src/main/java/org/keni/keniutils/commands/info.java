package org.keni.keniutils.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class info implements CommandExecutor {
    private final JavaPlugin plugin;

    public info(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("info")) {
            if (sender instanceof Player) {
                Player player = (Player)sender;
                player.sendMessage("§bServer Information:");
                player.sendMessage(" ");
                player.sendMessage("§eServer Version:§b " + this.plugin.getServer().getVersion());
                player.sendMessage("§eMaximum Players:§b " + this.plugin.getServer().getMaxPlayers());
                int var10001 = this.plugin.getServer().getOnlinePlayers().size();
                player.sendMessage("§eOnline Players:§b " + var10001 + "/" + this.plugin.getServer().getMaxPlayers());
                player.sendMessage("§eServer MOTD:§b " + this.plugin.getServer().getMotd());
                player.sendMessage("§eServer IP:§b " + this.plugin.getServer().getIp());
                player.sendMessage("§eServer is running on platform:§b " + this.plugin.getServer().getBukkitVersion());
                player.sendMessage("§eMinecraft Server Version:§b " + this.plugin.getServer().getVersion());
                player.sendMessage("§eServer is running on Java:§b " + System.getProperty("java.version"));
                player.sendMessage("§eServer Operating System:§b " + System.getProperty("os.name"));
                player.sendMessage("§eNumber of Loaded Plugins:§b" + this.plugin.getServer().getPluginManager().getPlugins().length);
            } else {
                sender.sendMessage("Only players can use the /info command.");
            }

            return true;
        } else {
            return false;
        }
    }
}
