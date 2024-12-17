package me.iatog.characterdialogue.trait;

import net.citizensnpcs.api.ai.Navigator;
import net.citizensnpcs.api.ai.NavigatorParameters;
import net.citizensnpcs.api.trait.Trait;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class FollowPlayerTrait extends Trait {
    private Player target;

    public FollowPlayerTrait() {
        super("followPlayer");
    }

    @Override
    public void run() {
        if (target != null && target.isOnline()) {
            Location location = target.getLocation();
            location.setX(location.getX() + 1.5d);
            location.setY(location.getZ() + 1.5d);
            Navigator navigator = npc.getNavigator();
            NavigatorParameters params = navigator.getDefaultParameters();
            navigator.getPathStrategy();
            params.range(30);

            npc.setSneaking(target.isSneaking());
            params.stuckAction((npc, nav) -> {
                npc.teleport(target.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
                return true;
            });
            params.baseSpeed(10);

            int distance = (int) target.getLocation().distance(npc.getEntity().getLocation());

            if(distance >= 4) {
                navigator.setPaused(false);
                navigator.setTarget(target.getLocation());
            } else {
                navigator.setPaused(true);
            }
        }
    }

    public void setTarget(Player player) {
        this.target = player;
    }

}
