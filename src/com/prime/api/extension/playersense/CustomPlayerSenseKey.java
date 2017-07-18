package com.prime.api.extension.playersense;

import com.runemate.game.api.hybrid.player_sense.PlayerSense;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.framework.AbstractBot;

import java.util.Arrays;

/**
 * @author Party
 * @version 1.0
 *          <p>
 *          Should be used to shared PlayerSense keys ONLY. Any bot-specific keys should be
 *          loaded into PlayerSense via the bot's main class.
 **/

public enum CustomPlayerSenseKey {
    FATIGUE(new BasicPlayerSenseKey("p_fatigue_factor", () -> 0)),
    PREDICT_DELAY(new BasicPlayerSenseKey("p_prediction_delay", () -> Random.nextInt(150, 350))),
    CAMERA_WHILE_STEPPING(new BasicPlayerSenseKey("p_camera_while_stepping", () -> Random.nextDouble(0.05, 0.25))),
    COMMON_MODIFIER(new BasicPlayerSenseKey("p_common_modifier", () -> Random.nextDouble(0.7, 1.2))),
    AFK_BREAKING(new BasicPlayerSenseKey("p_afk_breaking", () -> Random.nextDouble(0.1, 0.9))),
    ACTIONBAR_SPAM_CHANCE(new BasicPlayerSenseKey("p_actionbar_spam_chance", () -> Random.nextDouble(0.4, 0.8))),
    BREAK_SPACING_MIN(new BasicPlayerSenseKey("p_break_spacing_min_2", () -> Random.nextInt(35, 60))),
    BREAK_SPACING_MAX(new BasicPlayerSenseKey("p_break_spacing_max_2", () -> Random.nextInt(120, 180))),
    BREAK_DURATION_MIN(new BasicPlayerSenseKey("p_break_duration_min_2", () -> Random.nextInt(3, 7))),
    BREAK_DURATION_MAX(new BasicPlayerSenseKey("p_break_duration_max_2", () -> Random.nextInt(20, 30))),
    ANTIPATTERN_SPACING(new BasicPlayerSenseKey("p_antipattern_spacing", () -> Random.nextInt(10, 30))),
    IDLING_AVERAGE(new BasicPlayerSenseKey("p_idling_average", () -> Random.nextInt(30, 180))),
    AREA_ROAMING_PRECISION(new BasicPlayerSenseKey("p_area_roaming_precision", () -> Random.nextDouble(3, 8))),
    SHIFT_DROP_MOUSE_MOVING_TIMEOUT(new BasicPlayerSenseKey("p_shift_drop_mouse_moving_timeout_average", () -> Random.nextDouble(100, 600))),
    SHIFT_DROP_RELEASE_DELAY(new BasicPlayerSenseKey("p_shift_drop_release_delay", () -> Random.nextDouble(200, 700))),
    DROP_STYLE(new BasicPlayerSenseKey("p_drop_order", () -> Random.nextInt(0, 4))),
    FAST_MOUSE_CLICK_DELAY_AVERAGE(new BasicPlayerSenseKey("p_fast_mouse_click_delay_average", () -> Random.nextDouble(30, 60))),
    FAST_KEY_PRESS_DELAY_AVERAGE(new BasicPlayerSenseKey("p_fast_key_press_delay_average", () -> Random.nextDouble(45, 80))),
    ENTER_AMOUNT_NUMBER(new BasicPlayerSenseKey("p_enter_amount_number", () -> {
        final String number = Integer.toString(Random.nextInt(3, 9));
        return Integer.parseInt(number + number);
    })),
    MINIMUM_RUN_ENERGY(new BasicPlayerSenseKey("p_minimum_run_energy", () -> Random.nextInt(20, 95))),
    MINIMUM_ENTITY_VISIBILITY(new BasicPlayerSenseKey("p_minimum_entity_visibility", () -> Random.nextGaussian(60, 90, 80))),
    ASYNC_ACTION_DELAY(new GaussianKey("p_async_action_delay", 300, 800)),
    ASYNC_ACTION_SYNC_CHANCE(new BasicPlayerSenseKey("p_async_action_sync_chance", () -> Random.nextInt(13, 18))),
    ASYNC_ACTION_KEY_FIRST(new BasicPlayerSenseKey("p_async_action_key_first", () -> Random.nextDouble(0.25, 0.75)));

    private final PlayerSensable playerSensable;

    CustomPlayerSenseKey(PlayerSensable playerSensable) {
        this.playerSensable = playerSensable;
    }

    public static void initialize(AbstractBot bot) {
        Arrays.stream(values()).forEach(key -> {
            Object value = PlayerSense.get(key.getKey());
            if (value == null) {
                value = key.playerSensable.getInitialValue().get();
                PlayerSense.put(key.getKey(), value);
            }
            bot.getLogger().debug("[PlayerSense] key=\"" + key.getKey() + "\" value=" + value);
        });
    }

    public static void clear() {
        Arrays.stream(values())
                .map(CustomPlayerSenseKey::getKey)
                .forEach(PlayerSense::clear);
    }

    public String getKey() {
        return playerSensable.getKey();
    }

    public <T> T getAsObject(Class<T> tClass) {
        Object o = playerSensable.getValue();
        if (tClass.isInstance(o)) {
            return (T) o;
        } else {
            return null;
        }
    }

    public int getAsInteger() {
        final Object o = playerSensable.getValue();
        if (o instanceof Number) {
            return ((Number) o).intValue();
        } else {
            return 0;
        }
    }

    public double getAsDouble() {
        final Object o = playerSensable.getValue();
        if (o instanceof Number) {
            return ((Number) o).doubleValue();
        } else {
            return 0.00;
        }
    }

    public long getAsLong() {
        final Object o = playerSensable.getValue();
        if (o instanceof Number) {
            return ((Number) o).longValue();
        } else {
            return 0L;
        }
    }

    public boolean getAsBoolean() {
        Object o = playerSensable.getValue();
        if (o instanceof Boolean) {
            return (Boolean) o;
        } else {
            return false;
        }
    }
}
