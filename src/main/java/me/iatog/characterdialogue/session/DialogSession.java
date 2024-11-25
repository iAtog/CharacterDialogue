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
	private final Dialogue dialogue;
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


/*
	public void startOldNew(int index) {
		if(lines.isEmpty() || index >= lines.size() || getPlayer() == null) {
			this.destroy();
			return;
		}

		sendDebugMessage("Started in: " + index + " &7[&c"+ this.stop + "&7]", "DialogSession:58");

		getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 4, true, false, false));

		while (index < lines.size()) {
			if(stop) {
				stop = false;
				break;
			}

			String dialog = lines.get(index);
			this.index = index;

			if(!dialog.contains(":")) {
				index++;
				continue;
			}

			sendDebugMessage("Running expression", "DialogSession:71");
			AtomicReference<CompletedType> completionAtomic = new AtomicReference<>(CompletedType.CONTINUE);
			CountDownLatch latch = new CountDownLatch(1);

			main.getApi().runDialogueExpression(getPlayer(), dialog, displayName, SingleUseConsumer.create(completed -> {
				completionAtomic.set(completed);
				latch.countDown();
			}), this);

			try {
				latch.await();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}

			index++;
			CompletedType result = completionAtomic.get();

			if(result == CompletedType.PAUSE) {
				this.stop = true;
				break;
			} else if(result == CompletedType.DESTROY) {
				this.destroy();
				break;
			}

			if (index >= lines.size()) {
				this.sendDebugMessage("Dialogue reached the end", "DialogSession:98");
				destroy();
				if (main.getApi().canEnableMovement(getPlayer())) {
					main.getApi().enableMovement(getPlayer());
				}
				break;
			}
		}
	}

	public void startOld(int index) {

			if (lines.isEmpty() || index >= lines.size() || index < 0) {
				this.destroy();
				sendDebugMessage("Destroyed &7| &c" + lines.isEmpty() + " &7| &c" + (index >= lines.size()) + " &7| &c" + (index < 0), "DialogSession:56");
				return;
			}

			if(getPlayer() == null) {
				destroy();
				return;
			}

			sendDebugMessage("Started in: " + index + " &7[&c"+ this.stop + "&7]", "DialogSession:60");
			getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 4, true, false, false));

			while (index < lines.size() && !stop) {
				String dialog = lines.get(index);
				this.index = index;

				if (!dialog.contains(":")) {
					index++;
					continue;
				}

				sendDebugMessage("Running expression", "DialogSession:72");
				//main.getApi().runDialogueExpression(getPlayer(), dialog, displayName, (x) -> destroy(), this);

				index++;

				if (index >= lines.size()) {
					this.sendDebugMessage("Dialogue reached the end", "DialogSession:78");
					destroy();
					if (main.getApi().canEnableMovement(getPlayer())) {
						main.getApi().enableMovement(getPlayer());
					}
					break;
				}
			}

			this.stop = false;
	}

*/
	public boolean hasNext() {
		return (index + 1) < lines.size();
	}
	
	public void start() {
		this.start(0);
	}

	public void startNext() {

		start(index + 1);

		//this.sendDebugMessage("Started next", "DialogSession:130");
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

	public void pausee() {
		this.stop = true;
	}

	public void destroy() {
		this.stop = true;
		//sendDebugMessage("Attempting to destroy.", "DialogSession:135");
		if(isDestroyed) {
			return;
		}

		if(getPlayer() != null) {
			if(getPlayer().isOnline()) {
				getPlayer().removePotionEffect(PotionEffectType.SLOW);
			}

		}

		sendDebugMessage("Session destroyed", "DialogSession:147");
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

	public void setDebugMode(boolean debug) {
		this.debug = debug;
	}

	public void sendDebugMessage(String message, String codeReference) {
		if(getPlayer() != null && debug) {
			getPlayer().sendMessage(TextUtils.colorize("&7[&c" + this.getDialogue().getName() + "&7] &f" + message + " &8(&7" + codeReference + "&8)"));
		}
	}
}
