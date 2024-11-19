package me.iatog.characterdialogue.dialogs.method;

import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.session.DialogSession;

public class SendServerMethod extends DialogMethod<CharacterDialoguePlugin> {
	
	public SendServerMethod(CharacterDialoguePlugin main) {
		super("send_server", main);
	}

	@Override
	public void execute(Player player, String arg, DialogSession session) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		String[] args = arg.split(",");
		
		if(args.length == 0) {
			return;
		}
		
		String server = args[0];
		String channel = args.length > 1 ? args[1] : "BungeeCord";
		
		out.writeUTF("Connect");
		out.writeUTF(server);
		
		player.sendPluginMessage(provider, channel, out.toByteArray());
		session.destroy();
	}
}
