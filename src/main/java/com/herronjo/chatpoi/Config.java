package com.herronjo.chatpoi;

import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class Config {
    private boolean displayFloatingText = false;

    public Config() {
        load();
    }

    public boolean getDisplayFloatingText() {
        return displayFloatingText;
    }

    public void setDisplayFloatingText(boolean displayFloatingText) {
        this.displayFloatingText = displayFloatingText;
        save();
    }

    private void save() {
        try {
            new java.io.File("plugins/ChatPOI").mkdirs();
            BufferedWriter writer = new BufferedWriter(new FileWriter("plugins/ChatPOI/config.txt"));
            writer.write("displayFloatingText=" + displayFloatingText);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void load() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("plugins/ChatPOI/config.txt"));
            String line = reader.readLine();
            while (line != null) {
                String[] parts = line.split("=");
                if (parts[0].equals("displayFloatingText")) {
                    displayFloatingText = Boolean.parseBoolean(parts[1]);
                }
                line = reader.readLine();
            }

            reader.close();
        } catch (Exception e) {
            // Config file doesn't exist, so create it
            Bukkit.getConsoleSender().sendMessage("Creating config file...");
            save();
            e.printStackTrace();
        }
    }
}
