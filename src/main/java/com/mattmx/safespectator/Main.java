package com.mattmx.safespectator;

import com.mattmx.safespectator.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class Main extends JavaPlugin {

    static Main instance;
    public static Map<Player, Location> PLAYERS = new HashMap<>();
    public static String PREFIX = "&cSafe&aSpec";
    public static boolean ANNOUNCESPEC = true;
    public static String ANNOUNCEMSG = "%prefix% &f> &6%player% &chas %action% spectator.";
    public static String NOCMDMSG = "%prefix% &f> &cYou cant do this while in spectator.";
    public static int COOLDOWN = 0;
    public static Map<Player, Integer> COOLDOWNS = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        loadConfig();
        Bukkit.getServer().getPluginCommand("s").setExecutor(new Commands());
        Bukkit.getPluginManager().registerEvents(new Listeners(), this);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (Player p : COOLDOWNS.keySet()) {
                if (COOLDOWNS.get(p) == 0) {
                    COOLDOWNS.remove(p);
                } else {
                    int i = COOLDOWNS.get(p);
                    COOLDOWNS.remove(p);
                    COOLDOWNS.put(p, i - 1);
                }
            }
        }, 0L, 20L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static void loadConfig() {
        FileConfiguration c = getInstance().getConfig();
        PREFIX = c.getString("prefix");
        ANNOUNCESPEC = c.getBoolean("announce-spec");
        ANNOUNCEMSG = c.getString("announce-spec-msg");
        COOLDOWN = c.getInt("command-cooldown");
        NOCMDMSG = c.getString("anti-command-msg");
    }

    public static Main getInstance() {
        return instance;
    }

    public static void exitSpec(Player p) {
        if (PLAYERS.containsKey(p)) {
            p.teleport(PLAYERS.get(p));
            p.setGameMode(GameMode.SURVIVAL);
            PLAYERS.remove(p);
            if (ANNOUNCESPEC) Bukkit.broadcastMessage(Utils.chat(Utils.format(Main.ANNOUNCEMSG, p, "exited")));
        }
    }

    public static void enterSpec(Player p) {
        if (!PLAYERS.containsKey(p)) {
            p.setGameMode(GameMode.SPECTATOR);
            PLAYERS.put(p, p.getLocation());
            if (ANNOUNCESPEC) Bukkit.broadcastMessage(Utils.chat(Utils.format(Main.ANNOUNCEMSG, p, "entered")));
        }
    }

    public static void toggle(Player p) {
        if (PLAYERS.containsKey(p)) {
            exitSpec(p);
        } else {
            enterSpec(p);
        }
    }
}
