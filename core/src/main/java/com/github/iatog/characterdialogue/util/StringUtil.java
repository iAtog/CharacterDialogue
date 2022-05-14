package com.github.iatog.characterdialogue.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.iatog.characterdialogue.CharacterDialoguePlugin;
import com.github.iatog.characterdialogue.api.file.YamlFile;

import net.md_5.bungee.api.ChatColor;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class StringUtil {
    
    private static final boolean SUPPORTS_HEX = Integer.parseInt(Bukkit.getBukkitVersion().split("-")[0].split("\\.")[1]) >= 16;
    private static final Pattern HEX_PATTERN = Pattern.compile("&#[a-fA-F0-9]{6}");
    private static final CharacterDialoguePlugin PLUGIN = CharacterDialoguePlugin.getInstance();
    
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
        YamlFile config = PLUGIN.getFileRegistry().getFile("config");
        
        for(String name : config.getConfigurationSection("placeholders").getKeys(false)) {
            String value = config.getString("placeholders."+name);
            string = string.replace("%" + name + "%", value);
        }
        
        
        return colorize(string);
    }
    
    public static String translatePlaceholders(Player player, String string, String npcName) {
        return translatePlaceholders(player, string.replace("%npc_name%", npcName));
    }
    
}
