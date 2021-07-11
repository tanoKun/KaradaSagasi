package com.github.tanokun.karadasagasi.util.particle.data;

import com.github.tanokun.karadasagasi.util.particle.ParticleEffect;

public abstract class ParticleData {
    private ParticleEffect effect;

    public void setEffect(ParticleEffect effect) {
        this.effect = effect;
    }

    public abstract Object toNMSData();

    public ParticleEffect getEffect() {
        return effect;
    }
}