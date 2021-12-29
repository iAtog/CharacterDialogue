package me.iatog.characterdialogue.api.dialog;

import java.util.List;

import org.bukkit.entity.Player;

import me.iatog.characterdialogue.enums.ClickType;

public interface Dialogue {
	String getName();
	List<String> getLines();
	ClickType getClickType();
	String getDisplayName();
	DialogHologram getHologram();
	List<String> getFirstInteractionLines();
	boolean isFirstInteractionEnabled();
	boolean start(Player player);
	boolean startFirstInteraction(Player player, boolean log);
}
