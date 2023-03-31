package com.herronjo.chatpoi;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddPoiCommand implements CommandExecutor {
    Config config;
    HashMap<String, ArmorStand> floatingTextStands;
    public AddPoiCommand(Config config, HashMap<String, ArmorStand> floatingTextStands) {
        this.config = config;
        this.floatingTextStands = floatingTextStands;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        // This is a hack to get around the fact that Bukkit doesn't support spaces in arguments
        // Absolutely stolen from https://stackoverflow.com/questions/7804335/
        List<String> realArgsBecauseBukkitSucks = new ArrayList<String>();
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(String.join(" ", args));
        while (m.find()) realArgsBecauseBukkitSucks.add(m.group(1));

        if (realArgsBecauseBukkitSucks.size() < 1) {
            return false;
        }

        String name = realArgsBecauseBukkitSucks.get(0);
        if (name.startsWith("\"") && name.endsWith("\"")) name = name.substring(1, name.length() - 1);
        String description = name;
        if (realArgsBecauseBukkitSucks.size() > 1) {
            description = realArgsBecauseBukkitSucks.get(1);
            if (description.startsWith("\"") && description.endsWith("\"")) description = description.substring(1, description.length() - 1);
        }
        Player player = (Player) sender;
        PoiList poiList = new PoiList();
        String world = player.getWorld().getName();
        int x = player.getLocation().getBlockX();
        int y = player.getLocation().getBlockY();
        int z = player.getLocation().getBlockZ();
        if (poiList.addPOI(name, description, world, x, y, z)) {
            if (config.getDisplayFloatingText()) {
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
            }
            player.sendMessage("POI added.");
        } else {
            player.sendMessage("POI already exists.");
        }
        return true;
    }
}
