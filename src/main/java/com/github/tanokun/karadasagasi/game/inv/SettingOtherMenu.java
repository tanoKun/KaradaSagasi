package com.github.tanokun.karadasagasi.game.inv;

import com.github.tanokun.karadasagasi.KaradaSagasi;
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

        contents.set(0, 1, ClickableItem.of(ItemUtils.createItem(Material.DIAMOND, "§6§l収納箱の設定", Arrays.asList("§7現在: " + YamlUtils.LocationToString(stage.getBodyLocation())), 1, false), e -> {
            KaradaSagasi.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1 ,1);

            stage.setBodyLocation(player.getLocation());
            player.sendMessage(KaradaSagasi.PX + "§b収納箱を変更しました");
            contents.inventory().open(player);
        }));

    }
}
