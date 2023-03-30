package com.herronjo.chatpoi;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class ListPoisCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        PoiList poiList = new PoiList();
        HashMap<String, POI> POIs = poiList.getPOIs();
        ((Player) sender).sendMessage("POIs:");
        for (String name : POIs.keySet()) {
            POI poi = POIs.get(name);
            sender.sendMessage(name + ": " + poi.description + " (" + poi.world + " " + poi.x + ", " + poi.y + ", " + poi.z + ")");
        }
        return true;
    }
}
