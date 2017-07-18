package com.prime.api.extension.playersense;

import com.runemate.game.api.hybrid.player_sense.PlayerSense;

import java.util.function.Supplier;

public class BasicPlayerSenseKey implements PlayerSensable {

    private String key;
    private Supplier value;

    public BasicPlayerSenseKey(String key, Supplier value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public Supplier getInitialValue() {
        return value;
    }

    @Override
    public Object getValue() {
        return PlayerSense.get(getKey());
    }


}
