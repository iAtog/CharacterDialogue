package me.iatog.characterdialogue.api;

import java.util.List;
import java.util.Optional;

import org.bukkit.entity.Player;

import me.iatog.characterdialogue.enums.ClickType;

public interface CharacterDialogueAPI {
	
	/**
	 * search if a dialogue is present
	 * @param id the id of the npc
	 * @return the path of the dialogue
	 */
	Optional<String> searchDialogueByNPCId(int id);
	
	/**
	 * reload all character holograms.
	 */
	void reloadHolograms();
	
	/**
	 * load npc hologram
	 * @param npcId the id of the npc
	 */
	void loadHologram(int npcId);
	
	
	void executeDialog(List<String> dialog, Player player, ClickType type, int npcId, String displayName);
	
	void executeDialog(List<String> dialog, Player player, String displayName);
}
