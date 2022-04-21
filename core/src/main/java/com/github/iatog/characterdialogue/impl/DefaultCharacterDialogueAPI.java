package com.github.iatog.characterdialogue.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;

import com.github.iatog.characterdialogue.CharacterDialoguePlugin;
import com.github.iatog.characterdialogue.api.CharacterDialogueAPI;
import com.github.iatog.characterdialogue.api.PluginInstance;
import com.github.iatog.characterdialogue.api.cache.Cache;
import com.github.iatog.characterdialogue.api.dialogue.Dialogue;
import com.github.iatog.characterdialogue.api.dialogue.DialogueLine;
import com.github.iatog.characterdialogue.api.file.YamlFile;
import com.github.iatog.characterdialogue.api.method.AbstractMethod;
import com.github.iatog.characterdialogue.api.user.User;

public class DefaultCharacterDialogueAPI implements CharacterDialogueAPI {

    private CharacterDialoguePlugin PLUGIN = CharacterDialoguePlugin.getInstance();

    @Override
    public Dialogue getDialogue(String name) {
        return PLUGIN.getCacheFactory().getDialogues().get(name);
    }

    @Override
    public void reloadHolograms() {

    }

    @Override
    public void loadHologram(int npcId) {

    }

    @Override
    public Dialogue getNPCDialogue(int id) {
        return getDialogue(getNPCDialogueName(id));
    }

    @Override
    public Map<String, Dialogue> getDialogues() {
        return Collections.unmodifiableMap(PLUGIN.getCacheFactory().getDialogues().map());
    }

    @Override
    public String getNPCDialogueName(int id) {
        YamlFile config = PLUGIN.getFileRegistry().getFile("config");
        return config.getString("npc." + id);
    }

    @Override
    public int getBukkitVersion() {
        return Integer.parseInt(Bukkit.getBukkitVersion().split("-")[0].split("\\.")[1]);
    }

    @Override
    public PluginInstance getPlugin() {
        return CharacterDialoguePlugin.getInstance();
    }

    @Override
    public User getUser(UUID uuid) {
        return PLUGIN.getCacheFactory().getUsers().get(uuid);
    }

    @Override
    public List<DialogueLine> parseLines(List<String> lines, String npcName) {
        List<DialogueLine> parsedLines = new ArrayList<>();

        for (String line : lines) {
            if (!line.matches("(.*)+(:)+(.*)")) {
                PLUGIN.getLogger().warning("failed to load an invalid dialogue line (" + line + ")");
                continue;
            }

            Cache<String, AbstractMethod> methods = PLUGIN.getCacheFactory().getMethods();
            String[] splitted = line.split(line);
            String methodName = splitted[0].trim();
            String arguments = splitted[1].trim();

            if (!methods.contains(methodName)) {
                PLUGIN.getLogger().severe("failed to load an dialogue line (invalid method: " + methodName + ")");
                continue;
            }

            parsedLines.add(new SimpleDialogueLine(methods.get(methodName), arguments, npcName));
        }

        return parsedLines;
    }

}
