package com.herronjo.chatpoi;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemovePoiCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        if (args.length < 1) {
            return false;
        }
        String name = String.join(" ", args);
        if (name.startsWith("\"")) {
            name = name.substring(1);
            if (name.endsWith("\"")) {
                name = name.substring(0, name.length() - 1);
            }
        }
        PoiList poiList = new PoiList();
        poiList.removePOI(name);
        ((Player) sender).sendMessage("POI removed.");
        return true;
    }
}
