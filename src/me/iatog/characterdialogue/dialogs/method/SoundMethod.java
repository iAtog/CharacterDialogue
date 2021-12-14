package me.iatog.characterdialogue.dialogs.method;

import java.util.logging.Level;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.session.DialogSession;

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
		
		if(sound == null) {
			return;
		}
		
		float volume = tryPaste(part, 1, 1);
		float pitch = tryPaste(part, 2, 1);
		
		player.playSound(player.getLocation(), sound, volume, pitch);
	}
	
	private float tryPaste(String[] value, int index, float defaultValue) {
		if(index >= value.length) {
			return defaultValue;
		} else {
			return isInt(value[index]) ? Float.valueOf(value[index]) : defaultValue;
		}
	}
	
	private boolean isInt(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch(NumberFormatException ex) {
			return false;
		}
	}
	
}
