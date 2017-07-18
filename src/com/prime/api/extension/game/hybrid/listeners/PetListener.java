package com.prime.api.extension.game.hybrid.listeners;

import com.prime.api.extension.game.hybrid.util.Screenshot;
import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.local.hud.interfaces.Chatbox;
import com.runemate.game.api.script.framework.listeners.ChatboxListener;
import com.runemate.game.api.script.framework.listeners.events.MessageEvent;

import java.io.IOException;

/**
 * @author Party
 * @version 1.0
 **/
public class PetListener implements ChatboxListener {

    private final String screenshotName;

    public PetListener(String screenshotName) {
        this.screenshotName = screenshotName;
    }

    @Override
    public void onMessageReceived(MessageEvent event) {
        if (event.getType() == Chatbox.Message.Type.SERVER) {
            final String message = event.getMessage();
            if (message != null && !message.isEmpty()) {
                if (message.contains("followed")) {
                    Environment.getLogger().info("Pet????????? Taking screenshot!!!!");
                    try {
                        Screenshot.capture(screenshotName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
