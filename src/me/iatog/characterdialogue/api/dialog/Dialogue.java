package me.iatog.characterdialogue.interfaces;

import java.util.List;

import net.citizensnpcs.api.npc.NPC;

/**
 * This is an test interface.
 * @author Atog
 */
public interface Dialogue {
	List<String> getLines();
	NPC getNPC();
}
