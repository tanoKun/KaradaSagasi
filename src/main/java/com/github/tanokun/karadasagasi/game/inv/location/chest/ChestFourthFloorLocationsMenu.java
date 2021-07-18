package com.github.tanokun.karadasagasi.game.inv.location.chest;

import com.github.tanokun.karadasagasi.KaradaSagasi;
import com.github.tanokun.karadasagasi.game.stage.Stage;
import com.github.tanokun.karadasagasi.util.ItemUtils;
import com.github.tanokun.karadasagasi.util.YamlUtils;
import com.github.tanokun.karadasagasi.util.handle.BreakBlockHandle;
import com.github.tanokun.karadasagasi.util.smart_inv.inv.ClickableItem;
import com.github.tanokun.karadasagasi.util.smart_inv.inv.SmartInventory;
import com.github.tanokun.karadasagasi.util.smart_inv.inv.contents.InventoryContents;
import com.github.tanokun.karadasagasi.util.smart_inv.inv.contents.InventoryProvider;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

public class ChestFourthFloorLocationsMenu implements InventoryProvider {
    private final Stage stage;

    public SmartInventory getInv(){
        return SmartInventory.builder()
                .id("settingStageMenu")
                .title("§c§lステージ >> " + stage.getName() + " >> §d§lロケーション関係 >> §6§lチェスト >> §6§l四階")
                .update(false)
                .updatePeriod(0)
                .provider(this)
                .cancelable(true)
                .size(6, 9)
                .build();
    }

    public ChestFourthFloorLocationsMenu(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        for (Location location : stage.getChestLocationMap().getFourthFloorChestLocations()) {
            contents.add(ClickableItem.of(ItemUtils.createItem(Material.CHEST, "§7Location: " + YamlUtils.LocationToString(location), 1, false), no -> {
                new ChestLocationDeleteCheckMenu(stage, location, 4).getInv().open(player);
            }));
        }

        contents.fillRow(4, ClickableItem.empty(ItemUtils.createItem(Material.STAINED_GLASS_PANE, "  ", 1, false, DyeColor.MAGENTA)));

        contents.set(5, 4, ClickableItem.of(ItemUtils.createItem(Material.ANVIL, "§6§l四階のチェストを作成", 1, false), e -> {
            contents.inventory().close(player);
            new BreakBlockHandle(Material.CHEST, block -> {
                if (stage.getChestLocationMap().getFourthFloorChestLocations().contains(block.getLocation())) {
                    player.sendMessage(KaradaSagasi.PX + "そのチェストは既に追加済みです");
                    KaradaSagasi.playSound(player, Sound.BLOCK_NOTE_BASS, 1 ,1);
                    return;
                }

                stage.getChestLocationMap().getFourthFloorChestLocations().add(block.getLocation());
                player.sendMessage(KaradaSagasi.PX + "§b四階のチェストに「" + YamlUtils.LocationToString(block.getLocation()) + "」を追加しました");

                contents.inventory().open(player);
            }).enable(player);
        }));
    }
}
