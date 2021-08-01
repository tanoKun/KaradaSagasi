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
import com.github.tanokun.karadasagasi.util.smart_inv.inv.contents.Pagination;
import com.github.tanokun.karadasagasi.util.smart_inv.inv.contents.SlotIterator;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;

import java.util.ArrayList;

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
        Pagination pagination = contents.pagination();

        ArrayList<Location> locations = stage.getChestLocationMap().getFourthFloorChestLocations();

        ClickableItem[] items = new ClickableItem[locations.size()];

        contents.fillRow(4, ClickableItem.empty(ItemUtils.createItem(Material.STAINED_GLASS_PANE, "  ", 1, false, DyeColor.MAGENTA)));

        contents.set(5, 4, ClickableItem.of(ItemUtils.createItem(Material.ANVIL, "§6§l四階のチェストを作成", 1, false), e -> {
            contents.inventory().close(player);
            new BreakBlockHandle(Material.CHEST, block -> {
                if (locations.contains(block.getLocation())) {
                    player.sendMessage(KaradaSagasi.PX + "そのチェストは既に追加済みです");
                    KaradaSagasi.playSound(player, Sound.BLOCK_NOTE_BASS, 1 ,1);
                    return;
                }

                locations.add(block.getLocation());
                player.sendMessage(KaradaSagasi.PX + "§b四階のチェストに「" + YamlUtils.LocationToString(block.getLocation()) + "」を追加しました");

                contents.inventory().open(player);
            }).enable(player);
        }));

        contents.set(5, 6, ClickableItem.of(ItemUtils.createItem(Material.REDSTONE_BLOCK, "§c§l戻る", 1, false), e -> {
            new ChestLocationsMenu(stage).getInv().open(player);
        }));

        for (int i = 0; i < locations.size(); i++) {
            int finalI = i;
            items[i] = ClickableItem.of(ItemUtils.createItem(Material.CHEST, "§7Location: " + YamlUtils.LocationToString(locations.get(i)), 1, false), no -> {
                new ChestLocationDeleteCheckMenu(stage, locations.get(finalI), 1).getInv().open(player);
            });
        }

        pagination.setItems(items);
        pagination.setItemsPerPage(36);
        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));

        arrow(player, contents);
    }

    private void arrow(Player player, InventoryContents contents){
        Pagination pagination = contents.pagination();

        if (pagination.getPage() == 0)
            contents.set(5, 0, ClickableItem.empty(new ItemStack(Material.AIR)));
        else
            contents.set(5, 0, ClickableItem.of(ItemUtils.createItem(Material.SPECTRAL_ARROW,
                    "§aPrevious Page §b-> " + (pagination.getPage()), 1, true), e -> {
                contents.inventory().open(player, pagination.getPage() - 1);
                KaradaSagasi.playSound(player, Sound.ENTITY_SHULKER_OPEN, 10, 1);
            }));

        if (pagination.isLast())
            contents.set(5, 8, ClickableItem.empty(new ItemStack(Material.AIR)));
        else
            contents.set(5, 8, ClickableItem.of(ItemUtils.createItem(Material.SPECTRAL_ARROW,
                    "§aNext Page §b-> " + (pagination.getPage() + 2), 1, true), e -> {
                contents.inventory().open(player, pagination.getPage() + 1);
                KaradaSagasi.playSound(player, Sound.ENTITY_SHULKER_OPEN, 10, 1);
            }));
    }
}
