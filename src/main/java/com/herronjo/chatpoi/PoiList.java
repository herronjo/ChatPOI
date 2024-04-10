package com.herronjo.chatpoi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.TreeMap;

import org.bukkit.Bukkit;

public class PoiList {
    private TreeMap<String, POI> POIs;

    public PoiList() {
        this.POIs = loadData("plugins/ChatPOI/pois.txt");
        if (this.POIs == null) {
            Bukkit.getConsoleSender().sendMessage("No POI data found. Creating new POI data.");
            this.POIs = new TreeMap<String, POI>();
            // Create ChatPOI directory if it doesn't exist
            new java.io.File("plugins/ChatPOI").mkdirs();
            saveData("plugins/ChatPOI/pois.txt");
        }
    }

    public boolean addPOI(String name, String description, String world, int x, int y, int z) {
        POI poi = new POI();
        poi.description = description;
        poi.world = world;
        poi.x = x;
        poi.y = y;
        poi.z = z;
        if (POIs.containsKey(name)) {
            return false;
        }
        POIs.put(name, poi);
        saveData("plugins/ChatPOI/pois.txt");
        return true;
    }

    public boolean removePOI(String name) {
        if (POIs.remove(name) == null) {
            return false;
        }
        saveData("plugins/ChatPOI/pois.txt");
        return true;
    }

    public boolean renamePOI(String oldName, String newName) {
        if (!POIs.containsKey(oldName)) {
            return false;
        }
        POI poi = POIs.get(oldName);
        POIs.remove(oldName);
        POIs.put(newName, poi);
        saveData("plugins/ChatPOI/pois.txt");
        return true;
    }

    public POI getPOI(String name) {
        return POIs.get(name);
    }

    public TreeMap<String, POI> getPOIs() {
        return POIs;
    }

    private boolean saveData(String filePath) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));

            for (String name : POIs.keySet()) {
                POI poi = POIs.get(name);
                writer.write(name + "\n");
                writer.write(poi.description + "\n");
                writer.write(poi.world + "\n");
                writer.write(poi.x + " " + poi.y + " " + poi.z + "\n");
            }

            writer.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static TreeMap<String, POI> loadData(String filePath) {
        try {
            TreeMap<String, POI> poiList = new TreeMap<String, POI>();
            BufferedReader reader = new BufferedReader(new FileReader(filePath));

            String line;
            while ((line = reader.readLine()) != null) {
                POI poi = new POI();
                poi.description = reader.readLine();
                poi.world = reader.readLine();
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
