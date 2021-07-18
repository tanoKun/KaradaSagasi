package com.github.tanokun.karadasagasi.game.stage;

import com.github.tanokun.karadasagasi.KaradaSagasi;
import com.github.tanokun.karadasagasi.util.SaveMarker;
import com.github.tanokun.karadasagasi.util.io.Config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class StageManager implements SaveMarker<StageManager> {
    private HashMap<String, Stage> stages = new HashMap<>();

    public void registerStage(Stage stage){
        stages.put(stage.getName(), stage);
    }

    public void unregisterStage(String id){
        stages.remove(id);
    }

    public Stage getStage(String id) {
        return stages.get(id);
    }

    public HashMap<String, Stage> getStages() {
        return stages;
    }

    @Override
    public void save(Config config, String key) {
        config.createExists();

        config.getConfig().getKeys(false).forEach(e -> config.getConfig().set(e, null));

        for (String id : stages.keySet()) {
            stages.get(id).save(config, id);
        }
    }

    @Override
    public StageManager load(Config config, String key) {
        config.saveDefaultConfig();
       if (config.getConfig().getKeys(false) == null) return this;

        for (String stageId : config.getConfig().getKeys(false)) {
            Stage stage = new Stage(config.getConfig().getString(stageId + ".name", "unknown"));
            stage.load(config, stageId);
            stages.put(stageId, stage);
        }

        return this;
    }
}
