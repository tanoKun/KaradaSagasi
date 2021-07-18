package com.github.tanokun.karadasagasi.game.inv;

import com.github.tanokun.karadasagasi.KaradaSagasi;
import com.github.tanokun.karadasagasi.game.stage.Stage;
import com.github.tanokun.karadasagasi.util.ItemUtils;
import com.github.tanokun.karadasagasi.util.smart_inv.inv.ClickableItem;
import com.github.tanokun.karadasagasi.util.smart_inv.inv.SmartInventory;
import com.github.tanokun.karadasagasi.util.smart_inv.inv.contents.InventoryContents;
import com.github.tanokun.karadasagasi.util.smart_inv.inv.contents.InventoryProvider;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class StageDeleteCheckMenu implements InventoryProvider {
    private final Stage stage;

    public SmartInventory getInv(){
        return SmartInventory.builder()
                .id("settingStageMenu")
                .title("§c§lステージを削除します。よろしいですか")
                .update(false)
                .updatePeriod(0)
                .provider(this)
                .cancelable(true)
                .size(3, 9)
                .build();
    }

    public StageDeleteCheckMenu(Stage stage) {
        this.stage = stage;
    }


    @Override
    public void init(Player player, InventoryContents contents) {
        contents.set(1, 2, ClickableItem.of(ItemUtils.createItem(Material.WOOL, "§a削除します", 1, false, DyeColor.GREEN), no -> {
            KaradaSagasi.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1 ,1);
            KaradaSagasi.getPlugin().getStageManager().unregisterStage(stage.getName());
            player.sendMessage(KaradaSagasi.PX + "§bステージ「" + stage.getName() + "§b」を削除しました");
            new SettingStageListMenu().getInv().open(player);
        }));

        contents.set(1, 6, ClickableItem.of(ItemUtils.createItem(Material.WOOL, "§c削除しません", 1, false, DyeColor.RED), no -> {
            new SettingStageListMenu().getInv().open(player);
        }));
    }
}
