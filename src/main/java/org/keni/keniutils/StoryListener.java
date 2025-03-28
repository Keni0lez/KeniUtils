package org.keni.keniutils;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

class StoryListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Environment currentEnvironment = player.getWorld().getEnvironment();
        switch (currentEnvironment) {
            case NETHER:
                player.setPlayerListName("§c" + player.getName());
                break;
            case THE_END:
                player.setPlayerListName("§5" + player.getName());
                break;
            default:
                player.setPlayerListName("§a" + player.getName());
                break;
        }

        String customWelcomeMessage = String.format("[§a+§f] §f%s", player.getName());
        Bukkit.getServer().broadcastMessage(customWelcomeMessage);
        event.setJoinMessage("");
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        String customQuitMessage = String.format("[§c-§f] %s", event.getPlayer().getName());
        Bukkit.getServer().broadcastMessage(customQuitMessage);
        event.setQuitMessage("");
    }
}
