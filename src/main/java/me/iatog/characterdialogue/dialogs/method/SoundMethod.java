package me.iatog.characterdialogue.dialogs.method;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.session.DialogSession;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class SoundMethod extends DialogMethod<CharacterDialoguePlugin> {
		
	public SoundMethod(CharacterDialoguePlugin main) {
		super("sound", main);
	}

	@Override
	public void execute(Player player, String arg, DialogSession session) {
		String[] part = arg.split(",");
		Sound sound = null;
		
		try {
			sound = Sound.valueOf(part[0]);
		} catch(Exception exception) {
			getProvider().getLogger().log(Level.SEVERE, "Unknown sound \"" + part[0] + "\", stopping dialogue.", exception);
			session.destroy();
			return;
		}
		
		float volume = def(part, 1, 1);
		float pitch = def(part, 2, 1);
		
		player.playSound(player.getLocation(), sound, volume, pitch);
	}
	
	private float def(String[] value, int index, float defaultValue) {
		if(index >= value.length) {
			return defaultValue;
		} else {
			return isInt(value[index]) ? Float.valueOf(value[index]) : defaultValue;
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
