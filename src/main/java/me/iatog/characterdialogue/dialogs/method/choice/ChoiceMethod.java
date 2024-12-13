package me.iatog.characterdialogue.dialogs.method.choice;

import dev.dejvokep.boostedyaml.YamlDocument;
import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.dialogs.MethodConfiguration;
import me.iatog.characterdialogue.dialogs.MethodContext;
import me.iatog.characterdialogue.dialogs.method.choice.listener.ChoiceChatTypeListener;
import me.iatog.characterdialogue.enums.ChoiceType;
import me.iatog.characterdialogue.session.ChoiceSession;
import me.iatog.characterdialogue.session.DialogSession;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChoiceMethod extends DialogMethod<CharacterDialoguePlugin> {
    // 'choice{type=chat, cooldown=15}: choice_sample'
    // 'choice{type=bedrock, cooldown=20}: choice_sample'
    // 'choice{type=gui, cooldown=0}: no_cooldown_choice'
    /**
     * defaults:
     * type = chat
     * cooldown = 10
     * <p>
     * choice: choice_sample | TYPE = chat & COOLDOWN = 10
     * choice{type=bedrock}: choice_sample
     */

    private final Map<UUID, ChoiceSession> sessions;
    private final boolean floodgateEnabled;

    public static final Map<UUID, BukkitTask> taskList;

    static {
        taskList = new HashMap<>();
    }

    public ChoiceMethod(CharacterDialoguePlugin provider) {
        super("choice", provider);
        this.sessions = provider.getCache().getChoiceSessions();
        this.floodgateEnabled = Bukkit.getPluginManager().isPluginEnabled("floodgate");

        if(floodgateEnabled) {
            provider.getLogger().info("[ChoiceMethod] Floodgate found!");
        }

        Bukkit.getPluginManager().registerEvents(new ChoiceChatTypeListener(provider), provider);
    }

    @Override
    public void execute(MethodContext context) {
        MethodConfiguration configuration = context.getConfiguration();
        DialogSession dialogSession = context.getSession();
        Player player = context.getPlayer();
        YamlDocument configFile = provider.getFileFactory().getConfig();

        String selectedChoice = configuration.getArgument();
        int cooldown = configuration.getInteger("cooldown", 30);

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

        if(choiceType == ChoiceType.BEDROCK_GUI && !floodgateEnabled) {
            String msg = "\"BEDROCK\" choice cannot be used, geyser and floodgate plugin is not present on the server.";
            dialogSession.sendDebugMessage(msg, "ChoiceMethod");
            provider.getLogger().warning(msg);
            this.next(context);
            return;
        }

        ChoiceSession choiceSession = new ChoiceSession(provider, player);

        this.pause(context);

        ChoiceUtil.addChoices(choiceSession, selectedChoice);
        sessions.put(player.getUniqueId(), choiceSession);

        ChoiceData data = new ChoiceData(
                choiceSession,
                dialogSession,
                player,
                configFile
        );

        choiceType.loadChoices(data);

        if(cooldown > 0) {
            ChoiceUtil.manageCooldown(data, cooldown, choiceType.getCloseAction());
        }
    }
}
