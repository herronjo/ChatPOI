package com.herronjo.chatpoi;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RemovePoiCommand implements CommandExecutor {
    Config config;
    HashMap<String, ArmorStand> floatingTextStands;
    public RemovePoiCommand(Config config, HashMap<String, ArmorStand> floatingTextStands) {
        this.config = config;
        this.floatingTextStands = floatingTextStands;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        if (args.length < 1) {
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
        PoiList poiList = new PoiList();
        if (!poiList.removePOI(name)) {
            ((Player) sender).sendMessage("POI not found.");
        } else {
            if (config.getDisplayFloatingText()) {
                // Remove the floating text stand
                ArmorStand armorStand = floatingTextStands.get(name);
                if (armorStand != null) {
                    armorStand.remove();
                }
                floatingTextStands.remove(name);
            }
            ((Player) sender).sendMessage("POI removed.");
        }
        return true;
    }
}
