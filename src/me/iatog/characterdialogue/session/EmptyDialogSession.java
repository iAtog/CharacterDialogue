package me.iatog.characterdialogue.session;

import java.util.List;

import org.bukkit.entity.Player;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.enums.ClickType;

public class EmptyDialogSession extends DialogSession {

	public EmptyDialogSession(CharacterDialoguePlugin main, Player player, List<String> dialogs) {
		super(main, player, dialogs, ClickType.LEFT, -39, "$custom-dev-dialogue");
	}
}
