package me.iatog.characterdialogue.adapter.citizens;

import me.iatog.characterdialogue.adapter.AdaptedNPC;
import me.iatog.characterdialogue.dialogs.method.npc_control.trait.FollowPlayerTrait;
import me.iatog.characterdialogue.path.PathTrait;
import me.iatog.characterdialogue.path.RecordLocation;
import net.citizensnpcs.api.event.SpawnReason;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.List;

public class AdaptedCitizensNPC implements AdaptedNPC {

    private final NPC npc;

    public AdaptedCitizensNPC(NPC npc) {
        this.npc = npc;
    }

    @Override
    public String getName() {
        return npc.getName();
    }

    @Override
    public int getId() {
        return npc.getId();
    }

    @Override
    public Entity getEntity() {
        return npc.getEntity();
    }

    @Override
    public void setName(String name) {
        npc.setName(name);
    }

    @Override
    public Location getStoredLocation() {
        return npc.getStoredLocation();
    }

    @Override
    public AdaptedNPC copy() {
        return new AdaptedCitizensNPC(npc.copy());
    }

    @Override
    public void destroy() {
        npc.destroy();
    }

    @Override
    public void spawn(Location location) {
        npc.spawn(location, SpawnReason.PLUGIN);
    }

    @Override
    public void teleport(Location location) {
        npc.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    @Override
    public void faceLocation(Location location) {
        npc.faceLocation(location);
    }

    @Override
    public void follow(Player player) {
        FollowPlayerTrait trait = npc.getOrAddTrait(FollowPlayerTrait.class);
        trait.setTarget(player);
    }

    @Override
    public void unfollow(Player player) {
        FollowPlayerTrait trait = npc.getTraitNullable(FollowPlayerTrait.class);

        if(trait != null) {
            trait.setTarget(null);
            npc.removeTrait(FollowPlayerTrait.class);
        }
    }

    @Override
    public void followPath(List<RecordLocation> locations) {
        PathTrait trait = npc.getOrAddTrait(PathTrait.class);
        trait.setPaths(locations);
    }
}
