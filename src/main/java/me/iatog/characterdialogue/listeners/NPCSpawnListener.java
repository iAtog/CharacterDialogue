package me.iatog.characterdialogue.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

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
		if(!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
			return;
		}
		
		NPC npc = event.getNPC();
		int id = npc.getId();
		CharacterDialogueAPI api = main.getApi();
		
		api.loadHologram(id);
	}
}
