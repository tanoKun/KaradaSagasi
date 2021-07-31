package com.github.tanokun.karadasagasi.game.player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerManager {
    private final HashMap<UUID, GamePlayer> gamePlayers = new HashMap<>();

    public void register(GamePlayer gamePlayer) {
        gamePlayers.put(gamePlayer.getUuid(), gamePlayer);
    }

    public void unregister(GamePlayer gamePlayer) {
        gamePlayers.remove(gamePlayer.getUuid());
    }

    public void unregister(UUID uuid) {
        gamePlayers.remove(uuid);
    }

    public GamePlayer getGamePlayer(UUID uuid) {
        return gamePlayers.get(uuid);
    }

    public HashMap<UUID, GamePlayer> getGamePlayers() {
        return gamePlayers;
    }
}
