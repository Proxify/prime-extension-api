package com.prime.api.extension.game.hybrid.util;

import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.local.Screen;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Party
 * @version 1.0
 **/
public class Screenshot {

    private final static DateFormat SDF = new SimpleDateFormat("yyyyMMddHHmmss");

    public static boolean capture(String name) throws IOException {
        final BufferedImage bi = Screen.capture();
        final File file = new File(Environment.getStorageDirectory(), name + "_" + SDF.format(new Date()) + ".png");
        return ImageIO.write(bi, "png", file);
    }

}
