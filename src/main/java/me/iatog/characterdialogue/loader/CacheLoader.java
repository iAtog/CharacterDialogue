package me.iatog.characterdialogue.loader;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.dialogs.choice.*;
import me.iatog.characterdialogue.dialogs.method.*;

public class CacheLoader implements Loader {
	
	private final CharacterDialoguePlugin main;
	
	public CacheLoader(CharacterDialoguePlugin main) {
		this.main = main;
	}

	@Override
	public void load() {
		main.registerMethods(
				new SendMethod(),
				new SoundMethod(main),
				new BroadcastMethod(),
				new WaitMethod(main),
				new DispatchCommandMethod(),
				new CommandMethod(),
				new TeleportMethod(),
				new EffectMethod(main),
				new SendServerMethod(main),
				new ChoiceMethod(main),
				new SneakMethod(main),
				new ConditionalMethod(main),
				new GiveMethod()
				);
		
		main.registerChoices(
				new ContinueChoice(),
				new DestroyChoice(),
				new SendChoice(),
				new DialogueChoice(),
				new MessageChoice(),
				new StartDialogChoice()
				);
	}

	@Override
	public void unload() {
		main.getCache().clearAll();
	}
}
