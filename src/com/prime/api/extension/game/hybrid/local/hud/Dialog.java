package com.prime.api.extension.game.hybrid.local.hud;

import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog;
import com.runemate.game.api.hybrid.util.Regex;

import java.util.regex.Pattern;

/**
 * Class used to go through conversations with NPCs.
 * <p>
 * Usage:
 * if (dialog.isAvailable()) {
 * dialog.continueDialog();
 * }
 *
 * @author Torben Schmitz
 */
public class Dialog {
    private final Pattern[] patterns;
    private ChatDialog.Selectable option;

    public Dialog(final String... strings) {
        this(Regex.getPatternsForExactStrings(strings).toArray(new Pattern[0]));
    }

    /**
     * @param patterns Each pattern represents one possible chat dialog option that may occur in the conversation
     */
    public Dialog(final Pattern... patterns) {
        this.patterns = patterns;
    }

    public boolean isAvailable() {
        option = ChatDialog.getContinue();
        if (option == null) {
            option = ChatDialog.getOption(patterns);
        }

        return option != null;
    }

    public ChatDialog.Selectable getAvailable() {
        return isAvailable() ? option : null;
    }

    public boolean continueDialog() {
        return option != null && option.select();
    }

    public boolean continueDialog(boolean b) {
        return option != null && option.select(b);
    }
}