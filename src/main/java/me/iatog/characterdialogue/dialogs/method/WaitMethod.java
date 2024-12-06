package me.iatog.characterdialogue.dialogs.method;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.dialogs.MethodConfiguration;
import me.iatog.characterdialogue.enums.CompletedType;
import me.iatog.characterdialogue.session.DialogSession;
import me.iatog.characterdialogue.util.SingleUseConsumer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class WaitMethod extends DialogMethod<CharacterDialoguePlugin> {
		
	public WaitMethod(CharacterDialoguePlugin main) {
		super("wait", main);
	}

	@Override
	public void execute(Player player, MethodConfiguration configuration, DialogSession session, SingleUseConsumer<CompletedType> completed) {
		double seconds;
		String arg = configuration.getArgument();
		try {
			seconds = Double.parseDouble(arg);
		} catch(NumberFormatException e) {
			getProvider().getLogger().warning("The argument '" + arg + "' in " + session.getDisplayName() + " isn't valid. Line: " + session.getCurrentIndex());
			completed.accept(CompletedType.DESTROY);
			return;
		}

		if(seconds < 0.1) {
			seconds = 1;
		}

		int next = session.getCurrentIndex() + 1;
		//completed.accept(CompletedType.PAUSE);

		Bukkit.getScheduler().runTaskLater(getProvider(), () -> {
            if(next < session.getLines().size() && (player != null && player.isOnline())) {
				completed.accept(CompletedType.CONTINUE);
            } else {
				completed.accept(CompletedType.DESTROY);
            }
        }, (long)(20 * seconds));
	}
}
