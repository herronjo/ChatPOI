package com.herronjo.chatpoi;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RenamePoiCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        if (args.length < 2) {
            return false;
        }

        String oldName = args[0];
        String newName = args[1];
        int i = 0;

        if (args[0].startsWith("\"")) {
            oldName = args[0].substring(1);
            if (args[0].endsWith("\"")) {
                oldName = oldName.substring(0, oldName.length() - 1);
                i = 1;
            } else {
                i++;
                while (!args[i].endsWith("\"") && i < args.length) {
                    oldName += " " + args[i];
                    i++;
                }
                oldName += " " + args[i].substring(0, args[i].length() - 1);
                i++;
            }
        }

        if (i >= args.length) {
            return false;
        }

        if (args[i].startsWith("\"")) {
            newName = args[i].substring(1);
            if (args[i].endsWith("\"")) {
                newName = newName.substring(0, newName.length() - 1);
            } else {
                i++;
                while (!args[i].endsWith("\"") && i < args.length) {
                    newName += " " + args[i];
                    i++;
                }
                newName += " " + args[i].substring(0, args[i].length() - 1);
            }
        }

        PoiList poiList = new PoiList();
        poiList.renamePOI(oldName, newName);
        ((Player) sender).sendMessage("POI renamed.");
        return true;
    }
}
