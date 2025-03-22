package org.keni.keniutils;


import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.keni.keniutils.commands.info;
import org.keni.keniutils.commands.kreload;
import org.keni.keniutils.commands.msg;
import org.keni.keniutils.events.dimentions;
import org.keni.keniutils.events.PlayerDeathListener;
public final class KeniUtils extends JavaPlugin {
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        getServer().getPluginManager().registerEvents(new StoryListener(), (Plugin)this);
        dimentions dimentions = new dimentions(this);
        getServer().getPluginManager().registerEvents(dimentions, this);
        getCommand("dimention").setExecutor(dimentions);
        getCommand("info").setExecutor((CommandExecutor)new info(this));
        getCommand("kreload").setExecutor(new kreload(this));
        this.getCommand("msg").setExecutor(new msg());
    }
}