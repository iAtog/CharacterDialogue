package com.github.iatog.characterdialogue.api.impl;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.github.iatog.characterdialogue.api.PluginInstance;
import com.github.iatog.characterdialogue.api.dialogue.Dialogue;
import com.github.iatog.characterdialogue.api.dialogue.DialogueHologram;
import com.github.iatog.characterdialogue.api.dialogue.DialogueLine;
import com.github.iatog.characterdialogue.api.file.YamlFile;
import com.github.iatog.characterdialogue.api.types.ClickType;

public class DialogueImpl implements Dialogue {

    private String dialogName;
    private List<DialogueLine> lines;
    private ClickType clickType;
    private String displayName;
    private DialogueHologram hologram;
    private List<DialogueLine> firstInteraction;
    private DialoguePermission permissions;
    private boolean movement;

    private PluginInstance main;

    public DialogueImpl(PluginInstance instance, String dialogName, YamlFile file) {
        String click = file.getString("click", "RIGHT").toUpperCase();

        this.lines = instance.getAPI().parseLines(file.getStringList("dialogue"));
        this.clickType = ClickType.match(click) ? ClickType.valueOf(click) : ClickType.RIGHT;
        this.firstInteraction = instance.getAPI().parseLines(file.getStringList("first-interaction"));
        this.displayName = file.getString("display-name", "John");
        this.dialogName = dialogName;

        if (file.getBoolean("hologram.enabled", false)) {
            boolean enabled = file.getBoolean("hologram.enabled");
            float yPosition = Float.parseFloat(file.getString("hologram.y-position", "0.4"));
            List<String> hologramLines = file.getStringList("hologram.lines");
            this.hologram = new DialogueHologramImpl(enabled, yPosition, hologramLines);
        }

        if (file.contains("permission")) {
            if (file.isString("permission")) {
                this.permissions = new DialoguePermission(file.getString("permission"), null);
            } else {
                this.permissions = new DialoguePermission(file.getString("permission.value"),
                        file.getString("permission.message"));
            }
        }

        this.movement = file.getBoolean("allow-movement", true);

        this.main = instance;
    }
    
    public DialogueImpl(PluginInstance instance, String dialogName) {
        this(instance, dialogName, instance.getDialogueManager().getDialogue(dialogName));
    }

    @Override
    public String getName() {
        return dialogName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public ClickType getClickType() {
        return clickType;
    }

    @Override
    public DialoguePermission getPermissions() {
        return permissions;
    }

    @Override
    public List<DialogueLine> getLines() {
        return lines;
    }

    @Override
    public List<DialogueLine> getFirstInteractionLines() {
        return firstInteraction;
    }

    @Override
    public boolean isFirstInteractionEnabled() {
        return firstInteraction != null;
    }

    @Override
    public boolean start(Player player) {
        main.getAPI().getUser(player).runDialogue(this);
        return false;
    }

    @Override
    public boolean startFirstInteraction(Player player, boolean log) {
        if (log) {
            YamlFile playerCache = main.getFileRegistry().getFile("player-cache");
            List<String> readedDialogues = playerCache
                    .getStringList("players." + player.getUniqueId() + ".readed-dialogues");

            if (!playerCache.contains("players." + player.getUniqueId())) {
                readedDialogues = new ArrayList<>();
            }

            if (readedDialogues.contains(getName())) {
                return false;
            }

            readedDialogues.add(getName());
            playerCache.set("players." + player.getUniqueId() + ".readed-dialogues", readedDialogues);
            playerCache.save();
        }

        main.getAPI().getUser(player).runDialogueExpressions(firstInteraction, displayName);
        return true;
    }

    @Override
    public boolean isMovementAllowed() {
        return movement;
    }

    @Override
    public DialogueHologram getHologram() {
        return hologram;
    }

}
