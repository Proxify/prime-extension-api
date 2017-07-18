package com.prime.api.extension.game.osrs.local;

import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.local.Varbit;
import com.runemate.game.api.hybrid.local.Varbits;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceContainers;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.queries.results.InterfaceComponentQueryResults;
import com.runemate.game.api.osrs.local.hud.interfaces.ControlPanelTab;
import com.runemate.game.api.script.Execution;

/**
 * @author Party
 * @version 1.0
 **/
public enum AutoCast {

    WIND_STRIKE(1),
    WATER_STRIKE(2),
    EARTH_STRIKE(3),
    FIRE_STRIKE(4),
    WIND_BOLT(5),
    WATER_BOLT(6),
    EARTH_BOLT(7),
    FIRE_BOLT(8),
    WIND_BLAST(9),
    WATER_BLAST(10),
    EARTH_BLAST(11),
    FIRE_BLAST(12),
    WIND_WAVE(13),
    WATER_WAVE(14),
    EARTH_WAVE(15),
    FIRE_WAVE(16);

    private final int value;

    AutoCast(int value) {
        this.value = value;
    }

    public static boolean isOpen() {
        return InterfaceContainers.isLoaded(201);
    }

    public static boolean open() {
        if (isOpen()) {
            return true;
        }
        if (!ControlPanelTab.COMBAT_OPTIONS.isOpen()) {
            ControlPanelTab.COMBAT_OPTIONS.open();
            return false;
        }
        final InterfaceComponentQueryResults results = Interfaces.newQuery().containers(593).types(InterfaceComponent.Type.SPRITE).actions("Choose spell").results();
        InterfaceComponent button = null;
        Environment.getLogger().debug("Found components: " + results);
        for (final InterfaceComponent result : results) {
            if (button == null || button.getIndex() < result.getIndex()) {
                button = result;
            }
        }
        return button != null && button.interact("Choose spell");
    }

    public static boolean isAutoCasting() {
        final Varbit value = Varbits.load(275);
        return value != null && value.getValue() == 1;
    }

    public static AutoCast getAutoCastedSpell() {
        final Varbit varbit = Varbits.load(276);
        final int value = varbit == null ? 0 : varbit.getValue();
        for (final AutoCast cast : values()) {
            if (cast.value == value) {
                return cast;
            }
        }
        return null;
    }

    public static boolean canAutoCast() {
        final Varbit varbit = Varbits.load(357);
        return varbit != null && varbit.getValue() == 18;
    }

    public boolean select() {
        if (isSelected()) {
            return true;
        }
        if (!isOpen()) {
            open();
            return false;
        }
        final InterfaceComponent component = getComponent();
        return component != null && component.interact(getName()) && Execution.delayUntil(this::isSelected, 600, 1200);
    }

    private InterfaceComponent getComponent() {
        return Interfaces.newQuery().containers(201).actions(getName()).types(InterfaceComponent.Type.SPRITE).results().first();
    }

    public String getName() {
        return name().substring(0, 1) + name().substring(1).toLowerCase().replace("_", " ");
    }

    public boolean isSelected() {
        final Varbit varbit = Varbits.load(276);
        return varbit != null && varbit.getValue() == value;
    }

}
