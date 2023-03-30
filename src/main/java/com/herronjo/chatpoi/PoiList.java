package com.herronjo.chatpoi;

import org.bukkit.Bukkit;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.*;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class PoiList {
    private HashMap<String, POI> POIs;

    public PoiList() {
        this.POIs = loadData("plugins/ChatPOI/pois.txt");
        if (this.POIs == null) {
            Bukkit.getConsoleSender().sendMessage("No POI data found. Creating new POI data.");
            this.POIs = new HashMap<String, POI>();
            // Create ChatPOI directory if it doesn't exist
            new java.io.File("plugins/ChatPOI").mkdirs();
            saveData("plugins/ChatPOI/pois.txt");
        }
    }

    public void addPOI(String name, String description, int x, int y, int z) {
        POI poi = new POI();
        poi.description = description;
        poi.x = x;
        poi.y = y;
        poi.z = z;
        POIs.put(name, poi);
        saveData("plugins/ChatPOI/pois.txt");
    }

    public void removePOI(String name) {
        POIs.remove(name);
        saveData("plugins/ChatPOI/pois.txt");
    }

    public void renamePOI(String oldName, String newName) {
        POI poi = POIs.get(oldName);
        POIs.remove(oldName);
        POIs.put(newName, poi);
        saveData("plugins/ChatPOI/pois.txt");
    }

    public HashMap<String, POI> getPOIs() {
        return POIs;
    }

    private boolean saveData(String filePath) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));

            for (String name : POIs.keySet()) {
                POI poi = POIs.get(name);
                writer.write(name + "\n");
                writer.write(poi.description + "\n");
                writer.write(poi.x + " " + poi.y + " " + poi.z + "\n");
            }

            writer.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static HashMap<String, POI> loadData(String filePath) {
        try {
            HashMap<String, POI> poiList = new HashMap<String, POI>();
            BufferedReader reader = new BufferedReader(new FileReader(filePath));

            String line;
            while ((line = reader.readLine()) != null) {
                POI poi = new POI();
                poi.description = reader.readLine();
                String[] coords = reader.readLine().split(" ");
                poi.x = Integer.parseInt(coords[0]);
                poi.y = Integer.parseInt(coords[1]);
                poi.z = Integer.parseInt(coords[2]);
                poiList.put(line, poi);
            }

            reader.close();
            return poiList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
