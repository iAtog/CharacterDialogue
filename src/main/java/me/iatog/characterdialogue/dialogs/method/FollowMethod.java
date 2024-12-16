package me.iatog.characterdialogue.dialogs.method;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.User;
import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.dialogs.MethodContext;
import me.iatog.characterdialogue.reflection.ReflectionUtil;
import me.iatog.characterdialogue.trait.FollowPlayerTrait;
import me.iatog.characterdialogue.util.TextUtils;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.DespawnReason;
import net.citizensnpcs.api.event.SpawnReason;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitInfo;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FollowMethod extends DialogMethod<CharacterDialoguePlugin> {

    //private final NPCRegistry registry;
    private final Map<UUID, NPCS> linkedPlayers;

    public FollowMethod(CharacterDialoguePlugin main) {
        super("follow", main);
        this.linkedPlayers = new HashMap<>();
        this.setDisabled(true);
        //String packageName = Bukkit.getServer().getClass().getPackage().getName();
        //main.getLogger().info("Detected version: " + packageName);
        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(FollowPlayerTrait.class).withName("followPlayer"));
    }

    @Override
    public void execute(MethodContext context) {
        NPC npc = context.getNPC();
        Player player = context.getPlayer();

        if (linkedPlayers.containsKey(player.getUniqueId())) {
            NPCS npcs = linkedPlayers.get(player.getUniqueId());
            NPC saved = npcs.getCopy();
            CitizensAPI.getNPCRegistry().deregister(saved);
            player.sendMessage("Deleted");
            saved.despawn(DespawnReason.REMOVAL);
            //hider.showEntity(player, npcs.getOriginal().getEntity());
            linkedPlayers.remove(player.getUniqueId());
            this.next(context);
            return;
        }

        if (npc == null) {
            context.getSession().sendDebugMessage("No npc found, skipping...", "FollowMethod");
            this.next(context);
            return;
        }

        String generatedName = player.getName() + "_" + npc.getUniqueId();
        NPC clone = CitizensAPI.getNPCRegistry().createNPC(npc.getEntity().getType(), generatedName);

        clone.setName(TextUtils.colorize(context.getSession().getDialogue().getDisplayName()));
        //clone.getTraitNullable(Inventory.class).setContents(npc.getTraitNullable(Inventory.class).getContents());
        for (Trait trait : npc.getTraits()) {
            clone.addTrait(trait);
        }

        clone.spawn(npc.getStoredLocation(), SpawnReason.PLUGIN);

        if (! clone.hasTrait(FollowPlayerTrait.class)) {
            clone.addTrait(FollowPlayerTrait.class);
        }

        FollowPlayerTrait trait = clone.getTraitNullable(FollowPlayerTrait.class);
        trait.setTarget(player);
        player.sendMessage("Following (creo)");
        linkedPlayers.put(player.getUniqueId(), new NPCS(npc, clone));

        // SEND PACKETS

        // HIDE ALL
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (! onlinePlayer.getUniqueId().equals(player.getUniqueId())) {

            }
        }

        hideNPC(player, npc);
        player.sendMessage("Hided?");
        context.getSession().sendDebugMessage("Original NPC hided to " + (Bukkit.getOnlinePlayers().size() - 1) + " players.", "FollowMethod");

        this.next(context);
    }

    /*
        public void execute_ProtocolLib(MethodContext context) {
            NPC npc = context.getNPC();
            Player player = context.getPlayer();

            if(getProvider().getEntityHider() == null) {
                player.sendMessage(TextUtils.colorize("&cYou need &c&lProtocolLib&r&c to be able to use this method."));
                getProvider().getLogger().warning(TextUtils.colorize("&cAttempted to use the FollowMethod, but no ProtocolLib has been found."));
                next(context);
                return;
            }

            EntityHider hider = getProvider().getEntityHider();

            if(linkedPlayers.containsKey(player.getUniqueId())) {
                NPCS npcs = linkedPlayers.get(player.getUniqueId());
                NPC saved = npcs.getCopy();
                CitizensAPI.getNPCRegistry().deregister(saved);
                player.sendMessage("Deleted");
                saved.despawn(DespawnReason.REMOVAL);
                hider.showEntity(player, npcs.getOriginal().getEntity());
                linkedPlayers.remove(player.getUniqueId());
                this.next(context);
                return;
            }

            if(npc == null) {
                context.getSession().sendDebugMessage("No npc found, skipping...", "FollowMethod");
                this.next(context);
                return;
            }

            String generatedName = player.getName() + "_" + npc.getUniqueId();
            NPC clone = CitizensAPI.getNPCRegistry().createNPC(npc.getEntity().getType(), generatedName);

            clone.setName(TextUtils.colorize(context.getSession().getDialogue().getDisplayName()));
            //clone.getTraitNullable(Inventory.class).setContents(npc.getTraitNullable(Inventory.class).getContents());
            for(Trait trait : npc.getTraits()) {
                clone.addTrait(trait);
            }

            clone.spawn(npc.getStoredLocation(), SpawnReason.PLUGIN);

            if (!clone.hasTrait(FollowPlayerTrait.class)) {
                clone.addTrait(FollowPlayerTrait.class);
            }

            FollowPlayerTrait trait = clone.getTraitNullable(FollowPlayerTrait.class);
            trait.setTarget(player);
            player.sendMessage("Following (creo)");
            linkedPlayers.put(player.getUniqueId(), new NPCS(npc, clone));

            hider.showEntity(player, clone.getEntity());
            hider.hideEntity(player, npc.getEntity());

            this.next(context);
        }
    */
    public void executeOld(MethodContext context) {
        NPC npc = context.getNPC();
        Player player = context.getPlayer();
        UUID uuid = player.getUniqueId();

        if (linkedPlayers.containsKey(uuid)) {
            //CitizensAPI.getNPCRegistry().deregister(linkedPlayers.get(uuid));
            player.sendMessage("Deleted");
            this.next(context);
            return;
        }

        if (npc == null) {
            context.getSession().sendDebugMessage("No npc found, skipping...", "FollowMethod");
            this.next(context);
            return;
        }

        String generatedName = player.getName() + "_" + npc.getUniqueId();
        NPC clone = CitizensAPI.getNPCRegistry().createNPC(npc.getEntity().getType(), generatedName);

        clone.setName(TextUtils.colorize(context.getSession().getDialogue().getDisplayName()));
        //clone.getTraitNullable(Inventory.class).setContents(npc.getTraitNullable(Inventory.class).getContents());
        clone.setBukkitEntityType(npc.getEntity().getType());
        //clone.spawn(npc.getStoredLocation(), SpawnReason.PLUGIN);

        //hideNPC(player, npc);
        //showNPC(player, clone);

        clone.teleport(npc.getStoredLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);

        if (! clone.hasTrait(FollowPlayerTrait.class)) {
            clone.addTrait(FollowPlayerTrait.class);
        }

        FollowPlayerTrait trait = clone.getTraitNullable(FollowPlayerTrait.class);
        trait.setTarget(player);
        player.sendMessage("Following (creo)");
        //linkedPlayers.put(uuid, clone);
        this.next(context);
    }

    private void h2ideNPC(Player player, NPC npc) {
        try {
            User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
            getProvider().getLogger().info("USER: " + user.getName());
            //PacketPlayOutEntityDestroy

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void hideNPC(Player player, NPC npc) {
        try {
            Class<?> packetClass = ReflectionUtil.getNMSClass("PacketPlayOutEntityDestroy");
            if (packetClass == null) {
                getProvider().getLogger().info("What the fuuuuck no encontrado");
                return;
            }
            Constructor<?> constructor = packetClass.getConstructor(int[].class);
            Object destroyPacket = constructor.newInstance((Object) new int[]{ npc.getEntity().getEntityId() });

            ReflectionUtil.sendPacket(player, destroyPacket);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void _showNPC(Player player, NPC npc) {
        try {
            Object handle = npc.getEntity().getClass().getMethod("getHandle").invoke(npc.getEntity());
            Object spawnPacket = ReflectionUtil.getNMSClass("PacketPlayOutSpawnEntityLiving")
                  .getConstructor(ReflectionUtil.getNMSClass("EntityLiving"))
                  .newInstance(handle);

            ReflectionUtil.sendPacket(player, spawnPacket);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    static class NPCS {
        private final NPC original;
        private final NPC copy;

        public NPCS(NPC original, NPC copy) {
            this.original = original;
            this.copy = copy;
        }

        public NPC getOriginal() {
            return original;
        }

        public NPC getCopy() {
            return copy;
        }
    }
}
