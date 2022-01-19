package me.iatog.characterdialogue.api;

import java.util.List;

import org.bukkit.entity.Player;

import me.iatog.characterdialogue.api.dialog.Dialogue;
import me.iatog.characterdialogue.enums.ClickType;

public interface CharacterDialogueAPI {
	//String searchDialogueByNPCId(int id);
	
	Dialogue getDialogue(String name);
	
	/**
	 * reload all character holograms.
	 */
	void reloadHolograms();
	
	/**
	 * load npc hologram
	 * @param npcId the id of the npc
	 */
	void loadHologram(int npcId);
	
	boolean readDialogBy(Player player, String dialog);
	boolean wasReadedBy(Player player, String dialog);
	
	boolean readDialogBy(Player player, Dialogue dialog);
	boolean wasReadedBy(Player player, Dialogue dialog);
	
	void runDialog(Player player, List<String> dialog, String displayName);
	void runDialog(Player player, String dialog, String displayName);
	void runDialog(Player player, List<String> dialog, ClickType type, int npcId, String displayName);
	
	Dialogue getNPCDialogue(int id);
	String getNPCDialogueName(int id);
	/*
	void executeDialog(List<String> dialog, Player player, ClickType type, int npcId, String displayName);
	
	void executeDialog(List<String> dialog, Player player, String displayName);
	
	void executeDialog(String dialogName, Player player, String displayName);
	
	
	*/
	
}
