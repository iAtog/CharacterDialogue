package me.iatog.characterdialogue.dialogs;

import me.iatog.characterdialogue.enums.CompletedType;
import me.iatog.characterdialogue.session.DialogSession;
import me.iatog.characterdialogue.util.SingleUseConsumer;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MethodContext {

    private final Player player;
    private final DialogSession session;
    private final MethodConfiguration configuration;
    private final SingleUseConsumer<CompletedType> consumer;
    private final NPC npc;

    public MethodContext(@NotNull Player player,
                         @NotNull DialogSession session,
                         @NotNull MethodConfiguration configuration,
                         @NotNull SingleUseConsumer<CompletedType> consumer,
                         @Nullable NPC npc) {
        this.player = player;
        this.session = session;
        this.configuration = configuration;
        this.consumer = consumer;
        this.npc = npc;
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

    public NPC getNPC() {
        return npc;
    }

    public void next() {
        this.consumer.accept(CompletedType.CONTINUE);
    }

    public void destroy() {
        this.consumer.accept(CompletedType.DESTROY);
    }

    public void pause() {
        this.consumer.accept(CompletedType.PAUSE);
    }
}
