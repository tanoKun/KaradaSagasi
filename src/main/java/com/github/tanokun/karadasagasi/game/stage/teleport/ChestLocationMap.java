package com.github.tanokun.karadasagasi.game.stage.teleport;

import com.github.tanokun.karadasagasi.game.stage.Stage;
import com.github.tanokun.karadasagasi.util.SaveMarker;
import com.github.tanokun.karadasagasi.util.YamlUtils;
import com.github.tanokun.karadasagasi.util.io.Config;
import org.bukkit.Location;

import java.util.ArrayList;

public class ChestLocationMap  implements SaveMarker<ChestLocationMap> {
    private final ArrayList<Location> firstFloorChestLocations = new ArrayList<>();

    private final ArrayList<Location> secondFloorChestLocations = new ArrayList<>();

    private final ArrayList<Location> thirdFloorChestLocations = new ArrayList<>();

    private final ArrayList<Location> fourthFloorChestLocations = new ArrayList<>();

    public ArrayList<Location> getFirstFloorChestLocations() {
        return firstFloorChestLocations;
    }

    public ArrayList<Location> getSecondFloorChestLocations() {
        return secondFloorChestLocations;
    }

    public ArrayList<Location> getThirdFloorChestLocations() {
        return thirdFloorChestLocations;
    }

    public ArrayList<Location> getFourthFloorChestLocations() {
        return fourthFloorChestLocations;
    }

    @Override
    public void save(Config config, String key) {
        int i = 0;
        config.getConfig().set(key + ".ChestLocations", null);
        for (Location location : firstFloorChestLocations) {
            YamlUtils.setLocation(location, config, key + ".ChestLocations.first." + i);
            i++;
        }

        i = 0;
        for (Location location : secondFloorChestLocations) {
            YamlUtils.setLocation(location, config, key + ".ChestLocations.second." + i);
            i++;
        }

        i = 0;
        for (Location location : thirdFloorChestLocations) {
            YamlUtils.setLocation(location, config, key + ".ChestLocations.third." + i);
            i++;
        }

        i = 0;
        for (Location location : fourthFloorChestLocations) {
            YamlUtils.setLocation(location, config, key + ".ChestLocations.fourth." + i);
            i++;
        }
    }

    @Override
    public ChestLocationMap load(Config config, String key) {
        if (config.getConfig().getConfigurationSection(key + ".ChestLocations.first") != null) {
            for (String locT : config.getConfig().getConfigurationSection(key + ".ChestLocations.first").getKeys(false)) {
                firstFloorChestLocations.add(YamlUtils.getLocation(config, config.getConfig().getString(key + ".ChestLocations.first." + locT)));
            }
        }

        if (config.getConfig().getConfigurationSection(key + ".ChestLocations.second") != null) {
            for (String locT : config.getConfig().getConfigurationSection(key + ".ChestLocations.second").getKeys(false)) {
                secondFloorChestLocations.add(YamlUtils.getLocation(config, config.getConfig().getString(key + ".ChestLocations.second." + locT)));
            }
        }

        if (config.getConfig().getConfigurationSection(key + ".ChestLocations.third") != null) {
            for (String locT : config.getConfig().getConfigurationSection(key + ".ChestLocations.third").getKeys(false)) {
                thirdFloorChestLocations.add(YamlUtils.getLocation(config, config.getConfig().getString(key + ".ChestLocations.third." + locT)));
            }
        }

        if (config.getConfig().getConfigurationSection(key + ".ChestLocations.fourth") != null) {
            for (String locT : config.getConfig().getConfigurationSection(key + ".ChestLocations.fourth").getKeys(false)) {
                fourthFloorChestLocations.add(YamlUtils.getLocation(config, config.getConfig().getString(key + ".ChestLocations.fourth." + locT)));
            }
        }
        return this;
    }
}
