package me.iatog.characterdialogue.session;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.api.events.ExecuteMethodEvent;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.enums.ClickType;

public class DialogSession {

	private CharacterDialoguePlugin main;
	private UUID uuid;
	private List<String> dialogs;
	private int index;
	private ClickType clickType;
	private int npcId;
	private String dialogName;
	private boolean stop;

	public DialogSession(CharacterDialoguePlugin main, Player player, List<String> dialogs, ClickType clickType, int npcId, String dialogName) {
		this.main = main;
		this.uuid = player.getUniqueId();
		this.dialogs = dialogs;
		this.clickType = clickType;
		this.npcId = npcId;
		this.dialogName = dialogName;
	}

	public void start(int index) {
		for (int i = index; i < dialogs.size(); i++) {
			if (stop) {
				this.stop = false;
				break;
			}
			String dialog = dialogs.get(i);
			this.index = i;
			if (!dialog.contains(":")) {
				continue;
			}
			String[] splitted = dialog.split(":");
			String methodName = splitted[0].toUpperCase().trim();
			String arg = dialog.substring(methodName.length() + 1).trim();
			if(main.getHooks().isPlaceHolderAPIEnabled()) {
				arg = main.getHooks().getPlaceHolderAPIHook().translatePlaceHolders(getPlayer(), arg);
			} else {
				arg = arg.replace("%player_name%", getPlayer().getName());
			}
			
			if (!main.getCache().getMethods().containsKey(methodName)) {
				main.getLogger().warning("The method \"" + methodName + "\" doesn't exist");
				this.stop = true;
				destroy();
				break;
			}
			
			DialogMethod method = main.getCache().getMethods().get(methodName);
			ExecuteMethodEvent event = new ExecuteMethodEvent(getPlayer(), method, clickType, npcId, dialogName);
			Bukkit.getPluginManager().callEvent(event);
			
			if(!event.isCancelled()) {
				method.execute(Bukkit.getPlayer(uuid), arg, this);
			}
			
			if(i == dialogs.size() - 1) {
				destroy();
				break;
			}
		}
	}
	
	public void setCurrentIndex(int index) {
		this.index = index;
	}

	public int getCurrentIndex() {
		return index;
	}

	public List<String> getDialogs() {
		return dialogs;
	}
	
	public Player getPlayer() {
		return Bukkit.getPlayer(uuid);
	}

	public void cancel() {
		this.stop = true;
	}
	
	public void destroy() {
		if(main.getCache().getSessions().containsKey(uuid)) {
			main.getCache().getSessions().remove(uuid);
		}
	}

	public ClickType getClickType() {
		return clickType;
	}

	public int getNPCId() {
		return npcId;
	}

	public String getDialogName() {
		return dialogName;
	}
}
