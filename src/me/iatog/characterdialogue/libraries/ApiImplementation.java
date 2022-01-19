package me.iatog.characterdialogue.libraries;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.api.CharacterDialogueAPI;
import me.iatog.characterdialogue.api.dialog.Dialogue;
import me.iatog.characterdialogue.enums.ClickType;
import me.iatog.characterdialogue.session.DialogSession;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

public class ApiImplementation implements CharacterDialogueAPI {
	
	private CharacterDialoguePlugin main;
	
	public ApiImplementation(CharacterDialoguePlugin main) {
		this.main = main;
	}
	
	/*
	@Override
	public @Nullable String searchDialogueByNPCId(int id) {
		YamlFile dialogsFile = main.getFileFactory().getDialogs();
		String result = null;
		for(String name : dialogsFile.getConfigurationSection("dialogs.npcs").getKeys(false)) {
			String path = "dialogs.npcs."+name;
			if(dialogsFile.getInt(path+".npc-id") == id) {
				result = path;
				break;
			}
		}
		return result;
	}*/

	@Override
	public void reloadHolograms() {
		if(!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
			return;
		}
		
		YamlFile dialogsFile = main.getFileFactory().getDialogs();
		String dialoguesPath = "dialogue";
		
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
		NPC citizensNpc = CitizensAPI.getNPCRegistry().getById(npcId);
		String name = getNPCDialogueName(npcId);
		
		if(name == null || citizensNpc == null) {
			return;
		}
		
		ConfigurationSection dialog = dialogsFile.getConfigurationSection("dialogue." + name);
		
		if(dialog.getBoolean("hologram.enabled", false)) {
			Location location = citizensNpc.getStoredLocation();
			location.add(0, 2 + dialog.getDouble("hologram.y-position", 0.4), 0);
			Hologram hologram = HologramsAPI.createHologram(main, location);
			String npcName = dialog.getString("display-name", "John the NPC");
			List<String> lines = dialog.getStringList("hologram.lines");
			
			for(String line : lines) {
				hologram.appendTextLine(ChatColor.translateAlternateColorCodes('&', line.replace("%npc_name%", npcName)));
			}
			
			citizensNpc.setAlwaysUseNameHologram(false);
		}
	}

	@Override
	public Dialogue getDialogue(String name) {
		return main.getCache().getDialogues().get(name);
	}

	@Override
	public boolean readDialogBy(Player player, String dialog) {
		String path = "players." + player.getUniqueId();
		YamlFile playerCache = main.getFileFactory().getPlayerCache();
		List<String> readedDialogues = playerCache.getStringList(path + ".readed-dialogues");
		
		if(!playerCache.contains(path)) {
			readedDialogues = new ArrayList<>();
		}
		
		if(readedDialogues.contains(dialog)) {
			return false;
		}
		
		readedDialogues.add(dialog);
		playerCache.set(path + ".readed-dialogues", readedDialogues);
		playerCache.save();
		return true;
	}

	@Override
	public boolean wasReadedBy(Player player, String dialog) {
		YamlFile playerCache = main.getFileFactory().getPlayerCache();
		String path = "players." + player.getUniqueId();
		List<String> readedDialogues = playerCache.getStringList(path + ".readed-dialogues");
		
		return playerCache.contains(path) && readedDialogues.contains(dialog);
	}

	@Override
	public void runDialog(Player player, List<String> dialog, String displayName) {
		this.runDialog(player, dialog, ClickType.ALL, -1, displayName);
	}

	@Override
	public void runDialog(Player player, String dialog, String displayName) {
		Dialogue dialogue = getDialogue(dialog);
		
		if(dialogue == null) {
			return;
		}
		
		this.runDialog(player, dialogue.getLines(), displayName);
	}

	@Override
	public void runDialog(Player player, List<String> dialog, ClickType clickType, int npcId, String displayName) {
		if(main.getCache().getDialogSessions().containsKey(player.getUniqueId())) {
			return;
		}
		
		DialogSession session = new DialogSession(main, player, dialog, clickType, npcId, displayName == null ? "John the NPC" : displayName);
		
		main.getCache().getDialogSessions().put(player.getUniqueId(), session);
		session.start(0);
	}

	@Override
	public Dialogue getNPCDialogue(int id) {
		return getDialogue(getNPCDialogueName(id));
	}

	@Override
	public String getNPCDialogueName(int id) {
		YamlFile npc = main.getFileFactory().getNPC();
		
		return npc.getString("assigns." + id);
	}

	@Override
	public boolean readDialogBy(Player player, Dialogue dialog) {
		return readDialogBy(player, dialog.getName());
	}

	@Override
	public boolean wasReadedBy(Player player, Dialogue dialog) {
		return wasReadedBy(player, dialog.getName());
	}
}
