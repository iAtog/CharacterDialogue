package me.iatog.characterdialogue.session;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.api.dialog.Dialogue;
import me.iatog.characterdialogue.api.events.DialogueFinishEvent;
import me.iatog.characterdialogue.enums.ClickType;
import me.iatog.characterdialogue.enums.CompletedType;
import me.iatog.characterdialogue.interfaces.Session;
import me.iatog.characterdialogue.util.SingleUseConsumer;
import me.iatog.characterdialogue.util.TextUtils;
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
	private Dialogue dialogue;
	private final ClickType clickType;
	private final List<String> lines;
	private final String displayName;
	private int index = 0;
	private int npcId;
	private boolean stop;
	private boolean debug = false;
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
		this.dialogue = dialogue;
	}

	public DialogSession(CharacterDialoguePlugin main, Player player, Dialogue dialogue) {
		this(main, player, dialogue, -999);
	}

	public void start(int index) {
		if(lines.isEmpty() || index >= lines.size() || getPlayer() == null) {
			this.destroy();
			return;
		}

		setCurrentIndex(index);
		sendDebugMessage("Started in: " + index + " &7[&c"+ this.stop + "&7]",
				"DialogSession:63");
		getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE,
				4, true,
				false, false));

		sendDebugMessage("Running expression", "DialogSession:68");

		SingleUseConsumer<CompletedType> consumer = SingleUseConsumer.create((result) -> {
			sendDebugMessage("Starting consumer", "SingleUseConsumer");
			
			if (index >= lines.size()) {
				this.sendDebugMessage("Dialogue reached the end", "SingleUseConsumer");
				destroy();
				if (main.getApi().canEnableMovement(getPlayer())) {
					main.getApi().enableMovement(getPlayer());
				}
				return;
			}
			sendDebugMessage("Consumer passed", "SingleUseConsumer");
			if(result == CompletedType.DESTROY) {
				sendDebugMessage("Dialogue destroyed", "SingleUseConsumer");
				this.destroy();
				return;
			} else if (result == CompletedType.PAUSE) {
				if(getPlayer() != null)
					getPlayer().removePotionEffect(PotionEffectType.SLOW);
				sendDebugMessage("Dialogue paused", "SingleUseConsumer");
				return;
			}
			// CONTINUE
			sendDebugMessage("Dialogue continue", "DialogSession:86");
			this.startNext();
		});

		main.getApi().runDialogueExpression(getPlayer(), lines.get(getCurrentIndex()), displayName,
				consumer, this);
	}

	public boolean hasNext() {
		return (index + 1) < lines.size();
	}
	
	public void start() {
		this.start(0);
	}

	public void startNext() {
		start(index + 1);
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

	public boolean isPaused() {
		return this.stop;
	}

	public void destroy() {
		this.stop = true;
		if(isDestroyed) {
			return;
		}

		if(getPlayer() != null) {
			if(getPlayer().isOnline()) {
				getPlayer().removePotionEffect(PotionEffectType.SLOW);
			}
		}
		Map<UUID, DialogSession> sessions = main.getCache().getDialogSessions();

		Bukkit.getPluginManager().callEvent(new DialogueFinishEvent(getPlayer(), this));
		main.getApi().enableMovement(this.getPlayer());
		sessions.remove(uuid);
		this.isDestroyed = true;

		sendDebugMessage("Session destroyed", "DialogSession:147");
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

	public void setDebugMode(boolean debug) {
		this.debug = debug;
	}

	public boolean isOnDebugMode() {
		return this.debug;
	}

	public void sendDebugMessage(String message, String codeReference) {
		if(getPlayer() != null && debug) {
			getPlayer().sendMessage(TextUtils.colorize("&7[&c" + this.getDialogue().getName() + "&7] &f" + message + " &8(&7" + codeReference + "&8)"));
		}
	}
}
