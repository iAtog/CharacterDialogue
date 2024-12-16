package me.iatog.characterdialogue.dialogs;

import me.iatog.characterdialogue.api.dialog.ConfigurationType;
import me.iatog.characterdialogue.enums.CompletedType;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public abstract class DialogMethod<T extends JavaPlugin> {

    private final String id;
    private final T provider;
    private final List<String> dependencies;
    private boolean disabled;

    private final Map<String, ConfigurationType> configurationTypes;

    public DialogMethod(String id, T provider) {
        this.id = id;
        this.provider = provider;
        this.disabled = false;
        this.dependencies = new ArrayList<>();
        this.configurationTypes = new HashMap<>();
    }

    public DialogMethod(String id) {
        this(id, null);
    }

    /**
     * Execute the method action
     *
     * @param context The context including player, configuration, session and consumer
     */
    public abstract void execute(MethodContext context);

    /**
     * Get the id of the method
     *
     * @return the id
     */
    public final String getID() {
        return id.toLowerCase();
    }

    public final List<String> getDependencies() {
        return Collections.unmodifiableList(dependencies);
    }

    /**
     * The method provider (in this case the plugin that creates it)
     *
     * @return the provider
     */
    public final T getProvider() {
        return provider;
    }

    protected void next(MethodContext context) {
        context.getConsumer().accept(CompletedType.CONTINUE);
    }

    protected void pause(MethodContext context) {
        context.getConsumer().accept(CompletedType.PAUSE);
    }

    protected void destroy(MethodContext context) {
        context.getConsumer().accept(CompletedType.DESTROY);
    }

    public boolean isDisabled() {
        return disabled;
    }

    protected void addConfigurationType(String key, ConfigurationType valueType) {
        configurationTypes.put(key, valueType);
    }

    public Map<String, ConfigurationType> getConfigurationTypes() {
        return Collections.unmodifiableMap(configurationTypes);
    }

    protected void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    protected void addPluginDependency(String name, Runnable runnable) {
        this.dependencies.add(name);
        if (Bukkit.getPluginManager().isPluginEnabled(name)) {
            runnable.run();
        }
    }
}
