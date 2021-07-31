package com.github.tanokun.karadasagasi.game.player;

import com.github.tanokun.karadasagasi.KaradaSagasi;
import com.github.tanokun.karadasagasi.game.State;
import com.github.tanokun.karadasagasi.util.SaveMarker;
import com.github.tanokun.karadasagasi.util.io.Config;
import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GamePlayer implements SaveMarker<GamePlayer> {
    private final UUID uuid;

    private LevelType hasLevel = LevelType.Lv_1;

    private long hasEXP = 0;

    private TempPlayer tempPlayer = new TempPlayer();

    private int joinGameCount = 0;

    private int winKillerCount = 0;

    private int winStudentCount = 0;

    private int killStudentCount = 0;

    public GamePlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public LevelType getHasLevel() {
        return hasLevel;
    }

    public TempPlayer getTempPlayer() {
        return tempPlayer;
    }

    public long getHasEXP() {
        return hasEXP;
    }

    public int getJoinGameCount() {
        return joinGameCount;
    }

    public int getWinKillerCount() {
        return winKillerCount;
    }

    public int getWinStudentCount() {
        return winStudentCount;
    }

    public int getKillStudentCount() {
        return killStudentCount;
    }

    public void setHasEXP(long hasEXP) {
        this.hasEXP = hasEXP;
        Player p = Bukkit.getPlayer(uuid);

        if (!hasLevel.hasNext()) {
            this.hasEXP = 0;
            return;
        }

        for (int i = 0; this.hasEXP >= hasLevel.getMaxEXP(); i++) {
            KaradaSagasi.getPlugin().getBoardManager().update();
            this.hasEXP = this.hasEXP - hasLevel.getMaxEXP();
            hasLevel = (hasLevel.getNext());
            Bukkit.getPlayer(uuid).sendMessage(KaradaSagasi.PX + "§aレベルが §b" + hasLevel + "Lv §aになりました！");

            if (KaradaSagasi.getPlugin().getGameRunnable().getState() == State.WAIT) {
                Bukkit.getPlayer(uuid).setPlayerListName("§7" + p.getName() + " §7[§dLv:§e" + getHasLevel().getValue() + "§7]");
            } else if (KaradaSagasi.getPlugin().getGameRunnable().getStudents().contains(p)) {
                Bukkit.getPlayer(uuid).setPlayerListName("§b[生徒] " + p.getName() + " §7[§dLv:§e" + getHasLevel().getValue() + "§7]");
            } else if (KaradaSagasi.getPlugin().getGameRunnable().getKillers().contains(p)) {
                Bukkit.getPlayer(uuid).setPlayerListName("§c[赤い人] " + p.getName() + " §7[§dLv:§e" + getHasLevel().getValue() + "§7]");
            }
        }
    }

    public void addHasEXP(long hasEXP) {
        setHasEXP(getHasEXP() + hasEXP);
    }

    public void setTempPlayer(TempPlayer tempPlayer) {
        this.tempPlayer = tempPlayer;
    }

    public void setJoinGameCount(int joinGameCount) {
        this.joinGameCount = joinGameCount;
    }

    public void setWinKillerCount(int winKillerCount) {
        this.winKillerCount = winKillerCount;
    }

    public void setWinStudentCount(int winStudentCount) {
        this.winStudentCount = winStudentCount;
    }

    public void setKillStudentCount(int killStudentCount) {
        this.killStudentCount = killStudentCount;
    }

    @Override
    public void save(Config config, String key) {
        config.createExists();
        config.getConfig().set(uuid.toString(), new Gson().toJson(this));
        config.saveConfig();
    }

    @Override
    public GamePlayer load(Config config, String key) {
        if (config.getConfig().getString(uuid.toString(), "").equals("")) return this;
        return new Gson().fromJson(config.getConfig().getString(uuid.toString()), GamePlayer.class);
    }
}
