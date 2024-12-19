package me.iatog.characterdialogue.dialogs.method.npc_control;

import me.iatog.characterdialogue.dialogs.MethodConfiguration;
import me.iatog.characterdialogue.dialogs.MethodContext;
import me.iatog.characterdialogue.dialogs.method.npc_control.data.ActionData;
import me.iatog.characterdialogue.dialogs.method.npc_control.data.ControlData;
import me.iatog.characterdialogue.dialogs.method.npc_control.trait.FollowPlayerTrait;
import net.citizensnpcs.api.ai.Navigator;
import net.citizensnpcs.api.ai.NavigatorParameters;
import net.citizensnpcs.api.npc.NPC;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.function.Consumer;

import static me.iatog.characterdialogue.dialogs.method.npc_control.NPCControlMethod.registries;

public enum ControlAction {
    START((ctx) -> {
        ctx.util().createClone(ctx.context(), ctx.targetNPC());
        ctx.context().next();
    }),
    DESTROY((ctx) -> {
        if (registries.containsKey(ctx.player().getUniqueId())) {
            ctx.util().removeRegistered(ctx.player(), ctx.targetNPC());
            ctx.context().next();
        }
    }),
    FOLLOW((ctx) -> {
        ControlRegistry registry = registries.get(ctx.player().getUniqueId());
        ControlData playerRegistry = registry.get(ctx.targetNPC().getId());

        if(playerRegistry != null) {
            ctx.util().toggleFollow(playerRegistry.getCopy(), ctx.player(), true);
        }

        ctx.context().next();
    }),
    UNFOLLOW((ctx) -> {
        ControlRegistry registry = registries.get(ctx.player().getUniqueId());
        ControlData playerRegistry = registry.get(ctx.targetNPC().getId());

        if(playerRegistry != null) {
            ctx.util().toggleFollow(playerRegistry.getCopy(), ctx.player(), false);
        }

        ctx.context().next();
    }),
    POSE((ctx) -> {
        ControlRegistry registry = registries.get(ctx.player().getUniqueId());
        ControlData data = registry.get(ctx.targetNPC().getId());
        MethodConfiguration configuration = ctx.context().getConfiguration();

        if(data != null) {
            NPC clone = data.getCopy();
            Location newLocation = ctx.util().getConfigLocation(configuration, ctx.player().getLocation());

            if(newLocation == null) {
                ctx.plugin().getLogger().warning("Invalid coordinates specified in pose action.");
                ctx.context().destroy();
            } else {
                FollowPlayerTrait trait = clone.getOrAddTrait(FollowPlayerTrait.class);
                trait.setTarget(null);
                clone.teleport(newLocation, PlayerTeleportEvent.TeleportCause.PLUGIN);

                if(configuration.getBoolean("lookPlayer", false)) {
                    clone.faceLocation(ctx.player().getLocation());
                }

                ctx.context().next();
            }
        }
    }),
    MOVE_TO((ctx) -> {
        ControlRegistry registry = registries.get(ctx.player().getUniqueId());
        ControlData data = registry.get(ctx.targetNPC().getId());
        MethodConfiguration configuration = ctx.context().getConfiguration();

        if(data != null) {
            NPC clone = data.getCopy();
            Location moveTo = ctx.util().getConfigLocation(configuration, ctx.player().getLocation());

            if(moveTo == null) {
                ctx.plugin().getLogger().severe("Invalid coordinates provided");
                ctx.context().destroy();
                return;
            }

            FollowPlayerTrait trait = clone.getOrAddTrait(FollowPlayerTrait.class);
            trait.setTarget(null);

            Navigator navigator = clone.getNavigator();
            NavigatorParameters params = navigator.getDefaultParameters();
            if(navigator.isNavigating()) {
                navigator.cancelNavigation();
            }

            params.baseSpeed(15);
            params.range(30);

            navigator.getPathStrategy();
            navigator.setTarget(moveTo);
        }

        ctx.context().next();
    });

    private final Consumer<ActionData> consumer;

    ControlAction(Consumer<ActionData> consumer) {
        this.consumer = consumer;
    }

    public void execute(MethodContext context, ControlUtil util, NPC target) {
        this.consumer.accept(new ActionData(context, util, target));
    }
}
