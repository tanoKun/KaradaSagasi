package com.github.tanokun.karadasagasi.game.stage.teleport;

import com.github.tanokun.karadasagasi.game.stage.Stage;
import com.github.tanokun.karadasagasi.util.SaveMarker;
import com.github.tanokun.karadasagasi.util.io.Config;
import org.bukkit.Location;

import java.util.Objects;
import java.util.UUID;

public class TeleportLocation {

    private final Location location;

    private String name;

    private String rgName;

    public TeleportLocation(Location location, String name){
        this.location = location;
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String getRgName() {
        return rgName;
    }

    public void setRgName(String rgName) {
        this.rgName = rgName;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeleportLocation that = (TeleportLocation) o;
        return Objects.equals(location, that.location) && Objects.equals(name, that.name) && Objects.equals(rgName, that.rgName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, name, rgName);
    }
}
