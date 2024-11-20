package me.iatog.characterdialogue.libraries;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import eu.decentsoftware.holograms.api.DHAPI;
import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.util.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class HologramLibrary {

    private CharacterDialoguePlugin main;

    private final boolean holographicDisplays;
    private final boolean decentHolograms;

    private List<String> decentHologramsList;

    public HologramLibrary(CharacterDialoguePlugin main) {
        this.main = main;

        this.holographicDisplays = Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays");
        this.decentHolograms = Bukkit.getPluginManager().isPluginEnabled("DecentHolograms");

        this.decentHologramsList = new ArrayList<>();
    }

    public void addHologram(List<String> lines, Location location, String npcName) {
        //main.getLogger().info("Trying to add hologram");
        if(holographicDisplays) {
            Hologram holo = HologramsAPI.createHologram(main, location);
            for (String line : lines) {
                holo.appendTextLine(TextUtils.colorize(line.replace("%npc_name%", npcName)));
            }
        } else if(decentHolograms) {
            //main.getLogger().info("DecentHolograms found");
            List<String> formattedLines = new ArrayList<>();

            for(String line : lines) {
                formattedLines.add(TextUtils.colorize(line.replace("%npc_name%", npcName)));
            }

            DHAPI.createHologram(npcName, location, formattedLines);
            decentHologramsList.add(npcName);
            //main.getLogger().info("Hologram created");
        }
    }

    public void reloadHolograms() {
        YamlFile config = main.getFileFactory().getConfig();
        if(holographicDisplays) {
            for (Hologram hologram : HologramsAPI.getHolograms(main)) {
                hologram.delete();
            }

            config.getConfigurationSection("npc").getKeys(false).forEach((id) -> {
                main.getApi().loadHologram(Integer.parseInt(id));
            });
        } else if(decentHolograms) {
            for(String name : decentHologramsList) {
                eu.decentsoftware.holograms.api.holograms.Hologram hologram = DHAPI.getHologram(name);

                if(hologram == null) {
                    continue;
                }

                hologram.destroy();
            }

            config.getConfigurationSection("npc").getKeys(false).forEach((id) -> {
                main.getApi().loadHologram(Integer.parseInt(id));
            });
        }
    }
}
