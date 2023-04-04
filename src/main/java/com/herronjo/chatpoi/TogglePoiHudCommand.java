package com.herronjo.chatpoi;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Iterator;

public class TogglePoiHudCommand implements CommandExecutor {
    Config config;
    HashMap<String, ArmorStand> floatingTextStands;

    public TogglePoiHudCommand(Config config, HashMap<String, ArmorStand> floatingTextStands) {
        this.config = config;
        this.floatingTextStands = floatingTextStands;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;

        config.setDisplayFloatingText(!config.getDisplayFloatingText());

        if (config.getDisplayFloatingText()) {
            // Summon floating text for all POIs
            PoiList poiList = new PoiList();
            HashMap<String, POI> pois = poiList.getPOIs();
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
            player.sendMessage("Toggling HUD on.");
        } else {
            Iterator<String> iterator = floatingTextStands.keySet().iterator();
            while (iterator.hasNext()) {
                String poiName = iterator.next();
                ArmorStand armorStand = floatingTextStands.get(poiName);
                armorStand.remove();
                iterator.remove();
            }
            player.sendMessage("Toggling HUD off.");
        }

        return true;
    }
}
