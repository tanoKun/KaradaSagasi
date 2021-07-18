package com.github.tanokun.karadasagasi.game.inv.location.killer;

import com.github.tanokun.karadasagasi.KaradaSagasi;
import com.github.tanokun.karadasagasi.game.stage.Stage;
import com.github.tanokun.karadasagasi.game.stage.teleport.TeleportLocation;
import com.github.tanokun.karadasagasi.util.ItemUtils;
import com.github.tanokun.karadasagasi.util.smart_inv.inv.ClickableItem;
import com.github.tanokun.karadasagasi.util.smart_inv.inv.SmartInventory;
import com.github.tanokun.karadasagasi.util.smart_inv.inv.contents.InventoryContents;
import com.github.tanokun.karadasagasi.util.smart_inv.inv.contents.InventoryProvider;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class KillerSpawnDeleteCheckMenu implements InventoryProvider {
    private final Stage stage;
    private final TeleportLocation teleportLocation;


    public SmartInventory getInv(){
        return SmartInventory.builder()
                .id("settingStageMenu")
                .title("§c§lスポーン位置を削除します。よろしいですか")
                .update(false)
                .updatePeriod(0)
                .provider(this)
                .cancelable(true)
                .size(3, 9)
                .build();
    }

    public KillerSpawnDeleteCheckMenu(Stage stage, TeleportLocation teleportLocation) {
        this.stage = stage;
        this.teleportLocation = teleportLocation;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.set(1, 2, ClickableItem.of(ItemUtils.createItem(Material.WOOL, "§a削除します", 1, false, DyeColor.GREEN), no -> {
            KaradaSagasi.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1 ,1);
            stage.getKillerTeleportLocationMap().getKillerTeleportLocations().remove(teleportLocation);
            player.sendMessage(KaradaSagasi.PX + "§bスポーン位置「" + teleportLocation.getName() + "§b」を削除しました");
            new KillerSpawnLocationsMenu(KaradaSagasi.getPlugin().getStageManager().getStage(stage.getName())).getInv().open(player);
        }));

        contents.set(1, 6, ClickableItem.of(ItemUtils.createItem(Material.WOOL, "§c削除しません", 1, false, DyeColor.RED), no -> {
            new KillerSpawnLocationsMenu(KaradaSagasi.getPlugin().getStageManager().getStage(stage.getName())).getInv().open(player);
        }));
    }
}
