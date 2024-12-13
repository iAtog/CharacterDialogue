package me.iatog.characterdialogue.dialogs.method.conditionalevents;

import ce.ajneb97.api.ConditionalEventsEvent;
import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.api.events.DialogueFinishEvent;
import me.iatog.characterdialogue.enums.CaptureMode;
import me.iatog.characterdialogue.enums.CompletedType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;
import java.util.UUID;

public class ConditionalEventListener implements Listener {

    private final CharacterDialoguePlugin main;

    public ConditionalEventListener(CharacterDialoguePlugin main) {
        this.main = main;
    }

    @EventHandler
    public void onAction(ConditionalEventsEvent event) {
        Map<UUID, EventData> dataMap = ConditionalEventsMethod.cache;
        Player player = event.getPlayer();

        if(player == null || !dataMap.containsKey(player.getUniqueId())) {
            return;
        }

        EventData data = dataMap.get(player.getUniqueId());
        CaptureMode mode = data.getCaptureMode();
        String argument = data.getArgument().trim();

        String action = event.getActionGroup();
        String eventName = event.getEvent();

        boolean check = false;

        switch(mode) {
            case EVENT ->
                check = argument.equals(eventName);
            case ACTION ->
                check = argument.equals(action);
            case BOTH -> {
                String[] part = argument.split("\\|");
                check = (argument.equals(part[0]) && argument.equals(part[1]));
            }
        }

        if(!check) {
            return;
        }

        ConditionalEventsMethod.cache.remove(event.getPlayer().getUniqueId());
        player.sendMessage("Check passed");

        if(data.isPaused()) {
            data.getSession().startNext();
        } else {
            data.consumer().accept(CompletedType.CONTINUE);
        }
    }

    // Prevent memory leaks

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        ConditionalEventsMethod.cache.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onEnd(DialogueFinishEvent event) {
        ConditionalEventsMethod.cache.remove(event.getPlayer().getUniqueId());
    }

}
