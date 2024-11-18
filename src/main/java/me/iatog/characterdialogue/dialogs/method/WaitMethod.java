package me.iatog.characterdialogue.dialogs.method;
	
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.session.DialogSession;

public class WaitMethod extends DialogMethod<CharacterDialoguePlugin> {
		
	public WaitMethod(CharacterDialoguePlugin main) {
		super("wait", main);
	}

	@Override
	public void execute(Player player, String arg, DialogSession session) {
		double seconds;

		try {
			seconds = Double.parseDouble(arg);
		} catch(NumberFormatException e) {
			getProvider().getLogger().warning("The argument '" + arg + "' in " + session.getDisplayName() + " isn't valid. Line: " + session.getCurrentIndex());
			session.destroy();
			return;
		}

		int next = session.getCurrentIndex() + 1;
		session.cancel();
		Bukkit.getScheduler().runTaskLater(getProvider(), new Runnable() {

			@Override
			public void run() {
				if(next < session.getLines().size() && (player != null && player.isOnline())) {
					session.start(next);
				} else {
					session.cancel();
					session.destroy();
				}
			}
			
		}, (long)(20 * seconds));
	}
}
