package me.iatog.characterdialogue.dialogs.method.choice.listener;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.api.events.ChoiceSelectEvent;
import me.iatog.characterdialogue.dialogs.DialogChoice;
import me.iatog.characterdialogue.dialogs.method.choice.ChoiceMethod;
import me.iatog.characterdialogue.dialogs.method.choice.ChoiceUtil;
import me.iatog.characterdialogue.misc.Choice;
import me.iatog.characterdialogue.session.ChoiceSession;
import me.iatog.characterdialogue.session.DialogSession;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

import java.util.Map;
import java.util.UUID;

import static me.iatog.characterdialogue.dialogs.method.LegacyChoiceMethod.COMMAND_NAME;

public class ChoiceChatTypeListener implements Listener {

    private final CharacterDialoguePlugin main;

    public ChoiceChatTypeListener(CharacterDialoguePlugin main) {
        this.main = main;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage();
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        Map<UUID, ChoiceSession> sessions = main.getCache().getChoiceSessions();
        Map<UUID, DialogSession> dialogSessionMap = main.getCache().getDialogSessions();

        // /<cmd> <choice-uuid> <selected-option>
        // Command args: /<cmd> 4a8d7587-38f3-35e7-b29c-88c715aa6ba8 1

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
        Map<UUID, ChoiceSession> sessions = main.getCache().getChoiceSessions();
        UUID uuid = player.getUniqueId();

        if(!sessions.containsKey(uuid)) {
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

        DialogChoice choiceTarget = ChoiceUtil.getByClassName(choiceObject.getChoiceClass());
        DialogSession dialogSession = main.getCache().getDialogSessions().get(uuid);

        if (dialogSession == null || choiceTarget == null) {
            sessions.remove(uuid);
            return;
        }

        choiceTarget.onSelect(choiceObject.getArgument(), dialogSession, session);
        sessions.remove(uuid);
        ChoiceMethod.taskList.remove(uuid).cancel();
    }
}
