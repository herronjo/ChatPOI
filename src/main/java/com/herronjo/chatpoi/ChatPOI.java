package com.herronjo.chatpoi;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class ChatPOI extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "ChatPOI has been enabled.");
        Objects.requireNonNull(getCommand("addpoi")).setExecutor(new AddPoiCommand());
        Objects.requireNonNull(getCommand("listpois")).setExecutor(new ListPoisCommand());
        Objects.requireNonNull(getCommand("removepoi")).setExecutor(new RemovePoiCommand());
        Objects.requireNonNull(getCommand("renamepoi")).setExecutor(new RenamePoiCommand());
        getServer().getPluginManager().registerEvents(this, this);
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
            poiList.addPOI("POI #" + numPOIs, message, x, y, z);
            event.getPlayer().sendMessage("POI #" + numPOIs + " added.");

            // Let the message be sent to the chat
            event.setCancelled(false);
        }
    }
}
