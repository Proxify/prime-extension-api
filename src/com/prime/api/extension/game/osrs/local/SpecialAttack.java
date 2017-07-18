package com.prime.api.extension.game.hybrid.local;

import com.runemate.game.api.hybrid.local.Varps;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.osrs.local.hud.interfaces.ControlPanelTab;
import com.runemate.game.api.script.Execution;

/**
 * @author Party
 **/
public class SpecialAttack {

    public static int getEnergy() {
        return Varps.getAt(300).getValue() / 10;
    }

    public static boolean isActivated() {
        return Varps.getAt(301).getValue() == 1;
    }

    public static boolean activate() {
        return isActivated() || toggle(true);
    }

    public static boolean deactivate() {
        return !isActivated() || toggle(false);
    }

    private static InterfaceComponent getButton() {
        return Interfaces.newQuery().containers(593).types(InterfaceComponent.Type.SPRITE).actions("Use Special Attack").results().first();
    }

    private static boolean toggle(boolean enable) {
        if (isActivated() == enable) {
            return true;
        }
        if (ControlPanelTab.COMBAT_OPTIONS.isOpen() || ControlPanelTab.COMBAT_OPTIONS.open()) {
            final InterfaceComponent specialButton = getButton();
            return specialButton != null
                    && specialButton.isVisible()
                    && specialButton.interact("Use Special Attack")
                    && Execution.delayUntil(() -> isActivated() == enable, 600, 1200);
        }
        return false;
    }
}
