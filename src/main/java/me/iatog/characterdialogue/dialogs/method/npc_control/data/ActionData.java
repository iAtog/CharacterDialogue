package me.iatog.characterdialogue.dialogs.method.npc_control.data;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.dialogs.MethodContext;
import me.iatog.characterdialogue.dialogs.method.npc_control.ControlUtil;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;

public record ActionData(MethodContext context, ControlUtil util, NPC targetNPC) {

    public Player player() {
        return context.getPlayer();
    }

    public CharacterDialoguePlugin plugin() {
        return CharacterDialoguePlugin.getInstance();
    }

}
