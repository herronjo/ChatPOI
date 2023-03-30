package com.herronjo.chatpoi;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Objects;

public class ChatPOI extends JavaPlugin implements Listener {
    HashMap<String, ArmorStand> floatingTextStands = new HashMap<String, ArmorStand>();
    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "ChatPOI has been enabled.");
        Objects.requireNonNull(getCommand("addpoi")).setExecutor(new AddPoiCommand(floatingTextStands));
        Objects.requireNonNull(getCommand("listpois")).setExecutor(new ListPoisCommand());
        Objects.requireNonNull(getCommand("removepoi")).setExecutor(new RemovePoiCommand(floatingTextStands));
        Objects.requireNonNull(getCommand("renamepoi")).setExecutor(new RenamePoiCommand(floatingTextStands));
        getServer().getPluginManager().registerEvents(this, this);

        // Summon floating text for all POIs
        PoiList poiList = new PoiList();
        HashMap<String, POI> pois = poiList.getPOIs();
        for (String poiName : pois.keySet()) {
            POI poi = pois.get(poiName);
            // Create invisible armor stand
            Location location = new Location(Bukkit.getWorld(poi.world), poi.x, poi.y, poi.z);
            ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
            armorStand.setCustomName(poiName);
            armorStand.setCustomNameVisible(true);
            armorStand.setInvulnerable(true);
            armorStand.setGravity(false);
            armorStand.setSilent(true);
            armorStand.setInvisible(true);
            floatingTextStands.put(poiName, armorStand);
        }
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "ChatPOI has been disabled.");
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        // Check if message contains three numbers separated by spaces at any point in the message
        if (message.matches(".*\\d+\\s+\\d+\\s+\\d+.*")) {
            String[] words = message.split(" ");
            int numNumbers = 0;
            int x = 0;
            int y = 0;
            int z = 0;

            for (String word : words) {
                try {
                    int number = Integer.parseInt(word);
                    if (numNumbers == 0) {
                        x = number;
                    } else if (numNumbers == 1) {
                        y = number;
                    } else if (numNumbers == 2) {
                        z = number;
                    }
                    numNumbers++;
                } catch (NumberFormatException e) {
                    // Do nothing
                }
            }

            PoiList poiList = new PoiList();
            int numPOIs = poiList.getPOIs().size() + 1;
            String name = "POI #" + numPOIs;
            String world = event.getPlayer().getWorld().getName();
            int retries = 1;
            while (!poiList.addPOI(name, message, world, x, y, z)) {
                name = "POI #" + numPOIs + " (" + retries + ")";
                retries++;
            }
            POI poi = poiList.getPOI(name);
            // Create invisible armor stand
            Location location = new Location(Bukkit.getWorld(poi.world), poi.x, poi.y, poi.z);
            ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
            armorStand.setCustomName(name);
            armorStand.setCustomNameVisible(true);
            armorStand.setInvulnerable(true);
            armorStand.setGravity(false);
            armorStand.setSilent(true);
            armorStand.setInvisible(true);
            floatingTextStands.put(name, armorStand);
            event.getPlayer().sendMessage(name + " added.");

            // Let the message be sent to the chat
            event.setCancelled(false);
        }
    }
}
