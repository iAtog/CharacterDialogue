package me.iatog.characterdialogue.libraries;

import java.util.List;
import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.api.CharacterDialogueAPI;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

public class ApiImplementation implements CharacterDialogueAPI {
	
	private CharacterDialoguePlugin main;
	
	public ApiImplementation(CharacterDialoguePlugin main) {
		this.main = main;
	}
	
	@Override
	public Optional<String> searchDialogueByNPCId(int id) {
		YamlFile dialogsFile = main.getFileFactory().getDialogs();
		Optional<String> optional = Optional.ofNullable(null);
		for(String name : dialogsFile.getConfigurationSection("dialogs.npcs").getKeys(false)) {
			String path = "dialogs.npcs."+name;
			if(dialogsFile.getInt(path+".npc-id") == id) {
				optional = Optional.ofNullable(path);
				break;
			}
		}
		return optional;
	}

	@Override
	public void reloadHolograms() {
		YamlFile dialogsFile = main.getFileFactory().getDialogs();
		String dialoguesPath = "dialogs.npcs";
		for(Hologram hologram : HologramsAPI.getHolograms(main)) {
			hologram.delete();
		}
		
		HologramsAPI.getHolograms(main).clear();
		
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
			location.add(0, 2 + dialog.getDouble("hologram.y-position", 0.6), 0);
			Hologram hologram = HologramsAPI.createHologram(main, location);
			String npcName = dialog.getString("name");
			List<String> lines = dialog.getStringList("hologram.lines");
			
			for(String line : lines) {
				hologram.appendTextLine(ChatColor.translateAlternateColorCodes('&', line.replace("%npc_name%", npcName)));
			}
			
			npc.get().setAlwaysUseNameHologram(false);
			npc.get().getEntity().setCustomNameVisible(false);
		});
	}

}
