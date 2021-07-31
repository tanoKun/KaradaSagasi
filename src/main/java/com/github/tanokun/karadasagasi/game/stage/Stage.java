package com.github.tanokun.karadasagasi.game.stage;

import com.github.tanokun.karadasagasi.game.stage.teleport.ChestLocationMap;
import com.github.tanokun.karadasagasi.game.stage.teleport.KillerTeleportLocationMap;
import com.github.tanokun.karadasagasi.game.stage.teleport.PrisonLocationMap;
import com.github.tanokun.karadasagasi.util.SaveMarker;
import com.github.tanokun.karadasagasi.util.YamlUtils;
import com.github.tanokun.karadasagasi.util.io.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Stage implements SaveMarker<Stage> {
    private String name;

    private int time = 180;

    private Location spawnStudentLocation = new Location(Bukkit.getWorld("world"), 0, 0, 0);

    private final KillerTeleportLocationMap killerTeleportLocationMap;

    private final PrisonLocationMap prisonLocationMap;

    private final ChestLocationMap chestLocationMap;

    private Location bodyLocation = new Location(Bukkit.getWorld("world"), 0, 0, 0);

    private int minPlayerAmount = 2;
    private int maxPlayerAmount = 10;

    public Stage(String name) {
        this.name = name;
        killerTeleportLocationMap = new KillerTeleportLocationMap();
        prisonLocationMap = new PrisonLocationMap();
        chestLocationMap = new ChestLocationMap();
    }

    public String getName() {
        return name;
    }

    public int getTime() {
        return time;
    }

    public Location getSpawnStudentLocation() {
        return spawnStudentLocation;
    }

    public KillerTeleportLocationMap getKillerTeleportLocationMap() {
        return killerTeleportLocationMap;
    }

    public PrisonLocationMap getPrisonLocationMap() {
        return prisonLocationMap;
    }

    public ChestLocationMap getChestLocationMap() {
        return chestLocationMap;
    }

    public Location getBodyLocation() {
        return bodyLocation;
    }

    public int getMinPlayerAmount() {
        return minPlayerAmount;
    }

    public int getMaxPlayerAmount() {
        return maxPlayerAmount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setSpawnStudentLocation(Location spawnStudentLocation) {
        this.spawnStudentLocation = spawnStudentLocation;
    }

    public void setBodyLocation(Location bodyLocation) {
        this.bodyLocation = bodyLocation;
    }

    public void setMinPlayerAmount(int minPlayerAmount) {
        this.minPlayerAmount = minPlayerAmount;
    }

    public void setMaxPlayerAmount(int maxPlayerAmount) {
        this.maxPlayerAmount = maxPlayerAmount;
    }

    @Override
    public void save(Config config, String key) {
        config.getConfig().set(key + ".name", name);
        config.getConfig().set(key + ".time", time);
        YamlUtils.setLocation(spawnStudentLocation, config, key + ".spawnStudentLocation");
        YamlUtils.setLocation(bodyLocation, config, key + ".bodyLocation");
        config.getConfig().set(key + ".minPlayerAmount", minPlayerAmount);
        config.getConfig().set(key + ".maxPlayerAmount", maxPlayerAmount);

        killerTeleportLocationMap.save(config, key);
        prisonLocationMap.save(config, key);
        chestLocationMap.save(config, key);
    }

    @Override
    public Stage load(Config config, String key) {
        time = config.getConfig().getInt(key + ".time");
        spawnStudentLocation = YamlUtils.getLocation(config, key + ".spawnStudentLocation");
        bodyLocation = YamlUtils.getLocation(config, key + ".bodyLocation");
        minPlayerAmount = config.getConfig().getInt(key + ".minPlayerAmount");
        maxPlayerAmount = config.getConfig().getInt(key + ".maxPlayerAmount");

        killerTeleportLocationMap.load(config, key);
        prisonLocationMap.load(config, key);
        chestLocationMap.load(config, key);

        return this;
    }
}