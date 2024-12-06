package me.iatog.characterdialogue.dialogs.method;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.dialogs.MethodConfiguration;
import me.iatog.characterdialogue.enums.CompletedType;
import me.iatog.characterdialogue.session.DialogSession;
import me.iatog.characterdialogue.util.SingleUseConsumer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class SoundMethod extends DialogMethod<CharacterDialoguePlugin> {
		
	public SoundMethod(CharacterDialoguePlugin main) {
		super("sound", main);
	}

	@Override
	public void execute(Player player, MethodConfiguration configuration, DialogSession session, SingleUseConsumer<CompletedType> completed) {
		String[] part = configuration.getArgument().split(",");
		Sound sound = null;
		
		try {
			sound = Sound.valueOf(part[0]);
		} catch(Exception exception) {
			getProvider().getLogger().log(Level.SEVERE, "Unknown sound \"" + part[0] + "\", stopping dialogue.", exception);
			completed.accept(CompletedType.DESTROY);
			return;
		}
		
		float volume = def(part, 1);
		float pitch = def(part, 2);
		
		player.playSound(player.getLocation(), sound, volume, pitch);
		completed.accept(CompletedType.CONTINUE);
	}
	
	private float def(String[] value, int index) {
		if(index >= value.length) {
			return (float) 1;
		} else {
			return isInt(value[index]) ? Float.parseFloat(value[index]) : (float) 1;
		}
	}
	
	private boolean isInt(String number) {
		try {
			Integer.parseInt(number);
			return true;
		} catch(NumberFormatException ex) {
			return false;
		}
	}
	
}
