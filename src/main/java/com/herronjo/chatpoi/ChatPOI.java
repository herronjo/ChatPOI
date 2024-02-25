package com.herronjo.chatpoi;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class ChatPOI extends JavaPlugin implements Listener {
    Config config = new Config();
    HashMap<String, ArmorStand> floatingTextStands = new HashMap<String, ArmorStand>();
    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "ChatPOI has been enabled.");
        Objects.requireNonNull(getCommand("addpoi")).setExecutor(new AddPoiCommand(config, floatingTextStands));
        Objects.requireNonNull(getCommand("listpois")).setExecutor(new ListPoisCommand());
        Objects.requireNonNull(getCommand("searchpois")).setExecutor(new SearchPoisCommand());
        Objects.requireNonNull(getCommand("removepoi")).setExecutor(new RemovePoiCommand(config, floatingTextStands));
        Objects.requireNonNull(getCommand("renamepoi")).setExecutor(new RenamePoiCommand(config, floatingTextStands));
        Objects.requireNonNull(getCommand("togglepoihud")).setExecutor(new TogglePoiHudCommand(config, floatingTextStands));
        getServer().getPluginManager().registerEvents(this, this);

        if (config.getDisplayFloatingText()) {
            // Summon floating text for all POIs
            PoiList poiList = new PoiList();
            TreeMap<String, POI> pois = poiList.getPOIs();
            for (String poiName : pois.keySet()) {
                POI poi = pois.get(poiName);
                // Create invisible armor stand
                Location location = new Location(Bukkit.getWorld(poi.world), poi.x + .5, poi.y, poi.z + .5);
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
    }

    @Override
    public void onDisable() {
        if (config.getDisplayFloatingText()) {
            Iterator<String> iterator = floatingTextStands.keySet().iterator();
            while (iterator.hasNext()) {
                String poiName = iterator.next();
                ArmorStand armorStand = floatingTextStands.get(poiName);
                armorStand.remove();
                iterator.remove();
            }
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "ChatPOI has been disabled.");
    }

    @EventHandler (priority = EventPriority.HIGHEST)
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

            if (config.getDisplayFloatingText()) {
                BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
                scheduler.scheduleSyncDelayedTask(this, createFloatingText(name, new POI(message, world, x, y, z)), 1);
            }

            event.getPlayer().sendMessage(name + " added.");

            // Let the message be sent to the chat
            event.setCancelled(false);
        }
    }

    private Runnable createFloatingText(String poiName, POI poi) {
        return () -> {
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
        };
    }
}
