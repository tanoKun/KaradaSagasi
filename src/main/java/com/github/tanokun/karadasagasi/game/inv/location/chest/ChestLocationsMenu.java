package com.github.tanokun.karadasagasi.game.inv.location.chest;

import com.github.tanokun.karadasagasi.KaradaSagasi;
import com.github.tanokun.karadasagasi.game.inv.SettingStageMenu;
import com.github.tanokun.karadasagasi.game.stage.Stage;
import com.github.tanokun.karadasagasi.util.ItemUtils;
import com.github.tanokun.karadasagasi.util.YamlUtils;
import com.github.tanokun.karadasagasi.util.smart_inv.inv.ClickableItem;
import com.github.tanokun.karadasagasi.util.smart_inv.inv.SmartInventory;
import com.github.tanokun.karadasagasi.util.smart_inv.inv.contents.InventoryContents;
import com.github.tanokun.karadasagasi.util.smart_inv.inv.contents.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ChestLocationsMenu implements InventoryProvider {
    private final Stage stage;

    public SmartInventory getInv(){
        return SmartInventory.builder()
                .id("settingStageMenu")
                .title("§c§lステージ >> " + stage.getName() + " >> §d§lロケーション関係 >> §6§lチェスト")
                .update(false)
                .updatePeriod(0)
                .provider(this)
                .cancelable(true)
                .size(1, 9)
                .build();
    }

    public ChestLocationsMenu(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        KaradaSagasi.playSound(player, Sound.ENTITY_SHULKER_OPEN, 1 ,1);

        contents.set(0, 1, ClickableItem.of(ItemUtils.createItem(Material.CHEST, "§6§l1階のチェスト", 1, false), e -> {
            new ChestFirstFloorLocationsMenu(stage).getInv().open(player);
        }));

        contents.set(0, 3, ClickableItem.of(ItemUtils.createItem(Material.CHEST, "§6§l2階のチェスト", 1, false), e -> {
            new ChestSecondFloorLocationsMenu(stage).getInv().open(player);
        }));

        contents.set(0, 5, ClickableItem.of(ItemUtils.createItem(Material.CHEST, "§6§l3階のチェスト", 1, false), e -> {
            new ChestThirdFloorLocationsMenu(stage).getInv().open(player);
        }));

        contents.set(0, 7, ClickableItem.of(ItemUtils.createItem(Material.CHEST, "§6§l4階のチェスト", 1, false), e -> {
            new ChestFourthFloorLocationsMenu(stage).getInv().open(player);
        }));
    }
}
