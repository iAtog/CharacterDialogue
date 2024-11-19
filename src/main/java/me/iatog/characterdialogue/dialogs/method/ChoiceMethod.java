package me.iatog.characterdialogue.dialogs.method;

import static me.iatog.characterdialogue.util.TextUtils.colorize;

import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.api.events.ChoiceSelectEvent;
import me.iatog.characterdialogue.dialogs.DialogChoice;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.libraries.YamlFile;
import me.iatog.characterdialogue.misc.Choice;
import me.iatog.characterdialogue.session.ChoiceSession;
import me.iatog.characterdialogue.session.DialogSession;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.scheduler.BukkitTask;

public class ChoiceMethod extends DialogMethod<CharacterDialoguePlugin> implements Listener {

	public static String COMMAND_NAME = "/;;$/5-choice";
	private final Map<UUID, BukkitTask> taskList;

	public ChoiceMethod(CharacterDialoguePlugin main) {
		super("choice", main);
		taskList = new HashMap<>();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(Player player, String arg, DialogSession session) {
		Map<UUID, ChoiceSession> sessions = provider.getCache().getChoiceSessions();
		YamlFile config = provider.getFileFactory().getConfig();

		if (sessions.containsKey(player.getUniqueId())) {
			return;
		}

		ChoiceSession choiceSession = new ChoiceSession(provider, player);

		if (!config.contains("choice.choices." + arg)) {
			provider.getLogger().warning("The choice \"" + arg + "\" doesn't exists.");
			return;
		}

		session.cancel();

		for (String choice : config.getConfigurationSection("choice.choices." + arg).getKeys(false)) {
			ConfigurationSection section = config.getConfigurationSection("choice.choices." + arg + "." + choice);
			String type = section.getString("type");
			String message = section.getString("message", "no message specified");
			String argument = section.getString("argument", "");

			if (type == null || !provider.getCache().getChoices().containsKey(type)) {
				provider.getLogger().warning("The type of choice '" + choice + "' in " + arg + " isn't valid");
				continue;
			}

			DialogChoice choiceObject = provider.getCache().getChoices().get(type);

			if (message.isEmpty() && choiceObject.isArgumentRequired()) {
				provider.getLogger().severe("The argument in the choice \"" + choice + "\" is missing");
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

			questions.append(parsedModel + " \n")
					.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
							COMMAND_NAME + " " + choiceSession.getUniqueId() + " " + index))
					.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, getSelectText(index)));
		});

		player.spigot().sendMessage(questions.create());

		BukkitTask task = Bukkit.getScheduler().runTaskLater(getProvider(), () -> {
			Map<UUID, DialogSession> dialogSessionMap = getProvider().getCache().getDialogSessions();
			UUID uuid = player.getUniqueId();

			choiceSession.destroy();
			session.destroy();
			taskList.remove(uuid);

			if(player.isOnline()) {
				player.sendMessage(colorize("&cYou took a long time to answer"));

			}
 		}, (long)(20 * 10));

		taskList.put(player.getUniqueId(), task);
	}

	private BaseComponent[] getSelectText(int index) {
		YamlFile file = provider.getFileFactory().getLanguage();
		String text = file.getString("select-choice", colorize("&aClick here to select #%str%")).replace("%str%", index + "");
		return new BaseComponent[] { new TextComponent(colorize(text)) };
	}

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event) {
		String command = event.getMessage();
		Player player = event.getPlayer();
		UUID playerId = player.getUniqueId();
		Map<UUID, ChoiceSession> sessions = provider.getCache().getChoiceSessions();
		// Command: /;/select 4a8d7587-38f3-35e7-b29c-88c715aa6ba8 1

		String[] args = command.split(" ");
		if (!command.startsWith(COMMAND_NAME)) {
			return;
		}

		event.setCancelled(true);

		if (!sessions.containsKey(playerId) || args.length != 3) {
			return;
		}

		ChoiceSession session = sessions.get(playerId);
		UUID uuid = UUID.fromString(args[1]);
		int choice = Integer.parseInt(args[2]);
		Choice choiceObject = session.getChoice(choice);

		if (!uuid.toString().equals(session.getUniqueId().toString())) {
			return;
		}

		ChoiceSelectEvent choiceEvent = new ChoiceSelectEvent(player, uuid, choiceObject, session);
		Bukkit.getPluginManager().callEvent(choiceEvent);

		if (choiceEvent.isCancelled()) {
			return;
		}

		DialogChoice choiceTarget = getByClassName(choiceObject.getChoiceClass());
		DialogSession dialogSession = provider.getCache().getDialogSessions().get(playerId);

		if (dialogSession == null) {
			sessions.remove(playerId);
			return;
		}

        assert choiceTarget != null;

        choiceTarget.onSelect(choiceObject.getArgument(), dialogSession, session);
		sessions.remove(playerId);
		taskList.remove(playerId).cancel();
	}

	private DialogChoice getByClassName(Class<? extends DialogChoice> clazz) {
		for (DialogChoice target : provider.getCache().getChoices().values()) {
			if (target.getClass() == clazz) {
				return target;
			}
		}

		return null;
	}

}
