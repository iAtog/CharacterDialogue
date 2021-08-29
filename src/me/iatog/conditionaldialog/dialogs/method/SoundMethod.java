package me.iatog.conditionaldialog.dialogs.method;

import java.util.logging.Level;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import me.iatog.conditionaldialog.ConditionalDialogPlugin;
import me.iatog.conditionaldialog.dialogs.DialogMethod;

public class SoundMethod extends DialogMethod {
	
	private ConditionalDialogPlugin main;
	
	public SoundMethod(ConditionalDialogPlugin main) {
		super("sound");
		this.main = main;
	}

	@Override
	public void cast(Player player, String arg) {
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
