package me.iatog.characterdialogue.placeholders;

import org.bukkit.entity.Player;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.libraries.YamlFile;

public class Placeholders {
	public static String translate(Player player, String arg) {
		CharacterDialoguePlugin main = CharacterDialoguePlugin.getInstance();
		YamlFile placeholders = main.getFileFactory().getPlaceholders();
		
		for(String name : placeholders.getConfigurationSection("placeholders").getKeys(false)) {
			String value = placeholders.getString("placeholders."+name);
			arg = arg.replace("%" + name + "%", value);
		}
		
		if(main.getHooks().isPlaceHolderAPIEnabled()) {
			arg = main.getHooks().getPlaceHolderAPIHook().translatePlaceHolders(player, arg);
		} else {
			arg = arg.replace("%player_name%", player.getName());
		}
		
		return arg;
	}
}
