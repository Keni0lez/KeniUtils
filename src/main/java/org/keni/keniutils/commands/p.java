package org.keni.keniutils.commands;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class p implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player)sender;
        if (args.length < 2) {
            player.sendMessage("Using: /p <player> <message>");
            return true;
        } else {
            String targetPlayerName = args[0];
            Player targetPlayer = player.getServer().getPlayer(targetPlayerName);
            if (targetPlayer == null) {
                player.sendMessage("Player " + targetPlayerName + " not exist.");
                return true;
            } else {
                String message = String.join(" ", args).substring(targetPlayerName.length() + 1);
                player.sendMessage("§b[You ➠ §a" + targetPlayerName + "§b]:§f " + message);
                String var10001 = player.getName();
                targetPlayer.sendMessage("§b[§a" + var10001 + "§b ➠ You]:§f " + message);
                targetPlayer.playSound(targetPlayer.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                return true;
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
    }
}