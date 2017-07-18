package com.prime.api.extension.playersense;

import com.runemate.game.api.hybrid.player_sense.PlayerSense;
import com.runemate.game.api.hybrid.util.calculations.Random;

import java.util.function.Supplier;

public class GaussianKey implements PlayerSensable {

    private String key;
    private double min;
    private double max;

    public GaussianKey(String key, double min, double max) {
        this.key = key;
        this.min = min;
        this.max = max;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public Supplier getInitialValue() {
        return this::generatePlayerSenseValue;
    }

    @Override
    public Object getValue() {
        return Random.nextGaussian(min, max, PlayerSense.getAsDouble(key));
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    private double generatePlayerSenseValue() {
        return Random.nextDouble(min, max);
    }
}
