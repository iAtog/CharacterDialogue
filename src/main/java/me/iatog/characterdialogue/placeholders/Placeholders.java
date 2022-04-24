package me.iatog.characterdialogue.placeholders;

import org.bukkit.entity.Player;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.libraries.YamlFile;
import me.iatog.characterdialogue.util.TextUtils;

public class Placeholders {
	public static String translate(Player player, String arg) {
		CharacterDialoguePlugin main = CharacterDialoguePlugin.getInstance();
		YamlFile config = main.getFileFactory().getConfig();
		
		for(String name : config.getConfigurationSection("placeholders").getKeys(false)) {
			String value = config.getString("placeholders."+name);
			arg = arg.replace("%" + name + "%", value);
		}
		
		if(main.getHooks().isPlaceHolderAPIEnabled()) {
			arg = main.getHooks().getPlaceHolderAPIHook().translatePlaceHolders(player, arg);
		} else {
			arg = arg.replace("%player_name%", player.getName());
		}
		
		return TextUtils.colorize(arg);
	}
}
