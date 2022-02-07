package me.iatog.characterdialogue.session;

import java.util.List;

import org.bukkit.entity.Player;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.enums.ClickType;

public class EmptyDialogSession extends DialogSession {

	public EmptyDialogSession(CharacterDialoguePlugin main, Player player, List<String> lines, String name) {
		super(main, player, lines, ClickType.ALL, -999, name);
	}
	
	@Override
	public void start(int index) {
		// TODO: nothing
	}
	
}
