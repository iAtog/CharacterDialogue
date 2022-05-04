package com.github.iatog.characterdialogue.hook;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.github.iatog.characterdialogue.CharacterDialoguePlugin;
import com.github.iatog.characterdialogue.api.CharacterDialogueAPI;
import com.github.iatog.characterdialogue.api.dialogue.Dialogue;
import com.github.iatog.characterdialogue.api.dialogue.DialogueHologram;
import com.github.iatog.characterdialogue.api.file.YamlFile;
import com.github.iatog.characterdialogue.api.hook.HologramHook;
import com.github.iatog.characterdialogue.api.hook.HookConfig;
import com.github.iatog.characterdialogue.util.StringUtil;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

public class HolographicDisplaysHook extends HookConfig implements HologramHook {

    private CharacterDialoguePlugin main;

    public HolographicDisplaysHook(CharacterDialoguePlugin main) {
        super(HologramHook.class);
        this.main = main;
    }

    @Override
    public void reloadHolograms() {
        YamlFile config = main.getFileRegistry().getFile("config");
        if (!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
            return;
        }

        for (Hologram hologram : HologramsAPI.getHolograms(main)) {
            hologram.delete();
        }

        config.getConfigurationSection("npc").getKeys(false).forEach((id) -> {
            this.loadHologram(Integer.parseInt(id));
        });
    }

    @Override
    public void loadHologram(int npcId) {
        CharacterDialogueAPI api = main.getAPI();
        if (!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
            return;
        }

        NPC citizensNpc = CitizensAPI.getNPCRegistry().getById(npcId);

        if (citizensNpc == null) {
            return;
        }

        Dialogue dialogue = api.getNPCDialogue(npcId);

        if (dialogue == null) {
            return;
        }

        DialogueHologram hologram = dialogue.getHologram();

        if (hologram != null && hologram.isEnabled()) {
            Location location = citizensNpc.getStoredLocation();
            location.add(0, 2 + hologram.getY(), 0);
            Hologram holo = HologramsAPI.createHologram(main, location);
            String npcName = dialogue.getDisplayName();
            List<String> lines = hologram.getLines();

            for (String line : lines) {
                holo.appendTextLine(StringUtil.colorize(line.replace("%npc_name%", npcName)));
            }

            citizensNpc.setAlwaysUseNameHologram(false);
        }
    }

}
