package me.iatog.characterdialogue.dialogs.method.npc_control;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.dialogs.MethodConfiguration;
import me.iatog.characterdialogue.dialogs.MethodContext;
import me.iatog.characterdialogue.dialogs.method.npc_control.data.ControlData;
import me.iatog.characterdialogue.libraries.HologramLibrary;
import me.iatog.characterdialogue.dialogs.method.npc_control.trait.FollowPlayerTrait;
import me.iatog.characterdialogue.util.TextUtils;
import net.citizensnpcs.api.event.SpawnReason;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ControlUtil {

    private final CharacterDialoguePlugin main;
    private final HologramLibrary hologramLibrary;

    public ControlUtil(CharacterDialoguePlugin main) {
        this.main = main;
        this.hologramLibrary = main.getApi().getHologramLibrary();
    }

    public void createClone(@NotNull MethodContext context, @NotNull NPC npc) {
        Player player = context.getPlayer();
        MethodConfiguration configuration = context.getConfiguration();
        ControlRegistry registry = NPCControlMethod.registries.get(player.getUniqueId());
        int npcId = npc.getId();

        if(registry.isOnRegistry(npcId)) {
            context.getSession().sendDebugMessage("NPC (" +npcId+ ") is already cloned.", "FollowMethod");
            context.next();
            return;
        }

        Location spawnLocation;
        Location configLocation = getConfigLocation(configuration, player.getLocation());

        if(configLocation == null) {
            spawnLocation = npc.getStoredLocation();
        } else {
            spawnLocation = configLocation;
        }

        NPC clone = npc.copy();

        clone.spawn(spawnLocation, SpawnReason.PLUGIN);
        registry.addNPC(npc, clone);
        hologramLibrary.hideHologram(player, npc.getId());
        clone.setName(TextUtils.colorize(context.getSession().getDialogue().getDisplayName()));
        clone.getEntity().setVisibleByDefault(false);
        clone.getEntity().setSilent(true);

        for (Trait trait : npc.getTraits()) {
            clone.addTrait(trait);
        }

        player.showEntity(main, clone.getEntity());
        player.hideEntity(main, npc.getEntity());

        //toggleFollow(clone, player, true);
        context.getSession().sendDebugMessage("Now clone of npc is following the player", "FollowMethod");
    }

    public void toggleFollow(@NotNull NPC npc, Player player, boolean follow) {
        FollowPlayerTrait trait = npc.getOrAddTrait(FollowPlayerTrait.class);
        trait.setTarget(follow ? player : null);
    }

    public void removeRegistered(@NotNull Player player, NPC original) {
        ControlRegistry registry = NPCControlMethod.registries.get(player.getUniqueId());
        ControlData data = registry.removeNPC(original);

        if(data == null) {
            return;
        }

        player.showEntity(main, data.getOriginal().getEntity());
        data.getCopy().destroy();
        NPCControlMethod.registries.remove(player.getUniqueId());
        hologramLibrary.showHologram(player, data.getOriginal().getId());
    }

    @Nullable
    public Location getConfigLocation(@NotNull MethodConfiguration configuration, @NotNull Location defLoc) {
        if(!configuration.contains("x") || !configuration.contains("y") || !configuration.contains("z")) {
            return null;
        }

        float x = configuration.getFloat("x");
        float y = configuration.getFloat("y");
        float z = configuration.getFloat("z");
        float yaw = configuration.getFloat("yaw", defLoc.getYaw());
        float pitch = configuration.getFloat("pitch", defLoc.getPitch());

        return new Location(defLoc.getWorld(), x, y, z, yaw, pitch);
    }

}
