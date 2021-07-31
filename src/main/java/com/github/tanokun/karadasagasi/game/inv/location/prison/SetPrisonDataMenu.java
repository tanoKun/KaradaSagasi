package com.github.tanokun.karadasagasi.game.inv.location.prison;

import com.github.tanokun.karadasagasi.KaradaSagasi;
import com.github.tanokun.karadasagasi.game.stage.Stage;
import com.github.tanokun.karadasagasi.game.stage.teleport.TeleportLocation;
import com.github.tanokun.karadasagasi.util.ItemUtils;
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

public class SetPrisonDataMenu implements InventoryProvider {
    private final Stage stage;
    private final TeleportLocation teleportLocation;

    public SmartInventory getInv(){
        return SmartInventory.builder()
                .id("settingStageMenu")
                .title("§c§lステージ >> " + stage.getName() + " >> §d§lロケーション関係 >> §b§l牢屋位置")
                .update(false)
                .updatePeriod(0)
                .provider(this)
                .cancelable(true)
                .size(1, 9)
                .build();
    }
    public SetPrisonDataMenu(Stage stage, TeleportLocation teleportLocation) {
        this.stage = stage;
        this.teleportLocation = teleportLocation;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        KaradaSagasi.playSound(player, Sound.ENTITY_SHULKER_OPEN, 1 ,1);

        contents.set(0, 2, ClickableItem.of(ItemUtils.createItem(Material.WOOD_AXE, "§b§lWGの領域名変更", 1, false), no -> {
            new AnvilGUI("§b§lWGの領域名変更", ItemUtils.createItem(Material.ANVIL, "名前を入力", Arrays.asList("WG名: " + teleportLocation.getRgName()), 1, false), e -> {
                String name = e.getItem().getItemMeta().getDisplayName();

                KaradaSagasi.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1 ,1);
                teleportLocation.setRgName(name);
                player.closeInventory();
                contents.inventory().open(player);
                player.sendMessage(KaradaSagasi.PX + "§b領域名を「" + name + "§b」に変更しました");
            }).open(player);
        }));

        contents.set(0, 6, ClickableItem.of(ItemUtils.createItem(Material.FEATHER, "§c§l牢屋名", Arrays.asList("牢屋名: " + teleportLocation.getName()), 1, false), no -> {
            new AnvilGUI("§c§l牢屋名", ItemUtils.createItem(Material.ANVIL, "名前を入力 (" + "§7現在: " + teleportLocation.getName() + ")", 1, false), e -> {
                String name = e.getItem().getItemMeta().getDisplayName();

                KaradaSagasi.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1 ,1);
                teleportLocation.setName(name);
                player.closeInventory();
                contents.inventory().open(player);
                player.sendMessage(KaradaSagasi.PX + "§b牢屋名を「" + name + "§b」に変更しました");
            }).open(player);
        }));
    }
}
