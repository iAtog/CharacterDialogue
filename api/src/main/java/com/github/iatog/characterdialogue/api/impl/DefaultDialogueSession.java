package com.github.iatog.characterdialogue.api.impl;

import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.github.iatog.characterdialogue.api.CharacterDialogueAPI;
import com.github.iatog.characterdialogue.api.cache.Cache;
import com.github.iatog.characterdialogue.api.dialogue.Dialogue;
import com.github.iatog.characterdialogue.api.dialogue.DialogueLine;
import com.github.iatog.characterdialogue.api.dialogue.DialogueSession;
import com.github.iatog.characterdialogue.api.user.User;

public class DefaultDialogueSession implements DialogueSession {

    private final CharacterDialogueAPI API = CharacterDialogueAPI.create();

    private final Player player;
    private final User user;
    private final Dialogue dialogue;
    private List<DialogueLine> lines;

    protected int index;
    protected boolean paused;

    public DefaultDialogueSession(Player player, Dialogue dialogue) {
        this.player = player;
        this.dialogue = dialogue;
        this.lines = this.dialogue.getLines();
        this.user = API.getUser(player);
    }

    @Override
    public void start(int index) {
        if (lines.size() < 1) {
            this.destroy();
            return;
        }

        for (int i = index; i < lines.size(); i++) {
            if (paused) {
                this.paused = false;
                break;
            }

            DialogueLine dialog = lines.get(i);
            this.index = i;

            if (dialog == null) {
                continue;
            }

            user.runDialogueExpression(dialog.getMethod().getIdentifier(), dialog.getArgument(),
                    dialogue.getDisplayName(), (x) -> {
                        destroy();
                    }, this);

            if (i == lines.size() - 1) {
                destroy();
                if (user.canEnableMovement()) {
                    user.setMovement(true);
                }

                break;
            }
        }
    }

    @Override
    public void start() {
        start(0);
    }

    @Override
    public boolean hasNext() {
        return (index + 1) < lines.size();
    }

    @Override
    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public void pause() {
        this.paused = true;
    }

    @Override
    public void destroy() {
        pause();
        Cache<UUID, DialogueSession> sessions = API.getPlugin().getCacheFactory().getDialogueSessions();
        UUID uid = player.getUniqueId();

        if (sessions.contains(uid)) {
            sessions.remove(uid);
        }

    }

    @Override
    public List<DialogueLine> getLines() {
        return lines;
    }

    @Override
    public Player getOwner() {
        return player;
    }

}
