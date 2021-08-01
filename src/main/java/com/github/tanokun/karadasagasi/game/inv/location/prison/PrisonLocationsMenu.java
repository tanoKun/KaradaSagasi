package com.github.tanokun.karadasagasi.game.inv.location.prison;

import com.github.tanokun.karadasagasi.KaradaSagasi;
import com.github.tanokun.karadasagasi.game.inv.location.SettingStageLocationsMenu;
import com.github.tanokun.karadasagasi.game.inv.location.killer.KillerSpawnDeleteCheckMenu;
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
import org.bukkit.event.inventory.ClickType;

import java.util.Arrays;

public class PrisonLocationsMenu implements InventoryProvider {
    private final Stage stage;

    public SmartInventory getInv(){
        return SmartInventory.builder()
                .id("settingStageMenu")
                .title("§c§lステージ >> " + stage.getName() + " >> §d§lロケーション関係 >> §b§l牢屋位置")
                .update(false)
                .updatePeriod(0)
                .provider(this)
                .cancelable(true)
                .size(6, 9)
                .build();
    }
    public PrisonLocationsMenu(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        KaradaSagasi.playSound(player, Sound.ENTITY_SHULKER_OPEN, 1 ,1);
        contents.fillRow(4, ClickableItem.empty(ItemUtils.createItem(Material.STAINED_GLASS_PANE, "  ", 1, false, DyeColor.MAGENTA)));

        for (TeleportLocation t : stage.getPrisonLocationMap().getPrisonTeleportLocations()) {
            contents.add(ClickableItem.of(ItemUtils.createItem(Material.BEACON, t.getName(), Arrays.asList("§7Location: " + YamlUtils.LocationToString(t.getLocation()), "§7WG名: " + t.getRgName()), 1, false), no -> {
                if (no.getClick().equals(ClickType.RIGHT)) {
                    new PrisonLocationDeleteCheckMenu(stage, t).getInv().open(player);
                    return;
                }

                new SetPrisonDataMenu(stage, t).getInv().open(player);
            }));
        }

        contents.set(5, 4, ClickableItem.of(ItemUtils.createItem(Material.ANVIL, "§c§l牢屋の作成", 1, false), no -> {
            new AnvilGUI("§b§l牢屋の作成", ItemUtils.createItem(Material.ANVIL, "名前を入力", 1, false), e -> {
                String name = e.getItem().getItemMeta().getDisplayName().replace("&", "§");

                stage.getPrisonLocationMap().getPrisonTeleportLocations().add(new TeleportLocation(player.getLocation(), name));

                KaradaSagasi.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1 ,1);
                player.closeInventory();
                contents.inventory().open(player);
                player.sendMessage(KaradaSagasi.PX + "§b牢屋「" + name + "§b」を作成しました");
            }).open(player);
        }));

        contents.set(5, 8, ClickableItem.of(ItemUtils.createItem(Material.REDSTONE_BLOCK, "§c§l戻る", 1, false), e -> {
            new SettingStageLocationsMenu(stage).getInv().open(player);
        }));
    }
}
