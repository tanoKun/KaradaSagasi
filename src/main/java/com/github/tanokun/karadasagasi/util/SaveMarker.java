package com.github.tanokun.karadasagasi.util;

import com.github.tanokun.karadasagasi.util.io.Config;

public interface SaveMarker<V> {
    void save(Config config, String key);
    V load(Config config, String key);
}
