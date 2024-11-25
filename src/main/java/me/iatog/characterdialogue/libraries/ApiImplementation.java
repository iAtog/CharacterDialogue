package me.iatog.characterdialogue.libraries;

import dev.dejvokep.boostedyaml.YamlDocument;
import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.api.CharacterDialogueAPI;
import me.iatog.characterdialogue.api.dialog.DialogHologram;
import me.iatog.characterdialogue.api.dialog.Dialogue;
import me.iatog.characterdialogue.api.events.ExecuteMethodEvent;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.enums.ClickType;
import me.iatog.characterdialogue.enums.CompletedType;
import me.iatog.characterdialogue.placeholders.Placeholders;
import me.iatog.characterdialogue.session.DialogSession;
import me.iatog.characterdialogue.session.EmptyDialogSession;
import me.iatog.characterdialogue.util.SingleUseConsumer;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

public class ApiImplementation implements CharacterDialogueAPI {

	private final CharacterDialoguePlugin main;
	private final HologramLibrary hologramLibrary;

	public ApiImplementation(CharacterDialoguePlugin main) {
		this.main = main;
		hologramLibrary = new HologramLibrary(main);
	}

	@Override
	public void reloadHolograms() {
		hologramLibrary.reloadHolograms();
	}

	@Override
	public void loadHologram(int npcId) {
		NPC citizensNpc = CitizensAPI.getNPCRegistry().getById(npcId);

		if (citizensNpc == null) {
			return;
		}

		Dialogue dialogue = getNPCDialogue(npcId);

		if (dialogue == null) {
			return;
		}

		DialogHologram hologram = dialogue.getHologram();

		if (hologram != null && hologram.isEnabled()) {
			Location location = citizensNpc.getStoredLocation();
			location.add(0, 2 + hologram.getY(), 0);
			//Hologram holo = HologramsAPI.createHologram(main, location);
			String npcName = dialogue.getDisplayName();
			List<String> lines = hologram.getLines();

			hologramLibrary.addHologram(lines, location, npcName);
			//for (String line : lines) {
			//	holo.appendTextLine(ChatColor.translateAlternateColorCodes('&', line.replace("%npc_name%", npcName)));
			//}

			citizensNpc.setAlwaysUseNameHologram(false);
		}
	}

	@Override
	public Dialogue getDialogue(String name) {
		return main.getCache().getDialogues().get(name);
	}

	@Override
	public boolean readDialogBy(Player player, String dialog) {
		String path = "players." + player.getUniqueId();
		YamlDocument playerCache = main.getFileFactory().getPlayerCache();
		List<String> readedDialogues = playerCache.getStringList(path + ".readed-dialogues");

		if (!playerCache.contains(path)) {
			readedDialogues = new ArrayList<>();
		}

		if (readedDialogues.contains(dialog)) {
			return false;
		}

		readedDialogues.add(dialog);
		playerCache.set(path + ".readed-dialogues", readedDialogues);

        try {
            playerCache.save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
	}

	@Override
	public boolean wasReadedBy(Player player, String dialog) {
		YamlDocument playerCache = main.getFileFactory().getPlayerCache();
		String path = "players." + player.getUniqueId();
		List<String> readedDialogues = playerCache.getStringList(path + ".readed-dialogues");

		return playerCache.contains(path) && readedDialogues.contains(dialog);
	}

	@Override
	public Dialogue getNPCDialogue(int id) {
		return getDialogue(getNPCDialogueName(id));
	}

	@Override
	public String getNPCDialogueName(int id) {
		YamlDocument config = main.getFileFactory().getConfig();
		return config.getString("npc." + id);
	}

	@Override
	public boolean readDialogBy(Player player, Dialogue dialog) {
		return readDialogBy(player, dialog.getName());
	}

	@Override
	public boolean wasReadedBy(Player player, Dialogue dialog) {
		return wasReadedBy(player, dialog.getName());
	}

	@Override
	public void runDialogue(Player player, Dialogue dialogue, boolean debugMode) {
		if (main.getCache().getDialogSessions().containsKey(player.getUniqueId())) {
			//player.sendMessage(TextUtils.colorize("&c["+dialogue.getName()+"] Session rejected"));
			return;
		}

		DialogSession session = new DialogSession(main, player, dialogue);
		session.setDebugMode(debugMode);
		if (!dialogue.isMovementAllowed()) {
			disableMovement(player);
		}

		main.getCache().getDialogSessions().put(player.getUniqueId(), session);
		//player.sendMessage(TextUtils.colorize("&c["+dialogue.getName()+"] Session started"));
		session.start(0);
	}

	@Override
	public void runDialogue(Player player, String dialogueName, boolean debugMode) {
		runDialogue(player, getDialogue(dialogueName), debugMode);
	}

	@Override
	public void runDialogueExpression(Player player, String dialog) {
		runDialogueExpression(player, dialog, "Dummy");
	}
	
	public void runDialogueExpression(Player player, String dialog, String npcName) {
		runDialogueExpression(player, dialog, npcName, SingleUseConsumer.create((x) -> {}), new EmptyDialogSession(main, player, Collections.singletonList(dialog), npcName));
	}
	
	@Override
	public void runDialogueExpression(Player player, String dialog, String npcName, SingleUseConsumer<CompletedType> onComplete, DialogSession session) {
		String[] split = dialog.split(":");
		String methodName = split[0].toUpperCase().trim();
		String arg = dialog.substring(methodName.length() + 1).trim();

		if (!main.getCache().getMethods().containsKey(methodName)) {
			main.getLogger().warning("The method \"" + methodName + "\" doesn't exists");
			session.destroy();
			return;
		}

		arg = Placeholders.translate(player, arg);
		arg = arg.replace("%npc_name%", npcName);

		DialogMethod<? extends JavaPlugin> method = main.getCache().getMethods().get(methodName);
		ExecuteMethodEvent event = new ExecuteMethodEvent(player, method, ClickType.ALL, -999, npcName);
		Bukkit.getPluginManager().callEvent(event);

		if (!event.isCancelled()) {
			method.execute(player, arg, session, onComplete);
		}
	}

	@Override
	public void runDialogueExpressions(Player player, List<String> lines, ClickType clickType, int npcId,
			String displayName, String dialogueName) {
		if (main.getCache().getDialogSessions().containsKey(player.getUniqueId())) {
			return;
		}

		DialogSession session = new DialogSession(main, player, lines, clickType, npcId,
				displayName == null ? "Dummy" : displayName, dialogueName);

		main.getCache().getDialogSessions().put(player.getUniqueId(), session);
		session.start(0);
	}

	@Override
	public void runDialogueExpressions(Player player, List<String> lines, String displayName, String dialogueName) {
		runDialogueExpressions(player, lines, ClickType.ALL, -999, displayName, dialogueName);
	}

	@Override
	public boolean enableMovement(Player player) {
		YamlDocument playerCache = main.getFileFactory().getPlayerCache();
		String playerPath = "players." + player.getUniqueId();
		
		if(!playerCache.getBoolean(playerPath + ".remove-effect", false)) {
			return false;
		}
		
		float speed = Float.parseFloat(playerCache.getString(playerPath + ".last-speed"));
		player.setWalkSpeed(speed);
		player.removePotionEffect(PotionEffectType.JUMP);
		main.getCache().getFrozenPlayers().remove(player.getUniqueId());
		
		playerCache.set(playerPath + ".remove-effect", false);

        try {
            playerCache.save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return playerCache.getBoolean(playerPath + ".remove-effect", false);
	}

	@Override
	public boolean disableMovement(Player player) {
		YamlDocument playerCache = main.getFileFactory().getPlayerCache();
		String path = "players." + player.getUniqueId();
		
		if(playerCache.getBoolean(path + ".remove-effect", false)) {
			return false;
		}
		
		playerCache.set(path + ".last-speed", player.getWalkSpeed());
		playerCache.set(path + ".remove-effect", true);

        try {
            playerCache.save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        main.getCache().getFrozenPlayers().add(player.getUniqueId());
		player.setWalkSpeed(0);
		player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 128));

		return !playerCache.getBoolean(path + ".remove-effect", true);
	}

	@Override
	public boolean canEnableMovement(Player player) {
		YamlDocument playerCache = main.getFileFactory().getPlayerCache();
		String path = "players." + player.getUniqueId();
		
		return playerCache.getBoolean(path + ".remove-effect", false);
	}

	@Override
	public Map<String, Dialogue> getDialogues() {
		return main.getCache().getDialogues();
	}

	@Override
	public int getBukkitVersion() {
		return Integer.parseInt(Bukkit.getBukkitVersion().split("-")[0].split("\\.")[1]);
	}
}
