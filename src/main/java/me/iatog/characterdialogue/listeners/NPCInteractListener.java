package me.iatog.characterdialogue.listeners;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.api.CharacterDialogueAPI;
import me.iatog.characterdialogue.api.dialog.Dialogue;
import me.iatog.characterdialogue.api.dialog.Dialogue.DialoguePermission;
import me.iatog.characterdialogue.enums.ClickType;
import me.iatog.characterdialogue.placeholders.Placeholders;
import me.iatog.characterdialogue.util.TextUtils;
import net.citizensnpcs.api.event.NPCClickEvent;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class NPCInteractListener implements Listener {

	private final CharacterDialoguePlugin main;

	public NPCInteractListener(CharacterDialoguePlugin main) {
		this.main = main;
	}

	@EventHandler
	public void onNPCRightClick(NPCRightClickEvent event) {
		runEvent(event);
	}

	@EventHandler
	public void onNPCLeftClick(NPCLeftClickEvent event) {
		runEvent(event);
	}

	private void runEvent(NPCClickEvent event) {
		CharacterDialogueAPI api = main.getApi();
		Player player = event.getClicker();

		NPC npc = event.getNPC();
		Dialogue dialogue = api.getNPCDialogue(npc.getId());

		if (dialogue == null || main.getCache().getDialogSessions().containsKey(player.getUniqueId())) {
			return;
		}

		long currentTime = System.currentTimeMillis();
		// Cooldown logic

		if(player.hasMetadata("dialogueCooldown")) {
			long cooldown = player.getMetadata("dialogueCooldown").get(0).asLong();
			if(currentTime < cooldown) {
				player.sendMessage(TextUtils.colorize("&cCalm down."));
				return;
			}
		}

		long cooldownTime = 2000;
		player.setMetadata("dialogueCooldown", new FixedMetadataValue(main, currentTime + cooldownTime));

		ClickType clickType = dialogue.getClickType();

		if ((clickType != ClickType.ALL) &&
				((event instanceof NPCRightClickEvent && clickType != ClickType.RIGHT) ||
						(event instanceof NPCLeftClickEvent && clickType != ClickType.LEFT))) {
			return;
		}

		DialoguePermission permissions = dialogue.getPermissions();
		
		if(permissions != null && permissions.getPermission() != null) {
			String permission = permissions.getPermission();
			String message = permissions.getMessage();
			
			if(!player.hasPermission(permission)) {
				if(message != null) {
					player.sendMessage(
							Placeholders.translate(player, message.replace("%npc_name%", dialogue.getDisplayName()))
					);
				}

				return;
			}
		}

		if (dialogue.isFirstInteractionEnabled() && !api.wasReadedBy(player, dialogue)) {
			dialogue.startFirstInteraction(player, true, npc);
			return;
		}

		event.setCancelled(true);
		dialogue.start(player, npc);
	}

	@EventHandler
	private void interactAtNPC(PlayerInteractEntityEvent event) {
		if(event.getRightClicked().hasMetadata("characterdialogue-npc")) {
			//event.setCancelled(true);

		}
	}
}
