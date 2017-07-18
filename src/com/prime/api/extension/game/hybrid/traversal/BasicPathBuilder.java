package com.prime.api.extension.game.hybrid.traversal;

import com.runemate.game.api.hybrid.entities.details.Locatable;
import com.runemate.game.api.hybrid.location.navigation.Path;
import com.runemate.game.api.hybrid.location.navigation.Traversal;
import com.runemate.game.api.hybrid.location.navigation.basic.BresenhamPath;
import com.runemate.game.api.hybrid.location.navigation.basic.CoordinatePath;
import com.runemate.game.api.hybrid.location.navigation.basic.ViewportPath;
import com.runemate.game.api.hybrid.location.navigation.cognizant.RegionPath;

/**
 * @author Party
 * @version 1.0
 **/
public class BasicPathBuilder {

    public static Path buildTo(Locatable locatable) {
        Path path = RegionPath.buildTo(locatable);
        if (path == null) {
            path = BresenhamPath.buildTo(locatable);
        }
        return path;
    }

    public static CoordinatePath buildCoordPathTo(Locatable locatable) {
        CoordinatePath path = RegionPath.buildTo(locatable);
        if (path == null) {
            path = BresenhamPath.buildTo(locatable);
        }
        return path;
    }

    public static Path buildWebPathTo(Locatable locatable) {
        Path path = Traversal.getDefaultWeb().getPathBuilder().buildTo(locatable);
        if (path == null) {
            path = buildTo(locatable);
        }
        return path;
    }

    public static Path buildPath(Locatable locatable) {
        return buildPath(locatable, true);
    }

    public static Path buildPath(Locatable locatable, boolean considerObstacles) {
        Path path;

        if (!locatable.getPosition().isLoaded() || (path = RegionPath.buildTo(locatable)) == null) {
            path = Traversal.getDefaultWeb().getPathBuilder().buildTo(locatable);
        }

        if (!considerObstacles && path == null) {
            path = BresenhamPath.buildTo(locatable);
        }

        return path;
    }

    public static Path buildViewportPathTo(Locatable locatable) {
        Path p = buildPath(locatable, false);
        if (p != null && p instanceof CoordinatePath) {
            return ViewportPath.convert((CoordinatePath) p);
        }
        return p;
    }

}
