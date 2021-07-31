package com.github.tanokun.karadasagasi.event.listener;

import com.github.tanokun.karadasagasi.KaradaSagasi;
import com.github.tanokun.karadasagasi.event.wg.MovementWay;
import com.github.tanokun.karadasagasi.event.wg.WgRegionEnterEvent;
import com.github.tanokun.karadasagasi.event.wg.WgRegionLeaveEvent;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.util.*;

public class WgRegionEventListener implements Listener {
    private WorldGuardPlugin wgPlugin;
    private KaradaSagasi plugin;
    private Map<Player, Set<ProtectedRegion>> playerRegions;

    public WgRegionEventListener(KaradaSagasi plugin, WorldGuardPlugin wgPlugin) {
        this.plugin = plugin;
        this.wgPlugin = wgPlugin;
        this.playerRegions = new HashMap();
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent e) {
        Set<ProtectedRegion> regions = (Set)this.playerRegions.remove(e.getPlayer());
        if (regions != null) {
            Iterator var3 = regions.iterator();

            while(var3.hasNext()) {
                ProtectedRegion region = (ProtectedRegion)var3.next();
                WgRegionLeaveEvent leaveEvent = new WgRegionLeaveEvent(region, e.getPlayer(), MovementWay.DISCONNECT, e);
                this.plugin.getServer().getPluginManager().callEvent(leaveEvent);
            }
        }

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Set<ProtectedRegion> regions = (Set)this.playerRegions.remove(e.getPlayer());
        if (regions != null) {
            Iterator var3 = regions.iterator();

            while(var3.hasNext()) {
                ProtectedRegion region = (ProtectedRegion)var3.next();
                WgRegionLeaveEvent leaveEvent = new WgRegionLeaveEvent(region, e.getPlayer(), MovementWay.DISCONNECT, e);
                this.plugin.getServer().getPluginManager().callEvent(leaveEvent);
            }
        }

    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (this.updateRegions(e.getPlayer(), MovementWay.MOVE, e.getTo(), e)) e.getPlayer().teleport(e.getFrom());
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        e.setCancelled(this.updateRegions(e.getPlayer(), MovementWay.TELEPORT, e.getTo(), e));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        this.updateRegions(e.getPlayer(), MovementWay.SPAWN, e.getPlayer().getLocation(), e);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        this.updateRegions(e.getPlayer(), MovementWay.SPAWN, e.getRespawnLocation(), e);
    }

    private synchronized boolean updateRegions(final Player player, final MovementWay movement, Location to, final PlayerEvent event) {
        HashSet regions;
        if (playerRegions.get(player) == null) {
            regions = new HashSet();
        } else {
            regions = new HashSet(playerRegions.get(player));
        }

        Set<ProtectedRegion> oldRegions = new HashSet(regions);
        RegionManager rm = this.wgPlugin.getRegionManager(to.getWorld());
        if (rm == null) {
            return false;
        } else {
            HashSet<ProtectedRegion> appRegions = new HashSet(rm.getApplicableRegions(to).getRegions());
            ProtectedRegion globalRegion = rm.getRegion("__global__");
            if (globalRegion != null) {
                appRegions.add(globalRegion);
            }

            Iterator itr = appRegions.iterator();

            ProtectedRegion region;
            while(itr.hasNext()) {
                region = (ProtectedRegion)itr.next();
                if (!regions.contains(region)) {
                    WgRegionEnterEvent e = new WgRegionEnterEvent(region, player, movement, event);
                    this.plugin.getServer().getPluginManager().callEvent(e);
                    if (e.isCancelled()) {
                        regions.clear();
                        regions.addAll(oldRegions);
                        return true;
                    }
                    regions.add(region);
                }
            }

            itr = regions.iterator();

            while(itr.hasNext()) {
                region = (ProtectedRegion)itr.next();
                if (!appRegions.contains(region)) {
                    if (rm.getRegion(region.getId()) != region) {
                        itr.remove();
                    } else {
                        WgRegionLeaveEvent e = new WgRegionLeaveEvent(region, player, movement, event);
                        plugin.getServer().getPluginManager().callEvent(e);
                        if (e.isCancelled()) {
                            regions.clear();
                            regions.addAll(oldRegions);
                            return true;
                        }
                        itr.remove();
                    }
                }
            }

            this.playerRegions.put(player, regions);
            return false;
        }
    }
}
