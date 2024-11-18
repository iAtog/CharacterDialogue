package me.iatog.characterdialogue.api.events;

import me.iatog.characterdialogue.session.DialogSession;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class DialogueFinishEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    private final DialogSession session;

    public DialogueFinishEvent(Player player, DialogSession session) {
        super(player);
        this.session = session;
    }

    public DialogSession getSession() {
        return session;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }
    public final HandlerList getHandlers(){
        return handlers;
    }
}
