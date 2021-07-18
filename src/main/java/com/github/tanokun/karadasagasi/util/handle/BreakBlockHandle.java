package com.github.tanokun.karadasagasi.util.handle;

import com.github.tanokun.karadasagasi.KaradaSagasi;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.function.Consumer;

import static com.github.tanokun.karadasagasi.util.handle.listener.HandleListener.BREAK_BLOCK;

public class BreakBlockHandle {
    private final Material block;

    private final Consumer<Block> fun;

    public BreakBlockHandle(Material block, Consumer<Block> fun) {
        this.block = block;
        this.fun = fun;
    }

    public void enable(Player player) {
        player.setMetadata(BREAK_BLOCK, new FixedMetadataValue(KaradaSagasi.getPlugin(), this));
    }

    public Material getBlock() {
        return block;
    }

    public Consumer<Block> getFun() {
        return fun;
    }
}
