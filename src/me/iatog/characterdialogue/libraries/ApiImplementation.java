package me.iatog.characterdialogue.libraries;

import java.util.List;
import java.util.Optional;

import org.bukkit.Bukkit;
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
		if(!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
			return;
		}
		
		YamlFile dialogsFile = main.getFileFactory().getDialogs();
		String dialoguesPath = "dialogs.npcs";
		
		for(Hologram hologram : HologramsAPI.getHolograms(main)) {
			hologram.delete();
		}
				
		dialogsFile.getConfigurationSection(dialoguesPath).getKeys(false).forEach((dialogName) -> {
			ConfigurationSection dialog = dialogsFile.getConfigurationSection(dialoguesPath + "." + dialogName);
			if(!dialog.contains("npc-id")) {
				return;
			}
			
			int npcId = dialog.getInt("npc-id");
			
			this.loadHologram(npcId);
		});
	}

	@Override
	public void loadHologram(int npcId) {
		if(!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
			return;
		}
		
		YamlFile dialogsFile = main.getFileFactory().getDialogs();
		Optional<NPC> citizensNpc = Optional.ofNullable(CitizensAPI.getNPCRegistry().getById(npcId));
		Optional<String> npc = this.searchDialogueByNPCId(npcId);
		
		if(!npc.isPresent() || !citizensNpc.isPresent()) {
			return;
		}
		
		ConfigurationSection dialog = dialogsFile.getConfigurationSection(npc.get());
		
		if(dialog.getBoolean("hologram.enabled", false)) {
			Location location = citizensNpc.get().getStoredLocation();
			location.add(0, 2 + dialog.getDouble("hologram.y-position", 0.4), 0);
			Hologram hologram = HologramsAPI.createHologram(main, location);
			String npcName = dialog.getString("display-name", "John the NPC");
			List<String> lines = dialog.getStringList("hologram.lines");
			
			for(String line : lines) {
				hologram.appendTextLine(ChatColor.translateAlternateColorCodes('&', line.replace("%npc_name%", npcName)));
			}
			
			citizensNpc.get().setAlwaysUseNameHologram(false);
			citizensNpc.get().getEntity().setCustomNameVisible(false);
		}
	}

}
