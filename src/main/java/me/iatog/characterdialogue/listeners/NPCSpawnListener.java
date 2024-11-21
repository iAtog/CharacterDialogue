package me.iatog.characterdialogue.listeners;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.api.CharacterDialogueAPI;
import net.citizensnpcs.api.event.NPCSpawnEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class NPCSpawnListener implements Listener {
	
	private final CharacterDialoguePlugin main;
	
	public NPCSpawnListener(CharacterDialoguePlugin main) {
		this.main = main;
	}
	
	@EventHandler
	public void onNPCSpawn(NPCSpawnEvent event) {
		NPC npc = event.getNPC();
		int id = npc.getId();
		CharacterDialogueAPI api = main.getApi();
		
		api.loadHologram(id);
	}
}
