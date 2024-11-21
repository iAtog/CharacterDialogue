package me.iatog.characterdialogue.util;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtils {
	
	public static String colorize(String message) {
		String version = Bukkit.getBukkitVersion().split("-")[0].split("\\.")[1];
		
		if(Integer.parseInt(version) >= 16) {
			Pattern pattern = Pattern.compile("&#[a-fA-F0-9]{6}");
            Matcher matcher = pattern.matcher(message);

            while (matcher.find()) {
                String color = message.substring(matcher.start(), matcher.end());
                message = message.replace(color, ChatColor.of(color.substring(1)) + "");
                matcher = pattern.matcher(message);
            }
		}
		
		return org.bukkit.ChatColor.translateAlternateColorCodes('&', message);
	}
}
