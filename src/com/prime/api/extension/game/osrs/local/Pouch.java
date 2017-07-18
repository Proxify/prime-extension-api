package com.prime.api.extension.game.osrs.local;

import com.runemate.game.api.hybrid.local.Varbit;
import com.runemate.game.api.hybrid.local.Varbits;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.queries.results.SpriteItemQueryResults;
import com.runemate.game.api.script.Execution;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Party
 * @version 1.0
 *          <p>
 *          THIS IS A POORLY WRITTEN CLASS. I wrote this for personal use - I do not recommend you use it "as is" for use on the bot store.
 **/
public enum Pouch {

    SMALL(3, 603, -1),
    MEDIUM(6, 604, 5511),
    LARGE(9, 605, 5513),
    GIANT(12, 606, 5515);

    private final String name;
    private int storedVarbit, degradedId, maxEss;

    Pouch(int maxEss, int storedVarbit, int degradedId) {
        this.name = name().substring(0, 1).toUpperCase() + name().substring(1).toLowerCase() + " pouch";
        this.storedVarbit = storedVarbit;
        this.degradedId = degradedId;
        this.maxEss = maxEss;
    }

    public static boolean fill(SpriteItem pouch) {
        return interact("Fill", pouch);
    }

    public static boolean empty(SpriteItem pouch) {
        return interact("Empty", pouch);
    }

    public static boolean interact(String action, SpriteItem pouch) {
        return pouch != null && pouch.interact(action);
    }

    public static SpriteItemQueryResults getPouches() {
        return Inventory.newQuery().names(getNames()).results();
    }

    public static SpriteItemQueryResults getUnfilledPouches() {
        return new SpriteItemQueryResults(Stream.of(Pouch.values()).filter(pouch -> !pouch.isFull() && !pouch.isDegraded()).map(Pouch::getItem).filter(Objects::nonNull).collect(Collectors.toList()));
    }

    public static SpriteItemQueryResults getUnemptiedPouches() {
        return new SpriteItemQueryResults(Stream.of(Pouch.values()).filter(pouch -> !pouch.isEmpty()).map(Pouch::getItem).filter(Objects::nonNull).collect(Collectors.toList()));
    }

    public static SpriteItemQueryResults getDegradedPouches() {
        return Inventory.newQuery().names(getNames()).ids(getDegradedIds()).results();
    }

    public static String[] getNames() {
        return Stream.of(Pouch.values()).map(Pouch::getName).toArray(String[]::new);
    }

    public static int[] getDegradedIds() {
        return Stream.of(Pouch.values()).filter(p -> p != Pouch.SMALL).mapToInt(Pouch::getDegradedId).toArray();
    }

    public static boolean fillAll() {
        return Stream.of(Pouch.values()).filter(p -> !p.isDegraded()).peek(Pouch::fill).noneMatch(Pouch::isEmpty);
    }

    public static boolean emptyAll() {
        return Stream.of(Pouch.values()).filter(p -> !p.isEmpty()).peek(Pouch::empty).allMatch(Pouch::isEmpty);
    }

    public int getStored() {
        final Varbit v = Varbits.load(storedVarbit);
        return v == null ? -1 : v.getValue();
    }

    public boolean isFull() {
        return getStored() == maxEss;
    }

    public boolean isEmpty() {
        return getStored() == 0;
    }

    public boolean isDegraded() {
        final SpriteItem item = getItem();
        return item != null && item.getId() == degradedId;
    }

    public SpriteItem getItem() {
        return Inventory.newQuery().names(name).results().first();
    }

    public int getDegradedId() {
        return degradedId;
    }

    public String getName() {
        return name;
    }

    public boolean fill() {
        return interact("Fill", getItem()) && Execution.delayWhile(this::isEmpty, 1200, 1800);
    }

    public boolean empty() {
        return interact("Empty", getItem()) && Execution.delayUntil(this::isEmpty, 1200, 1800);
    }

    @Override
    public String toString() {
        return "Pouch." + name();
    }

}
