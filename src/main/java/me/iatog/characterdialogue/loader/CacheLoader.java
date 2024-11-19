package me.iatog.characterdialogue.loader;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.dialogs.choice.ContinueChoice;
import me.iatog.characterdialogue.dialogs.choice.DestroyChoice;
import me.iatog.characterdialogue.dialogs.choice.DialogueChoice;
import me.iatog.characterdialogue.dialogs.choice.MessageChoice;
import me.iatog.characterdialogue.dialogs.choice.SendChoice;
import me.iatog.characterdialogue.dialogs.choice.StartDialogChoice;
import me.iatog.characterdialogue.dialogs.method.*;

public class CacheLoader implements Loader {
	
	private CharacterDialoguePlugin main;
	
	public CacheLoader(CharacterDialoguePlugin main) {
		this.main = main;
	}
	
	@SuppressWarnings("unchecked")
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
				new ConditionalMethod(main)
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
