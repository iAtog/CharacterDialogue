package me.iatog.characterdialogue.api;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.api.dialog.DialogHologram;
import me.iatog.characterdialogue.api.dialog.Dialogue;
import me.iatog.characterdialogue.enums.ClickType;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DialogueImpl implements Dialogue {
	
	private final String dialogName;
	private final List<String> lines;
	private final ClickType clickType;
	private final String displayName;
	private DialogHologram hologram;
	private final List<String> firstInteraction;
	private DialoguePermission permissions;
	private final boolean movement;
	
	private final CharacterDialoguePlugin main;
	
	public DialogueImpl(CharacterDialoguePlugin instance, String dialogName, YamlDocument dialogsFile) {
		Section section = dialogsFile.getSection("dialogue." + dialogName);
		
		if(!dialogsFile.contains("dialogue." + dialogName)) {
			throw new NullPointerException("No dialog named \"" + dialogName + "\" was found.");
		}
		
		String click = section.getString("click", "RIGHT").toUpperCase();
		
		this.lines = section.getStringList("dialog");
		this.clickType = ClickType.match(click) ? ClickType.valueOf(click) : ClickType.RIGHT;
		this.firstInteraction = section.getStringList("first-interaction");
		this.displayName = section.getString("display-name", "John the NPC");
		this.dialogName = dialogName;
		
		if(section.getBoolean("hologram.enabled", false)) {
			boolean enabled = section.getBoolean("hologram.enabled");
			float yPosition = Float.parseFloat(section.getString("hologram.y-position", "0.4"));
			List<String> hologramLines = section.getStringList("hologram.lines");
			this.hologram = new DialogHologramImpl(enabled, yPosition, hologramLines);
		}
		
		if(section.contains("permission")) {
			if(section.isString("permission")) {
				this.permissions = new DialoguePermission(section.getString("permission"), null);
			} else {
				this.permissions = new DialoguePermission(section.getString("permission.value"), section.getString("permission.message"));
			}
		}
		
		this.movement = section.getBoolean("allow-movement", true);
		
		this.main = instance;
	}
	
	//public DialogueImpl(String dialogName) { this(CharacterDialoguePlugin.getInstance(), dialogName); }
	
	@Override
	public String getName() {
		return dialogName;
	}
	
	@Override
	public List<String> getLines() {
		return lines;
	}

	@Override
	public ClickType getClickType() {
		return clickType;
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}

	@Override
	public DialogHologram getHologram() {
		return hologram;
	}

	@Override
	public List<String> getFirstInteractionLines() {
		return firstInteraction;
	}

	@Override
	public boolean isFirstInteractionEnabled() {
		return firstInteraction != null;
	}

	@Override
	public boolean start(Player player) {
		return runDialogue(player);
	}

	@Override
	public boolean startFirstInteraction(Player player, boolean log) {
		if(log) {
			YamlDocument playerCache = main.getFileFactory().getPlayerCache();
			List<String> readedDialogues = playerCache.getStringList("players." + player.getUniqueId() + ".readed-dialogues");
			
			if(!playerCache.contains("players." + player.getUniqueId())) {
				readedDialogues = new ArrayList<>();
			}
			
			if(readedDialogues.contains(getName())) {
				return false;
			}
			
			readedDialogues.add(getName());
			playerCache.set("players." + player.getUniqueId() + ".readed-dialogues", readedDialogues);
            try {
                playerCache.save();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
		
		main.getApi().runDialogueExpressions(player, firstInteraction, displayName, this.dialogName);
		return true;
	}
	
	private boolean runDialogue(Player player) {
		main.getApi().runDialogue(player, this);
		return true;
	}

	@Override
	public DialoguePermission getPermissions() {
		return permissions;
	}

	@Override
	public boolean isMovementAllowed() {
		return movement;
	}
	
}
