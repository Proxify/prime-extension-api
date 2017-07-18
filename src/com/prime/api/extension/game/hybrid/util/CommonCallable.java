package com.prime.api.extension.game.hybrid.util;

import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.region.Players;

import java.util.concurrent.Callable;

/**
 * @author Party
 * @version 1.0
 **/
public final class CommonCallable {

    public static Callable<Boolean> PLAYER_MOVING = () -> {
        final Player local = Players.getLocal();
        return local != null && local.isMoving();
    };

    public static Callable<Boolean> PLAYER_ANIMATING = () -> {
        final Player local = Players.getLocal();
        return local != null && local.getAnimationId() != -1;
    };

}
