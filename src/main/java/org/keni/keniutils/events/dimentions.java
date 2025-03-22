package org.keni.keniutils.events;

import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class    dimentions implements Listener {
    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        Environment currentEnvironment = event.getPlayer().getWorld().getEnvironment();
        String playerName = event.getPlayer().getName();
        if (currentEnvironment == Environment.NETHER) {
            event.getPlayer().setPlayerListName("§4" + playerName);
        } else if (currentEnvironment == Environment.THE_END) {
            event.getPlayer().setPlayerListName("§5" + playerName);
        } else {
            event.getPlayer().setPlayerListName("§a" + playerName);
        }

    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        this.updatePlayerNameColor(player);
        String chatMessage = event.getMessage();
        String var10000 = player.getPlayerListName();
        String formattedMessage = var10000 + "§r: " + chatMessage;
        event.setFormat(formattedMessage);
    }

    private void updatePlayerNameColor(Player player) {
        Environment currentEnvironment = player.getWorld().getEnvironment();
        String playerName = player.getName();
        if (currentEnvironment == Environment.NETHER) {
            player.setPlayerListName("§4" + playerName);
        } else if (currentEnvironment == Environment.THE_END) {
            player.setPlayerListName("§5" + playerName);
        } else {
            player.setPlayerListName("§a" + playerName);
        }

    }
}