package com.prime.api.game.osrs.local.hud.interfaces;

import com.runemate.game.api.hybrid.local.Varbit;
import com.runemate.game.api.hybrid.local.Varbits;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.queries.results.SpriteItemQueryResults;

import java.util.Arrays;

/**
 * @author Party
 * @version 1.0
 **/
public class RunePouch {

    private RunePouch() {
    }

    public static Rune getRuneInSlot(Slot slot) {
        return slot.getRune();
    }

    public static int getQuantityInSlot(Slot slot) {
        return slot.getQuantity();
    }

    public static int getQuantityOfRune(Rune rune) {
        for (final Slot slot : Slot.values()) {
            if (slot.getRune() == rune) {
                return slot.getQuantity();
            }
        }
        return 0;
    }

    public static boolean isEmpty() {
        return getEmptySlots() == 3;
    }

    public static int getEmptySlots() {
        int empty = 0;
        for (final Slot slot : Slot.values()) {
            if (slot.getQuantity() == 0) {
                empty++;
            }
        }
        return empty;
    }

    public enum Slot {
        ONE(29, 1624),
        TWO(1622, 1625),
        THREE(1623, 1626);

        private int type, quantity;

        Slot(int type, int quantity) {
            this.type = type;
            this.quantity = quantity;
        }

        public Rune getRune() {
            final Varbit varbit = Varbits.load(type);
            return varbit == null ? null : Rune.valueOf(varbit.getValue());
        }

        public int getQuantity() {
            final Varbit varbit = Varbits.load(quantity);
            return varbit == null ? 0 : varbit.getValue();
        }

        @Override
        public String toString() {
            return "RunePouch.Slot." + name();
        }
    }

    public enum Rune {
        AIR(1),
        WATER(2),
        EARTH(3),
        FIRE(4),
        MIND(5),
        CHAOS(6),
        DEATH(7),
        BLOOD(8),
        COSMIC(9),
        NATURE(10),
        LAW(11),
        BODY(12),
        SOUL(13),
        ASTRAL(14),
        MIST(15, AIR, WATER),
        MUD(16, EARTH, WATER),
        DUST(17, AIR, EARTH),
        LAVA(18, EARTH, FIRE),
        STEAM(19, WATER, FIRE),
        SMOKE(20, FIRE, AIR);

        private final String name;
        private final Rune[] replaces;
        private final int osrs_pouch_type;

        Rune(int osrs_pouch_type, Rune r1, Rune r2) {
            this.osrs_pouch_type = osrs_pouch_type;
            this.name = name().substring(0, 1).toUpperCase() + name().substring(1) + " rune";
            this.replaces = new Rune[]{r1, r2};
        }

        Rune(int osrs_pouch_type) {
            this.osrs_pouch_type = osrs_pouch_type;
            this.name = name().substring(0, 1).toUpperCase() + name().substring(1) + " rune";
            this.replaces = null;
        }

        public static Rune valueOf(SpriteItem item) {
            return item == null ? null : valueOf(item.getDefinition().getName().replace(" rune", "").toUpperCase());
        }

        public static Rune valueOf(int id) {
            for (final Rune rune : values()) {
                if (rune.osrs_pouch_type == id) {
                    return rune;
                }
            }
            return null;
        }

        public String getName() {
            return name;
        }

        public boolean isCombinationRune() {
            return replaces != null;
        }

        /**
         * Determines whether or not a combination rune replaces a given rune type
         *
         * @param rune rune that is replaced by enum object
         * @return if this Rune replaces the specified rune
         */
        public boolean replaces(Rune rune) {
            return this == rune || replaces != null && Arrays.asList(replaces).contains(rune);
        }

        /**
         * @return the runes that a given combination rune replaces, null if not a combo rune
         */
        public Rune[] getReplacingRunes() {
            return replaces;
        }

        /**
         * @return SpriteItemQueryResults for runes in inventory
         */
        public SpriteItemQueryResults getItems() {
            return Inventory.newQuery().names(getName()).results();
        }

        public int getQuantity() {
            return getItems().size();
        }

        @Override
        public String toString() {
            return "Rune." + name();
        }
    }
}
