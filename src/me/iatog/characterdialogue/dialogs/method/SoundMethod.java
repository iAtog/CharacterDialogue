package me.iatog.characterdialogue.dialogs.method;

import java.util.logging.Level;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.session.DialogSession;

public class SoundMethod extends DialogMethod {
	
	private CharacterDialoguePlugin main;
	
	public SoundMethod(CharacterDialoguePlugin main) {
		super("sound");
		this.main = main;
	}

	@Override
	public void execute(Player player, String arg, DialogSession session) {
		String[] part = arg.split(",");
		Sound sound = null;
		
		try {
			sound = Sound.valueOf(part[0]);
		} catch(Exception exception) {
			main.getLogger().log(Level.SEVERE, "Unknown sound \"" + part[0] + "\"", exception);
			return;
		}
		
		if(sound == null) {
			return;
		}
		
		float volume = Float.valueOf(part[1]);
		float pitch = Float.valueOf(part[2]);
		
		player.playSound(player.getLocation(), sound, volume, pitch);
	}
	
}
