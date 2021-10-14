package me.iatog.characterdialogue.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.api.CharacterDialogueAPI;
import me.iatog.characterdialogue.enums.ClickType;
import me.iatog.characterdialogue.interfaces.FileFactory;
import me.iatog.characterdialogue.libraries.YamlFile;
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
		YamlFile playerCache = main.getFileFactory().getPlayerCache();
		CharacterDialogueAPI api = main.getApi();
		Optional<String> search = api.searchDialogueByNPCId(id);
		String path = "players." + player.getUniqueId();
		
		if(!search.isPresent() || main.getCache().getSessions().containsKey(player.getUniqueId())) {
			return;
		}
		
		ConfigurationSection dialogueSection = dialogsFile.getConfigurationSection(search.get());
		ClickType clickType = ClickType.valueOf(dialogueSection.getString("click"));

		if(((event instanceof NPCRightClickEvent && clickType != ClickType.RIGHT) || (event instanceof NPCLeftClickEvent && clickType != ClickType.LEFT)) && clickType != ClickType.ALL) {
			return;
		}
		
		if(dialogueSection.getBoolean("after-first-time.enabled", false)) {
			List<String> readedDialogues = playerCache.getStringList(path + ".readed-dialogues");
			List<String> afterFirstTimeDialog = dialogueSection.getStringList("after-first-time.dialog");
						
			if(afterFirstTimeDialog != null && playerCache.contains(path) && readedDialogues.contains(dialogueSection.getName())) {
				main.getApi().executeDialog(afterFirstTimeDialog, player, clickType, id, dialogueSection.getString("display-name", "John the NPC"));
				return;
			}
		}
		
		List<String> dialogs = dialogueSection.getStringList("dialog");
		
		main.getApi().executeDialog(dialogs, player, clickType, id, dialogueSection.getString("display-name", "John the NPC"));
		
		if(dialogueSection.getBoolean("after-first-time.enabled", false)) {
			List<String> readedDialogues = playerCache.getStringList(path + ".readed-dialogues");
			
			if(!playerCache.contains(path)) {
				readedDialogues = new ArrayList<>();
			}
			
			if(readedDialogues.contains(dialogueSection.getName())) {
				return;
			}
			
			readedDialogues.add(dialogueSection.getName());
			playerCache.set(path + ".readed-dialogues", readedDialogues);
			playerCache.save();
			main.getLogger().log(Level.FINE, "Saved cache for " + player.getName() + " in " + dialogueSection.getName());
		}
		/*
		DialogSession session = new DialogSession(main, player, dialogs, clickType, id, dialogueSection.getString("display-name", "John the NPC"));
		main.getCache().getSessions().put(player.getUniqueId(), session);
		session.start(0);*/
	}
}
