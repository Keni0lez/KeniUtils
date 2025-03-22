package org.keni.keniutils;


import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.keni.keniutils.commands.info;
import org.keni.keniutils.commands.kreload;
import org.keni.keniutils.commands.p;
import org.keni.keniutils.events.dimentions;
import org.keni.keniutils.events.PlayerDeathListener;
public final class KeniUtils extends JavaPlugin {
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        getServer().getPluginManager().registerEvents(new StoryListener(), (Plugin)this);
        getServer().getPluginManager().registerEvents((Listener)new dimentions(), (Plugin)this);
        getCommand("info").setExecutor((CommandExecutor)new info(this));
        getCommand("kreload").setExecutor((CommandExecutor)new kreload(this));
        getCommand("p").setExecutor((CommandExecutor)new p());
    }
}