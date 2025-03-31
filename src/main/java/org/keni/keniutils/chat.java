package org.keni.keniutils;

import org.bukkit.Bukkit;
import org.bukkit.World.Environment;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

class chat implements Listener {
    private File privateStatusFile;
    private FileConfiguration privateStatusConfig;

    public chat() {
        File pluginFolder = Bukkit.getServer().getPluginManager().getPlugin("KeniUtils").getDataFolder();
        if (!pluginFolder.exists()) {
            pluginFolder.mkdirs();
        }

        privateStatusFile = new File(pluginFolder, "private_status.yml");

        if (!privateStatusFile.exists()) {
            try {
                privateStatusFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        privateStatusConfig = YamlConfiguration.loadConfiguration(privateStatusFile);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.setPlayerListName(getColoredName(player));

        String customWelcomeMessage = String.format("[§a+§f] §f%s", player.getName());
        Bukkit.getServer().broadcastMessage(customWelcomeMessage);
        event.setJoinMessage("");
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);

        Player sender = event.getPlayer();
        String message = event.getMessage();

        TextComponent playerNameComponent = new TextComponent(getColoredName(sender));
        playerNameComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + sender.getName() + " "));

        String[] words = message.split(" ");

        for (Player player : Bukkit.getOnlinePlayers()) {
            TextComponent chatMessage = new TextComponent(ChatColor.GRAY + ": ");

            for (String word : words) {
                TextComponent textPart;
                Player mentionedPlayer = Bukkit.getPlayerExact(word);

                if (mentionedPlayer != null && mentionedPlayer.equals(player)) {
                    textPart = new TextComponent(ChatColor.AQUA + mentionedPlayer.getName() + ChatColor.RESET);
                } else {
                    textPart = new TextComponent(word);
                }

                chatMessage.addExtra(textPart);
                chatMessage.addExtra(" ");
            }

            TextComponent finalMessage = new TextComponent();
            finalMessage.addExtra(playerNameComponent);
            finalMessage.addExtra(chatMessage);

            player.spigot().sendMessage(finalMessage);
        }
    }

    private String getColoredName(Player player) {
        privateStatusConfig = YamlConfiguration.loadConfiguration(privateStatusFile); // Обновляем конфиг перед каждым вызовом
        UUID playerUUID = player.getUniqueId();
        boolean isPrivate = privateStatusConfig.getBoolean(playerUUID.toString(), false);

        if (isPrivate) {
            return ChatColor.GRAY + player.getName() + ChatColor.RESET;
        }

        ChatColor color;
        switch (player.getWorld().getEnvironment()) {
            case NORMAL:
                color = ChatColor.GREEN;
                break;
            case NETHER:
                color = ChatColor.RED;
                break;
            case THE_END:
                color = ChatColor.LIGHT_PURPLE;
                break;
            default:
                color = ChatColor.WHITE;
        }
        return color + player.getName() + ChatColor.RESET;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        String customQuitMessage = String.format("[§c-§f] %s", event.getPlayer().getName());
        Bukkit.getServer().broadcastMessage(customQuitMessage);
        event.setQuitMessage("");
    }
}
