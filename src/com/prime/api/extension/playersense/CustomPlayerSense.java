package com.prime.api.extension.playersense;

import com.runemate.game.api.hybrid.player_sense.PlayerSense;

/**
 * @author Party
 * @version 1.0
 **/
public interface CustomPlayerSense {

    String getKey();

    default Object get() {
        return PlayerSense.get(getKey());
    }

    default int getAsInt() {
        return PlayerSense.getAsInteger(getKey());
    }

    default double getAsDouble() {
        return PlayerSense.getAsDouble(getKey());
    }

    default boolean getAsBoolean() {
        return PlayerSense.getAsBoolean(getKey());
    }

    default long getAsLong() {
        return PlayerSense.getAsLong(getKey());
    }
}
