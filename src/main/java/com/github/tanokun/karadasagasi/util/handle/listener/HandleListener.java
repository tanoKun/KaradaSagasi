package com.github.tanokun.karadasagasi.util.handle.listener;

import com.github.tanokun.karadasagasi.KaradaSagasi;
import com.github.tanokun.karadasagasi.util.handle.BreakBlockHandle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class HandleListener implements Listener {
    public static final String BREAK_BLOCK = "BREAK_BLOCK_HANDLE";

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        e.getPlayer().removeMetadata(BREAK_BLOCK, KaradaSagasi.getPlugin());
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (!e.getPlayer().hasMetadata(BREAK_BLOCK)) return;
        e.setCancelled(true);

        BreakBlockHandle breakBlockHandle = (BreakBlockHandle) e.getPlayer().getMetadata(BREAK_BLOCK).get(0).value();

        if (e.getBlock().getType().equals(breakBlockHandle.getBlock())) {
            e.getPlayer().removeMetadata(BREAK_BLOCK, KaradaSagasi.getPlugin());
            breakBlockHandle.getFun().accept(e.getBlock());
        }
    }
}
