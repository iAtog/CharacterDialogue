package com.github.iatog.characterdialogue.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class StringUtil {
    
    private static final boolean SUPPORTS_HEX = Integer.parseInt(Bukkit.getBukkitVersion().split("-")[0].split("\\.")[1]) >= 16;
    private static final Pattern HEX_PATTERN = Pattern.compile("&#[a-fA-F0-9]{6}");
    
    public static String colorize(String message) {
        if (SUPPORTS_HEX) {
            Matcher matcher = HEX_PATTERN.matcher(message);

            while (matcher.find()) {
                String color = message.substring(matcher.start(), matcher.end());
                message = message.replace(color, ChatColor.of(color.substring(1)) + "");
                matcher = HEX_PATTERN.matcher(message);
            }
        }

        return translateAlternateColorCodes('&', message);
    }

    public static String translatePlaceholders(Player player, String string) {
        return string;
    }
    
}
