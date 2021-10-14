package me.iatog.characterdialogue.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.libraries.YamlFile;
import me.iatog.characterdialogue.session.DialogSession;

@Command(names = {
		"characterdialogue", "cdp", "characterd", "ddialogue"
},      permission = "characterdialogue.use",
		desc = "CharacterDialogue main command")
public class CharacterDialogueCommand implements CommandClass {
	
	private CharacterDialoguePlugin main;
	private YamlFile language;
	
	public CharacterDialogueCommand(CharacterDialoguePlugin main) {
		this.main = main;
		this.language = this.main.getFileFactory().getLang();
	}
	
	@Command(names = "", desc = "Main command")
	public void mainCommand(CommandSender sender) {
		sender.sendMessage(translateList(language.getStringList("help-message")).stream().toArray(String[]::new));
	}
	
	@Command(names = "reload",
			permission = "characterdialogue.reload",
			desc = "Reload the plugin")
	public void reloadCommand(CommandSender sender) {
		main.getFileFactory().reload();
		if(Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
			main.getApi().reloadHolograms();
		}
		
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', language.getString("reload-message")));
	}
	
	@Command(names = "clear-cache",
	         permission = "characterdialogue.clear-cache",
	         desc = "Clear a player memory cache")
	public void clearCache(CommandSender sender, @OptArg("$") String arg) {
		if(arg.equals("$")) {
			sender.sendMessage("§cYou need to specify a player name.");
			return;
		}
		
		Player target = Bukkit.getPlayer(arg);
		if(target == null || !target.isOnline()) {
			sender.sendMessage("§cThe player '" + arg + "' isn't online.");
			return;
		}
		
		Map<UUID, DialogSession> cache = main.getCache().getSessions();
		
		if(cache.containsKey(target.getUniqueId())) {
			DialogSession session = cache.remove(target.getUniqueId());
			session.cancel();
			sender.sendMessage("§aCleared cache for " + arg + " in "+session.getDisplayName() + " §r§adialog.");
		} else {
			sender.sendMessage("§cThat player doesn't have any data in the memory cache.");
		}
	}
	
	private List<String> translateList(List<String> list) {
		List<String> newList = new ArrayList<>();
		list.forEach((line) -> {
			newList.add(ChatColor.translateAlternateColorCodes('&', line));
		});
		return newList;
	}
	
}
