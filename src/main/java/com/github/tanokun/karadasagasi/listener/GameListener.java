package com.github.tanokun.karadasagasi.listener;

import com.comphenix.protocol.PacketType;
import com.github.tanokun.karadasagasi.KaradaSagasi;
import com.github.tanokun.karadasagasi.event.wg.WgRegionEnterEvent;
import com.github.tanokun.karadasagasi.event.wg.WgRegionLeaveEvent;
import com.github.tanokun.karadasagasi.game.GameEndState;
import com.github.tanokun.karadasagasi.game.GameRunnable;
import com.github.tanokun.karadasagasi.game.State;
import com.github.tanokun.karadasagasi.game.player.GamePlayer;
import com.github.tanokun.karadasagasi.game.stage.Stage;
import com.github.tanokun.karadasagasi.game.stage.teleport.PrisonLocationMap;
import com.github.tanokun.karadasagasi.game.stage.teleport.TeleportLocation;
import net.minecraft.server.v1_12_R1.PacketPlayInClientCommand;
import net.minecraft.server.v1_12_R1.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.Random;

public class GameListener implements Listener {

    @EventHandler
    public void onKillerEnterRegion(WgRegionEnterEvent e) {
        GameRunnable runnable = KaradaSagasi.getPlugin().getGameRunnable();
        GamePlayer gamePlayer = KaradaSagasi.getPlugin().getPlayerManager().getGamePlayer(e.getPlayer().getUniqueId());
        if (runnable.getState() != State.GAMING || !runnable.getPlayers().contains(e.getPlayer()) || gamePlayer.getTempPlayer().isDie()) return;

        for (TeleportLocation tl : runnable.getStage().getPrisonLocationMap().getPrisonTeleportLocations()) {
            if (e.getRegion().getId().equals(tl.getRgName())) {
                e.getPlayer().sendMessage(KaradaSagasi.PX + "自分から牢獄に入ることはできません");
                e.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onKillerLeaveRegion(WgRegionLeaveEvent e) {
        GameRunnable runnable = KaradaSagasi.getPlugin().getGameRunnable();
        GamePlayer gamePlayer = KaradaSagasi.getPlugin().getPlayerManager().getGamePlayer(e.getPlayer().getUniqueId());
        if (runnable.getState() != State.GAMING || !runnable.getPlayers().contains(e.getPlayer()) || !gamePlayer.getTempPlayer().isDie()) return;

        for (TeleportLocation tl : runnable.getStage().getPrisonLocationMap().getPrisonTeleportLocations()) {
            if (e.getRegion().getId().equals(tl.getRgName())) {
                runnable.getDiedStudents().remove(e.getPlayer());
                e.getPlayer().setPlayerListName("§b[生徒] " + e.getPlayer().getName() + " §7[§dLv:§e" + gamePlayer.getHasLevel().getValue() + "§7]");
                gamePlayer.getTempPlayer().setDie(false);
                KaradaSagasi.getPlugin().getBoardManager().update();
            }
        }
    }

    @EventHandler
    public void onChestClick(PlayerInteractEvent e) {
        GameRunnable runnable = KaradaSagasi.getPlugin().getGameRunnable();

        if (e.getItem() == null || e.getItem().getType() == Material.AIR || e.getAction() != Action.RIGHT_CLICK_BLOCK || e.getClickedBlock().getType() != Material.CHEST || runnable.getState() != State.GAMING) return;

        if (runnable.getKillers().contains(e.getPlayer())) {
            e.setCancelled(true);
            return;
        }

        if (runnable.getStudents().contains(e.getPlayer())
                && (int) runnable.getStage().getBodyLocation().getX() == e.getClickedBlock().getLocation().getBlockX()
                && (int) runnable.getStage().getBodyLocation().getY() == e.getClickedBlock().getLocation().getBlockY()
                && (int) runnable.getStage().getBodyLocation().getZ() == e.getClickedBlock().getLocation().getBlockZ()) {

            if (e.getItem().getType() == Material.DIAMOND_HELMET) {
                runnable.setHelmet(e.getPlayer());
                Bukkit.broadcastMessage(KaradaSagasi.PX + "§b" + e.getPlayer().getName() + "が頭を入れました！");
                Bukkit.getOnlinePlayers().stream().forEach(player -> player.sendTitle("  ", "§b" + e.getPlayer().getName() + "が頭を入れました！", 4, 20, 4));
            }
            else if (e.getItem().getType() == Material.DIAMOND_CHESTPLATE) {
                runnable.setChestplate(e.getPlayer());
                Bukkit.broadcastMessage(KaradaSagasi.PX + "§b" + e.getPlayer().getName() + "が胸を入れました！");
                Bukkit.getOnlinePlayers().stream().forEach(player -> player.sendTitle("  ", "§b" + e.getPlayer().getName() + "が胸を入れました！", 4, 20, 4));
            }
            else if (e.getItem().getType() == Material.DIAMOND_LEGGINGS) {
                runnable.setLeggings(e.getPlayer());
                Bukkit.broadcastMessage(KaradaSagasi.PX + "§b" + e.getPlayer().getName() + "が股を入れました！");
                Bukkit.getOnlinePlayers().stream().forEach(player -> player.sendTitle("  ", "§b" + e.getPlayer().getName() + "が股を入れました！", 4, 20, 4));
            }
            else if (e.getItem().getType() == Material.DIAMOND_BOOTS) {
                runnable.setBoots(e.getPlayer());
                Bukkit.broadcastMessage(KaradaSagasi.PX + "§b" + e.getPlayer().getName() + "が足を入れました！");
                Bukkit.getOnlinePlayers().stream().forEach(player -> player.sendTitle("  ", "§b" + e.getPlayer().getName() + "が足を入れました！", 4, 20, 4));
            }

            KaradaSagasi.getPlugin().getBoardManager().update();
            e.getPlayer().getInventory().remove(e.getItem());
            e.setCancelled(true);

            if (runnable.isHelmet() && runnable.isChestplate() && runnable.isLeggings() && runnable.isBoots()) {
                Bukkit.broadcastMessage(KaradaSagasi.PX + "§b体が全て集まったため...");
                Bukkit.getOnlinePlayers().stream().forEach(player -> player.sendTitle("  ", "§b体が全て集まったため...", 4, 20, 4));
                Bukkit.getScheduler().runTaskLater(KaradaSagasi.getPlugin(), () -> {
                    runnable.stop(GameEndState.STUDENT);
                }, 40);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        GameRunnable runnable = KaradaSagasi.getPlugin().getGameRunnable();

        if (runnable.getState() != State.GAMING) return;

        if (runnable.getKillers().contains(e.getDamage())) return;
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent e) {
        GameRunnable runnable = KaradaSagasi.getPlugin().getGameRunnable();

        if (runnable.getState() != State.GAMING) return;

        if (runnable.getStudents().contains(e.getDamager())) {
            e.setCancelled(true);
            return;
        }

        if (runnable.getKillers().contains(e.getDamager()) && runnable.getStudents().contains(e.getEntity())) {
            Player p = (Player) e.getEntity();
            p.damage(100000);
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        GameRunnable runnable = KaradaSagasi.getPlugin().getGameRunnable();

        if (runnable.getState() != State.GAMING) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(KaradaSagasi.getPlugin(), () -> {
                e.getPlayer().teleport(KaradaSagasi.getPlugin().getDataManager().getLobbyPosition());
            });
            return;
        }

        if (!runnable.getStudents().contains(e.getPlayer())) return;

        GamePlayer gamePlayer = KaradaSagasi.getPlugin().getPlayerManager().getGamePlayer(e.getPlayer().getUniqueId());
        gamePlayer.getTempPlayer().setDie(true);
        PrisonLocationMap prisonLocationMap = runnable.getStage().getPrisonLocationMap();
        TeleportLocation teleportLocation = prisonLocationMap.getPrisonTeleportLocations().toArray(
                new TeleportLocation[prisonLocationMap.getPrisonTeleportLocations().size()])[new Random().nextInt(prisonLocationMap.getPrisonTeleportLocations().size())];

        Bukkit.getScheduler().scheduleSyncDelayedTask(KaradaSagasi.getPlugin(), () -> {
            e.getPlayer().teleport(teleportLocation.getLocation());
            runnable.getDiedStudents().add(e.getPlayer());
            e.getPlayer().setPlayerListName("§7[生徒] " + e.getPlayer().getName() + " §7[§7Lv:§7" + gamePlayer.getHasLevel().getValue() + "§7]");
            KaradaSagasi.getPlugin().getBoardManager().update();
        });

    }
}
