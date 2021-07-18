package com.github.tanokun.karadasagasi.game.inv;

import com.github.tanokun.karadasagasi.KaradaSagasi;
import com.github.tanokun.karadasagasi.game.inv.location.SettingStageLocationsMenu;
import com.github.tanokun.karadasagasi.game.stage.Stage;
import com.github.tanokun.karadasagasi.util.ItemUtils;
import com.github.tanokun.karadasagasi.util.anvil.AnvilGUI;
import com.github.tanokun.karadasagasi.util.smart_inv.inv.ClickableItem;
import com.github.tanokun.karadasagasi.util.smart_inv.inv.SmartInventory;
import com.github.tanokun.karadasagasi.util.smart_inv.inv.contents.InventoryContents;
import com.github.tanokun.karadasagasi.util.smart_inv.inv.contents.InventoryProvider;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class SettingStageMenu implements InventoryProvider {
    private final Stage stage;

    public SmartInventory getInv(){
        return SmartInventory.builder()
                .id("settingStageMenu")
                .title("§c§lステージ >> " + stage.getName())
                .update(false)
                .updatePeriod(0)
                .provider(this)
                .cancelable(true)
                .size(1, 9)
                .build();
    }

    public SettingStageMenu(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        KaradaSagasi.playSound(player, Sound.ENTITY_SHULKER_OPEN, 1 ,1);

        contents.set(0, 1, ClickableItem.of(ItemUtils.createItem(Material.WATCH, "§b§l時間変更", Arrays.asList("§7現在: " + stage.getTime()), 1, false), no -> {
            new AnvilGUI("§b§l時間変更", ItemUtils.createItem(Material.ANVIL, "時間を入力", 1, false), e -> {
                String time = e.getItem().getItemMeta().getDisplayName();

                if (!StringUtils.isNumeric(time)) {
                    KaradaSagasi.playSound(player, Sound.BLOCK_NOTE_BASS, 1 ,1);
                    player.sendMessage(KaradaSagasi.PX + "数字で入力してください");
                    player.closeInventory();
                    return;
                }

                stage.setTime(Integer.valueOf(time));

                KaradaSagasi.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1 ,1);
                player.closeInventory();
                contents.inventory().open(player);
                player.sendMessage(KaradaSagasi.PX + "§bステージの時間を「" + time + "§b」秒に設定しました");
            }).open(player);
        }));

        contents.set(0, 4, ClickableItem.of(ItemUtils.createItem(Material.ENDER_PORTAL_FRAME, "§d§lロケーション関係", 1, false), e -> {
            new SettingStageLocationsMenu(stage).getInv().open(player);
        }));

        contents.set(0, 7, ClickableItem.of(ItemUtils.createItem(Material.MAP, "§f§lその他", 1, false), e -> {
            new SettingOtherMenu(stage).getInv().open(player);
        }));
    }
}
