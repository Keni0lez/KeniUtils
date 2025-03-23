package org.keni.keniutils.events;

import org.bukkit.Bukkit;
import org.bukkit.World.Environment;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class tab implements Listener, CommandExecutor, TabCompleter {
    private final Plugin plugin;
    private final File file;
    private final FileConfiguration config;
    private final HashMap<UUID, Boolean> privateStatus = new HashMap<>();
    private final HashMap<UUID, Long> lastActivity = new HashMap<>();
    private final HashSet<UUID> afkPlayers = new HashSet<>();

    public tab(Plugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "private_status.yml");
        this.config = YamlConfiguration.loadConfiguration(file);
        loadPrivateStatus();
        startAfkChecker();
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        updatePlayerNameColor(event.getPlayer());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        lastActivity.put(player.getUniqueId(), System.currentTimeMillis());
        updatePlayerNameColor(player);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        lastActivity.put(player.getUniqueId(), System.currentTimeMillis());
        afkPlayers.remove(player.getUniqueId());
        updatePlayerNameColor(player);
        event.setFormat(player.getPlayerListName() + "§r: " + event.getMessage());
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (event.getFrom().distanceSquared(event.getTo()) > 0) {
            lastActivity.put(player.getUniqueId(), System.currentTimeMillis());
            afkPlayers.remove(player.getUniqueId());
            updatePlayerNameColor(player);
        }
    }

    private void updatePlayerNameColor(Player player) {
        UUID uuid = player.getUniqueId();
        boolean isPrivate = privateStatus.getOrDefault(uuid, false);
        boolean isAfk = afkPlayers.contains(uuid);
        String playerName = player.getName();
        String afkSymbol = isAfk ? " §7\uD83D\uDCA4" : "";

        if (isPrivate) {
            player.setPlayerListName("§7" + playerName + afkSymbol);
        } else {
            Environment env = player.getWorld().getEnvironment();
            if (env == Environment.NETHER) {
                player.setPlayerListName("§c" + playerName + afkSymbol);
            } else if (env == Environment.THE_END) {
                player.setPlayerListName("§5" + playerName + afkSymbol);
            } else {
                player.setPlayerListName("§a" + playerName + afkSymbol);
            }
        }
    }

    private void startAfkChecker() {
        new BukkitRunnable() {
            @Override
            public void run() {
                long now = System.currentTimeMillis();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    UUID uuid = player.getUniqueId();
                    long lastTime = lastActivity.getOrDefault(uuid, now);
                    if (now - lastTime >= 60_000) {
                        if (!afkPlayers.contains(uuid)) {
                            afkPlayers.add(uuid);
                            updatePlayerNameColor(player);
                        }
                    } else {
                        if (afkPlayers.contains(uuid)) {
                            afkPlayers.remove(uuid);
                            updatePlayerNameColor(player);
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 20L, 20L);
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