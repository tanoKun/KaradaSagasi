package com.github.tanokun.karadasagasi.listener;

import com.github.tanokun.karadasagasi.KaradaSagasi;
import com.github.tanokun.karadasagasi.event.wg.WgRegionEnterEvent;
import com.github.tanokun.karadasagasi.game.GameRunnable;
import com.github.tanokun.karadasagasi.game.State;
import com.github.tanokun.karadasagasi.game.player.GamePlayer;
import com.github.tanokun.karadasagasi.game.stage.teleport.PrisonLocationMap;
import com.github.tanokun.karadasagasi.game.stage.teleport.TeleportLocation;
import com.github.tanokun.karadasagasi.util.io.Config;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.player.*;

import java.util.Random;
import java.util.UUID;

public class StopListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.setJoinMessage("§a[Join] " + e.getPlayer().getName() + "が入室しました");
        KaradaSagasi.getPlugin().getPlayerManager().register(new GamePlayer(e.getPlayer().getUniqueId()).load(new Config("pd.yml", KaradaSagasi.getPlugin()), ""));
        e.getPlayer().setPlayerListName("§7" + e.getPlayer().getName() + " §7[§dLv:§e" + KaradaSagasi.getPlugin().getPlayerManager().getGamePlayer(e.getPlayer().getUniqueId()).getHasLevel().getValue() + "§7]");

        e.getPlayer().teleport(KaradaSagasi.getPlugin().getDataManager().getLobbyPosition());
        KaradaSagasi.getPlugin().getBoardManager().update();

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        KaradaSagasi.getPlugin().getBoardManager().remove(e.getPlayer());
        e.setQuitMessage("§b[Quit] " + e.getPlayer().getName() + "が退出しました");
        GameRunnable gr = KaradaSagasi.getPlugin().getGameRunnable();
        KaradaSagasi.getPlugin().getPlayerManager().getGamePlayer(
                e.getPlayer().getUniqueId()).save(new Config("pd.yml", KaradaSagasi.getPlugin()), e.getPlayer().getUniqueId().toString());
        KaradaSagasi.getPlugin().getPlayerManager().unregister(e.getPlayer().getUniqueId());
        if (gr.getState() == State.GAMING) {
            gr.getPlayers().remove(e.getPlayer());
            gr.getStudents().remove(e.getPlayer());
        } else {
            gr.getPlayers().remove(e.getPlayer());
            gr.getKillers().remove(e.getPlayer());
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        GameRunnable gr = KaradaSagasi.getPlugin().getGameRunnable();
        if (gr.getState() == State.GAMING || !e.getPlayer().isOp()) e.setCancelled(true);
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e) {
        GameRunnable gr = KaradaSagasi.getPlugin().getGameRunnable();
        if (gr.getState() == State.GAMING || !e.getPlayer().isOp()) {
            if (gr.getKillers().contains(e.getPlayer())) e.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemDespawn(ItemDespawnEvent e) {
        GameRunnable gr = KaradaSagasi.getPlugin().getGameRunnable();
        if (gr.getState() == State.GAMING) e.setCancelled(true);
    }
}
