package com.herronjo.chatpoi;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddPoiCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        if (args.length < 2) {
            return false;
        }

        String name = args[0];
        String description = args[1];
        int i = 0;

        if (args[0].startsWith("\"")) {
            name = args[0].substring(1);
            if (args[0].endsWith("\"")) {
                name = name.substring(0, name.length() - 1);
                i = 1;
            } else {
                i++;
                while (!args[i].endsWith("\"") && i < args.length) {
                    name += " " + args[i];
                    i++;
                }
                name += " " + args[i].substring(0, args[i].length() - 1);
                i++;
            }
        }

        if (i >= args.length) {
            return false;
        }

        if (args[i].startsWith("\"")) {
            description = args[i].substring(1);
            if (args[i].endsWith("\"")) {
                description = description.substring(0, description.length() - 1);
            } else {
                i++;
                while (!args[i].endsWith("\"") && i < args.length) {
                    description += " " + args[i];
                    i++;
                }
                description += " " + args[i].substring(0, args[i].length() - 1);
            }
        }

        Player player = (Player) sender;
        PoiList poiList = new PoiList();
        int x = player.getLocation().getBlockX();
        int y = player.getLocation().getBlockY();
        int z = player.getLocation().getBlockZ();
        poiList.addPOI(name, description, x, y, z);
        player.sendMessage("Added POI");
        return true;
    }
}
