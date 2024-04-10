package com.herronjo.chatpoi;

import java.util.TreeMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SearchPoisCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        PoiList poiList = new PoiList();
        TreeMap<String, POI> POIs = poiList.getPOIs();
        
        String query = String.join(" ", args);
        // Remove quotes if they were included to improve consistency with other ChatPOI commands
        if(query.length() > 1 && query.charAt(0) == '"' && query.charAt(query.length()-1) == '"') {
        	query = query.substring(1, query.length()-1);
        }
        boolean foundResult = false;
        
        for (String name : POIs.keySet()) {
            POI poi = POIs.get(name);
            if(name.contains(query) || poi.description.contains(query)) {
            	if(!foundResult) {
            		sender.sendMessage("POIs matching \""+query+"\":");
            		foundResult = true;
            	}
            	sender.sendMessage(name + ": " + poi.description + " (" + poi.world + " " + poi.x + ", " + poi.y + ", " + poi.z + ")");
            }
        }
        
        if(!foundResult) {
        	sender.sendMessage("No POIs matching \""+query+"\" found");
        }
        
        return true;
    }
}
