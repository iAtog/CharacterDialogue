package me.iatog.characterdialogue.trait;

import net.citizensnpcs.api.ai.Navigator;
import net.citizensnpcs.api.ai.NavigatorParameters;
import net.citizensnpcs.api.trait.Trait;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;

public class FollowPlayerTrait extends Trait {
    private Player target;

    private int sneakTicks;
    private int onAirTries;

    public FollowPlayerTrait() {
        super("followPlayer");
    }

    @Override
    public void run() {
        Navigator navigator = npc.getNavigator();
        NavigatorParameters params = navigator.getDefaultParameters();

        params.stuckAction((npc, nav) -> {
            teleportBehind();
            return true;
        });

        if (target != null && target.isOnline()) {
            navigator.getPathStrategy();
            params.range(30);
            boolean sneaking = target.isSneaking();
            if (sneaking) {
                if(sneakTicks >= 15) {
                    npc.setSneaking(sneaking);
                } else {
                    sneakTicks++;
                }
            } else {
                sneakTicks = 0;
                npc.setSneaking(false);
            }

            params.baseSpeed(15);
            npc.setProtected(true);
            npc.faceLocation(target.getLocation());
            int distance = (int) target.getLocation().distance(npc.getEntity().getLocation());
            Location npcLoc = npc.getEntity().getLocation();
            Location blockBelow = new Location(npcLoc.getWorld(), npcLoc.getX(),(int)npcLoc.getY() - 1, npcLoc.getZ());
            boolean isAir = blockBelow.getBlock().getType() == Material.AIR;

            if(isAir) {
                onAirTries++;
                if(onAirTries >= 30) {
                    teleportBehind();
                    onAirTries = 0;
                }
            } else if(onAirTries > 0) {
                onAirTries = 0;
            }

            if(distance >= 3 && distance < 50) {
                navigator.setPaused(false);
                navigator.setTarget(target.getLocation());
            } else if(distance >= 50) {
                teleportBehind();
            } else {
                navigator.setPaused(true);
            }
        }
    }

    public void setTarget(Player player) {
        this.target = player;
    }

    public void teleportBehind() {
        Location tLoc = target.getLocation();
        Location nLoc = npc.getEntity().getLocation();

        Vector swap = tLoc.toVector().subtract(nLoc.toVector()).normalize();
        Location behind = tLoc.clone().add(swap);
        behind.setY(tLoc.getY());
        npc.teleport(behind, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

}
