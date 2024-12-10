package me.iatog.characterdialogue.dialogs;

import me.iatog.characterdialogue.enums.CompletedType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class DialogMethod<T extends JavaPlugin> {
	
	private final String id;
	protected T provider;
	private boolean disabled;
	
	public DialogMethod(String id, T provider) {
		this.id = id;
		this.provider = provider;
		this.disabled = false;
	}
	
	public DialogMethod(String id) {
		this(id, null);
	}
	
	/**
	 * Execute the method action
	 * @param context The context including player, configuration, session and consumer
	 */
	public abstract void execute(MethodContext context);

	/**
	 * Get the id of the method
	 * @return the id
	 */
	public final String getID() {
		return id.toUpperCase();
	}
	
	/**
	 * The method provider (in this case the plugin that creates it)
	 * @return the provider
	 */
	public final T getProvider() {
		return provider;
	}

	public void next(MethodContext context) {
		context.getConsumer().accept(CompletedType.CONTINUE);
	}

	public void pause(MethodContext context) {
		context.getConsumer().accept(CompletedType.PAUSE);
	}

	public void destroy(MethodContext context) {
		context.getConsumer().accept(CompletedType.DESTROY);
	}

	public boolean isDisabled() {
		return disabled;
	}

	protected void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
}
