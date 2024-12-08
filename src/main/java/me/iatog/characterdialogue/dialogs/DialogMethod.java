package me.iatog.characterdialogue.dialogs;

import me.iatog.characterdialogue.enums.CompletedType;
import me.iatog.characterdialogue.session.DialogSession;
import me.iatog.characterdialogue.util.SingleUseConsumer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class DialogMethod<T extends JavaPlugin> {
	
	private final String id;
	protected T provider;
	
	public DialogMethod(String id, T provider) {
		this.id = id;
		this.provider = provider;
	}
	
	public DialogMethod(String id) {
		this(id, null);
	}
	
	/**
	 * Execute the method action
	 * @param context The context including player, configuration, session and consumer
	 */
	public abstract void execute(MethodContext context);
	// Player player, MethodConfiguration configuration, DialogSession session, SingleUseConsumer<CompletedType> complete
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
}
