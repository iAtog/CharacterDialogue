package me.iatog.characterdialogue.loader;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.dialogs.choice.*;
import me.iatog.characterdialogue.dialogs.method.*;
import me.iatog.characterdialogue.dialogs.method.choice.ChoiceMethod;
import me.iatog.characterdialogue.dialogs.method.conditional.ConditionalMethod;
import me.iatog.characterdialogue.dialogs.method.conditionalevents.ConditionalEventsMethod;
import me.iatog.characterdialogue.dialogs.method.talk.TalkMethod;
import me.iatog.characterdialogue.libraries.Cache;
import me.iatog.characterdialogue.util.TextUtils;

public class CacheLoader implements Loader {
	
	private final CharacterDialoguePlugin main;
	
	public CacheLoader(CharacterDialoguePlugin main) {
		this.main = main;
	}

	@Override
	public void load() {
		Cache cache = main.getCache();

		main.registerMethods(
				new SendMethod(),
				new SoundMethod(main),
				new BroadcastMethod(),
				new WaitMethod(main),
				//new DispatchCommandMethod(),
				new CommandMethod(main),
				new TeleportMethod(),
				new EffectMethod(main),
				new SendServerMethod(main),
				//new LegacyChoiceMethod(main),
				new SneakMethod(main),
				new ConditionalMethod(main),
				new GiveMethod(),
				new TalkMethod(main),
				new TitleMethod(),

				new StartDialogueMethod(main),
				new FollowMethod(main),
				new ChoiceMethod(main),
				new ConditionalEventsMethod(main)
		);
		
		main.registerChoices(
				new ContinueChoice(),
				new DestroyChoice(),
				new SendChoice(),
				new DialogueChoice(),
				new MessageChoice(),
				new StartDialogChoice(main)
		);

		main.getLogger().info(TextUtils.colorize("Correctly loaded " + cache.getChoices().size() + " choices."));
		main.getLogger().info(TextUtils.colorize("Correctly loaded " + cache.getMethods().size() + " methods."));
	}

	@Override
	public void unload() {
		main.getCache().clearAll();
	}
}
