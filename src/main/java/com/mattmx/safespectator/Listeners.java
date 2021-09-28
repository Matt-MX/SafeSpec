package com.mattmx.safespectator;

import com.mattmx.safespectator.utils.Utils;
import jdk.internal.joptsimple.internal.Strings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Listeners implements Listener {
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        Main.exitSpec(p);
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        String cmd = e.getMessage().toLowerCase();
        String[] args = cmd.split(" ");
        Player p = e.getPlayer();
        boolean cancel = !args[0].matches("^(^/s$)|(^/safespec$)|(^/spec$)$");
        if (Main.PLAYERS.containsKey(p) && cancel) {
            p.sendMessage(Utils.chat(Utils.format(Main.NOCMDMSG, p, " ")));
            e.setCancelled(true);
        }
    }
}
