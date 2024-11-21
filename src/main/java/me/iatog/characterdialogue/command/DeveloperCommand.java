package me.iatog.characterdialogue.command;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.entity.Player;

@Command(names = {
		"cdev"
},      permission = "*")
public class DeveloperCommand implements CommandClass {
	
	@Command(names = "")
	public void onCommand(@Sender Player player) {
		
	}
	
}
