package me.iatog.conditionaldialog.loader.cache;

import me.iatog.conditionaldialog.ConditionalDialogPlugin;
import me.iatog.conditionaldialog.dialogs.DialogMethod;
import me.iatog.conditionaldialog.dialogs.method.BroadcastMethod;
import me.iatog.conditionaldialog.dialogs.method.SendMethod;
import me.iatog.conditionaldialog.dialogs.method.SoundMethod;
import me.iatog.conditionaldialog.dialogs.method.WaitMethod;
import me.iatog.conditionaldialog.loader.Loader;

public class CacheLoader implements Loader {
	
	private ConditionalDialogPlugin main;
	
	public CacheLoader(ConditionalDialogPlugin main) {
		this.main = main;
	}
	
	@Override
	public void load() {
		registerMethods(
				new SendMethod(),
				new SoundMethod(main),
				new BroadcastMethod(),
				new WaitMethod()
				);
	}

	@Override
	public void unload() {
		main.getCache().clearAll();
	}
	
	private void registerMethods(DialogMethod... methods) {
		for(DialogMethod method : methods) {
			main.getCache().getMethods().put(method.getID(), method);
		}
	}
	
}
