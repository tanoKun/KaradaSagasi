package com.github.tanokun.karadasagasi.game.inv.location.chest;

import com.github.tanokun.karadasagasi.KaradaSagasi;
import com.github.tanokun.karadasagasi.game.inv.location.killer.KillerSpawnLocationsMenu;
import com.github.tanokun.karadasagasi.game.stage.Stage;
import com.github.tanokun.karadasagasi.util.ItemUtils;
import com.github.tanokun.karadasagasi.util.YamlUtils;
import com.github.tanokun.karadasagasi.util.smart_inv.inv.ClickableItem;
import com.github.tanokun.karadasagasi.util.smart_inv.inv.SmartInventory;
import com.github.tanokun.karadasagasi.util.smart_inv.inv.contents.InventoryContents;
import com.github.tanokun.karadasagasi.util.smart_inv.inv.contents.InventoryProvider;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class ChestLocationDeleteCheckMenu implements InventoryProvider {
    private final Stage stage;
    private final Location location;
    private final int floor;

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

    public ChestLocationDeleteCheckMenu(Stage stage, Location location, int floor) {
            this.stage = stage;
            this.location = location;
            this.floor = floor;
        }

        @Override
        public void init(Player player, InventoryContents contents) {
            contents.set(1, 2, ClickableItem.of(ItemUtils.createItem(Material.WOOL, "§a削除します", 1, false, DyeColor.GREEN), no -> {
                KaradaSagasi.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1 ,1);
                switch (floor) {
                    case 1:
                        stage.getChestLocationMap().getFirstFloorChestLocations().remove(location);
                        new ChestFirstFloorLocationsMenu(stage).getInv().open(player);
                        break;
                    case 2:
                        stage.getChestLocationMap().getSecondFloorChestLocations().remove(location);
                        new ChestSecondFloorLocationsMenu(stage).getInv().open(player);
                        break;
                    case 3:
                        stage.getChestLocationMap().getThirdFloorChestLocations().remove(location);
                        new ChestThirdFloorLocationsMenu(stage).getInv().open(player);
                        break;
                    case 4:
                        stage.getChestLocationMap().getFourthFloorChestLocations().remove(location);
                        new ChestFourthFloorLocationsMenu(stage).getInv().open(player);
                        break;
                }
                player.sendMessage(KaradaSagasi.PX + "§bチェスト位置「" + YamlUtils.LocationToString(location) + "§b」を削除しました");
            }));

            contents.set(1, 6, ClickableItem.of(ItemUtils.createItem(Material.WOOL, "§c削除しません", 1, false, DyeColor.RED), no -> {
                switch (floor) {
                    case 1:
                        new ChestFirstFloorLocationsMenu(stage).getInv().open(player);
                        break;
                    case 2:
                        new ChestSecondFloorLocationsMenu(stage).getInv().open(player);
                        break;
                    case 3:
                        new ChestThirdFloorLocationsMenu(stage).getInv().open(player);
                        break;
                    case 4:
                        new ChestFourthFloorLocationsMenu(stage).getInv().open(player);
                        break;
                }
            }));
        }
}
