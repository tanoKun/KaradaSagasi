package com.github.tanokun.karadasagasi.listener;

import com.github.tanokun.karadasagasi.KaradaSagasi;
import com.github.tanokun.karadasagasi.game.GameRunnable;
import com.github.tanokun.karadasagasi.game.State;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class StopListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.setJoinMessage("§a[Join] " + e.getPlayer().getName() + "が入室しました");
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        e.setQuitMessage("§b[Quit] " + e.getPlayer().getName() + "が退出しました");
        GameRunnable gr = KaradaSagasi.getPlugin().getGameRunnable();
        if (gr.getState() == State.GAMING) {
            if (gr.getStudents().contains(e.getPlayer())) {
                gr.getDiedStudents().add(e.getPlayer());
            }
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
}
