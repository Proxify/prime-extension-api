package com.prime.api.extension.playersense;

import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.player_sense.PlayerSense;
import com.runemate.game.api.hybrid.util.calculations.Random;

import java.util.Collection;
import java.util.function.Supplier;

public class PlayerSenseCoordinate implements PlayerSensable {

    private String key;
    private Coordinate[] coordinates;
    private Coordinate coordinate;

    public PlayerSenseCoordinate(String key, Coordinate... coordinates) {
        this.key = key;
        this.coordinates = coordinates;
    }

    public PlayerSenseCoordinate(String key, Collection<Coordinate> coordinates) {
        this.key = key;
        this.coordinates = coordinates.toArray(new Coordinate[coordinates.size()]);
    }

    public Coordinate[] getCoordinates() {
        return coordinates;
    }

    public String getKey() {
        return key;
    }

    @Override
    public Supplier getInitialValue() {
        return () -> Random.nextElement(getCoordinates()).hashCode();
    }

    @Override
    public Object getValue() {
        return PlayerSense.get(getKey());
    }

    public Coordinate get() {
        if (coordinate == null) {
            Integer code = PlayerSense.getAsInteger(getKey());
            if (code == null) {
                Coordinate selected = Random.nextElement(getCoordinates());
                PlayerSense.put(getKey(), selected.hashCode());
                return (coordinate = selected);
            }
            return (coordinate = new Coordinate(code));
        }
        return coordinate;
    }
}
