package me.iatog.characterdialogue.api;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.api.dialog.Dialogue;
import me.iatog.characterdialogue.enums.ClickType;
import me.iatog.characterdialogue.enums.CompletedType;
import me.iatog.characterdialogue.libraries.ApiImplementation;
import me.iatog.characterdialogue.session.DialogSession;
import me.iatog.characterdialogue.util.SingleUseConsumer;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface CharacterDialogueAPI {

	Dialogue getDialogue(String name);

	void reloadHolograms();

	void loadHologram(int npcId);
	
	boolean readDialogBy(Player player, String dialog) throws IOException;
	boolean wasReadedBy(Player player, String dialog);
	
	boolean readDialogBy(Player player, Dialogue dialog);
	boolean wasReadedBy(Player player, Dialogue dialog);
	
	void runDialogue(Player player, Dialogue dialogue, boolean debugMode);
	void runDialogue(Player player, String dialogueName, boolean debugMode);
	void runDialogueExpression(Player player, String dialog);
	void runDialogueExpression(Player player, String dialog, String npcName);
	void runDialogueExpression(Player player, String dialog, String npcName, SingleUseConsumer<CompletedType> onComplete, DialogSession session);
	void runDialogueExpressions(Player player, List<String> lines, ClickType type, int npcId, String displayName, String dialogueName);
	void runDialogueExpressions(Player player, List<String> lines, String displayName, String dialogueName) ;
	
	Dialogue getNPCDialogue(int id);
	Map<String, Dialogue> getDialogues();
	String getNPCDialogueName(int id);
	int getBukkitVersion();
	
	boolean enableMovement(Player player);
	boolean disableMovement(Player player);
	boolean canEnableMovement(Player player);
	
	public static CharacterDialogueAPI get() {
		return new ApiImplementation(CharacterDialoguePlugin.getInstance());
	}
}
