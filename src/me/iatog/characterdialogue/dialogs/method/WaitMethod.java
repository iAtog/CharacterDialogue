package me.iatog.characterdialogue.dialogs.method;
	
import java.util.concurrent.TimeUnit;

import org.bukkit.entity.Player;

import me.iatog.characterdialogue.dialogs.DialogMethod;

public class WaitMethod extends DialogMethod {

	public WaitMethod() {
		super("wait");
	}

	@Override
	public void cast(Player player, String arg) {
		long seconds = Long.valueOf(arg);
		try {
		    TimeUnit.SECONDS.sleep(seconds);
		    System.out.println(player.getName()+": waited "+seconds+" seconds");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
