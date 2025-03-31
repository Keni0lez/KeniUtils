package org.keni.keniutils.commands;

import org.bukkit.Sound;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class msg implements CommandExecutor, TabCompleter, Listener {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        if (args.length < 2) {
            player.sendMessage("Using: /msg <player> <message>");
            return true;
        }

        String targetPlayerName = args[0];
        Player targetPlayer = player.getServer().getPlayer(targetPlayerName);
        if (targetPlayer == null) {
            player.sendMessage("Player " + targetPlayerName + " not exist.");
            return true;
        }

        String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        player.sendMessage("§7[§x§3§4§B§D§D§1You -> §a" + targetPlayer.getName() + "§x§3§4§B§D§D§1]§f:§f " + message);
        targetPlayer.sendMessage("§7[§a" + player.getName() + "§x§3§4§B§D§D§1 -> You§7]§f:§f " + message);

        targetPlayer.playSound(targetPlayer.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            return Collections.emptyList();
        }

        if (args.length == 1) {
            return sender.getServer().getOnlinePlayers().stream()
                    .map(Player::getName)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
    }
}
