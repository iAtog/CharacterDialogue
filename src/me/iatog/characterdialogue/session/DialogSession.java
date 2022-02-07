package me.iatog.characterdialogue.session;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.api.dialog.Dialogue;
import me.iatog.characterdialogue.api.events.ExecuteMethodEvent;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.enums.ClickType;
import me.iatog.characterdialogue.interfaces.Session;
import me.iatog.characterdialogue.libraries.YamlFile;
import me.iatog.characterdialogue.placeholders.Placeholders;

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
		if (lines.size() < 1) {
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

			String[] splitted = dialog.split(":");
			String methodName = splitted[0].toUpperCase().trim();
			String arg = dialog.substring(methodName.length() + 1).trim();

			arg = Placeholders.translate(getPlayer(), arg);

			arg = arg.replace("%npc_name%", getDisplayName());
			if (!main.getCache().getMethods().containsKey(methodName)) {
				main.getLogger().warning("The method \"" + methodName + "\" doesn't exist");
				this.stop = true;
				destroy();
				break;
			}

			DialogMethod<? extends JavaPlugin> method = main.getCache().getMethods().get(methodName);
			ExecuteMethodEvent event = new ExecuteMethodEvent(getPlayer(), method, clickType, npcId, "");
			Bukkit.getPluginManager().callEvent(event);

			if (!event.isCancelled()) {
				method.execute(getPlayer(), arg, this);
			}

			if (i == lines.size() - 1) {
				YamlFile playerCache = main.getFileFactory().getPlayerCache();
				String playerPath = "players." + uuid;

				if (playerCache.getBoolean(playerPath + ".remove-effect", false)) {
					float speed = Float.valueOf(playerCache.getString(playerPath + ".last-speed"));
					getPlayer().setWalkSpeed(speed);
					playerCache.set(playerPath + ".remove-effect", false);
					playerCache.save();
				}

				destroy();
				break;
			}
		}
	}

	public void start() {
		this.start(0);
		;
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
