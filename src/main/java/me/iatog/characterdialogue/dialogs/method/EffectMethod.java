package me.iatog.characterdialogue.dialogs.method;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.dialogs.MethodConfiguration;
import me.iatog.characterdialogue.enums.CompletedType;
import me.iatog.characterdialogue.session.DialogSession;
import me.iatog.characterdialogue.util.SingleUseConsumer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Optional;
import java.util.logging.Level;

public class EffectMethod extends DialogMethod<CharacterDialoguePlugin> {
	
	
	public EffectMethod(CharacterDialoguePlugin main) {
		super("effect", main);
	}

	@Override
	public void execute(Player player, MethodConfiguration configuration, DialogSession session, SingleUseConsumer<CompletedType> completed) {
		String arg = configuration.getArgument();
		String[] split = arg.split(",");
		if(arg.toLowerCase().startsWith("clear")) {
			String[] separator = arg.split(" ");
			if(separator.length < 2) {
				player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
			} else {
				String effectName = separator[1];
				Optional<PotionEffectType> effect = Optional.ofNullable(PotionEffectType.getByName(effectName));
				if(effect.isEmpty()) {
					getProvider().getLogger().log(Level.WARNING, "The name of the \""+effectName+"\" effect has not been found.");
				} else if(player.hasPotionEffect(effect.get())) {
					player.removePotionEffect(effect.get());
				}				
			}
			completed.accept(CompletedType.CONTINUE);
			return;
		}
		// EFFECT: effect_name,seconds,amplifier
		String name = split[0];
		int seconds = Integer.parseInt(split[1]);
		int amplifier = Integer.parseInt(split[2]);
		Optional<PotionEffectType> effectType = Optional.ofNullable(PotionEffectType.getByName(name));
		
		if(amplifier < 1) {
			amplifier = 1;
		} else if(amplifier > 255) {
			amplifier = 255;
		}
		
		if(effectType.isEmpty()) {
			getProvider().getLogger().log(Level.WARNING, "The name of the \""+name+"\" effect has not been found.");
			completed.accept(CompletedType.DESTROY);
			return;
		}
		
		PotionEffect potionEffect = new PotionEffect(effectType.get(), seconds * 20, amplifier, true);
		player.addPotionEffect(potionEffect);
		completed.accept(CompletedType.CONTINUE);
	}

}
