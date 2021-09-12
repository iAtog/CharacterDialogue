package me.iatog.characterdialogue.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.enums.ClickType;

public class ExecuteMethodEvent extends PlayerEvent implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	
	private DialogMethod method;
	private ClickType clickType;
	private int npcId;
	private String dialogName;
	private boolean cancelled;
	
	public ExecuteMethodEvent(Player player, DialogMethod method, ClickType clickType, int npcId, String dialogName) {
		super(player);
		this.method = method;
		this.clickType = clickType;
		this.npcId = npcId;
		this.dialogName = dialogName;
	}
	
	/**
	 * get the method when it is used
	 * @return the method
	 */
	public DialogMethod getMethod() {
		return method;
	}
	
	public ClickType getClick() {
		return clickType;
	}
	
	public int getNPCId() {
		return npcId;
	}
	
	public String getDialogName() {
		return dialogName;
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
