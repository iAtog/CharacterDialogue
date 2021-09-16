package me.iatog.characterdialogue.listeners;

import java.util.List;
import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.api.CharacterDialogueAPI;
import net.citizensnpcs.api.event.NPCSpawnEvent;
import net.citizensnpcs.api.npc.NPC;

public class NPCSpawnListener implements Listener {
	
	private CharacterDialoguePlugin main;
	
	public NPCSpawnListener(CharacterDialoguePlugin main) {
		this.main = main;
	}
	
	@EventHandler
	public void onNPCSpawn(NPCSpawnEvent event) {
		NPC npc = event.getNPC();
		int id = npc.getId();
		CharacterDialogueAPI api = main.getApi();
		Optional<String> search = api.searchDialogueByNPCId(id);
		
		if(!search.isPresent()) {
			return;
		}
		
		ConfigurationSection section = main.getFileFactory().getDialogs().getConfigurationSection(search.get());
		if(section.contains("hologram") && section.getBoolean("hologram.enabled", false)) {
			Location location = npc.getStoredLocation();
			location.add(0, 2 + section.getDouble("hologram.y-position", 0.6), 0);
			Hologram hologram = HologramsAPI.createHologram(main, location);
			String npcName = section.getString("display-name", "John the NPC");
			List<String> lines = section.getStringList("hologram.lines");
			
			for(String line : lines) {
				hologram.appendTextLine(ChatColor.translateAlternateColorCodes('&', line.replace("%npc_displayname%", npcName)));
			}
			
			npc.setAlwaysUseNameHologram(false);
			npc.getEntity().setCustomNameVisible(false);
		}
		
	}
	
}
