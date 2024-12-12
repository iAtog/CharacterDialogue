package me.iatog.characterdialogue.dialogs.method.choice;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.dialogs.DialogChoice;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.dialogs.MethodConfiguration;
import me.iatog.characterdialogue.dialogs.MethodContext;
import me.iatog.characterdialogue.dialogs.method.choice.listener.ChoiceChatTypeListener;
import me.iatog.characterdialogue.enums.ChoiceType;
import me.iatog.characterdialogue.placeholders.Placeholders;
import me.iatog.characterdialogue.session.ChoiceSession;
import me.iatog.characterdialogue.session.DialogSession;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static me.iatog.characterdialogue.util.TextUtils.colorize;
import static me.iatog.characterdialogue.dialogs.method.LegacyChoiceMethod.COMMAND_NAME;

public class ChoiceMethod extends DialogMethod<CharacterDialoguePlugin> {
    // 'choice{type=chat, cooldown=15}: choice_sample'
    // 'choice{type=bedrock, cooldown=20}: choice_sample'
    // 'choice{type=gui, cooldown=0}: no_cooldown_choice'
    /**
     * defaults:
     * type = chat
     * cooldown = 10
     *
     * choice: choice_sample | TYPE = chat & COOLDOWN = 10
     * choice{type=bedrock}: choice_sample
     */

    private final Map<UUID, ChoiceSession> sessions;
    public static final Map<UUID, BukkitTask> taskList;

    static {
        taskList = new HashMap<>();
    }

    public ChoiceMethod(CharacterDialoguePlugin provider) {
        super("choice", provider);
        this.sessions = provider.getCache().getChoiceSessions();


        ChoiceChatTypeListener listener = new ChoiceChatTypeListener(provider);
        Bukkit.getPluginManager().registerEvents(listener, provider);
    }

    @Override
    public void execute(MethodContext context) {
        MethodConfiguration configuration = context.getConfiguration();
        DialogSession dialogSession = context.getSession();
        Player player = context.getPlayer();
        YamlDocument configFile = provider.getFileFactory().getConfig();

        String selectedChoice = configuration.getArgument();

        if(!ChoiceUtil.isContextValid(context)) {
            this.next(context);
            return;
        }

        String type = configuration.getString("type", "chat").toUpperCase();
        ChoiceType choiceType;

        try {
            choiceType = ChoiceType.valueOf(type);
        } catch(Exception exception) {
            String msg = "Invalid choice type provided \"" + type + "\" in \"" +
                    dialogSession.getDialogue().getName() + "\" dialogue.";
            provider.getLogger().warning(msg);
            dialogSession.sendDebugMessage(msg, "ChoiceMethod");
            this.next(context);
            return;
        }

        ChoiceSession choiceSession = new ChoiceSession(provider, player);

        this.pause(context);

        addChoices(choiceSession, selectedChoice);
        sessions.put(player.getUniqueId(), choiceSession);

        switch (choiceType) {
            case CHAT -> {
                ComponentBuilder questions = new ComponentBuilder("\n");
                String model = configFile.getString("choice.text-model", "&a{I})&e {S}");

                choiceSession.getChoices().forEach((index, choice) -> {
                    String parsedModel = colorize(model).replace("{I}", String.valueOf(index)).replace("{S}",
                            choice.getMessage());

                    questions.append(Placeholders.translate(player, parsedModel) + " \n")
                            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                                    COMMAND_NAME + " " + choiceSession.getUniqueId() + " " + index))
                            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, ChoiceUtil.getSelectText(index)));
                });

                player.spigot().sendMessage(questions.create());
                break;
            }
            case GUI -> {

                break;
            }
            case BEDROCK -> {

                break;
            }
        }

        int cooldown = configuration.getInteger("cooldown", 10);
        if(cooldown > 0) {
            manageCooldown(player, choiceSession, dialogSession, cooldown);
        }
    }

    private void addChoices(ChoiceSession choiceSession, String choiceName) {
        YamlDocument choicesFile = provider.getFileFactory().getChoicesFile();

        for (String choice : choicesFile.getSection("choices." + choiceName).getRoutesAsStrings(false)) {
            Section section = choicesFile.getSection("choices." + choiceName + "." + choice);
            String type = section.getString("type");
            String message = section.getString("message", "no message specified");
            String argument = section.getString("argument", "");

            if (type == null || !provider.getCache().getChoices().containsKey(type)) {
                provider.getLogger().warning("The type of choice '" + choice + "' in " + choiceName + " isn't valid");
                continue;
            }

            DialogChoice choiceObject = provider.getCache().getChoices().get(type);

            if (message.isEmpty() && choiceObject.isArgumentRequired()) {
                provider.getLogger().severe("The argument in the choice \"" + choice + "\" is missing");
                continue;
            }

            choiceSession.addChoice(message, choiceObject.getClass(), argument);
        }
    }

    private void manageCooldown(Player player, ChoiceSession choiceSession, DialogSession session, int secondsCooldown) {
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
        }, 20L * secondsCooldown);

        taskList.put(player.getUniqueId(), task);
    }
}
