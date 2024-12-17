package me.iatog.characterdialogue.dialogs.method.follow;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.api.dialog.ConfigurationType;
import me.iatog.characterdialogue.api.events.DialogueFinishEvent;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.dialogs.MethodConfiguration;
import me.iatog.characterdialogue.dialogs.MethodContext;
import me.iatog.characterdialogue.libraries.HologramLibrary;
import me.iatog.characterdialogue.trait.FollowPlayerTrait;
import me.iatog.characterdialogue.util.TextUtils;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.SpawnReason;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitInfo;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FollowMethod extends DialogMethod<CharacterDialoguePlugin> implements Listener {

    public static final Map<UUID, FollowRegistry> registries = new HashMap<>();
    private final HologramLibrary hologramLibrary;

    // Custom actions | default = start
    // follow{action=start, npcId=18}
    // follow{action=walk_stop, npcId=18}
    // follow{action=walk_continue, npcId=18}
    // follow{action=pose, x=0, y=0, z=0, yaw=0, pitch=0, npcId=18}
    // follow{action=destroy, npcId=18}
    public FollowMethod(CharacterDialoguePlugin main) {
        super("follow", main);
        this.hologramLibrary = getProvider().getApi().getHologramLibrary();
        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(FollowPlayerTrait.class));

        addConfigurationType("action", ConfigurationType.TEXT);
        addConfigurationType("npcId", ConfigurationType.INTEGER);

        addConfigurationType("x", ConfigurationType.FLOAT);
        addConfigurationType("y", ConfigurationType.FLOAT);
        addConfigurationType("z", ConfigurationType.FLOAT);
        addConfigurationType("yaw", ConfigurationType.FLOAT);
        addConfigurationType("pitch", ConfigurationType.FLOAT);
    }

    @Override
    public void execute(MethodContext context) {
        NPC npc = context.getNPC();
        Player player = context.getPlayer();
        MethodConfiguration configuration = context.getConfiguration();
        String action = configuration.getString("action", "start").toLowerCase();
        int npcId = configuration.getInteger("npcId", npc.getId());

        NPC targetNpc = CitizensAPI.getNPCRegistry().getById(npcId);

        if(targetNpc == null) {
            getProvider().getLogger().severe("The specified npc has not been found while using follow method.");
            this.destroy(context);
            return;
        }

        context.getSession().sendDebugMessage("Executing follow method with npc: " + targetNpc.getName() + " (" + npcId + ")", "FollowMethod");

        if (!registries.containsKey(player.getUniqueId())) {
            registries.put(player.getUniqueId(), new FollowRegistry(player));
        }

        switch (action) {
            case "start": {
                createClone(context, targetNpc);
                break;
            }
            case "destroy": {
                if (registries.containsKey(player.getUniqueId())) {
                    removeRegistered(player, targetNpc);
                    this.next(context);
                }
                break;
            }
            case "walk_stop": {
                FollowRegistry registry = registries.get(player.getUniqueId());
                FollowData data = registry.get(npcId);

                if(data != null) {
                    toggleFollow(data.getCopy(), player, false);
                }

                this.next(context);
                break;
            }
            case "walk_continue": {
                FollowRegistry registry = registries.get(player.getUniqueId());
                FollowData data = registry.get(npcId);

                if(data != null) {
                    toggleFollow(data.getCopy(), player, true);
                }

                this.next(context);
                break;
            }
            case "pose": {
                FollowRegistry registry = registries.get(player.getUniqueId());
                FollowData data = registry.get(npcId);

                if(data != null) {
                    NPC clone = data.getCopy();
                    Location newLocation = getConfigLocation(configuration, player.getLocation());

                    if(newLocation == null) {
                        getProvider().getLogger().warning("Invalid coordinates specified in pose action.");
                        this.destroy(context);
                    } else {
                        FollowPlayerTrait trait = clone.getOrAddTrait(FollowPlayerTrait.class);
                        trait.setTarget(null);
                        clone.teleport(newLocation, PlayerTeleportEvent.TeleportCause.PLUGIN);
                        if(configuration.getBoolean("lookPlayer", false)) {
                            clone.faceLocation(player.getLocation());
                        }
                        this.next(context);
                    }
                }
                break;
            }
            default: {
                getProvider().getLogger().warning("The '" + action +  "' action is not valid for the follow method. L" + context.getSession().getCurrentIndex());
                this.destroy(context);
                break;
            }

        }
    }

    private void createClone(@NotNull MethodContext context, @NotNull NPC npc) {
        Player player = context.getPlayer();
        MethodConfiguration configuration = context.getConfiguration();
        FollowRegistry registry = registries.get(player.getUniqueId());
        int npcId = npc.getId();

        if(registry.isOnRegistry(npcId)) {
            context.getSession().sendDebugMessage("NPC (" +npcId+ ") is already cloned.", "FollowMethod");
            this.next(context);
            return;
        }

        NPC clone = npc.copy();

        clone.spawn(npc.getStoredLocation(), SpawnReason.PLUGIN);
        registry.addNPC(npc, clone);
        hologramLibrary.hideHologram(player, npc.getId());
        clone.setName(TextUtils.colorize(context.getSession().getDialogue().getDisplayName()));
        clone.getEntity().setVisibleByDefault(false);
        clone.getEntity().setSilent(true);

        for (Trait trait : npc.getTraits()) {
            clone.addTrait(trait);
        }

        player.showEntity(getProvider(), clone.getEntity());
        player.hideEntity(getProvider(), npc.getEntity());

        toggleFollow(clone, player, true);
        context.getSession().sendDebugMessage("Now clone of npc is following the player", "FollowMethod");
        this.next(context);
    }

    private void toggleFollow(@NotNull NPC npc, Player player, boolean follow) {
        FollowPlayerTrait trait = npc.getOrAddTrait(FollowPlayerTrait.class);
        trait.setTarget(follow ? player : null);
    }

    private void removeRegistered(@NotNull Player player, NPC original) {
        FollowRegistry registry = registries.get(player.getUniqueId());
        FollowData data = registry.removeNPC(original);

        if(data == null) {
            return;
        }

        player.showEntity(getProvider(), data.getOriginal().getEntity());
        data.getCopy().destroy();
        registries.remove(player.getUniqueId());
        hologramLibrary.showHologram(player, data.getOriginal().getId());
    }

    @Nullable
    private Location getConfigLocation(@NotNull MethodConfiguration configuration, @NotNull Location defLoc) {
        float x = configuration.getFloat("x");
        float y = configuration.getFloat("y");
        float z = configuration.getFloat("z");
        float yaw = configuration.getFloat("yaw", defLoc.getYaw());
        float pitch = configuration.getFloat("pitch", defLoc.getPitch());

        if(!configuration.contains("x") || !configuration.contains("y") || !configuration.contains("z")) {
            return null;
        }

        return new Location(defLoc.getWorld(), x, y, z, yaw, pitch);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (registries.containsKey(player.getUniqueId())) {
            registries.remove(player.getUniqueId()).clearAll();
            //removeRegistered(player);
            return;
        }
    }

    //@EventHandler
    public void onDialogueEnd(DialogueFinishEvent event) {
        Player player = event.getPlayer();
        if (registries.containsKey(player.getUniqueId())) {
            //removeRegistered(player);
            return;
        }
    }

}
