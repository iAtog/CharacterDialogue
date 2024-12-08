package me.iatog.characterdialogue.dialogs;

import me.iatog.characterdialogue.enums.CompletedType;
import me.iatog.characterdialogue.session.DialogSession;
import me.iatog.characterdialogue.util.SingleUseConsumer;
import org.bukkit.entity.Player;

public class MethodContext {

    private final Player player;
    private final DialogSession session;
    private final MethodConfiguration configuration;
    private final SingleUseConsumer<CompletedType> consumer;

    public MethodContext(Player player, DialogSession session, MethodConfiguration configuration, SingleUseConsumer<CompletedType> consumer) {
        this.player = player;
        this.session = session;
        this.configuration = configuration;
        this.consumer = consumer;
    }

    public Player getPlayer() {
        return player;
    }

    public DialogSession getSession() {
        return session;
    }

    public MethodConfiguration getConfiguration() {
        return configuration;
    }

    public SingleUseConsumer<CompletedType> getConsumer() {
        return consumer;
    }
}
