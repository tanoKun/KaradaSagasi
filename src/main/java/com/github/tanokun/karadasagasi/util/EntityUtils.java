package com.github.tanokun.karadasagasi.util;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.NoSuchElementException;

public class EntityUtils {

    public static Entity[] getNearEntities(Location l, double radius) {
        double chunkRadius = radius < 16 ? 1 : (radius - (radius % 16)) / 16;
        HashSet<Entity> radiusEntities = new HashSet< Entity >();
        try {
            for (double chX = 0 - chunkRadius; chX <= chunkRadius; chX++) {
                for (double chZ = 0 - chunkRadius; chZ <= chunkRadius; chZ++) {
                    int x = (int) l.getX(), y = (int) l.getY(), z = (int) l.getZ();
                    for (Entity e: new Location(l.getWorld(), x + (chX * 16), y, z + (chZ * 16)).getChunk().getEntities()) {
                        if (e.getLocation().distance(l) <= radius && e.getLocation().getBlock() != l.getBlock())
                            radiusEntities.add(e);
                    }
                }
            }
        }catch (NoSuchElementException | NullPointerException e){
            return radiusEntities.toArray(new Entity[radiusEntities.size()]);
        }
        return radiusEntities.toArray(new Entity[radiusEntities.size()]);
    }

    public static Entity[] getNearPlayers(Location l, double radius) {
        double chunkRadius = radius < 16 ? 1 : (radius - (radius % 16)) / 16;
        HashSet <Entity> radiusEntities = new HashSet< Entity >();
        try {
            for (double chX = 0 - chunkRadius; chX <= chunkRadius; chX++) {
                for (double chZ = 0 - chunkRadius; chZ <= chunkRadius; chZ++) {
                    int x = (int) l.getX(), y = (int) l.getY(), z = (int) l.getZ();
                    for (Entity e: new Location(l.getWorld(), x + (chX * 16), y, z + (chZ * 16)).getChunk().getEntities()) {
                        if (e.getLocation().distance(l) <= radius && e.getLocation().getBlock() != l.getBlock() && e instanceof Player)
                            radiusEntities.add(e);
                    }
                }
            }
        }catch (NoSuchElementException | NullPointerException e){
            return radiusEntities.toArray(new Entity[radiusEntities.size()]);
        }
        return radiusEntities.toArray(new Entity[radiusEntities.size()]);
    }

    public static Entity[] getNearActiveEntity(Location l, double radius) {
        double chunkRadius = radius < 16 ? 1 : (radius - (radius % 16)) / 16;
        HashSet <Entity> radiusEntities = new HashSet<Entity>();
        try {
            for (double chX = 0 - chunkRadius; chX <= chunkRadius; chX++) {
                for (double chZ = 0 - chunkRadius; chZ <= chunkRadius; chZ++) {
                    int x = (int) l.getX(), y = (int) l.getY(), z = (int) l.getZ();
                    for (Entity e: new Location(l.getWorld(), x + (chX * 16), y, z + (chZ * 16)).getChunk().getEntities()) {
                        if (e.getLocation().distance(l) <= radius && e.getLocation().getBlock() != l.getBlock() && e.hasMetadata("TanoRPG_entity"))
                            radiusEntities.add(e);
                    }
                }
            }
        }catch (NoSuchElementException | NullPointerException e){
            return radiusEntities.toArray(new Entity[radiusEntities.size()]);
        }
        return radiusEntities.toArray(new Entity[radiusEntities.size()]);
    }
}
