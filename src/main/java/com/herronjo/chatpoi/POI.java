package com.herronjo.chatpoi;

public class POI {
    public String description;
    public String world;
    public int x;
    public int y;
    public int z;

    public POI() {
        this.description = "";
        this.world = "";
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public POI(String description, String world, int x, int y, int z) {
        this.description = description;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }
}