package me.iatog.characterdialogue.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import me.iatog.characterdialogue.dialogs.DialogChoice;

public class ChoiceSelectEvent extends PlayerEvent implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	
	private boolean cancelled;
	private DialogChoice choice;
	
	public ChoiceSelectEvent(Player player, DialogChoice choice) {
		super(player);
		this.choice = choice;
		
	}
	
	public DialogChoice getChoice() {
		return choice;
	}
	
	/**
	 * Gets a list of handlers handling this event.
	 *
	 * @return A list of handlers handling this event.
	 */
	public static HandlerList getHandlerList(){
		return handlers;
	}
	
	/**
	 * Gets a list of handlers handling this event.
	 *
	 * @return A list of handlers handling this event.
	 */
	@Override
	public final HandlerList getHandlers(){
		return handlers;
	}
	
	
	/**
	 * Check if the event is cancelled
	 */
	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	/**
	 * Set event cancelled
	 */
	@Override
	public void setCancelled(boolean arg0) {
		this.cancelled = arg0;
	}
}
