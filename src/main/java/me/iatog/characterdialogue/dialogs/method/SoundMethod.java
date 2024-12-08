package me.iatog.characterdialogue.dialogs.method;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.dialogs.MethodConfiguration;
import me.iatog.characterdialogue.dialogs.MethodContext;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class SoundMethod extends DialogMethod<CharacterDialoguePlugin> {
		
	public SoundMethod(CharacterDialoguePlugin main) {
		super("sound", main);
	}

	@Override
	public void execute(MethodContext context) {
		MethodConfiguration configuration = context.getConfiguration();
		Player player = context.getPlayer();

		Sound sound = null;
		
		try {
			sound = Sound.valueOf(configuration.getString("sound"));
		} catch(Exception exception) {
			getProvider().getLogger().log(Level.SEVERE, "Unknown sound \"" + configuration.getString("sound") + "\", stopping dialogue.", exception);
			this.destroy(context);
			return;
		}
		
		float volume = configuration.getFloat("volume", 1f);
		float pitch = configuration.getFloat("pitch", 1f);
		
		player.playSound(player.getLocation(), sound, volume, pitch);
		next(context);
	}

}
