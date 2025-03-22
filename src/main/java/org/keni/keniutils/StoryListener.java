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
        Environment currentEnvironment = event.getPlayer().getWorld().getEnvironment();
        String playerName = event.getPlayer().getName();
        if (currentEnvironment == Environment.NETHER) {
            event.getPlayer().setPlayerListName("§c" + playerName);
        } else if (currentEnvironment == Environment.THE_END) {
            event.getPlayer().setPlayerListName("§5" + playerName);
        } else {
            event.getPlayer().setPlayerListName("§a" + playerName);
        }

        Player player = event.getPlayer();
        String customWelcomeMessage = "§a[+] §f%player%";
        customWelcomeMessage = customWelcomeMessage.replace("%player%", event.getPlayer().getName());
        Bukkit.getServer().broadcastMessage(customWelcomeMessage);
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200, 1));
        player.playSound(player.getLocation(), Sound.BLOCK_ENDERCHEST_OPEN, 1.0F, 1.0F);
        event.setJoinMessage((String)null);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        String customQuitMessage = "§c[-] §f%player%";
        customQuitMessage = customQuitMessage.replace("%player%", event.getPlayer().getName());
        Bukkit.getServer().broadcastMessage(customQuitMessage);
        event.setQuitMessage((String)null);
    }
}