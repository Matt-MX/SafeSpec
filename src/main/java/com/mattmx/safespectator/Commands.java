package com.mattmx.safespectator;

import com.mattmx.safespectator.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Commands implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Player-Only command.");
            return false;
        }
        Player p = (Player) sender;
        if (args.length > 0) {
            //////////////////////////////////////////////////////////////////////////////////
            if (args[0].equalsIgnoreCase("reload")) {
                if (!p.hasPermission("safespec.reload")) return Utils.noPerms(p);
                Main.getInstance().reloadConfig();
                Main.loadConfig();
                p.sendMessage(Utils.chat(Main.PREFIX + " &f> &cReloaded config.yml!"));
                //////////////////////////////////////////////////////////////////////////////
            } else if (args[0].equalsIgnoreCase("list")) {
                if (!p.hasPermission("safespec.list")) return Utils.noPerms(p);
                StringBuilder builder = new StringBuilder();
                for (Player pl : Main.PLAYERS.keySet()) {
                    builder.append(pl.getDisplayName() + "&f, &c");
                }
                p.sendMessage(Utils.chat(Main.PREFIX + " &f> &cPlayers: " + builder));
                //////////////////////////////////////////////////////////////////////////////
            } else {
                p.sendMessage(Utils.chat(Main.PREFIX + " &f> &cUnknown sub-command."));
            }
        } else {
            //////////////////////////////////////////////////////////////////////////////////
            if (!p.hasPermission("safespec.spec")) return Utils.noPerms(p);
            if (Main.COOLDOWNS.containsKey(p)) return Utils.waitMsg(p);
            Main.COOLDOWNS.put(p, Main.COOLDOWN);
            Main.toggle(p);
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!command.getName().startsWith("s")) return null;
        if (!(sender instanceof Player)) {
            return null;
        }
        Player p = (Player) sender;
        ArrayList<String> c = new ArrayList<>();
        if (args.length > 0) {
            if ("reload".startsWith(args[0]) && p.hasPermission("safespec.reload")) c.add("reload");
            if ("list".startsWith(args[0]) && p.hasPermission("safespec.list")) c.add("list");
            return c;
        }
        return null;
    }
}
