package me.iatog.characterdialogue.session;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.api.dialog.Dialogue;
import me.iatog.characterdialogue.api.events.DialogueFinishEvent;
import me.iatog.characterdialogue.enums.ClickType;
import me.iatog.characterdialogue.interfaces.Session;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DialogSession implements Session {

	private final CharacterDialoguePlugin main;
	private final UUID uuid;
	private final Dialogue dialogue;
	private final ClickType clickType;
	private final List<String> lines;
	private final String displayName;
	private int index = 0;
	private int npcId;
	private boolean stop;

	private boolean isDestroyed = false;

	public DialogSession(CharacterDialoguePlugin main, Player player, List<String> lines, ClickType clickType,
			int npcId, String displayName, String dialogueName) {
		this.main = main;
		this.uuid = player.getUniqueId();
		this.clickType = clickType;
		this.lines = lines;
		this.displayName = displayName;
		this.dialogue = main.getCache().getDialogues().get(dialogueName);
	}

	public DialogSession(CharacterDialoguePlugin main, Player player, Dialogue dialogue, int npcId) {
		this(main, player, dialogue.getLines(), dialogue.getClickType(), npcId, dialogue.getDisplayName(), dialogue.getName());
		//this.dialogue = dialogue;
		/*this.main = main;
		this.uuid = player.getUniqueId();
		this.clickType = dialogue.getClickType();
		this.lines = dialogue.getLines();
		this.displayName = dialogue.getDisplayName();
		this.dialogue = dialogue;
		this.npcId = npcId;*/
	}

	public DialogSession(CharacterDialoguePlugin main, Player player, Dialogue dialogue) {
		this(main, player, dialogue, -999);
	}

	public void start(int index) {
		if (lines.isEmpty() || index >= lines.size() || index < 0) {
			this.destroy();
			return;
		}

		getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 59999, 4, true, false, false));

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

	public void pause() {
		this.stop = true;
	}

	public void startNext() {
		start(index + 1);
	}

	public void destroy() {
		pause();
		if(isDestroyed) {

			return;
		}

		if(getPlayer() != null || getPlayer().isOnline()) {
			getPlayer().removePotionEffect(PotionEffectType.SLOW);
		}

		DialogueFinishEvent dialogueFinishEvent = new DialogueFinishEvent(getPlayer(), this);
		Bukkit.getPluginManager().callEvent(dialogueFinishEvent);
		main.getApi().enableMovement(this.getPlayer());

		Map<UUID, DialogSession> sessions = main.getCache().getDialogSessions();
		sessions.remove(uuid);

		this.isDestroyed = true;
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
