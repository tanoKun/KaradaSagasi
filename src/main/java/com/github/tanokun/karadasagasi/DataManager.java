package com.github.tanokun.karadasagasi;

import com.github.tanokun.karadasagasi.util.YamlUtils;
import com.github.tanokun.karadasagasi.util.io.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

public class DataManager {
    private Config dataFile;

    private Location killerWaitingPosition;

    private Location lobbyPosition;

    public DataManager(Plugin plugin) {
        dataFile = new Config("data.yml", plugin);
        dataFile.saveDefaultConfig();

        killerWaitingPosition = YamlUtils.getLocation(dataFile, "killerWaitingPosition");

        lobbyPosition = YamlUtils.getLocation(dataFile, "lobbyPosition");
    }

    public void save(){
        YamlUtils.setLocation(killerWaitingPosition, dataFile, "killerWaitingPosition");

        YamlUtils.setLocation(lobbyPosition, dataFile, "lobbyPosition");


        dataFile.saveConfig();
    }

    public Location getKillerWaitingPosition() {
        return killerWaitingPosition;
    }

    public Location getLobbyPosition() {
        return lobbyPosition;
    }

    public void setKillerWaitingPosition(Location killerWaitingPosition) {
        this.killerWaitingPosition = killerWaitingPosition;
    }

    public void setLobbyPosition(Location lobbyPosition) {
        this.lobbyPosition = lobbyPosition;
    }
}
