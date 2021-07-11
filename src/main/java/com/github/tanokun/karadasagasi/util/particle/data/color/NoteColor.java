package com.github.tanokun.karadasagasi.util.particle.data.color;


import com.github.tanokun.karadasagasi.util.particle.ParticleEffect;
import com.github.tanokun.karadasagasi.util.particle.util.MathUtils;

public class NoteColor extends ParticleColor {

    public NoteColor(int note) {
        super(MathUtils.getMaxOrMin(note, 24, 0), 0, 0);
        this.setEffect(ParticleEffect.NOTE);
    }

    public void setEffect(ParticleEffect effect) {
        super.setEffect(ParticleEffect.NOTE);
    }

    public float getRed() {
        return super.getRed() / 24.0F;
    }

    public float getGreen() {
        return 0.0F;
    }

    public float getBlue() {
        return 0.0F;
    }

    public Object toNMSData() {
        return null;
    }

    public static NoteColor random() {
        return new NoteColor(MathUtils.generateRandomInteger(0, 24));
    }
}