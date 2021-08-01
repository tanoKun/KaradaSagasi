package com.github.tanokun.karadasagasi.game.inv;

import com.github.tanokun.karadasagasi.KaradaSagasi;
import com.github.tanokun.karadasagasi.game.inv.location.chest.ChestLocationsMenu;
import com.github.tanokun.karadasagasi.game.stage.Stage;
import com.github.tanokun.karadasagasi.util.ItemUtils;
import com.github.tanokun.karadasagasi.util.YamlUtils;
import com.github.tanokun.karadasagasi.util.anvil.AnvilGUI;
import com.github.tanokun.karadasagasi.util.handle.BreakBlockHandle;
import com.github.tanokun.karadasagasi.util.smart_inv.inv.ClickableItem;
import com.github.tanokun.karadasagasi.util.smart_inv.inv.SmartInventory;
import com.github.tanokun.karadasagasi.util.smart_inv.inv.contents.InventoryContents;
import com.github.tanokun.karadasagasi.util.smart_inv.inv.contents.InventoryProvider;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class SettingOtherMenu implements InventoryProvider {
    private final Stage stage;

    public SmartInventory getInv(){
        return SmartInventory.builder()
        .id("settingStageMenu")
        .title("§c§lステージ >> " + stage.getName() + " >> §f§lその他")
        .update(false)
        .updatePeriod(0)
        .provider(this)
        .cancelable(true)
        .size(1, 9)
        .build();
        }

    public SettingOtherMenu(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        KaradaSagasi.playSound(player, Sound.ENTITY_SHULKER_OPEN, 1 ,1);

        contents.set(0, 1, ClickableItem.of(ItemUtils.createItem(Material.CHEST, "§6§l収納箱の設定", Arrays.asList("§7現在: " + YamlUtils.LocationToString(stage.getBodyLocation())), 1, false), e -> {
            KaradaSagasi.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1 ,1);
            contents.inventory().close(player);
            player.sendMessage(KaradaSagasi.PX + "§b収納箱を壊してください");

            new BreakBlockHandle(Material.CHEST, block -> {
                stage.setBodyLocation(block.getLocation());
                player.sendMessage(KaradaSagasi.PX + "§b収納箱を変更しました");

                contents.inventory().open(player);
            }).enable(player);
        }));

        contents.set(0, 3, ClickableItem.of(ItemUtils.createItem(Material.DIAMOND, "§a§l最少人数", Arrays.asList("§7現在: " + stage.getMinPlayerAmount()), 1, false), np -> {
            new AnvilGUI("§a§l最少人数", ItemUtils.createItem(Material.ANVIL, "最少人数を入力", 1, false), e -> {
                String a = e.getItem().getItemMeta().getDisplayName();

                if (!StringUtils.isNumeric(a)) {
                    KaradaSagasi.playSound(player, Sound.BLOCK_NOTE_BASS, 1 ,1);
                    player.sendMessage(KaradaSagasi.PX + "数字で入力してください");
                    player.closeInventory();
                    return;
                }

                stage.setMinPlayerAmount(Integer.valueOf(a));

                KaradaSagasi.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1 ,1);
                player.closeInventory();
                contents.inventory().open(player);
                player.sendMessage(KaradaSagasi.PX + "§bステージの最少人数を「" + a + "§b」人に設定しました");
            }).open(player);
        }));

        contents.set(0, 4, ClickableItem.of(ItemUtils.createItem(Material.DIAMOND, "§a§l最大人数", Arrays.asList("§7現在: " + stage.getMaxPlayerAmount()), 1, false), np -> {
            new AnvilGUI("§a§l最大人数", ItemUtils.createItem(Material.ANVIL, "最大人数を入力", 1, false), e -> {
                String a = e.getItem().getItemMeta().getDisplayName();

                if (!StringUtils.isNumeric(a)) {
                    KaradaSagasi.playSound(player, Sound.BLOCK_NOTE_BASS, 1 ,1);
                    player.sendMessage(KaradaSagasi.PX + "数字で入力してください");
                    player.closeInventory();
                    return;
                }

                stage.setMaxPlayerAmount(Integer.valueOf(a));

                KaradaSagasi.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1 ,1);
                player.closeInventory();
                contents.inventory().open(player);
                player.sendMessage(KaradaSagasi.PX + "§bステージの最大人数を「" + a + "§b」人に設定しました");
            }).open(player);
        }));

        contents.set(0, 8, ClickableItem.of(ItemUtils.createItem(Material.REDSTONE_BLOCK, "§c§l戻る", 1, false), e -> {
            new SettingStageMenu(stage).getInv().open(player);
        }));
    }
}
