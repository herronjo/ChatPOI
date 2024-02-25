package com.herronjo.chatpoi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListPoisCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        PoiList poiList = new PoiList();
        TreeMap<String, POI> POIs = poiList.getPOIs();
        
        String order = "alpha";        
        if(args.length > 0) {
        	order = args[0];
        }
        
        Collection<String> orderedNames;
        switch(order) {
        	case "alpha", "alphabetical", "alphanumeric":
        		orderedNames = POIs.keySet();
        	break;
        	case "distance", "dist":
        		List<String> names = new ArrayList<String>(POIs.keySet());
        		Player player = (Player) sender;
        		Location playerLocation = player.getLocation();
        		String world = player.getWorld().getName();
        		
        		// Sort POIs in reverse order - farthest first
        		// That way the closest POIs appear in the bottom lines of chat rather than having to scroll way up
        		names.sort(new Comparator<String>() {
        			@Override
					public int compare(String a, String b) {
        				POI poiA = POIs.get(a);
        				POI poiB = POIs.get(b);
        				if(a == null || b == null) throw new AssertionError("Attempted to compare non-existent POI");
        				// Sort first by world
        				if(poiA.world.equals(world) && !poiB.world.equals(world)) return 1;
        				if(!poiA.world.equals(world) && poiB.world.equals(world)) return -1;
        				if(!poiA.world.equals(world) && !poiB.world.equals(world) && !poiA.world.equals(poiB.world)) return -poiA.world.compareTo(poiB.world);
        				// The two POIs are in the same world, sort by distance
        				double distA = Math.pow(playerLocation.getX()-poiA.x, 2)+Math.pow(playerLocation.getY()-poiA.y, 2)+Math.pow(playerLocation.getZ()-poiA.z, 2);
        				double distB = Math.pow(playerLocation.getX()-poiB.x, 2)+Math.pow(playerLocation.getY()-poiB.y, 2)+Math.pow(playerLocation.getZ()-poiB.z, 2);
        				return -Double.compare(distA, distB);
        			}
        		});
        		
        		orderedNames = names;
        		break;
        	default:
        		((Player) sender).sendMessage("Order must be one of alpha or distance");
        		return false;
        }
        ((Player) sender).sendMessage("POIs:");
        for (String name : orderedNames) {
            POI poi = POIs.get(name);
            sender.sendMessage(name + ": " + poi.description + " (" + poi.world + " " + poi.x + ", " + poi.y + ", " + poi.z + ")");
        }
        return true;
    }
}
