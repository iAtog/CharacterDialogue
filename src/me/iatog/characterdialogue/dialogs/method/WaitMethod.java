package me.iatog.characterdialogue.dialogs.method;
	
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.iatog.characterdialogue.CharacterDialogPlugin;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.session.DialogSession;

public class WaitMethod extends DialogMethod {
	
	private CharacterDialogPlugin main;
	
	public WaitMethod(CharacterDialogPlugin main) {
		super("wait");
		this.main = main;
	}

	@Override
	public void execute(Player player, String arg, DialogSession session) {
		long seconds = Long.valueOf(arg);
		int next = session.getCurrentIndex() + 1;
		session.cancel();
		Bukkit.getScheduler().runTaskLater(main, new Runnable() {

			@Override
			public void run() {
				if(next < session.getDialogs().size() && (player != null && player.isOnline())) {
					session.start(next);
					System.out.println(player.getName()+": waited "+seconds+" seconds");
				}
			}
			
		}, seconds*20);
		
	}
}
