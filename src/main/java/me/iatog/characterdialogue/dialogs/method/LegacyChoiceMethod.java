package me.iatog.characterdialogue.dialogs.method;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.api.events.ChoiceSelectEvent;
import me.iatog.characterdialogue.dialogs.DialogChoice;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.dialogs.MethodConfiguration;
import me.iatog.characterdialogue.dialogs.MethodContext;
import me.iatog.characterdialogue.misc.Choice;
import me.iatog.characterdialogue.placeholders.Placeholders;
import me.iatog.characterdialogue.session.ChoiceSession;
import me.iatog.characterdialogue.session.DialogSession;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static me.iatog.characterdialogue.util.TextUtils.colorize;

public class LegacyChoiceMethod extends DialogMethod<CharacterDialoguePlugin> implements Listener {

	public static String COMMAND_NAME = "/;;$/5-choice";
	private final Map<UUID, BukkitTask> taskList;

	@Deprecated
	public LegacyChoiceMethod(CharacterDialoguePlugin main) {
		super("legacy_choice", main);
		taskList = new HashMap<>();
		setDisabled(true);
	}

	@Override
	public void execute(MethodContext context) {
		MethodConfiguration configuration = context.getConfiguration();
		DialogSession session = context.getSession();
		Player player = context.getPlayer();

		Map<UUID, ChoiceSession> sessions = getProvider().getCache().getChoiceSessions();
		YamlDocument choicesFile = getProvider().getFileFactory().getChoicesFile();
		YamlDocument config = getProvider().getFileFactory().getConfig();
		String arg = configuration.getArgument();

		if (sessions.containsKey(player.getUniqueId())) {
			session.sendDebugMessage("Choice session found, cancelling.", "ChoiceMethod");
			this.next(context);
			return;
		}

		ChoiceSession choiceSession = new ChoiceSession(getProvider(), player);

		if (!choicesFile.contains("choices." + arg)) {
			String msg = "The choice \"" + arg + "\" doesn't exists.";
			getProvider().getLogger().warning(msg);
			session.sendDebugMessage(msg, "ChoiceMethod");
			this.next(context);
			return;
		}

		this.pause(context);

		for (String choice : choicesFile.getSection("choices." + arg).getRoutesAsStrings(false)) {
			Section section = choicesFile.getSection("choices." + arg + "." + choice);
			String type = section.getString("type");
			String message = section.getString("message", "no message specified");
			String argument = section.getString("argument", "");

			if (type == null || !getProvider().getCache().getChoices().containsKey(type)) {
				getProvider().getLogger().warning("The type of choice '" + choice + "' in " + arg + " isn't valid");
				continue;
			}

			DialogChoice choiceObject = getProvider().getCache().getChoices().get(type);

			if (message.isEmpty() && choiceObject.isArgumentRequired()) {
				getProvider().getLogger().severe("The argument in the choice \"" + choice + "\" is missing");
				continue;
			}

			choiceSession.addChoice(message, choiceObject.getClass(), argument);
		}

		sessions.put(player.getUniqueId(), choiceSession);
		ComponentBuilder questions = new ComponentBuilder("\n");
		String model = config.getString("choice.text-model", "&a{I})&e {S}");

		choiceSession.getChoices().forEach((index, choice) -> {
			String parsedModel = colorize(model).replace("{I}", String.valueOf(index)).replace("{S}",
					choice.getMessage());

			questions.append(Placeholders.translate(player, parsedModel) + " \n")
					.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
							COMMAND_NAME + " " + choiceSession.getUniqueId() + " " + index))
					.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, getSelectText(index)));
		});

		player.spigot().sendMessage(questions.create());

		BukkitTask task = Bukkit.getScheduler().runTaskLater(getProvider(), () -> {
			//Map<UUID, DialogSession> dialogSessionMap = getProvider().getCache().getDialogSessions();
			UUID uuid = player.getUniqueId();

			choiceSession.destroy();
			session.destroy();
			taskList.remove(uuid);

			if(player != null) {
				player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
						TextComponent.fromLegacyText(colorize("&cYou took a long time to answer")));

			}
 		}, 20 * 10);

		taskList.put(player.getUniqueId(), task);
	}

	private BaseComponent[] getSelectText(int index) {
		YamlDocument file = getProvider().getFileFactory().getLanguage();
		String text = file.getString("select-choice", "&aClick here to select #%str%").replace("%str%", index + "");
		return new BaseComponent[] { new TextComponent(colorize(text)) };
	}

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event) {
		String command = event.getMessage();
		Player player = event.getPlayer();
		UUID playerId = player.getUniqueId();
		Map<UUID, ChoiceSession> sessions = getProvider().getCache().getChoiceSessions();
		Map<UUID, DialogSession> dialogSessionMap = getProvider().getCache().getDialogSessions();

		// Command: /;/select 4a8d7587-38f3-35e7-b29c-88c715aa6ba8 1

		String[] args = command.split(" ");
		if (!command.startsWith(COMMAND_NAME)) {
			return;
		}

		event.setCancelled(true);

		if (!sessions.containsKey(playerId) || args.length != 3) {
			return;
		}
		DialogSession dialogSession = dialogSessionMap.get(playerId);
		ChoiceSession session = sessions.get(playerId);
		UUID uuid = UUID.fromString(args[1]);
		int choice = Integer.parseInt(args[2]);

		if (!uuid.toString().equals(session.getUniqueId().toString())) {
			dialogSession.sendDebugMessage("UUID's dont match", "ChoiceMethod:onCommand");
			return;
		}

		runChoice(player, choice);
	}

	@EventHandler
	public void onHeld(PlayerItemHeldEvent event) {
		Player player = event.getPlayer();

		runChoice(player, (event.getNewSlot() + 1));
	}

	public final void runChoice(Player player, int choice) {
		Map<UUID, ChoiceSession> sessions = getProvider().getCache().getChoiceSessions();
		UUID uuid = player.getUniqueId();

		if(!sessions.containsKey(uuid) || player == null) {
			return;
		}

		ChoiceSession session = sessions.get(uuid);
		Choice choiceObject = session.getChoice(choice);

		if(choiceObject == null) {
			return;
		}

		ChoiceSelectEvent choiceEvent = new ChoiceSelectEvent(player, uuid, choiceObject, session);
		Bukkit.getPluginManager().callEvent(choiceEvent);

		if (choiceEvent.isCancelled()) {
			return;
		}

		DialogChoice choiceTarget = getByClassName(choiceObject.getChoiceClass());
		DialogSession dialogSession = getProvider().getCache().getDialogSessions().get(uuid);

		if (dialogSession == null || choiceTarget == null) {
			sessions.remove(uuid);
			return;
		}

		choiceTarget.onSelect(choiceObject.getArgument(), dialogSession, session);
		sessions.remove(uuid);
		taskList.remove(uuid).cancel();
	}

	private DialogChoice getByClassName(Class<? extends DialogChoice> clazz) {
		for (DialogChoice target : getProvider().getCache().getChoices().values()) {
			if (target.getClass() == clazz) {
				return target;
			}
		}

		return null;
	}

}
