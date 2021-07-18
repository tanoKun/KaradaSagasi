package com.github.tanokun.karadasagasi.game.inv;

import com.github.tanokun.karadasagasi.KaradaSagasi;
import com.github.tanokun.karadasagasi.game.stage.Stage;
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
import org.bukkit.event.inventory.ClickType;

public class SettingStageListMenu implements InventoryProvider {

    public SmartInventory getInv(){
        return SmartInventory.builder()
                .id("settingStageMenu")
                .title("§c§lステージ")
                .update(false)
                .updatePeriod(0)
                .provider(this)
                .cancelable(true)
                .size(6, 9)
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        KaradaSagasi.playSound(player, Sound.ENTITY_SHULKER_OPEN, 1 ,1);

        contents.fillRow(4, ClickableItem.empty(ItemUtils.createItem(Material.STAINED_GLASS_PANE, "  ", 1, false, DyeColor.MAGENTA)));

        for (Stage stage : KaradaSagasi.getPlugin().getStageManager().getStages().values()) {
            contents.add(ClickableItem.of(ItemUtils.createItem(Material.GRASS, stage.getName(), 1, false), e -> {
                if (e.getClick().equals(ClickType.RIGHT)) {
                    new StageDeleteCheckMenu(stage).getInv().open(player);
                    return;
                }
                new SettingStageMenu(stage).getInv().open(player);
            }));
        }

        contents.set(5, 4, ClickableItem.of(ItemUtils.createItem(Material.ANVIL, "§b§lステージ作成", 1, false), no -> {
            new AnvilGUI("§b§lステージ作成", ItemUtils.createItem(Material.ANVIL, "名前を入力", 1, false), e -> {
                String name = e.getItem().getItemMeta().getDisplayName().replace("&", "§");

                if (KaradaSagasi.getPlugin().getStageManager().getStage(name) != null) {
                    KaradaSagasi.playSound(player, Sound.BLOCK_NOTE_BASS, 1 ,1);
                    player.sendMessage(KaradaSagasi.PX + "その名前のステージは既に存在します");
                    player.closeInventory();
                    return;
                }

                KaradaSagasi.getPlugin().getStageManager().registerStage(new Stage(name));

                KaradaSagasi.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1 ,1);
                player.closeInventory();
                contents.inventory().open(player);
                player.sendMessage(KaradaSagasi.PX + "§bステージ「" + name + "§b」を作成しました");
            }).open(player);
        }));
    }
}
