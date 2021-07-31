package com.github.tanokun.karadasagasi.game.stage.teleport;

import com.github.tanokun.karadasagasi.game.stage.Stage;
import com.github.tanokun.karadasagasi.util.SaveMarker;
import com.github.tanokun.karadasagasi.util.YamlUtils;
import com.github.tanokun.karadasagasi.util.io.Config;
import org.bukkit.Location;

import java.util.ArrayList;

public class PrisonLocationMap implements SaveMarker<PrisonLocationMap> {
    private final ArrayList<TeleportLocation> prisonTeleportLocations = new ArrayList<>();

    public ArrayList<TeleportLocation> getPrisonTeleportLocations() {
        return prisonTeleportLocations;
    }

    @Override
    public void save(Config config, String key) {
        config.getConfig().set(key + ".prisonTeleportLocations", null);
        for (TeleportLocation tl : prisonTeleportLocations) {
            YamlUtils.setLocation(tl.getLocation(), config, key + ".prisonTeleportLocations." + tl.getName() + ".location");
            config.getConfig().set(key + ".prisonTeleportLocations." + tl.getName() + ".rgName", tl.getRgName());
        }
    }

    @Override
    public PrisonLocationMap load(Config config, String key) {
        if (config.getConfig().getConfigurationSection(key + ".prisonTeleportLocations") == null) return this;

        for (String name : config.getConfig().getConfigurationSection(key + ".prisonTeleportLocations").getKeys(false)) {
            Location location = YamlUtils.getLocation(config, key + ".prisonTeleportLocations." + name + ".location");
            TeleportLocation teleportLocation = new TeleportLocation(location, name);
            teleportLocation.setRgName(config.getConfig().getString(key + ".prisonTeleportLocations." + name + ".rgName", null));
            prisonTeleportLocations.add(teleportLocation);
        }

        return this;
    }
}
