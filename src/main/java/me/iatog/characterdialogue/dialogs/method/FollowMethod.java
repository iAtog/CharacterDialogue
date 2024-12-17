package me.iatog.characterdialogue.dialogs.method;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.api.events.DialogueFinishEvent;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.dialogs.MethodContext;
import me.iatog.characterdialogue.libraries.HologramLibrary;
import me.iatog.characterdialogue.trait.FollowPlayerTrait;
import me.iatog.characterdialogue.util.TextUtils;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.SpawnReason;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitInfo;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FollowMethod extends DialogMethod<CharacterDialoguePlugin> implements Listener {

    public static final Map<UUID, NPCS> linkedPlayers = new HashMap<>();

    public FollowMethod(CharacterDialoguePlugin main) {
        super("follow", main);

        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(FollowPlayerTrait.class));
    }

    @Override
    public void execute(MethodContext context) {
        NPC npc = context.getNPC();
        Player player = context.getPlayer();
        HologramLibrary hologramLibrary = getProvider().getApi().getHologramLibrary();

        if (linkedPlayers.containsKey(player.getUniqueId())) {
            hologramLibrary.showHologram(player, npc.getId());
            removeRegistered(player);
            this.next(context);
            return;
        }

        if (npc == null) {
            context.getSession().sendDebugMessage("No npc found, skipping...", "FollowMethod");
            this.next(context);
            return;
        }

        NPC clone = npc.copy();

        clone.spawn(npc.getStoredLocation(), SpawnReason.PLUGIN);
        linkedPlayers.put(player.getUniqueId(), new NPCS(npc, clone));
        hologramLibrary.hideHologram(player, npc.getId());
        clone.setName(TextUtils.colorize(context.getSession().getDialogue().getDisplayName()));
        clone.getEntity().setVisibleByDefault(false);
        clone.getEntity().setSilent(true);

        if (!clone.hasTrait(FollowPlayerTrait.class)) {
            clone.addTrait(FollowPlayerTrait.class);
        }

        for (Trait trait : npc.getTraits()) {
            clone.addTrait(trait);
        }

        player.showEntity(getProvider(), clone.getEntity());
        player.hideEntity(getProvider(), npc.getEntity());

        FollowPlayerTrait trait = clone.getTraitNullable(FollowPlayerTrait.class);
        trait.setTarget(player);
        context.getSession().sendDebugMessage("Now clone of npc is following the player", "FollowMethod");
        this.next(context);
    }

    private void removeRegistered(Player player) {
            NPCS npcs = linkedPlayers.get(player.getUniqueId());
            NPC saved = npcs.getCopy();
            player.showEntity(getProvider(), npcs.getOriginal().getEntity());
            saved.destroy();
            linkedPlayers.remove(player.getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (linkedPlayers.containsKey(player.getUniqueId())) {
            removeRegistered(player);
            return;
        }
    }

    @EventHandler
    public void onDialogueEnd(DialogueFinishEvent event) {
        Player player = event.getPlayer();
        if (linkedPlayers.containsKey(player.getUniqueId())) {
            removeRegistered(player);
            return;
        }
    }

    public record NPCS(NPC getOriginal, NPC getCopy) { }
}
