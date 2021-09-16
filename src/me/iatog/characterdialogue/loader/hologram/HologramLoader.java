package me.iatog.characterdialogue.loader.hologram;

import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.libraries.YamlFile;
import me.iatog.characterdialogue.loader.Loader;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

public class HologramLoader implements Loader {
	
	private CharacterDialoguePlugin main;
	
	public HologramLoader(CharacterDialoguePlugin main) {
		this.main = main;
	}
	
	@Override
	public void load() {
		YamlFile dialogsFile = main.getFileFactory().getDialogs();
		String dialoguesPath = "dialogs.npcs";
		
		dialogsFile.getConfigurationSection(dialoguesPath).getKeys(false).forEach((dialogName) -> {
			ConfigurationSection dialog = dialogsFile.getConfigurationSection(dialoguesPath + "." + dialogName);
			if(!dialog.contains("npc-id")) {
				return;
			}
			
			int npcId = dialog.getInt("npc-id");
			
			Optional<NPC> npc = Optional.ofNullable(CitizensAPI.getNPCRegistry().getById(npcId));
			if(!npc.isPresent()) {
				return;
			}
			Location location = npc.get().getStoredLocation();
			location.add(0, 1.6, 0);
			Hologram hologram = HologramsAPI.createHologram(main, location);
			hologram.appendTextLine("§bSoldier");
			hologram.appendTextLine("§e§lCLICK");
			npc.get().setAlwaysUseNameHologram(false);
		});
	}
	
	@Override
	public void unload() {
		HologramsAPI.getHolograms(main).forEach((hologram) -> {
			hologram.delete();
		});
	}
	
}
