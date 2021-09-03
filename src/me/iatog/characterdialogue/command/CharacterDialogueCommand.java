package me.iatog.characterdialogue.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.iatog.characterdialogue.CharacterDialogPlugin;
import me.iatog.characterdialogue.libraries.YamlFile;

@Command(names = {
		"characterdialogue", "cdp", "characterd", "ddialogue"
})
public class CharacterDialogueCommand implements CommandClass {
	
	private CharacterDialogPlugin main;
	private YamlFile language;
	
	public CharacterDialogueCommand(CharacterDialogPlugin main) {
		this.main = main;
		this.language = this.main.getFileFactory().getLang();
	}
	
	@Command(names = "", desc = "Main command")
	public void mainCommand(CommandSender sender) {
		sender.sendMessage(translateList(language.getStringList("help-message")).stream().toArray(String[]::new));
	}
	
	@Command(names = "reload", desc = "Reload all files")
	public void reloadCommand(CommandSender sender) {
		main.getFileFactory().reload();
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', language.getString("reload-message")));
	}
	
	private List<String> translateList(List<String> list) {
		List<String> newList = new ArrayList<>();
		list.forEach((line) -> {
			newList.add(ChatColor.translateAlternateColorCodes('&', line));
		});
		return newList;
	}
	
}
