package me.iatog.characterdialogue.session;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.iatog.characterdialogue.api.events.DialogueFinishEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.api.dialog.Dialogue;
import me.iatog.characterdialogue.enums.ClickType;
import me.iatog.characterdialogue.interfaces.Session;

public class DialogSession implements Session {

	private CharacterDialoguePlugin main;
	private UUID uuid;
	private Dialogue dialogue;
	private ClickType clickType;
	private List<String> lines;
	private String displayName;
	private int index;
	private int npcId;
	private boolean stop;

	public DialogSession(CharacterDialoguePlugin main, Player player, List<String> lines, ClickType clickType,
			int npcId, String displayName) {
		this.main = main;
		this.uuid = player.getUniqueId();
		this.clickType = clickType;
		this.lines = lines;
		this.displayName = displayName;
	}

	public DialogSession(CharacterDialoguePlugin main, Player player, Dialogue dialogue, int npcId) {
		this(main, player, dialogue.getLines(), dialogue.getClickType(), npcId, dialogue.getDisplayName());
		this.dialogue = dialogue;
	}

	public DialogSession(CharacterDialoguePlugin main, Player player, Dialogue dialogue) {
		this(main, player, dialogue, -999);
	}

	public void start(int index) {
		if (lines.isEmpty()) {
			this.destroy();
			return;
		}

		for (int i = index; i < lines.size(); i++) {
			if (stop) {
				this.stop = false;
				break;
			}

			String dialog = lines.get(i);
			this.index = i;

			if (!dialog.contains(":")) {
				continue;
			}
			
			main.getApi().runDialogueExpression(getPlayer(), dialog, displayName, (x) -> {
				destroy();
			}, this);

			if (i == lines.size() - 1) {
				destroy();
				if(main.getApi().canEnableMovement(getPlayer())) {
					main.getApi().enableMovement(getPlayer());
				}
				
				break;
			}
		}
	}

	public boolean hasNext() {
		return (index + 1) < lines.size();
	}
	
	public void start() {
		this.start(0);
	}

	public void setCurrentIndex(int index) {
		this.index = index;
	}

	public int getCurrentIndex() {
		return index;
	}

	public List<String> getLines() {
		return lines;
	}

	public Player getPlayer() {
		return Bukkit.getPlayer(uuid);
	}

	public void cancel() {
		this.stop = true;
	}

	public void destroy() {
		cancel();
		DialogueFinishEvent dialogueFinishEvent = new DialogueFinishEvent(getPlayer(), this);
		Bukkit.getPluginManager().callEvent(dialogueFinishEvent);
		main.getApi().enableMovement(this.getPlayer());

		Map<UUID, DialogSession> sessions = main.getCache().getDialogSessions();
		if (sessions.containsKey(uuid)) {
			sessions.remove(uuid);
		}
	}

	public ClickType getClickType() {
		return clickType;
	}

	public int getNPCId() {
		return npcId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public Dialogue getDialogue() {
		return dialogue;
	}
}
