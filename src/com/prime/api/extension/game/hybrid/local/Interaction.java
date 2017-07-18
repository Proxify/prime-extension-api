package com.prime.api.extension.game.hybrid.local;

import com.prime.api.extension.playersense.CustomPlayerSenseKey;
import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.LocatableEntity;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.entities.definitions.ItemDefinition;
import com.runemate.game.api.hybrid.entities.details.Interactable;
import com.runemate.game.api.hybrid.input.Mouse;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.Menu;
import com.runemate.game.api.hybrid.local.hud.MenuItem;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.Regex;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.Execution;

import java.util.regex.Pattern;

/**
 * @author Party
 *         Helper interaction class to make interacting with SpriteItems and Locatables easier.
 **/
public class Interaction {

    public static boolean fastClick() {
        return fastClick(Mouse.Button.LEFT);
    }

    /**
     * Performs a mouse click with a rather low click delay.
     *
     * @param button mouse button to press
     * @return true if the click was performed successfully
     */
    public static boolean fastClick(Mouse.Button button) {
        final long delay = (long) Random.nextGaussian(20, 140, CustomPlayerSenseKey.FAST_MOUSE_CLICK_DELAY_AVERAGE.getAsDouble());
        if (Mouse.press(button)) {
            Execution.delay(delay);
        }
        return Mouse.release(button);
    }

    /**
     * @return if no item currently selected or we deselect the item
     */
    public static boolean deselectItem() {
        SpriteItem item = Inventory.getSelectedItem();
        return item == null || item.click() || Mouse.click(Mouse.Button.LEFT);
    }

    /**
     * Selects the desired item
     *
     * @param item - SpriteItem to select
     * @return if item is already selected or item is successfully selected
     */
    public static boolean selectItem(final SpriteItem item) {
        if (item == null) {
            Environment.getLogger().warn("Cannot select a null item!");
            return false;
        }
        final SpriteItem selectedItem = Inventory.getSelectedItem();
        if (selectedItem != null && !selectedItem.equals(item) && !deselectItem()) {
            return false;
        }
        return interact("Use", item) && Execution.delayUntil(() -> item.equals(Inventory.getSelectedItem()), 600, 1200);
    }

    /**
     * Interacts with ab given NPC/GameObject/SpriteItem using ab given action
     *
     * @param action - Pattern matching the interact text
     * @param obj    - Locatable/SpriteItem to interact with
     * @return if the item is successfully interacted with
     */
    public static boolean interact(final Pattern action, final Interactable obj) {
        return obj != null && deselectItem() && obj.interact(action);
    }

    public static boolean interact(final String action, final Interactable obj) {
        return interact(Regex.getPatternForExactString(action), obj);
    }

    public static boolean interact(final Pattern action, final Interactable obj, final Pattern name) {
        return obj != null && deselectItem() && obj.interact(action, name);
    }

    public static boolean interact(final Pattern action, final Interactable obj, final String name) {
        return interact(action, obj, Regex.getPatternForExactString(name));
    }

    public static boolean interact(final String action, final Interactable obj, final Pattern name) {
        return interact(Regex.getPatternForExactString(action), obj, name);
    }

    public static boolean interact(final String action, final Interactable obj, final String name) {
        return interact(Regex.getPatternForExactString(action), obj, Regex.getPatternForExactString(name));
    }

    public static boolean turnAndInteract(final Pattern action, final LocatableEntity obj) {
        return turnAndInteract(action, obj, false);
    }

    public static boolean turnAndInteract(final Pattern action, final LocatableEntity obj, final boolean force) {
        if (obj != null && (force || obj.getVisibility() <= 80)) {
            Camera.concurrentlyTurnTo(obj, Random.nextDouble(0.8, 1));
        }
        return interact(action, obj);
    }

    public static boolean turnAndInteract(final String action, final LocatableEntity obj) {
        return turnAndInteract(Regex.getPatternForExactString(action), obj);
    }

    public static boolean turnAndInteract(final String action, final LocatableEntity obj, final boolean force) {
        return turnAndInteract(Regex.getPatternForExactString(action), obj, force);
    }

    public static boolean turnAndInteractDynamic(final Pattern action, final LocatableEntity obj) {
        if (!obj.isVisible()) {
            final double pitch = Camera.getPitch();
            Camera.concurrentlyTurnTo(obj, Random.nextDouble(pitch * 0.75, pitch));
        }

        return interact(action, obj);
    }

    public static boolean turnAndInteractDynamic(final String action, final LocatableEntity obj) {
        return turnAndInteractDynamic(Regex.getPatternForExactString(action), obj);
    }


    /**
     * Uses ab SpriteItem on ab given NPC
     *
     * @param spriteItem - SpriteItem to use on the target
     * @param target     - Target NPC
     * @return if the item is successfully used on the target
     */
    public static boolean useOn(final SpriteItem spriteItem, final Npc target) {
        Player player = Players.getLocal();
        if (spriteItem != null && target != null) {
            String name = spriteItem.getDefinition().getName();
            String targetName = target.getName();

            if (selectItem(spriteItem) && target.interact("Use", name + " -> " + targetName)) {
                return Execution.delayUntil(() -> Inventory.getSelectedItem() == null, player::isMoving, 600, 1200);
            }
        }
        return false;
    }

    /**
     * Uses ab SpriteItem on ab given GameObject
     *
     * @param spriteItem - SpriteItem to use on the target
     * @param target     - Target GameObject
     * @return if the item is successfully used on the target
     */
    public static boolean useOn(final SpriteItem spriteItem, final GameObject target) {
        Player player = Players.getLocal();
        if (spriteItem != null && target != null) {
            String name = spriteItem.getDefinition().getName();
            String targetName = target.getDefinition().getName();

            if (selectItem(spriteItem) && target.interact("Use", name + " -> " + targetName)) {
                return Execution.delayUntil(() -> Inventory.getSelectedItem() == null, player::isMoving, 600, 1200);
            }
        }
        return false;
    }

    /**
     * Uses one inventory item on another
     *
     * @param item1 First item
     * @param item2 Second item
     * @return is successfully combined
     */
    public static boolean useOn(SpriteItem item1, SpriteItem item2) {
        if (item1 == null || item2 == null) {
            return false;
        }
        ItemDefinition d1 = item1.getDefinition(), d2 = item2.getDefinition();
        if (d1 != null && d2 != null) {
            if (selectItem(item1) && item2.interact("Use", d1.getName() + " -> " + d2.getName())) {
                return Execution.delayWhile(() -> item1.isValid() && item2.isValid(), 1200, 1800);
            }
        }
        return false;
    }

    //Moves item in slot from to slot to
    public static boolean moveInventoryItem(int fromSlot, int toSlot) {
        SpriteItem from = Inventory.getItemIn(fromSlot);
        return from == null || Mouse.drag(from, Inventory.getBoundsOf(toSlot), Mouse.Button.LEFT);
    }

    //Moves spriteItem from into slot to
    public static boolean moveInventoryItem(SpriteItem from, int toSlot) {
        if (from == null) return true;
        if (!Mouse.move(from)) return false;
        Mouse.press(Mouse.Button.LEFT);
        Mouse.move(Inventory.getBoundsOf(toSlot));
        Mouse.release(Mouse.Button.LEFT);
        return true;/*Execution.delayUntil(() -> {
            SpriteItem i = Inventory.getItemIn(toSlot);
            if(i == null) return false;
            return from.getDefinition().getName().equals(i.getDefinition().getName()) &&
                    i.getQuantity() == from.getQuantity();
        }, 350);*/
    }

    /**
     * Checks if we're already hovering, if we aren't hovering we will hover it
     *
     * @param interactable The interactable to hover over
     * @return The action result
     */
    public static boolean hover(Interactable interactable) {
        return interactable.contains(Mouse.getPosition()) || interactable.hover();
    }

    public static boolean hoverMenu(Interactable interactable, String menuName) {
        return hoverMenu(interactable, Regex.getPatternForExactString(menuName));
    }

    /**
     * Hovers a menu item from the interactable matching the menuName pattern
     *
     * @param interactable the interactable to fetch the menu from
     * @param menuName     The name of the menu
     * @return Whether we're hovering
     */
    public static boolean hoverMenu(Interactable interactable, Pattern menuName) {
        if (!Menu.isOpen()) {
            if (Mouse.move(interactable)) {
                fastClick(Mouse.Button.RIGHT);
            }
            return false;
        }
        if (Menu.isOpen()) {
            MenuItem menuItem = getMenuItem(menuName);
            if (menuItem == null) {
                Menu.close();
                return false;
            }
            return hover(menuItem);
        }
        return false;
    }

    private static MenuItem getMenuItem(Pattern pattern) {
        return Menu.getItems().stream().filter(mi -> pattern.matcher(mi.getAction()).matches()).findFirst().orElse(null);
    }

}
