package me.iatog.characterdialogue.listeners;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.enums.ClickType;
import me.iatog.characterdialogue.interfaces.FileFactory;
import me.iatog.characterdialogue.libraries.YamlFile;
import me.iatog.characterdialogue.session.DialogSession;
import net.citizensnpcs.api.event.NPCClickEvent;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;

public class NPCInteractListener implements Listener {
	
	private CharacterDialoguePlugin main;
	
	public NPCInteractListener(CharacterDialoguePlugin main) {
		this.main = main;
	}
	
	@EventHandler
	public void onNPCRightClick(NPCRightClickEvent event) {
		runEvent(event);
	}
	
	@EventHandler
	public void onNPCLeftClick(NPCLeftClickEvent event) {
		runEvent(event);
	}
	
	private void runEvent(NPCClickEvent event) {
		Player player = event.getClicker();
		NPC npc = event.getNPC();
		int id = npc.getId();
		FileFactory files = main.getFileFactory();
		YamlFile dialogsFile = files.getDialogs();
		
		if(main.getCache().getSessions().containsKey(player.getUniqueId())) {
			return;
		}
		
		if(!dialogsFile.contains("dialogs.npcs."+id)) {
			return;
		}
		
		ClickType clickType = ClickType.valueOf(dialogsFile.getString("dialogs.npcs."+id+".click"));
		if((event instanceof NPCRightClickEvent && clickType != ClickType.RIGHT) || (event instanceof NPCLeftClickEvent && clickType != ClickType.LEFT)) {
			return;
		}
		
		List<String> dialogs = dialogsFile.getStringList("dialogs.npcs."+id+".dialog");
		DialogSession session = new DialogSession(main, player, dialogs);
		main.getCache().getSessions().put(player.getUniqueId(), session);
		session.start(0);
	}
}
