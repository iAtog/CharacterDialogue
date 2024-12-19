package me.iatog.characterdialogue.path;

import net.citizensnpcs.api.npc.NPC;

import java.util.List;

public class PathReplayer {

    private final List<RecordLocation> paths;
    private final NPC npc;
    private boolean teleport;

    public PathReplayer(List<RecordLocation> paths, NPC npc) {
        this.paths = paths;
        this.npc = npc;
    }

    public void startReplay() {
        npc.getEntity().teleport(paths.getFirst().toLocation());
        PathTrait trait = npc.getOrAddTrait(PathTrait.class);

        trait.setPaths(paths, teleport);
    }

    public void setTeleport(boolean teleport) {
        this.teleport = teleport;
    }
}
