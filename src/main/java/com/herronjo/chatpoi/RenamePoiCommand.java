package com.herronjo.chatpoi;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RenamePoiCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        if (args.length < 2) {
            return false;
        }

        // This is a hack to get around the fact that Bukkit doesn't support spaces in arguments
        // Absolutely stolen from https://stackoverflow.com/questions/7804335/
        List<String> realArgsBecauseBukkitSucks = new ArrayList<String>();
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(String.join(" ", args));
        while (m.find()) realArgsBecauseBukkitSucks.add(m.group(1));

        if (realArgsBecauseBukkitSucks.size() < 2) {
            return false;
        }

        String oldName = realArgsBecauseBukkitSucks.get(0);
        if (oldName.startsWith("\"") && oldName.endsWith("\"")) oldName = oldName.substring(1, oldName.length() - 1);
        String newName = realArgsBecauseBukkitSucks.get(1);
        if (newName.startsWith("\"") && newName.endsWith("\"")) newName = newName.substring(1, newName.length() - 1);

        PoiList poiList = new PoiList();
        if (poiList.getPOI(newName) != null) {
            ((Player) sender).sendMessage("POI already exists.");
            return true;
        }
        if (poiList.renamePOI(oldName, newName)) {
            ((Player) sender).sendMessage("POI renamed.");
        } else {
            ((Player) sender).sendMessage("POI not found.");
        }
        return true;
    }
}
