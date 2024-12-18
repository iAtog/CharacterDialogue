package me.iatog.characterdialogue.dialogs.method.npc_control;

import me.iatog.characterdialogue.dialogs.method.npc_control.data.ControlData;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ControlRegistry {

    private final UUID playerId;

    // Integer = the original npc id
    // FollowData = original and his clone
    private final Map<Integer, ControlData> npcMap;

    public ControlRegistry(Player player) {
        this.npcMap = new HashMap<>();
        this.playerId = player.getUniqueId();
    }

    public ControlData get(int npcId) {
        return npcMap.get(npcId);
    }

    public void addNPC(NPC original, NPC clone) {
        this.npcMap.put(original.getId(), new ControlData(original, clone));
    }

    public ControlData removeNPC(int npcId) {
        return this.npcMap.remove(npcId);
    }

    public ControlData removeNPC(NPC originalNpc) {
        return this.removeNPC(originalNpc.getId());
    }

    public boolean isOnRegistry(int npcId) {
        return this.npcMap.containsKey(npcId);
    }

    public void clearAll() {
        this.npcMap.forEach((id, data) -> {
            data.getCopy().destroy();
        });

        this.npcMap.clear();
    }

    public UUID getOwner() {
        return playerId;
    }

}