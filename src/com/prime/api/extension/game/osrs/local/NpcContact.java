package com.prime.bots.nature_runecrafter.data;

import com.prime.api.game.osrs.local.hud.interfaces.RunePouch;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.osrs.local.hud.interfaces.ControlPanelTab;
import com.runemate.game.api.osrs.local.hud.interfaces.Magic;
import com.runemate.game.api.script.Execution;

/**
 * @author Party
 * @version 1.0
 **/
public class NpcContact {

    private final static int WIDGET_INDEX = 75;

    public static InterfaceComponent getContainer() {
        return Interfaces.newQuery().containers(WIDGET_INDEX).types(InterfaceComponent.Type.CONTAINER).widths(467).heights(250).results().first();
    }

    public static boolean isOpen() {
        final InterfaceComponent ic = getContainer();
        return ic != null && ic.isVisible();
    }

    public static boolean open() {
        if(!canOpen()) {
            return false;
        }
        return Magic.Lunar.NPC_CONTACT.activate() && Execution.delayUntil(NpcContact::isOpen, 1200, 1800);
    }

    public static boolean canQuickCast(String name) {
        if(!ControlPanelTab.MAGIC.isOpen()) {
            ControlPanelTab.MAGIC.open();
        }
        final InterfaceComponent ic = Magic.Lunar.NPC_CONTACT.getComponent();
        return ic != null && ic.getActions().contains(name);
    }

    public static boolean quickCast(String name) {
        if(!canQuickCast(name)) {
            return false;
        }
        return Magic.Lunar.NPC_CONTACT.activate(name);
    }

    public static boolean select(String name) {
        final InterfaceComponent ic = getNpcComponent(name);
        final InterfaceComponent cont = getContainer();

        if(ic == null || cont == null) {
            return false;
        }

        return Interfaces.scrollTo(ic, cont) && ic.interact(name) && Execution.delayWhile(NpcContact::isOpen, 1200, 1800);
    }

    public static boolean canOpen() {
        return Inventory.contains("Rune pouch")
                && RunePouch.getQuantityOfRune(RunePouch.Rune.AIR) >= 2
                && RunePouch.getQuantityOfRune(RunePouch.Rune.ASTRAL) >= 1
                && RunePouch.getQuantityOfRune(RunePouch.Rune.COSMIC) >= 1;
    }

    public static InterfaceComponent getNpcComponent(String name) {
        return Interfaces.newQuery().actions(name).containers(WIDGET_INDEX).types(InterfaceComponent.Type.CONTAINER).results().first();
    }

}
