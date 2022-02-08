package me.iatog.characterdialogue.dialogs.method;

import java.util.Optional;
import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.session.DialogSession;

public class EffectMethod extends DialogMethod<CharacterDialoguePlugin> {
	
	
	public EffectMethod(CharacterDialoguePlugin main) {
		super("effect", main);
	}

	@Override
	public void execute(Player player, String arg, DialogSession session) {
		
		String[] split = arg.split(",");
		if(arg.toLowerCase().startsWith("clear")) {
			String[] separator = arg.split(" ");
			if(separator.length < 2) {
				player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
			} else {
				String effectName = separator[1];
				Optional<PotionEffectType> effect = Optional.ofNullable(PotionEffectType.getByName(effectName));
				if(!effect.isPresent()) {
					getProvider().getLogger().log(Level.WARNING, "The name of the \""+effectName+"\" effect has not been found.");
				} else if(player.hasPotionEffect(effect.get())) {
					player.removePotionEffect(effect.get());
				}				
			}
			return;
		}
		// EFFECT: effect_name,seconds,amplifier
		String name = split[0];
		int seconds = Integer.valueOf(split[1]);
		int amplifier = Integer.valueOf(split[2]);
		Optional<PotionEffectType> effectType = Optional.ofNullable(PotionEffectType.getByName(name));
		
		if(amplifier < 1) {
			amplifier = 1;
		} else if(amplifier > 255) {
			amplifier = 255;
		}
		
		if(!effectType.isPresent()) {
			getProvider().getLogger().log(Level.WARNING, "The name of the \""+name+"\" effect has not been found.");
			return;
		}
		
		PotionEffect potionEffect = new PotionEffect(effectType.get(), seconds * 20, amplifier, true);
		player.addPotionEffect(potionEffect);
	}

}
