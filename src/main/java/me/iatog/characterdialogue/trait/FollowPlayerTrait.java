package me.iatog.characterdialogue.trait;

import net.citizensnpcs.api.ai.Navigator;
import net.citizensnpcs.api.ai.NavigatorParameters;
import net.citizensnpcs.api.trait.Trait;
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
            Navigator navigator = npc.getNavigator();
            NavigatorParameters params = navigator.getDefaultParameters();

            params.useNewPathfinder(true);
            params.baseSpeed(target.getWalkSpeed());
            params.stuckAction((npc, nav) -> {
                npc.teleport(target.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
                return true;
            });
            navigator.setTarget(target.getLocation());
        }
    }

    public void setTarget(Player player) {
        this.target = player;
    }

}
