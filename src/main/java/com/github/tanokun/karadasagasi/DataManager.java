package com.github.tanokun.karadasagasi;

import com.github.tanokun.karadasagasi.util.io.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

public class DataManager {
    private Config dataFile;

    private Location killerWaitingPosition;

    public DataManager(Plugin plugin) {
        dataFile = new Config("data.yml", plugin);
        dataFile.saveDefaultConfig();
        String[] kwp = dataFile.getConfig().getString("killerWaitingPosition", "none").split(" ");
        this.killerWaitingPosition = kwp[0].equals("none") ? Bukkit.getWorld("world").getSpawnLocation() :
                new Location(Bukkit.getWorld(kwp[3]), Double.valueOf(kwp[0]), Double.valueOf(kwp[1]), Double.valueOf(kwp[2]));
        if (!this.killerWaitingPosition.equals(Bukkit.getWorld("world").getSpawnLocation())) {
            this.killerWaitingPosition.setPitch(Float.parseFloat(kwp[4]));
            this.killerWaitingPosition.setYaw(Float.parseFloat(kwp[5]));
        }
    }

    public void save(){
        dataFile.getConfig().set("killerWaitingPosition", killerWaitingPosition.equals(Bukkit.getWorld("world").getSpawnLocation()) ? "none" :
                killerWaitingPosition.getBlockX() + " " + killerWaitingPosition.getBlockY() + " " + killerWaitingPosition.getBlockZ() + " " + killerWaitingPosition.getWorld().getName() + " " + killerWaitingPosition.getPitch() + " " + killerWaitingPosition.getYaw());

        dataFile.saveConfig();
    }

    public Location getKillerWaitingPosition() {
        return killerWaitingPosition;
    }

    public void setKillerWaitingPosition(Location killerWaitingPosition) {
        this.killerWaitingPosition = killerWaitingPosition;
    }
}
