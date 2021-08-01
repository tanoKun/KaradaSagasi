package com.github.tanokun.karadasagasi.game.inv.location;

import com.github.tanokun.karadasagasi.KaradaSagasi;
import com.github.tanokun.karadasagasi.game.inv.SettingStageMenu;
import com.github.tanokun.karadasagasi.game.inv.location.chest.ChestLocationsMenu;
import com.github.tanokun.karadasagasi.game.inv.location.killer.KillerSpawnLocationsMenu;
import com.github.tanokun.karadasagasi.game.inv.location.prison.PrisonLocationsMenu;
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

public class SettingStageLocationsMenu implements InventoryProvider {
    private final Stage stage;

    public SmartInventory getInv(){
        return SmartInventory.builder()
                .id("settingStageMenu")
                .title("§c§lステージ >> " + stage.getName() + " >> §d§lロケーション関係")
                .update(false)
                .updatePeriod(0)
                .provider(this)
                .cancelable(true)
                .size(3, 9)
                .build();
    }

    public SettingStageLocationsMenu(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        KaradaSagasi.playSound(player, Sound.ENTITY_SHULKER_OPEN, 1 ,1);

        contents.set(0, 1, ClickableItem.of(ItemUtils.createItem(Material.DIAMOND, "§b§l生徒のスポーン位置", Arrays.asList("§7現在: " + YamlUtils.LocationToString(stage.getSpawnStudentLocation())), 1, false), e -> {
            KaradaSagasi.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1 ,1);

            stage.setSpawnStudentLocation(player.getLocation());
            player.sendMessage(KaradaSagasi.PX + "§b生徒のスポーン位置を変更しました");
            contents.inventory().open(player);
        }));

        contents.set(1, 3, ClickableItem.of(ItemUtils.createItem(Material.REDSTONE, "§c§l赤い人のスポーン位置", 1, false),e -> {
            new KillerSpawnLocationsMenu(stage).getInv().open(player);
        }));

        contents.set(1, 5, ClickableItem.of(ItemUtils.createItem(Material.BEACON, "§b§l牢屋位置", 1, false),e -> {
            new PrisonLocationsMenu(stage).getInv().open(player);
        }));

        contents.set(1, 7, ClickableItem.of(ItemUtils.createItem(Material.CHEST, "§6§lチェスト位置", 1, false),e -> {
            new ChestLocationsMenu(stage).getInv().open(player);
        }));

        contents.set(2, 8, ClickableItem.of(ItemUtils.createItem(Material.REDSTONE_BLOCK, "§c§l戻る", 1, false), e -> {
            new SettingStageMenu(stage).getInv().open(player);
        }));

    }
}
