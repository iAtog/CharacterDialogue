package me.iatog.characterdialogue.dialogs.method;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

public class ChoiceMethod extends DialogMethod<CharacterDialoguePlugin> implements Listener {

	public ChoiceMethod(CharacterDialoguePlugin main) {
		super("choice", main);
	}

	@Override
	public void execute(Player player, String arg, DialogSession session) {
		Map<UUID, ChoiceSession> sessions = provider.getCache().getChoiceSessions();
		YamlFile config = provider.getFileFactory().getConfig();
		
		
		if (sessions.containsKey(player.getUniqueId())) {
			return;
		}

		ChoiceSession choiceSession = new ChoiceSession(provider, player);
		
		if(!config.contains("choice.choices." + arg)) {
			provider.getLogger().warning("The choice \"" + arg + "\" doesn't exists.");
			return;
		}
		
		for(String choice : config.getConfigurationSection("choice.choices." + arg).getKeys(false)) {
			ConfigurationSection section = config.getConfigurationSection("choice.choices." + arg + "." + choice);
			String type = section.getString("type");
			String message = section.getString("message", "no message specified");
			String argument = section.getString("argument", "");
			
			if(type == null || !provider.getCache().getChoices().containsKey(type)) {
				provider.getLogger().warning("The type of choice '" + choice + "' in " + arg + " isn't valid");
				continue;
			}
			
			Class<? extends DialogChoice> choiceClass = provider.getCache().getChoices().get(type).getClass();
			
			choiceSession.addChoice(message, choiceClass, argument);
		}
		
		/*
		choiceSession.addChoice("My mom", DestroyChoice.class);
		choiceSession.addChoice("My sis", ContinueChoice.class);
		choiceSession.addChoice("My stepsis", ContinueChoice.class);
		*/
		
		session.cancel();
		sessions.put(player.getUniqueId(), choiceSession);
		ComponentBuilder questions = new ComponentBuilder("");
		String model = config.getString("choice.text-model", "&a{I})&e {S}");
		
		choiceSession.getChoices().forEach((index, choice) -> {
			String parsedModel = ChatColor.translateAlternateColorCodes('&', model)
					.replace("{I}", String.valueOf(index))
					.replace("{S}", choice.getMessage());
			// "§a" + index + "> §e" + choice.getMessage()
			questions.append(parsedModel + " \n")
					.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
							"/;/select " + choiceSession.getUniqueId() + " " + index))
					.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, getSelectText(index)));
		});

		player.spigot().sendMessage(questions.create());
	}

	private BaseComponent[] getSelectText(int index) {
		return new BaseComponent[] { new TextComponent("§aClick here to select #" + index) };
	}

	@EventHandler
	public void onChoiceSelect(ChoiceSelectEvent event) {
		Player player = event.getPlayer();

		player.sendMessage("USED " + event.getChoice().getMessage());
	}

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event) {
		String command = event.getMessage();
		Player player = event.getPlayer();
		UUID playerId = player.getUniqueId();
		Map<UUID, ChoiceSession> sessions = provider.getCache().getChoiceSessions();
		// Command: /;/select 4a8d7587-38f3-35e7-b29c-88c715aa6ba8 1

		String[] args = command.split(" ");
		if (!command.startsWith("/;/select") || args.length != 3 || !sessions.containsKey(playerId)) {
			return;
		}

		event.setCancelled(true);
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
		choiceTarget.onSelect(choiceObject.getArgument(), dialogSession, session);
		sessions.remove(playerId);
	}

	private DialogChoice getByClassName(Class<? extends DialogChoice> clazz) {
		DialogChoice choice = null;

		for (DialogChoice target : provider.getCache().getChoices().values()) {
			if (target.getClass() == clazz) {
				choice = target;
				break;
			}
		}

		return choice;
	}
}
