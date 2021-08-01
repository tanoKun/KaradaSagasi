package com.github.tanokun.karadasagasi.game.inv.location.killer;

import com.github.tanokun.karadasagasi.KaradaSagasi;
import com.github.tanokun.karadasagasi.game.inv.StageDeleteCheckMenu;
import com.github.tanokun.karadasagasi.game.inv.location.SettingStageLocationsMenu;
import com.github.tanokun.karadasagasi.game.stage.Stage;
import com.github.tanokun.karadasagasi.game.stage.teleport.TeleportLocation;
import com.github.tanokun.karadasagasi.util.ItemUtils;
import com.github.tanokun.karadasagasi.util.YamlUtils;
import com.github.tanokun.karadasagasi.util.anvil.AnvilGUI;
import com.github.tanokun.karadasagasi.util.smart_inv.inv.ClickableItem;
import com.github.tanokun.karadasagasi.util.smart_inv.inv.SmartInventory;
import com.github.tanokun.karadasagasi.util.smart_inv.inv.contents.InventoryContents;
import com.github.tanokun.karadasagasi.util.smart_inv.inv.contents.InventoryProvider;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class KillerSpawnLocationsMenu implements InventoryProvider {
    private final Stage stage;

    public SmartInventory getInv(){
        return SmartInventory.builder()
                .id("settingStageMenu")
                .title("§c§lステージ >> " + stage.getName() + " >> §d§lロケーション関係 >> §c§l赤い人のスポーン位置")
                .update(false)
                .updatePeriod(0)
                .provider(this)
                .cancelable(true)
                .size(6, 9)
                .build();
    }

    public KillerSpawnLocationsMenu(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        KaradaSagasi.playSound(player, Sound.ENTITY_SHULKER_OPEN, 1 ,1);
        contents.fillRow(4, ClickableItem.empty(ItemUtils.createItem(Material.STAINED_GLASS_PANE, "  ", 1, false, DyeColor.MAGENTA)));

        for (TeleportLocation teleportLocation : stage.getKillerTeleportLocationMap().getKillerTeleportLocations()) {
            contents.add(ClickableItem.of(ItemUtils.createItem(Material.ANVIL, teleportLocation.getName(), Arrays.asList("§7Location: " + YamlUtils.LocationToString(teleportLocation.getLocation())), 1, false), no -> {
                new KillerSpawnDeleteCheckMenu(stage, teleportLocation).getInv().open(player);
            }));
        }

        contents.set(5, 4, ClickableItem.of(ItemUtils.createItem(Material.ANVIL, "§c§lスポーン位置の作成", 1, false), no -> {
            new AnvilGUI("§c§lスポーン位置の作成", ItemUtils.createItem(Material.ANVIL, "名前を入力", 1, false), e -> {
                String name = e.getItem().getItemMeta().getDisplayName().replace("&", "§");

                stage.getKillerTeleportLocationMap().getKillerTeleportLocations().add(new TeleportLocation(player.getLocation(), name));

                KaradaSagasi.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1 ,1);
                player.closeInventory();
                contents.inventory().open(player);
                player.sendMessage(KaradaSagasi.PX + "§bスポーン位置「" + name + "§b」を作成しました");
            }).open(player);
        }));

        contents.set(5, 8, ClickableItem.of(ItemUtils.createItem(Material.REDSTONE_BLOCK, "§c§l戻る", 1, false), e -> {
            new SettingStageLocationsMenu(stage).getInv().open(player);
        }));
    }
}
