package com.github.tanokun.karadasagasi.util;

        import com.github.tanokun.karadasagasi.util.io.Config;
        import org.bukkit.Bukkit;
        import org.bukkit.Location;
        import org.bukkit.World;

public class YamlUtils {
    public static void setLocation(Location location, Config config, String key) {
        String locT = location.getWorld().getName() + " " + location.getX() + " " + location.getY() + " " + location.getZ() + " " + location.getPitch() + " " + location.getYaw();
        config.getConfig().set(key, locT);
    }

    public static Location getLocation(Config config, String key) {
        try {
            String[] locT = config.getConfig().getString(key).split(" ");

            World world = Bukkit.getWorld(locT[0]);
            double x = Double.valueOf(locT[1]);
            double y = Double.valueOf(locT[2]);
            double z = Double.valueOf(locT[3]);

            float p = Float.valueOf(locT[4]);
            float y2 = Float.valueOf(locT[5]);

            return new Location(world, x, y, z, p, y2);
        }catch (Exception e) {
            e.printStackTrace();
            return new Location(Bukkit.getWorld("world"), 0, 0, 0);
        }
    }

    public static String LocationToString(Location location) {
        return Math.ceil(location.getX()) + " " + Math.ceil(location.getY()) +  " " + Math.ceil(location.getZ());
    }
}
