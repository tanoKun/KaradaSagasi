package com.github.tanokun.karadasagasi.game.stage.teleport;

import com.github.tanokun.karadasagasi.game.stage.Stage;
import com.github.tanokun.karadasagasi.util.SaveMarker;
import com.github.tanokun.karadasagasi.util.YamlUtils;
import com.github.tanokun.karadasagasi.util.io.Config;
import org.bukkit.Location;
import org.yaml.snakeyaml.Yaml;

import java.util.ArrayList;

public class KillerTeleportLocationMap  implements SaveMarker<KillerTeleportLocationMap> {
    private final ArrayList<TeleportLocation> killerTeleportLocations = new ArrayList<>();

    public ArrayList<TeleportLocation> getKillerTeleportLocations() {
        return killerTeleportLocations;
    }

    @Override
    public void save(Config config, String key) {
        config.getConfig().set(key + ".killerTeleportLocations", null);
        for (TeleportLocation tl : killerTeleportLocations) {
            YamlUtils.setLocation(tl.getLocation(), config, key + ".killerTeleportLocations." + tl.getName());
        }
    }

    @Override
    public KillerTeleportLocationMap load(Config config, String key) {
        if (config.getConfig().getConfigurationSection(key + ".killerTeleportLocations") == null) return this;

        for (String name : config.getConfig().getConfigurationSection(key + ".killerTeleportLocations").getKeys(false)) {
            Location location = YamlUtils.getLocation(config, key + ".killerTeleportLocations." + name);
            killerTeleportLocations.add(new TeleportLocation(location, name));
        }

        return this;
    }
}
