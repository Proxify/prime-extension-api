package com.prime.api.game.osrs.local;

import com.runemate.game.api.hybrid.local.Varps;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.osrs.local.hud.interfaces.ControlPanelTab;
import com.runemate.game.api.script.Execution;

/**
 * @author Party
 * @version 1.0
 **/
public class AutoRetaliate {

    public static boolean isEnabled() {
        return Varps.getAt(172).getValue() == 0;
    }

    public static boolean enable() {
        return isEnabled() || toggle(false);
    }

    public static boolean disable() {
        return !isEnabled() || toggle(true);
    }

    private static boolean toggle(boolean enable) {
        if(isEnabled() == enable) {
            return true;
        }
        final InterfaceComponent ic = getComponent();
        return ic != null
                && ControlPanelTab.COMBAT_OPTIONS.open()
                && ic.interact("Auto retaliate")
                && Execution.delayUntil(() -> isEnabled() != enable, 1200, 1800);
    }

    private static InterfaceComponent getComponent() {
        return Interfaces.newQuery().containers(593).types(InterfaceComponent.Type.SPRITE).actions("Auto retaliate").results().first();
    }

}
