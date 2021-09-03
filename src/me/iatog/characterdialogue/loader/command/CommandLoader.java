package me.iatog.characterdialogue.loader.command;

import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilder;
import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilderImpl;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.part.PartInjector;
import me.fixeddev.commandflow.annotated.part.defaults.DefaultsModule;
import me.fixeddev.commandflow.bukkit.BukkitCommandManager;
import me.fixeddev.commandflow.bukkit.factory.BukkitModule;
import me.iatog.characterdialogue.CharacterDialogPlugin;
import me.iatog.characterdialogue.command.CharacterDialogueCommand;
import me.iatog.characterdialogue.loader.Loader;

public class CommandLoader implements Loader {
	
	private CharacterDialogPlugin main;
	private AnnotatedCommandTreeBuilder builder;
	private BukkitCommandManager commandManager;
	
	public CommandLoader(CharacterDialogPlugin main) {
		this.main = main;
		this.commandManager = new BukkitCommandManager("CharacterDialogue");
		PartInjector injector = PartInjector.create();
		injector.install(new DefaultsModule());
		injector.install(new BukkitModule());
		this.builder = new AnnotatedCommandTreeBuilderImpl(injector);
	}
	
	@Override
	public void load() {
		registerCommands(
				new CharacterDialogueCommand(main)
				);
	}
	
	public void registerCommands(CommandClass... commands) {
		for(CommandClass command : commands) {
			commandManager.registerCommands(builder.fromClass(command));
		}
	}
	
}
