package org.keni.keniutils.events;

import org.bukkit.Bukkit;
import org.bukkit.World.Environment;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class dimentions implements Listener, CommandExecutor, TabCompleter {
    private final Plugin plugin;
    private final File file;
    private final FileConfiguration config;
    private final HashMap<UUID, Boolean> privateStatus = new HashMap<>();

    public dimentions(Plugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "private_status.yml");
        this.config = YamlConfiguration.loadConfiguration(file);
        loadPrivateStatus();
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        updatePlayerNameColor(player);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        updatePlayerNameColor(player);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        updatePlayerNameColor(player);
        event.setFormat(player.getPlayerListName() + "§r: " + event.getMessage());
    }

    private void updatePlayerNameColor(Player player) {
        UUID uuid = player.getUniqueId();
        boolean isPrivate = privateStatus.getOrDefault(uuid, false);
        String playerName = player.getName();

        if (isPrivate) {
            player.setPlayerListName("§7" + playerName);
        } else {
            Environment env = player.getWorld().getEnvironment();
            if (env == Environment.NETHER) {
                player.setPlayerListName("§c" + playerName);
            } else if (env == Environment.THE_END) {
                player.setPlayerListName("§5" + playerName);
            } else {
                player.setPlayerListName("§a" + playerName);
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();

        if (args.length != 1) {
            player.sendMessage("§cUsage: /dimention <private/public>");
            return true;
        }

        if (args[0].equalsIgnoreCase("private")) {
            privateStatus.put(uuid, true);
            player.sendMessage("§7You have set private mode.");
        } else if (args[0].equalsIgnoreCase("public")) {
            privateStatus.put(uuid, false);
            player.sendMessage("§aYou have set public mode.");
        } else {
            player.sendMessage("§cUsage: /dimention <private/public>");
            return true;
        }

        savePrivateStatus();
        updatePlayerNameColor(player);
        return true;
    }

    private void loadPrivateStatus() {
        if (file.exists()) {
            for (String key : config.getKeys(false)) {
                privateStatus.put(UUID.fromString(key), config.getBoolean(key));
            }
        }
    }

    private void savePrivateStatus() {
        for (UUID uuid : privateStatus.keySet()) {
            config.set(uuid.toString(), privateStatus.get(uuid));
        }
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("private", "public");
        }
        return Collections.emptyList();
    }
}
