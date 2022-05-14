package com.github.iatog.characterdialogue.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.github.iatog.characterdialogue.CharacterDialoguePlugin;
import com.github.iatog.characterdialogue.api.cache.Cache;
import com.github.iatog.characterdialogue.api.dialogue.Dialogue;
import com.github.iatog.characterdialogue.api.dialogue.DialogueLine;
import com.github.iatog.characterdialogue.api.dialogue.DialogueSession;
import com.github.iatog.characterdialogue.api.file.YamlFile;
import com.github.iatog.characterdialogue.api.impl.DefaultDialogueSession;
import com.github.iatog.characterdialogue.api.method.AbstractMethod;
import com.github.iatog.characterdialogue.api.user.User;
import com.github.iatog.characterdialogue.util.StringUtil;

public class UserImpl implements User {
    
    private final CharacterDialoguePlugin PLUGIN = CharacterDialoguePlugin.getInstance();
    
    private Player player;
    
    public UserImpl(UUID uuid) {
        this.player = Bukkit.getPlayer(uuid);
    }
    
    @Override
    public boolean readDialogue(String dialogue) {
        YamlFile playerCache = PLUGIN.getFileRegistry().getFile("player-cache");
        String path = "players." + player.getUniqueId();
        List<String> readedDialogues = playerCache.getStringList(path + ".readed-dialogues");

        if (readedDialogues == null) {
            readedDialogues = new ArrayList<>();
        }

        if (readedDialogues.contains(dialogue)) {
            return false;
        }

        readedDialogues.add(dialogue);
        playerCache.set(path + ".readed-dialogues", readedDialogues);
        playerCache.save();
        return true;
    }

    @Override
    public boolean readedDialogue(Dialogue dialogue) {
        return readedDialogue(dialogue.getName());
    }

    @Override
    public boolean readedDialogue(String dialogue) {
        YamlFile playerCache = PLUGIN.getFileRegistry().getFile("player-cache");
        String path = "players." + player.getUniqueId();
        List<String> readedDialogues = playerCache.getStringList(path + ".readed-dialogues");

        return playerCache.contains(path) && readedDialogues.contains(dialogue);
    }

    @Override
    public void runDialogue(Dialogue dialogue) {
        Cache<UUID, DialogueSession> cache = PLUGIN.getCacheFactory().getDialogueSessions();
        if (cache.contains(player.getUniqueId())) {
            return;
        }

        DialogueSession session = new DefaultDialogueSession(player, dialogue);

        if (!dialogue.isMovementAllowed()) {
            setMovement(false);
        }

        cache.set(player.getUniqueId(), session);
        session.start(0);
    }

    @Override
    public void runDialogueExpression(String method, String arg, String npcName, Consumer<DialogueLine> fail, DialogueSession session) {
        Cache<String, AbstractMethod> methods = PLUGIN.getCacheFactory().getMethods();
        
        if(!methods.contains(method)) {
            PLUGIN.getLogger().warning(String.format("Invalid method in dialog line (%s)", method));
            return;
        }
        
        String argument = StringUtil.translatePlaceholders(player, arg, npcName);
        AbstractMethod methodObject = methods.get(method);
        
        methodObject.run(player, argument, session);
    }

    @Override
    public void runDialogueExpressions(List<DialogueLine> lines, String displayName) {
        Cache<UUID, DialogueSession> sessions = PLUGIN.getCacheFactory().getDialogueSessions();
        if (sessions.contains(player.getUniqueId())) {
            return;
        }

        DialogueSession session = new DefaultDialogueSession(player, displayName == null ? "John" : displayName, lines);

        sessions.set(player.getUniqueId(), session);
        session.start();
    }

    @Override
    public boolean setMovement(boolean movement) {
        YamlFile playerCache = PLUGIN.getFileRegistry().getFile("player-cache");
        String path = "players." + player.getUniqueId();

        if(movement) {
            float speed = playerCache.getFloat(path + ".last-speed");

            player.setWalkSpeed(speed);
            player.removePotionEffect(PotionEffectType.JUMP);
        } else {
            playerCache.set(path + ".last-speed", player.getWalkSpeed());
            player.setWalkSpeed(0);
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 128));
        }

        playerCache.set(path + ".remove-effect", !movement);
        playerCache.save();
        return !movement;
    }

    @Override
    public boolean canEnableMovement() {
        YamlFile playerCache = PLUGIN.getFileRegistry().getFile("player-cache");
        String path = "players." + player.getUniqueId();

        return playerCache.getBoolean(path + ".remove-effect", false);
    }

    @Override
    public void sendMessage(String message) {
        player.sendMessage(StringUtil.colorize(message));
    }

    @Override
    public Player toPlayer() {
        return player;
    }

}
