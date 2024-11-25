package me.iatog.characterdialogue.api;

import me.iatog.characterdialogue.api.dialog.DialogHologram;
import me.iatog.characterdialogue.api.dialog.Dialogue;
import me.iatog.characterdialogue.enums.ClickType;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class TestDialogueImpl implements Dialogue {
    @Override
    public String getName() {
        return "Test";
    }

    @Override
    public List<String> getLines() {
        return Collections.emptyList();
    }

    @Override
    public ClickType getClickType() {
        return ClickType.RIGHT;
    }

    @Override
    public String getDisplayName() {
        return "Test-Dialogue";
    }

    @Override
    public DialogHologram getHologram() {
        return null;
    }

    @Override
    public List<String> getFirstInteractionLines() {
        return Collections.emptyList();
    }

    @Override
    public boolean isFirstInteractionEnabled() {
        return false;
    }

    @Override
    public boolean start(Player player, boolean debug) {
        return false;
    }

    @Override
    public boolean startFirstInteraction(Player player, boolean log) {
        return false;
    }

    @Override
    public DialoguePermission getPermissions() {
        return null;
    }

    @Override
    public boolean isMovementAllowed() {
        return true;
    }
}
